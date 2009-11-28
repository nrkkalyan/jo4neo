package thewebsemantic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;
import static thewebsemantic.PersistenceManager.JAVA_CLASS;

public class PersistOperation {

	//TypeWrapper type;
	IndexedNeo neo;
	Map<Long, Object> visited;

	public PersistOperation(IndexedNeo neo) {
		this.neo = neo;
		visited = new HashMap<Long, Object>();
	}

	public void save(Object bean) {
		Transaction t = neo.beginTx();
		try {
			TypeWrapper type = TypeWrapperFactory.wrap(bean);
			save(asNode(type, bean), bean);
			t.success();
		} finally {
			t.finish();
		}
	}

	private void save(Node node, Object o) {
		/*
		 * cycle detection
		 * object graphs may contain cycles, which would cause
		 * infinite recursion without this check
		 */
		if (visited.containsKey(node.getId()))
			return;
		visited.put(node.getId(), node);
		
		TypeWrapper type = TypeWrapperFactory.wrap(o);
		for (FieldContext field : type.getValueContexts(o))
			save(node, field);
	}

	private void save(Node node, FieldContext field) {
		if (field.isSimpleType())
			saveAndIndex(node, field);
		else if (field.isPluralPrimitive())
			saveAndIndex(node, field);
		else if (field.isSingular())
			relate(node, field);
		else if (field.isPlural())
			relations(node, field);
	}

	private void saveAndIndex(Node node, FieldContext field) {
		field.applyTo(node);
		if (field.value() != null && field.isIndexed())
			neo.getIndexService().index(node, field.getIndexName(),
					field.value());
	}

	private void relations(Node node, FieldContext field) {
		Collection<Object> values = field.values();
		if (values == null)
			return;	
		
		/*
		 *  Ignore unmodified collections.
		 */
		if (values instanceof Lazy) {
			if (!((Lazy) values).modified())
				return;
			for (Relationship r : field.getRelationships(node, neo
					.getRelationFactory()))
				r.delete();
		}
		
		for (Object value : values) {
			TypeWrapper genericType = TypeWrapperFactory.wrap(value);
			Node n2 = asNode(genericType, value);
			node.createRelationshipTo(n2, field.toRelationship(neo
					.getRelationFactory()));
			save(n2, value);
		}
	}

	private Node asNode(TypeWrapper t, Object value) {
		Neo id = t.id(value);
		Node n = id.mirror(neo);
		neo.getIndexService().index(n, JAVA_CLASS, t.getWrappedType().getName());
		t.setId(value, id);
		return n;
	}

	private void relate(Node node, FieldContext field) {
		if (field.value() == null)
			return;		
		Node n2 = field.targetNode(neo);
		node.createRelationshipTo(n2, field.toRelationship(neo
				.getRelationFactory()));
		save(n2, field.value());
	}

}
