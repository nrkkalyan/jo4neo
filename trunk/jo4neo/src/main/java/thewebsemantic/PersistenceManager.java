package thewebsemantic;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;

public class PersistenceManager {

	public static final String JAVA_CLASS = "javaclass";
	NeoService neo;
	IndexedNeo ineo;

	public PersistenceManager(NeoService neo) {
		this.neo = neo;
		ineo = new IndexedNeo(neo, new RelationFactoryImpl());
	}

	public PersistenceManager(NeoService neo, RelationFactory f) {
		this.neo = neo;
		ineo = new IndexedNeo(neo, f);
	}

	public void persist(Object o) {
		new PersistOperation(ineo).save(o);
	}

	public void delete(Object o) {
		Transaction t = ineo.beginTx();
		try {
			TypeWrapper type = TypeWrapperFactory.wrap(o);
			Neo neo = type.id(o);
			Node delNode = ineo.getNodeById(neo.id());
			if (neo == null)
				return;

			for (FieldContext field : type.getValueContexts(o)) {
				if (field.isIndexed())
					ineo.getIndexService().removeIndex(delNode,
							field.getIndexName(), field.value());
			}
			for (Relationship r : delNode.getRelationships())
				r.delete();
			delNode.delete();
			t.success();
		} finally {
			t.finish();
		}
	}

	public <T> T get(Class<T> t, long key) {
		return new LoadOperation<T>(t, ineo).load(key);
	}

	public void close() {
		ineo.close();
	}

	public <T> Collection<T> get(Class<T> t, String indexname, Object value) {
		ArrayList<T> list = new ArrayList<T>();
		for (Node n : ineo.getIndexService().getNodes(indexname, value))
			list.add(get(t, n.getId()));
		return list;
	}
	
	public <T> T getSingle(Class<T> t, String indexname, Object value) {
		Node n = ineo.getIndexService().getSingleNode(indexname, value);
		return (n != null) ?get(t, n.getId()) : null;
	}

	public <T> Collection<T> get(Class<T> t) {
		return new LoadOperation<T>(t, ineo).loadAll();
	}

}
