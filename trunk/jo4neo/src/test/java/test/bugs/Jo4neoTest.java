package test.bugs;

import java.util.Collection;

import jo4neo.ObjectGraph;
import jo4neo.ObjectGraphFactory;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class Jo4neoTest {

	public static void main(String args[]) {

		GraphDatabaseService neo = new EmbeddedGraphDatabase("bugs");
		ObjectGraph graph = ObjectGraphFactory.instance().get(neo);

		Transaction t = graph.beginTx();
		try {

			ExampleObject to = new ExampleObject("Hello", "World", 1);

			graph.persist(to);
			// to will always be added

			ExampleObject to2 = new ExampleObject("Hello", "World", 1);
			graph.persist(to2);

			ExampleObject to3 = new ExampleObject("Hello", "World", 2);

			graph.persist(to3);

			// to3 should be added as is different

			to3.objects.add(new ExampleObject("goodbye", "cruel world", 3));
			to3.objects.add(new ExampleObject("goodbyes", "cruel worlds", 4));

			graph.persist(to3);

			t.success();
		} finally {
			t.finish();
		}
		
		Collection<ExampleObject> nodes = graph.get(ExampleObject.class);
		for (ExampleObject o : nodes) {
			System.out.println(o.number + o.stringOne);
		}
		System.out.println("done");
		graph.close();
		neo.shutdown();
	}
}
