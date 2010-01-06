package jo4neo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jo4neo.fluent.Where;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;

/**
 * 
 * 
 *
 */
public class ObjectGraph {

	IndexedNeo ineo;

	public ObjectGraph(NeoService neo) {
		ineo = new IndexedNeo(neo);
	}

	public Transaction beginTx() {
		return ineo.beginTx();
	}

	public <A> void persist(A... o) {
		new PersistOperation<A>(ineo).save(o);
	}

	public Node get(Object o) {
		return ineo.asNode(o);
	}

	public void delete(Object... o) {
		new DeleteOpertation(ineo).delete(o);
	}

	private Node asNode(Object o) {
		Transaction t = ineo.beginTx();
		try {
			TypeWrapper type = TypeWrapperFactory.$(o);
			Nodeid neo = type.id(o);
			Node n = ineo.getNodeById(neo);
			t.success();
			return n;
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
		Transaction tx = ineo.beginTx();
		try {
			ArrayList<T> list = new ArrayList<T>();
			for (Node n : ineo.getIndexService().getNodes(indexname, value))
				list.add(get(t, n.getId()));
			tx.success();
			return list;
		} finally {
			tx.finish();
		}
	}

	public <A> Where<A> find(A a) {
		return new FieldValueMap<A>(a, this);
	}
	
	public long count(Collection<? extends Object> values) {
		if (values instanceof LazyList)
			return ((LazyList) values).getCount();
		return 0;
	}
	
	<T> T getSingle(Class<T> t, String indexname, Object value) {
		return new LoadOperation<T>(t, ineo).load(indexname, value);
	}

	public <T> Collection<T> get(Class<T> t) {
		return new LoadOperation<T>(t, ineo).loadAll();
	}

	public <T> Collection<T> getAddedSince(Class<T> t, Date d) {
		return new LoadOperation<T>(t, ineo).since(d.getTime());
	}

	public <T> Collection<T> getAddedBetween(Class<T> t, Date from, Date to) {
		return new LoadOperation<T>(t, ineo).within(from.getTime(), to
				.getTime());
	}

	public <T> Collection<T> getMostRecent(Class<T> t, int max) {
		if ( supportsRecency(t))
			return new LoadOperation<T>(t, ineo).latest(max);
		else 
			throw new RuntimeException("Recency unsupported for " + t + ": must be annotated as @neo(recency=true)");
	}

	public Node get(URI uri) {
		return ineo.getURINode(uri);
	}

	public <T> Collection<T> get(Class<T> type, Iterable<Node> nodes) {
		return new LoadOperation<T>(type, ineo).loadAndFilter(nodes);
	}
	
	private boolean supportsRecency(Class<?> c) {
		return c.isAnnotationPresent(neo.class) && c.getAnnotation(neo.class).recency();
	}

}

/**
 * jo4neo is a java object binding library for neo4j Copyright (C) 2009 Taylor
 * Cowan
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
