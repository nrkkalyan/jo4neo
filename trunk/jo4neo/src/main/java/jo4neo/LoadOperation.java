package jo4neo;

import static jo4neo.util.Resources.MISSING_TIMELINE_ANNOTATION;
import static jo4neo.util.Resources.msg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.ReturnableEvaluator;
import org.neo4j.api.core.StopEvaluator;
import org.neo4j.api.core.Transaction;
import org.neo4j.api.core.Traverser;
import org.neo4j.api.core.Traverser.Order;


class LoadOperation<T> {

	IndexedNeo neo;
	Class<?> cls;
	Map<Long, Object> cache;

	public LoadOperation(Class<?> type, IndexedNeo ineo) {
		this.cls = type;
		this.neo = ineo;
		cache = new HashMap<Long, Object>();
	}

	public T load(long key) {
		Transaction t = neo.beginTx();
		try {
			Node n = neo.getNodeById(key);
			if (n == null)
				return null;
			Object o = loadDirect(n);
			t.success();
			return (T) o;
		} finally {
			t.finish();
		}
	}
	
	public boolean isClosed() {
		return neo.isClosed();
	}


	public Collection<T> loadAll() {
		Transaction t = neo.beginTx();
		try {
			Node n = neo.getMetaNode(cls);
			return loadAll(n.getRelationships(Relationships.HAS_MEMBER));
		} finally {
			t.finish();
		}			
	}
	
	private Collection<T> load(Iterable<Node> nodes, long max) {
		Transaction t = neo.beginTx();
		long l = 0;
		try {
			ArrayList<T> results = new ArrayList<T>();
			for (Node node : nodes) {
				if (l++ >= max) break;
				results.add((T) loadDirect(node));
			}
			t.success();
			return results;
		} finally {
			t.finish();
		}
	}

	private Collection<T> load(Iterable<Node> nodes) {
		return load(nodes, Long.MAX_VALUE);
	}
	
	public Collection<T> loadAndFilter(Iterable<Node> nodes) {
		return (Collection<T>)loadAndFilter(nodes,cls);
	}

	public Collection<Object> loadAndFilter(Iterable<Node> nodes, Class<?> clazz) {
		Transaction t = neo.beginTx();
		try {
			ArrayList<Object> results = new ArrayList<Object>();
			for (Node node : nodes) {
				Class<?> nodeType = nodesJavaType(node).getWrappedType();
				if (clazz.isAssignableFrom(nodeType))
					results.add(loadDirect(node));
			}
			t.success();
			return results;
		} finally {
			t.finish();
		}
	}

	
	private Collection<T> loadAll(Iterable<Relationship> relations) {
			ArrayList<T> results = new ArrayList<T>();
			for (Relationship r : relations)
				results.add((T) loadDirect(r.getEndNode()));
			return results;
		
	}

	private void single(Node n, FieldContext field) {
		for (Relationship r : field.relationships(n, neo.getRelationFactory())) {
			field.setProperty(loadDirect(r.getEndNode()));
			return;
		}
	}
	
	private void inverse(Node n, FieldContext field) {
		for (Relationship r : field.inverseRelationships(n, neo.getRelationFactory())) {
			field.setProperty(loadDirect(r.getStartNode()));
			return;
		}
	}

	public Collection<Object> load(FieldContext field) {
		Transaction t = neo.beginTx();
		try { 
			Set<Object> values = new TreeSet<Object>(new NeoComparator());
			Node n = field.subjectNode(neo);
			for (Relationship r : outgoingRelationships(field, n)) {
				if (!values.add(loadDirect(r.getEndNode())))
					System.err.println("duplicate relations in graph.");
			}
			t.success();
			return values;
		} finally {
			t.finish();
		}
	}
	
	public Collection<Object> loadInverse(FieldContext field) {
		Transaction t = neo.beginTx();
		try { 
			Set<Object> values = new TreeSet<Object>(new NeoComparator());
			Node n = field.subjectNode(neo);
			for (Relationship r : incommingRelationships(field, n)) {
				if (!values.add(loadDirect(r.getStartNode())))
					System.err.println("duplicate relations in graph.");
			}
			t.success();
			return values;
		} finally {
			t.finish();
		}
	}
	
