package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

import thewebsemantic.IndexedNeo;
import thewebsemantic.PersistenceManager;

public class TestTypes {
	
	@Test
	public void basic() {
		NeoService neobase = new EmbeddedNeo("neo_store");
		IndexedNeo neo = new IndexedNeo(neobase);
		
		
		PersistenceManager pm = new PersistenceManager(neo);
		Transaction t = neo.beginTx();
		try {
			PersistenceManager p = new PersistenceManager(neo);
			
			TypeBean bean = new TypeBean();
			//bean.setId(27);
			bean.setAges( new int[] {1,2,3,4,10});
			bean.setDate(new Date());
			bean.setIntItem(15);
			bean.setLongItem(10001);
			bean.setNames( new String[] {"a", "b", "c", "d" });
			bean.setTags(Arrays.asList("a", "b", "c", "d"));
			
			p.persist(bean);
			
			TypeBean check = p.get(TypeBean.class, bean.neo.id());
			assertEquals(15, check.getIntItem());
			assertArrayEquals(new String[] {"a", "b", "c", "d" }, check.getNames());
			assertArrayEquals(new int[] {1,2,3,4,10}, check.getAges());		
			assertNotNull(check.getTags());
			assertEquals(4, check.getTags().size());
			t.success();
		} catch (Exception e) {
			e.printStackTrace();
			t.failure();
		} finally {
			t.finish();
			pm.close();
			neo.shutdown();
		}
	}

}
