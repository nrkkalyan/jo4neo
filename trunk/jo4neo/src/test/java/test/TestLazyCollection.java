package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.TreeSet;

import jo4neo.NeoComparator;
import jo4neo.Nodeid;
import jo4neo.ObjectGraph;
import jo4neo.TypeWrapper;
import jo4neo.TypeWrapperFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;


public class TestLazyCollection extends BaseTest {


	
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
		Transaction t = graph.beginTx();
		try {
			State ny = new State();
			ny.setCode("NY");
			ny.setName("New York");
			graph.persist(ny);
		
			final City nyc = new City();
			nyc.setName("new york city");
			nyc.setState(ny);
			graph.persist(nyc);
			t.success();
		} finally {
			t.finish();
		}

		Runnable doit = new Runnable() {
			public void run() {
				Transaction t = graph.beginTx();
				try {
					State state = new State();
					State s = graph.find(state).where(state.code).is("NY").result();				
					City c = new City();
					c.setName("unknown");
					s.getCities().add(c);			
					graph.persist(s);
					t.success();				
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					t.finish();
				}
			}
		};
		
		for (int i=0; i<10; i++) {
			Thread thread = new Thread(doit);
			thread.start();
			thread.join();
		}

		t = graph.beginTx();
		try {
			State state = new State();
			State ny = new State();
			ny = graph.find(state).where(state.code).is("NY").result();
			assertEquals(10, ny.getCities().size());
			t.success();
		} finally {
			t.finish();
		}

	}

}