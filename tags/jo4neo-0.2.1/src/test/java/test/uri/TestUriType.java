package test.uri;

import java.net.URI;
import java.util.Collection;

import org.junit.Test;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;

import static org.junit.Assert.*;
import jo4neo.ObjectGraph;
import test.BaseTest;

public class TestUriType extends BaseTest{
	
	@Test
	public void basic() {
		URI uri = URI.create("http://neo4j.org");
		Review r = new Review();
		r.link = uri;
		r.content = "slick";
		r.stars = 5;
		
		ObjectGraph graph = new ObjectGraph(neo);
		
		graph.persist(r);
		
		Collection<Review> reviews = graph.getMostRecent(Review.class, 1);
		assertEquals(1, reviews.size());
		
		r = reviews.iterator().next();
		assertEquals(uri, r.link);
		
		Transaction t = graph.beginTx();
		try {
			Node n = graph.get(uri);
			System.out.println(n.getProperty("uri"));
			for (Relationship rel : n.getRelationships(Direction.INCOMING)) {
				System.out.println(rel.getType().name());
			}
		} finally {
			t.finish();
		}
		
		reviews = graph.find(r).where(r.stars).is(5).results();
		assertEquals(1, reviews.size());
		
	}

}
