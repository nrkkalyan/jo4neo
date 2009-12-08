package test;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

public class BaseTest {

	static NeoService neo;
	
	@BeforeClass
	public static void setup() {
		deleteDirectory(new File("neo_store"));
		neo = new EmbeddedNeo("neo_store");
	}
	
	@AfterClass
	public static void teardown() {
		neo.shutdown();
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
