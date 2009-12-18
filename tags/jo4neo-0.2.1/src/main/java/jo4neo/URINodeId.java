package jo4neo;

import java.net.URI;

import org.neo4j.api.core.Node;

class URINodeId extends Nodeid {

	
	URI uri;
	public static final String uriconst = "uri";
	
	public URINodeId(URI o) {
		uri = o;
	}

	@Override
	public Node mirror(IndexedNeo ns) {
		return ns.getURINode(uri);
	}
	
	

}
