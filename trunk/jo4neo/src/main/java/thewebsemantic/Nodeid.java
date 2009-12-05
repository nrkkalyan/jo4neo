package thewebsemantic;

import org.neo4j.api.core.Node;

/**
 * Used to inject neo4j context into a javabean.
 */
public class Nodeid {
	
	long id;
	Class<?> type;
	
	public Nodeid(long id, Class<?> c) {
		this.id = id;
		type = c;
	}
	
	public Nodeid(Class<?> c) {
		this.id = Long.MIN_VALUE;
		type = c;
	}
	
	public long id() {
		return id;
	}

	public String hostingType() {
		return type.getName();
	}

	public boolean valid() {
		return id != Long.MIN_VALUE;
	}
	
	public Node mirror(IndexedNeo ns) {
		return (valid()) ? ns.getNodeById(id):newNode(ns);
	}

	private Node newNode(IndexedNeo neo) {
		Node newNode = neo.createNode();
		newNode.setProperty(Nodeid.class.getName(), type.getName());
		id = newNode.getId();
		//find metanode for type t
		Node metanode = neo.getMetaNode(type);		
		metanode.createRelationshipTo(newNode, Relationships.HAS_MEMBER);
		newNode.createRelationshipTo(metanode, Relationships.HAS_TYPE);	
		if (type.isAnnotationPresent(Timeline.class))
			neo.getTimeLine(type).addNode(newNode, System.currentTimeMillis());
		return newNode;	
	}

}
