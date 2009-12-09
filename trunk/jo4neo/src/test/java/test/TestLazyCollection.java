package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.TreeSet;

import jo4neo.NeoComparator;
import jo4neo.Nodeid;
import jo4neo.PersistenceManager;
import jo4neo.TypeWrapper;
import jo4neo.TypeWrapperFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;


public class TestLazyCollection {

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
	
	@Test
	public void basic() {
		TreeSet<Object> set = new TreeSet<Object>(new NeoComparator());

		Person p1 = new Person();
		Person p2 = new Person();
		Person p3 = new Person();

		TypeWrapper tw = TypeWrapperFactory.$(p3);
		Nodeid neo = new Nodeid(12, Person.class);
		tw.setId(p3, neo);

		set.add(p1);
		assertFalse(set.add(p2));
		assertEquals(set.size(), 1);

		assertTrue(set.add(p3));
		assertEquals(set.size(), 2);
	}

	@Test
	public void threaded() throws InterruptedException {

		PersistenceManager pm = new PersistenceManager(neo);
		State ny = new State();
		ny.setCode("NY");
		ny.setName("New York");
		pm.persist(ny);
		
		final City nyc = new City();
		nyc.setName("new york city");
		nyc.setState(ny);
		pm.persist(nyc);
		Runnable doit = new Runnable() {
			public void run() {
				PersistenceManager pm2 = new PersistenceManager(neo);
				Transaction t = pm2.beginTx();
				try {
					State state = new State();
					State s = pm2.find(state).where(state.code).is("NY").result();
					
					City c = new City();
					c.setName("unknown");
					s.getCities().add(nyc);
					s.getCities().add(c);
				
					pm2.persist(s);
					t.success();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					t.finish();
				}
			}
		};
		
		for (int i=0; i<100; i++)
			new Thread(doit).start();

		Thread.sleep(5000);
		State state = new State();
		ny = pm.find(state).where(state.code).is("NY").result();
		assertEquals(101, ny.getCities().size());

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