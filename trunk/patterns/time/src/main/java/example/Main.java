package example;

import java.util.Calendar;

import jo4neo.ObjectGraph;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

public class Main {
	public static void main(String[] args) {
		
		NeoService neo = new EmbeddedNeo("neo_store");
		ObjectGraph graph = new ObjectGraph(neo);
		try {
			
			Day d = Util.findDay(graph, Calendar.getInstance());
			System.out.println(d.value);
			
		} finally {
			graph.close();
			neo.shutdown();
		}
	}
}
