package test.specific;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import test.BaseTest;

public class TestLoader extends BaseTest {
	
	@Test
	public void basic() {
		Transaction t = neo.beginTx();
		try {
		for (Node n :neo.getAllNodes())
			graph.get(n);
		} finally {
			t.finish();
		}
	}

}
