package test;

import java.io.File;

import jo4neo.ObjectGraph;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

public class BaseTest {

	public static NeoService neo;
	public static ObjectGraph graph;
	
	@BeforeClass
	public static void setup() {
		neo = new EmbeddedNeo("neo_store");
		graph = new ObjectGraph(neo);
	}
	
	@AfterClass
	public static void teardown() {
		graph.close();
		neo.shutdown();
		deleteDirectory(new File("neo_store"));

	}
	
	@Test
	public void simple() {
	}
	
	static public boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	         	files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
}
