package jo4neo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jo4neo.fluent.Where;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;


/**
 * 
 * 
 *
 */
public class PersistenceManager {

	IndexedNeo ineo;

	public PersistenceManager(NeoService neo) {
		ineo = new IndexedNeo(neo);
	}
	
	public Transaction beginTx() {
		return ineo.beginTx(); 
	}
	
	public void persist(Object... o) {
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
			for (FieldContext field : type.getFields(o))
				if (field.isIndexed())
					indexRemove(delNode, field);
			
			for (Relationship r : delNode.getRelationships())
				r.delete();
			delNode.delete();
			t.success();
		} finally {
			t.finish();
		}
	}

	private void indexRemove(Node delNode, FieldContext field) {
		ineo.getIndexService().removeIndex(delNode,
				field.getIndexName(), field.value());
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

/**
 * Copyright (C) 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */