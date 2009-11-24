package thewebsemantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;

public class LoadOperation<T> {

	long key;
	IndexedNeo neo;
	Class<?> cls;
	Map<Long, Object> cache;

	public LoadOperation(Class<?> type, long key, IndexedNeo ineo) {
		this.key = key;
		this.cls = type;
		this.neo = ineo;
		cache = new HashMap<Long, Object>();
	}

	public T load() {
		Transaction t = neo.beginTx();
		try {
			Node n = neo.getNodeById(key);
			if (n == null)
				return null;
			Object o = loadFully(n);
			t.success();
			return (T) o;
		} finally {
			t.finish();
		}
	}

	private Object loadFully(Node n) {
		TypeWrapper type = nodesJavaType(n);
		Object o = loadDirect(n);
		for (FieldContext field : type.getValueContexts(o)) {
			if (field.isSingular())
				single(n, field);
			else if (field.isPluralPrimitive())
				field.applyFrom(n);
			else if (field.isPlural())
				field.setProperty(new LazyList(field, this));
		}
		return o;
	}

	public Collection<T> loadAll() {
		return load(neo.getIndexService().getNodes("javaclass", cls.getName()));
	}
	public Collection<T> load(Iterable<Node> nodes) {
		Transaction t = neo.beginTx();
		try {
			ArrayList<T> results = new ArrayList<T>();
			for (Node node : nodes)
				results.add((T) loadFully(node));
			t.success();
			return results;
		} finally {
			t.finish();
		}
	}

	private void single(Node n, FieldContext field) {
		for (Relationship r : field.relationships(n, neo.getRelationFactory())) {
			field.setProperty(loadDirect(r));
			return;
		}
	}

	public List<Object> load(FieldContext field) {
		Transaction t = neo.beginTx();
		try {
			ArrayList<Object> values = new ArrayList<Object>();
			
			//Node n = neo.getNodeById(nodeid);
			Node n = field.subjectNode(neo);
			for (Relationship r : n.getRelationships(field.toRelationship(neo
					.getRelationFactory()), Direction.OUTGOING)) {
				values.add(loadDirect(r));
			}
			t.success();
			return values;
		} finally {
			t.finish();
		}
	}

	protected Object loadDirect(Relationship r) {
		return loadDirect(r.getEndNode());
	}

	protected Object loadDirect(Node n) {
		if (cache.containsKey(n.getId()))
			return cache.get(n.getId());
		TypeWrapper type = nodesJavaType(n);
		Object o = type.newInstance();
		type.setId(o, new Neo(n.getId(), type.getWrappedType()));
		cache.put(n.getId(), o);
		for (FieldContext field : type.getValueContexts(o))
			if (field.isSimpleType())
				field.applyFrom(n);
			else if (field.isSingular())
				single(n, field);
		return o;
	}

	/**
	 * Each node created is annotated with the javatype from
	 * whence it came.
	 * @param n neo4j node.
	 * @return
	 */
	private TypeWrapper nodesJavaType(Node n) {
		String typename = (String) n.getProperty(Neo.class.getName());
		TypeWrapper type = TypeWrapperFactory.wrap(typename);
		return type;
	}



}
