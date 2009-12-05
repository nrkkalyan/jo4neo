package test;

import java.io.File;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

import static org.junit.Assert.*;

import thewebsemantic.IndexedNeo;
import thewebsemantic.Nodeid;
import thewebsemantic.NeoComparator;
import thewebsemantic.PersistenceManager;
import thewebsemantic.TypeWrapper;
import thewebsemantic.TypeWrapperFactory;

public class TestLazyCollection {

	static NeoService neobase;
	static IndexedNeo neo;
	
	@BeforeClass
	public static void setup() {
		deleteDirectory(new File("neo_store"));
		neobase = new EmbeddedNeo("neo_store");
		neo = new IndexedNeo(neobase);
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

		TypeWrapper tw = TypeWrapperFactory.wrap(p3);
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
					State s = pm2.getSingle(State.class, State.STATE_CODE_IDX, "NY");
					
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
		
		ny = pm.getSingle(State.class, State.STATE_CODE_IDX, "NY");
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