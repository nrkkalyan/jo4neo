package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jo4neo.ObjectGraph;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;


public class TestBasic {

	static NeoService neo;
	static ObjectGraph pm;

	@BeforeClass
	public static void setup() {
		neo = new EmbeddedNeo("neo_store");
		pm = new ObjectGraph(neo);
	}

	@AfterClass
	public static void teardown() {
		pm.close();
		neo.shutdown();
	}

	@Test
	public void testIndex() {
		
		Transaction t = neo.beginTx();
		neo.beginTx();
		try {
			Hotel h = new Hotel();
			h.setName("Hyatt Boston");
			pm.persist(h);
			Hotel hotel = new Hotel();
			Collection<Hotel> hotels = pm.find(hotel).where(hotel.name).is("Hyatt Boston").results();
			
			assertEquals(hotels.size(), 1);
			pm.delete(h);
			hotels = pm.find(hotel).where(hotel.name).is("Hyatt Boston").results();
			assertEquals(hotels.size(), 0);
			t.success();
		} finally {
			t.finish();
		}
	}

	@Test
	public void collections() {

		Transaction t = neo.beginTx();
		neo.beginTx();
		try {
			Student s1 = new Student();

			Course c1 = new Course();
			c1.setName("math101");
			Course c2 = new Course();
			c2.setName("spanish101");
			Course c3 = new Course();
			c3.setName("history101");

			s1.getCourses().add(c1);
			s1.getCourses().add(c2);
			s1.getCourses().add(c3);

			pm.persist(s1);

			Student s1ref = pm.get(Student.class, s1.neo.id());
			assertEquals(s1ref.getCourses().size(), 3);
			t.success();
		} finally {
			t.finish();
			

		}
	}

	@Test
	public void collections2() {
		Transaction t = neo.beginTx();
		neo.beginTx();
		try {
			Student s1 = new Student();
			Course c1 = new Course();
			c1.setName("math101");
			Course c2 = new Course();
			c2.setName("spanish101");
			Course c3 = new Course();
			c3.setName("history101");
			s1.getCourses().add(c1);
			s1.getCourses().add(c2);
			s1.getCourses().add(c3);
			pm.persist(s1);
			t.success();
			t.finish();

			Student s1ref = pm.get(Student.class, s1.neo.id());
			assertEquals(s1ref.getCourses().size(), 3);
		} finally {
			
		}

	}

	@Test
	public void collections3() {

		try {
			Course c1 = new Course();
			c1.setName("math101");
			Course c2 = new Course();
			c2.setName("spanish101");
			Course c3 = new Course();
			c3.setName("history101");

			Student s1 = new Student();
			s1.setName("Mark Lesser");

			s1.setCourses(Arrays.asList(c1, c2, c3));

			pm.persist(s1);

			assertNotNull(c3.neo);
			c1.setName("modified");
			c2.setName("modified");
			c3.setName("modified");
			pm.persist(c1);
			pm.persist(c2);
			pm.persist(c3);

			s1 = pm.get(Student.class, s1.neo.id());
			assertEquals(3, s1.getCourses().size());
			for (Course c : s1.getCourses()) {
				assertEquals("modified", c.getName());
			}
		} finally {
			
		}

	}

	@Test
	public void nullCollections() {
		Transaction t = neo.beginTx();
		neo.beginTx();
		try {
			Student s1 = new Student();
			s1.setCourses(null);
			pm.persist(s1);
			t.success();
		} finally {
			t.finish();
			
		}
	}

	@Test
	public void collections4() {

		try {
			Student s1 = new Student();
			s1.setName("student");

			Course c1 = new Course();
			c1.setName("math101");
			Course c2 = new Course();
			c2.setName("spanish101");
			Course c3 = new Course();
			c3.setName("history101");

			s1.getCourses().add(c1);
			s1.getCourses().add(c2);
			s1.getCourses().add(c3);
			pm.persist(s1);
			long id = s1.neo.id();

			Student s1ref = pm.get(Student.class, id);
			assertEquals(s1ref.getCourses().size(), 3);
			Course remove = null;
			for (Course course : s1ref.getCourses()) {
				if ("spanish101".equals(course.getName()))
					remove = course;
			}
			s1ref.getCourses().remove(remove);
			pm.persist(s1ref);

			s1 = pm.get(Student.class, id);
			assertEquals(2, s1.getCourses().size());
			assertEquals(s1.getName(), "student");
		} finally {
			
		}
	}

	@Test
	public void nullrelation() {
		Transaction t = pm.beginTx();
		long id = 0;
		try {
			Person friend = new Person();
			friend.setFirstName("friend");

			Person p1 = new Person();
			p1.setAge(32);
			p1.setFirstName("taylor");
			p1.setLastName("cowan");
			p1.setFriend(friend);

			pm.persist(p1);

			id = pm.get(p1).getId();
			t.success();
		} finally {
			t.finish();
			
		}

		t = pm.beginTx();
		try {
			Person p = pm.get(Person.class, id);
			Person friend = p.getFriend();
			assertEquals("friend", friend.getFirstName());
			p.setFriend(null);
			pm.persist(p);
			t.success();
		} finally {
			t.finish();
			
		}

		t = pm.beginTx();
		try {
			Person p = pm.get(Person.class, id);
			assertTrue(p.getFriend() == null);
			t.success();
		} finally {
			t.finish();
			
		}
	}

	@Test
	public void basic() {
		Transaction t = neo.beginTx();
		try {
			Address a = new Address();
			a.state = "TX";
			a.city = "Keller";
			a.street = "123 Oak";

			Person friend = new Person();
			friend.setFirstName("friend");
			friend.setAddress(a);

			Person p1 = new Person();
			p1.setAge(32);
			p1.setFirstName("taylor");
			p1.setLastName("cowan");
			p1.setAddress(a);
			p1.setFriend(friend);
			friend.setFriend(p1);
			pm.persist(p1);

			Person p2 = pm.get(Person.class, p1.neo.id());
			assertEquals(32, p2.getAge());
			assertNotNull(p2.getFriend());
			assertEquals(p2.getFriend().getFirstName(), "friend");
			assertNotNull(p2.getFriend().getFriend());
			assertTrue(p2 == p2.getFriend().getFriend());
			assertTrue(p1 == p1.getFriend().getFriend());
			t.success();
		} finally {
			t.finish();
			
		}
	}

	@Test
	public void notransaction() {
		try {
			Address a = new Address();
			a.state = "TX";
			a.city = "Keller";
			a.street = "123 Oak";

			Person friend = new Person();
			friend.setFirstName("friend");
			friend.setAddress(a);

			Person p1 = new Person();
			p1.setAge(32);
			p1.setFirstName("taylor");
			p1.setLastName("cowan");
			p1.setAddress(a);
			p1.setFriend(friend);
			friend.setFriend(p1);
			pm.persist(p1);

			Person p2 = pm.get(Person.class, p1.neo.id());
			assertEquals(32, p2.getAge());
			assertNotNull(p2.getFriend());
			assertEquals(p2.getFriend().getFirstName(), "friend");
			System.out.println(p2.getAddress().getState());
		} finally {
			
		}
	}

	@Test
	public void travel() {
		try {
			Hotel h = new Hotel();
			h.setName("Elite Hotel Savoy");
			ArrayList<Ammenity> ammenities = new ArrayList<Ammenity>();

			Ammenity pool = new Ammenity();
			pool.setName("pool");
			pool.setCode("SP");

			ammenities.add(pool);
			h.setAmmenities(ammenities);
			pm.persist(h);

		} finally {
			
		}
	}

}
