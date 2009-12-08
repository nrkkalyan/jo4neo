package thewebsemantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;


public class PersistenceManager {

	IndexedNeo ineo;

	public PersistenceManager(NeoService neo) {
		ineo = new IndexedNeo(neo);
	}
	
	public Transaction beginTx() {
		return ineo.beginTx(); 
	}
	
	public void persist(Object o) {
		new PersistOperation(ineo).save(o);
	}
	
	public Node get(Object o) {
		return ineo.asNode(o);
	}

	public void delete(Object o) {
		TypeWrapper type = TypeWrapperFactory.$(o);
		Transaction t = ineo.beginTx();
		try {
			Nodeid neo = type.id(o);
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

	<T> Collection<T> get(Class<T> t, String indexname, Object value) {
		ArrayList<T> list = new ArrayList<T>();
		for (Node n : ineo.getIndexService().getNodes(indexname, value))
			list.add(get(t, n.getId()));
		return list;
	}
	
	public <A> Where<A> find(A a) {
		return new FieldValueMap<A>(a, this);
	}
	
	<T> T getSingle(Class<T> t, String indexname, Object value) {
		Node n = ineo.getIndexService().getSingleNode(indexname, value);
		return (n != null) ?get(t, n.getId()) : null;
	}

	public <T> Collection<T> get(Class<T> t) {
		return new LoadOperation<T>(t, ineo).loadAll();
	}
	
	public <T> Collection<T> getAddedSince(Class<T> t, Date d) {
		return new LoadOperation<T>(t, ineo).since(d.getTime());
	}

	public <T> Collection<T> getAddedBetween(Class<T> t, Date from, Date to) {
		return new LoadOperation<T>(t, ineo).within(from.getTime(), to.getTime());
	}

}
