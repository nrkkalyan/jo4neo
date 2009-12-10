package example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

import jo4neo.*;



public class ContextListener implements ServletContextListener {

	public static PersistenceManager pm;
	private static NeoService neo;
	
	public void contextDestroyed(ServletContextEvent ev) {
		pm.close();
		neo.shutdown();	
	}

	public void contextInitialized(ServletContextEvent ev) {
		System.out.println("hello");
		neo = new EmbeddedNeo("neo_base"); 
		pm = new PersistenceManager(neo);
	}


}
