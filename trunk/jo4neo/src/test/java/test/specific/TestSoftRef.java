package test.specific;

import java.util.Arrays;

import jo4neo.ObjectGraph;

import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

public class TestSoftRef {
	
	@Test
	public void garbageCollected() {
		NeoService neo = new EmbeddedNeo("neo_store");
		ObjectGraph graph = new ObjectGraph(neo);			

		Transaction t = graph.beginTx();
		try {
			
			for (Orchard o : graph.get(Orchard.class))
				graph.delete(o);
			Apple a = new Apple();
			a.state = "WA";
			a.type = "Red Delicious";
			
			Orchard o = new Orchard();
			o.grows = Arrays.asList(a);
			o.name = "Johnson Orchard";
			graph.persist(o);
			t.success();
		} finally {
			t.finish();
		}
		Orchard o = new Orchard();
		o = graph.find(o).where(o.name).is("Johnson Orchard").result();
		
		graph.close();
		neo.shutdown();
		neo = null;
		graph = null;
		
		System.gc();
		
		Apple a = new Apple();
		a.state = "CA";
		a.type = "Jonah Gold";
		
		boolean caught = false;
		try {
			o.grows.add(a);
		} catch (UnsupportedOperationException e) {
			caught = true;
		}
		assertTrue(caught);
		
	}

}
