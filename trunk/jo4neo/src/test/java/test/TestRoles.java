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
			
			Role roleuser = new Role();
			roleuser.name = "user";
			Role roleadmin = new Role();
			Role roledeveloper = new Role();
			Role rolesuperuser = new Role();
			
			User u = new User();
			u.id = "user1";
			u.roles.add(roleuser);
			u.roles.add(roleuser);
			u.roles.add(roleuser);
			
			User a = new User();
			a.id = "user2";
			a.roles.add(roleadmin);
			a.roles.add(roleuser);

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
			assertEquals(1, user.roles.size());
			
			Role r = user.roles.iterator().next();
			assertEquals("user", r.name);
			
			r = p.find(r).where(r.name).is("user").result();
			assertEquals(2, r.users.size());

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
