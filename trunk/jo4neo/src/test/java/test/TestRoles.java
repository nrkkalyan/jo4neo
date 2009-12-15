package test;

import jo4neo.ObjectGraph;

import org.junit.Test;
import static org.junit.Assert.*;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;


public class TestRoles extends BaseTest {

	@Test
	public void inverse() {
		ObjectGraph p = new ObjectGraph(neo);
		Transaction t = p.beginTx();
		long id = 0;
		try {
			Role a = new Role("a");
			Role b = new Role("b", a);
			Role c = new Role("c", b);
			Role d = new Role("d", c);
			p.persist(d);
			t.success();
			id = d.id.id();
		} finally {
			t.finish();
		}
		
		Role role = new Role();
		for (Role r : p.get(Role.class)) {
			System.out.println(r.name + ":" + r.parent + ":" + r.child);
		}
		
		Node n = neo.getNodeById(id);
		for (Relationship rel : n.getRelationships()) {
			System.out.println(n.getProperty("name").toString() + rel.getType());
		}
		
		role = p.find(role).where(role.name).is("d").result();
		System.out.println(role.parent.name);
		//System.out.println(role.child.name);
		
	}
	@Test
	public void roleEqual() {
		Role r = new Role();
		Role s = new Role();
		assertFalse(r.equals(null));
		assertFalse(r.equals("hello"));
		assertTrue(r.equals(s));
		
		r.name = "foo";
		s.name = "bar";
		assertFalse(r.equals(s));
		s.name = "foo";
		assertTrue(r.equals(s));
	}
	@Test
	public void basic() {
		ObjectGraph p = new ObjectGraph(neo);
		Transaction t = p.beginTx();
		try {
			
			Role roleuser = new Role("roleuser");
			Role roleadmin = new Role("roleadmin", roleuser);
			Role roledeveloper = new Role("roledeveloper", roleuser);
			Role rolesuperuser = new Role("rolesuperuser", roleadmin);
			
			User u = new User();
			u.id = "user1";
			u.directRoles.add(roleuser);
			u.directRoles.add(roleuser);
			u.directRoles.add(roleuser);
			
			User a = new User();
			a.id = "user2";
			a.directRoles.add(rolesuperuser);
	
			p.persist(roleuser, roleadmin, roledeveloper, rolesuperuser, u, a);			
			t.success();
		} finally {
			p.close();
			t.finish();
		}
		
		p = new ObjectGraph(neo);
		t = p.beginTx();
		try {
			User user = new User();
			user = p.find(user).where(user.id).is("user1").result();
			assertEquals(1, user.directRoles.size());
			Role r = new Role();
			
			r = p.find(r).where(r.name).is("rolesuperuser").result();
			while (r != null) {
				System.out.println("has parent " + r.name);
				r = r.parent;
			}
			r = new Role();
			r = p.find(r).where(r.name).is("roleuser").result();
			while (r != null) {
				System.out.println(r.name);
				r = r.child;
			}
			

			assertFalse(user.hasRole(r));
			
			r = user.directRoles.iterator().next();
			assertEquals("roleuser", r.name);
			
			
			r = p.find(r).where(r.name).is("roleuser").result();
			assertEquals(1, r.users.size());
			
			user = p.find(user).where(user.id).is("user2").result();
			assertTrue(user.hasRole(r));
			
			r = p.find(r).where(r.name).is("roleadmin").result();
			assertTrue(user.hasRole(r));
			
			r = p.find(r).where(r.name).is("rolesuperuser").result();
			assertTrue(user.hasRole(r));


			assertEquals(4, p.getMostRecent(Role.class, 4).size());
			assertEquals(3, p.getMostRecent(Role.class, 3).size());
			assertEquals(2, p.getMostRecent(Role.class, 2).size());
			assertEquals(1, p.getMostRecent(Role.class, 1).size());
			assertEquals(4, p.getMostRecent(Role.class, 5).size());
			
			

			t.success();
		} finally {
			t.finish();
			p.close();
		}		
		
	}
}
