package test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

import thewebsemantic.PersistenceManager;

public class TestErrors {
	
	@Test
	public void basic() {
		NeoService neo = new EmbeddedNeo("neo_store");
		PersistenceManager pm = new PersistenceManager(neo);
		Transaction t = neo.beginTx();
		neo.beginTx();
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
