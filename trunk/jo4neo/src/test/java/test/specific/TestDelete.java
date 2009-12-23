package test.specific;

import org.junit.Test;
import org.neo4j.api.core.Transaction;
import static org.junit.Assert.*;

import test.BaseTest;

public class TestDelete extends BaseTest {

	@Test
	public void simple() {
		Transaction t = graph.beginTx();
		try {
			for (int i=0; i<20; i++) {
				Apple a = new Apple();
				a.state = "Washington";
				a.type = "red delicious";
				graph.persist(a);
			}
			t.success();
		} finally {
			t.finish();
		}
		
		assertEquals(20, graph.get(Apple.class).size());
		for (Apple a: graph.get(Apple.class)) 
			assertEquals("Washington", a.state);
		
		t = graph.beginTx();
		try {
			for (Apple a: graph.get(Apple.class)) 
				graph.delete(a);
			t.success();
		} finally {
			t.finish();
		}
		
		assertEquals(0, graph.get(Apple.class).size());
	}
}
