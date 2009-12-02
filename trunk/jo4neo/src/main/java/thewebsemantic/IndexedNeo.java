package thewebsemantic;

import java.io.Serializable;
import java.util.Map;

import org.neo4j.api.core.DynamicRelationshipType;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.index.NeoIndexService;

public class IndexedNeo implements NeoService {
	
	private NeoService neo;
	private IndexService index;
	private RelationFactory relFactory;	

	public IndexedNeo(NeoService neo, RelationFactory factory) {
		this.neo = neo;
		index = new NeoIndexService(neo);
		relFactory = factory;
	}

	public void close() {
		index.shutdown();
	}
	
	public IndexService getIndexService() {
		return index;
	}
	
	public Transaction beginTx() {
		return neo.beginTx();
	}

	public boolean enableRemoteShell() {
		return neo.enableRemoteShell();
	}

	public boolean enableRemoteShell(Map<String, Serializable> arg0) {
		return neo.enableRemoteShell(arg0);
	}

	public Iterable<Node> getAllNodes() {
		return neo.getAllNodes();
	}

	public Node getNodeById(long arg0) {
		return neo.getNodeById(arg0);
	}

	public Node getReferenceNode() {
		return neo.getReferenceNode();
	}

	public Relationship getRelationshipById(long arg0) {
		return neo.getRelationshipById(arg0);
	}

	public Iterable<RelationshipType> getRelationshipTypes() {
		return neo.getRelationshipTypes();
	}

	public void shutdown() {
		neo.shutdown();
	}

	public RelationFactory getRelationFactory() {
		return relFactory;
	}
	
	public Node createNode() {
		return neo.createNode();
	}
	
	protected Node getMetaNode(String name) {
		Node metanode;
		RelationshipType relType = DynamicRelationshipType.withName(name);
		Node root = neo.getReferenceNode();
		Iterable<Relationship> r =  root.getRelationships(relType);
		if (r.iterator().hasNext())
			metanode = r.iterator().next().getEndNode();
		else {
			metanode = neo.createNode();
			metanode.setProperty(Neo.class.getName(), name);
			root.createRelationshipTo(metanode, relType);
		}
		return metanode;
	}
	
	protected Node getMetaNode(Class<?> c) {
		return getMetaNode(c.getName());
	}

}
