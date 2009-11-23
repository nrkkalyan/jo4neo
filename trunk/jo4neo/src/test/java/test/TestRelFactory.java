package test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

import thewebsemantic.PersistenceManager;

public class TestRelFactory {

	static NeoService neo;
	
	@BeforeClass
	public static void setup() {
		neo = new EmbeddedNeo("neo_store");
	}
	
	@AfterClass
	public static void teardown() {
		neo.shutdown();
	}
	
	@Test
	public void basic() {

		PersistenceManager pm = new PersistenceManager(neo, new PeopleRelFactory());
		Transaction t = neo.beginTx();
		neo.beginTx();
		try {
			Person p1 = new Person();
			Person p2 = new Person();
			p1.setFriend(p2);
			pm.save(p1);
			t.success();			
		} finally {
			t.finish();
			pm.close();
		}
		
	}

}
