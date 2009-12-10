package test;

import static org.junit.Assert.assertTrue;
import jo4neo.ObjectGraph;

import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;


public class TestErrors {
	
	@Test
	public void basic() {
		NeoService neo = new EmbeddedNeo("neo_store");
		ObjectGraph pm = new ObjectGraph(neo);
		boolean caught = false;
		try {
			AintGotId bad = new AintGotId();
			bad.setName("I'm bad");
			pm.persist(bad);
		} catch (Exception e) {
			caught = true;
		} finally {
			pm.close();
			neo.shutdown();
		}
		assertTrue(caught);
	}

}
