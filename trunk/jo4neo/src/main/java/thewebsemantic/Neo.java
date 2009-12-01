package thewebsemantic;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;

/**
 * Used to inject neo4j context into a javabean.
 */
public class Neo {
	
	long id;
	Class<?> type;
	
	public Neo(long id, Class<?> c) {
		this.id = id;
		type = c;
	}
	
	public Neo(Class<?> c) {
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

	private Node newNode(IndexedNeo ns) {
		Node n = ns.createNode();
		n.setProperty(Neo.class.getName(), type.getName());
		id = n.getId();
		//find metanode for type t
		Node metanode = ns.getMetaNode(type.getName());		
		metanode.createRelationshipTo(n, Relationships.HAS_MEMBER);
		return n;	
	}

}