	public Collection<Object> loadTraverser(FieldContext field) {
		Transaction t = neo.beginTx();
		try { 
			Node n = field.subjectNode(neo);
			Traverser tvsr = field.getTraverserProvider().get(n);
			t.success();
			return loadAndFilter(tvsr,  field.type2());
		} finally {
			t.finish();
		}
	}
	
	public void removeRelationship(FieldContext field, Object o) {
		Transaction t = neo.beginTx();
		try {
			Node source = field.subjectNode(neo);
			Node target = neo.asNode(o);
			for (Relationship r : outgoingRelationships(field, source)) {
				if (r.getEndNode().equals(target))
					r.delete();
			}
			t.success();
		} finally {
			t.finish();
		}
	}

	private Iterable<Relationship> outgoingRelationships(FieldContext field,
			Node n) {
		return n.getRelationships(field.toRelationship(neo
				.getRelationFactory()), Direction.OUTGOING);
	}
	
	private Iterable<Relationship> incommingRelationships(FieldContext field,
			Node n) {
		return n.getRelationships(field.toRelationship(neo
				.getRelationFactory()), Direction.INCOMING);
	}

	

	public Object loadDirect(Node n) {
		if (cache.containsKey(n.getId()))
			return cache.get(n.getId());
		TypeWrapper type = nodesJavaType(n);
		Object o = type.newInstance(n);
		type.setId(o, new Nodeid(n.getId(), type.getWrappedType()));
		cache.put(n.getId(), o);
		for (FieldContext field : type.getFields(o))
			if (field.isInverse() && field.isSingular())
				inverse(n, field);
			else if (field.isSimpleType())
				field.applyFrom(n);
			else if (field.isSingular())
				single(n, field);
			else if (field.isPluralPrimitive())
				field.applyFrom(n);
			else if (field.isPlural())
				field.setProperty(ListFactory.get(field, this));

		return o;
	}

	/**
	 * Each node created is annotated with the javatype from
	 * whence it came.
	 * @param n neo4j node.
	 * @return
	 */
	public static TypeWrapper nodesJavaType(Node n) {
		String typename = (String) n.getProperty(Nodeid.class.getName());
		TypeWrapper type = TypeWrapperFactory.wrap(typename);
		return type;
	}
	
	/*
	 * Timeline features
	 * 
	 */
	
	/**
	 * 
	 */
	public Collection<T> since(long since) {
		timelineAnnotationRequired();
		Transaction t = neo.beginTx();
		try {
			org.neo4j.util.timeline.Timeline tl = neo.getTimeLine(cls);		
			return load(tl.getAllNodesAfter(since));
		} finally {
			t.finish();
		}
	}

	/**
	 * 
	 */
	private void timelineAnnotationRequired() {
		if (!cls.isAnnotationPresent(Timeline.class))
			throw new UnsupportedOperationException(msg(MISSING_TIMELINE_ANNOTATION, cls));
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Collection<T> within(long from, long to) {		
		timelineAnnotationRequired();
		Transaction t = neo.beginTx();
		try {
			org.neo4j.util.timeline.Timeline tl = neo.getTimeLine(cls);			
			return load(tl.getAllNodesBetween(from, to));
		} finally {
			t.finish();
		}
	}
	
	public Collection<T> latest(long max) {
		Transaction t = neo.beginTx();
		try {
			Node metanode = neo.getMetaNode(cls);
			Traverser tvsr = metanode.traverse(Order.BREADTH_FIRST, StopEvaluator.END_OF_GRAPH, 
					ReturnableEvaluator.ALL_BUT_START_NODE, Relationships.NEXT_MOST_RECENT, Direction.OUTGOING);
			return load(tvsr, max);
		} finally {
			t.finish();
		}
	}

	public T load(String indexname, Object value) {
		Transaction t = neo.beginTx();
		try {
			Node n = neo.getIndexService().getSingleNode(indexname, value);
			if (n==null)
				return null;
			Object o = loadDirect(n);
			t.finish();
			return (T)o;
		} finally {
			t.finish();
		}
	}
}

/**
 * jo4neo is a java object binding library for neo4j
 * Copyright (C) 2009  Taylor Cowan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */