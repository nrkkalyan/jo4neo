package thewebsemantic;

import java.io.Serializable;
import java.util.Map;

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
	public static final String entityIndex = "ENTITY_INDEX";
	

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
	
	public Node getNodeByIndex(Object o) {
		return index.getSingleNode(entityIndex, o);
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

}
