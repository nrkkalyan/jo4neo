package jo4neo;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import static jo4neo.Relationships.*;

/**
 * Used to inject neo4j context into a javabean.  Before persisting a 
 * javabean/POJO with jo4neo your bean should have a Nodeid field declared
 * as transient:
 * 
 * <code>
 * public DomainObject {
 *     //used be jo4neo
 *     private transient Nodeid id;  
 *     // indicates a field to be persisted in graph  
 *     @neo private String name;
 *     ...
 * }
 * </code>
 * This example is the minumum necessary to augment a graph with a single node
 * holding a single property.
 */
public class Nodeid {
	
	long id;
	Class<?> type;
	
	public Nodeid() {}
	
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

	/**
	 * Creates a new node within the context of a given javaclass.  First the node
	 * is annotated with the classname.  The neo4j node's id, as a long, is remembered
	 * so that the containing java object knows to where and from where its values will be persisted.
	 * Next the node is related to a "metanode" which represents metainformation about the javaclass
	 * within the neo graph.  This allows jo4neo to find all instances of a given type easily.
	 * Finally, if the javaclass is annotated with the Timeline annotation, the new node
	 * is stored within a timeline rooted at the javaclasses metanode. 
	 * @param neo
	 * @return
	 */
	private Node newNode(IndexedNeo neo) {
		Node newNode = neo.createNode();
		newNode.setProperty(Nodeid.class.getName(), type.getName());
		id = newNode.getId();
		//find metanode for type t
		Node metanode = neo.getMetaNode(type);		
		newNode.createRelationshipTo(metanode, JO4NEO_HAS_TYPE);	
		if (type.isAnnotationPresent(Timeline.class))
			neo.getTimeLine(type).addNode(newNode, System.currentTimeMillis());
		
		//delete "latest" relation
		Node latest=null;
		for(Relationship r : metanode.getRelationships(JO4NEO_NEXT_MOST_RECENT, Direction.OUTGOING)) {
			latest = r.getEndNode();
			r.delete();
		}
		if (latest!=null)
			newNode.createRelationshipTo(latest, JO4NEO_NEXT_MOST_RECENT);
		metanode.createRelationshipTo(newNode, JO4NEO_NEXT_MOST_RECENT);		
		return newNode;	
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