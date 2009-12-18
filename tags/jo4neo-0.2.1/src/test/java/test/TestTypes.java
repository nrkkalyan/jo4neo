package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import jo4neo.ObjectGraph;

import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;


public class TestTypes extends BaseTest {
	
	@Test
	public void index() {
		ObjectGraph p = new ObjectGraph(neo);
		Transaction t = p.beginTx();
		try {
			
			for (int i=0; i<100; i++) {
				FunkyWinkerBean o = new FunkyWinkerBean();
				o.setJ(i%10);
				p.persist(o);
			}
			t.success();
		} finally {
			t.finish();
		}
		
		
		t = p.beginTx();
		try {
			
			FunkyWinkerBean bean = new FunkyWinkerBean();
			Collection<FunkyWinkerBean> results = p.find(bean).where(bean.j).is(9).results();
			assertEquals(10, results.size());
			
			results = p.find(bean).where(bean.j).is(3).results();
			assertEquals(10, results.size());

		} finally {
			t.finish();
			p.close();			
		}
	}
	@Test
	public void basic() {
		ObjectGraph pm = new ObjectGraph(neo);
		Transaction t = pm.beginTx();
		try {
			ObjectGraph p = new ObjectGraph(neo);
			
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
		
		} finally {
			t.finish();
			pm.close();
		}
	}

}
