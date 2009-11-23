package thewebsemantic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;

public class PersistOperation {

	TypeWrapper type;
	IndexedNeo neo;
	Map<Long, Object> visited;

	public PersistOperation(TypeWrapper type, IndexedNeo neo) {

		this.type = type;
		this.neo = neo;
		visited = new HashMap<Long, Object>();
	}

	public void save(Object bean) {
		Transaction t = neo.beginTx();
		try {
			save(asNode(type, bean), bean);
			t.success();
		} finally {
			t.finish();
		}

	}

	private void save(Node node, Object o) {
		for (FieldContext field : type.getValueContexts(o))
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

		// ignore unmodified collections
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

			// save primitive properties
			for (FieldContext f : genericType.getValueContexts(value))
				if (f.isSimpleType())
					saveAndIndex(n2, f);
		}
	}

	private Node asNode(TypeWrapper t, Object value) {
		Neo id = t.id(value);
		Node n = id.mirror(neo);
		t.setId(value, id);
		return n;
	}

	private void relate(Node node, FieldContext field) {
		if (field.value() == null)
			return;
		// prevent infinite loop on cycles
		if (visited.containsKey(node.getId()))
			return;
		visited.put(node.getId(), node);

		Node n2 = field.targetNode(neo);
		node.createRelationshipTo(n2, field.toRelationship(neo
				.getRelationFactory()));

		// save primitive properties
		for (FieldContext f : field.getTargetFields())
			if (f.isSimpleType())
				saveAndIndex(n2, f);
			else if (f.isSingular())
				relate(n2, f);
	}

}
