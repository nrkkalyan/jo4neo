package test;

import jo4neo.ObjectGraph;

import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.api.core.Transaction;


public class TestRoles extends BaseTest {

	@Test
	public void basic() {
		ObjectGraph p = new ObjectGraph(neo);
		Transaction t = p.beginTx();
		try {
			
			Role roleuser = new Role("roleuser");
			Role roleadmin = new Role("roleadmin");
			roleadmin.parent = roleuser;
			Role roledeveloper = new Role("roledeveloper");
			roledeveloper.parent = roleuser;
			Role rolesuperuser = new Role("rolesuperuser");
			rolesuperuser.parent = roleadmin;
			
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
			t.finish();
		}
		
		p = new ObjectGraph(neo);
		t = p.beginTx();
		try {
			User user = new User();
			user = p.find(user).where(user.id).is("user1").result();
			assertEquals(1, user.directRoles.size());
			Role r = new Role();
			System.out.println(System.currentTimeMillis());
			r = p.find(r).where(r.name).is("rolesuperuser").result();
			System.out.println(System.currentTimeMillis());

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
		}		
		
	}
}
