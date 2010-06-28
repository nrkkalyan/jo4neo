package test.traverser;

import java.util.Collection;

import org.junit.Test;
import org.neo4j.graphdb.Transaction;

import test.BaseTest;

import static junit.framework.Assert.*;

public class TestTraversal extends BaseTest {
	
	@Test
	public void basic() {
		Transaction t = neo.beginTx();
		try {
			Module root = new Module("root");
			Module child = new Module("child");
			BasicItem item = new BasicItem("item");
			root.addBasicItem(item);
			root.addModule(child);
			graph.persist(root);
			t.success();
		} finally {
			t.finish();
		}	
		
		Collection<Module> modules = graph.get(Module.class);
		assertEquals(modules.size(),2);
		for (Module module : modules) {		
			System.out.println(module + " has the following elements...");
			for (Object obj : module.getElements()) {
				System.out.println(obj);
			}
		}
	}
}
