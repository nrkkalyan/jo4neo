package test.specific;

import org.junit.Test;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;

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
