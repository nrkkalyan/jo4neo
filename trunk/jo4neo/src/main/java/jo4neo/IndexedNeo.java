package jo4neo;

import static jo4neo.TypeWrapperFactory.$;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import jo4neo.util.RelationFactory;

import org.neo4j.api.core.DynamicRelationshipType;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.index.Isolation;
import org.neo4j.util.index.LuceneIndexService;
import org.neo4j.util.timeline.Timeline;

public class IndexedNeo implements NeoService {

	private NeoService neo;
	private LuceneIndexService index;
	private RelationFactory relFactory;
	private boolean isClosed = false;
	private Map<Class<?>, Timeline> timelines;

	public IndexedNeo(NeoService neo) {
		this.neo = neo;
		index = new LuceneIndexService(neo);
		relFactory = new RelationFactoryImpl();
		timelines = new HashMap<Class<?>, Timeline>();
	}

	public synchronized void close() {
		index.shutdown(); 
		isClosed = true;
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

	public Node getNodeById(Nodeid id) {
		return neo.getNodeById(id.id());
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
		close();
		neo.shutdown();
	}

	public RelationFactory getRelationFactory() {
		return relFactory;
	}

	public Node createNode() {
		return neo.createNode();
	}

	protected Node asNode(Object o) {
		return getNodeById($(o).id(o));
	}

	protected Node getMetaNode(Class<?> type) {
		Node metanode;
		RelationshipType relType = DynamicRelationshipType.withName(type
				.getName());
		Node root = neo.getReferenceNode();
		Iterable<Relationship> r = root.getRelationships(relType);
		if (r.iterator().hasNext())
			metanode = r.iterator().next().getEndNode();
		else {
			metanode = neo.createNode();
			metanode.setProperty(Nodeid.class.getName(), type.getName());
			root.createRelationshipTo(metanode, relType);
		}
		return metanode;
	}

	protected Timeline getTimeLine(Class<?> c) {

		if (timelines.containsKey(c))
			return timelines.get(c);

		Node metaNode = getMetaNode(c);
		org.neo4j.util.timeline.Timeline t = new org.neo4j.util.timeline.Timeline(
				c.getName(), metaNode, neo);
		timelines.put(c, t);
		return t;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public Node getURINode(URI uri) {
		Transaction t = neo.beginTx();
		try {
			Node n = getIndexService().getSingleNode(URI.class.getName(),
					uri.toString());
			if (n == null) {
				n = createNode();
				getIndexService().index(n, URI.class.getName(), uri.toString());
				n.setProperty("uri", uri.toString());
				n.setProperty(Nodeid.class.getName(), URI.class.getName());
			}
			t.success();
			return n;
		} finally {
			t.finish();
		}
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
