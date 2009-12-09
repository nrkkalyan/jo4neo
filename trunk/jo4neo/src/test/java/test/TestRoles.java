package test;

import jo4neo.PersistenceManager;

import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.api.core.Transaction;


public class TestRoles extends BaseTest {

	@Test
	public void basic() {
		PersistenceManager p = new PersistenceManager(neo);
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

			p.persist(roleuser, roleadmin, roledeveloper, rolesuperuser, u, a);			
			t.success();
		} finally {
			t.finish();
		}
		
		p = new PersistenceManager(neo);
		t = p.beginTx();
		try {
			User user = new User();
			user = p.find(user).where(user.id).is("user1").result();
			assertEquals(1, user.roles.size());
			
			Role r = user.roles.iterator().next();
			assertEquals("user", r.name);
			
			r = p.find(r).where(r.name).is("user").result();
			assertEquals(1, r.users.size());
			t.success();
		} finally {
			t.finish();
		}		
		
	}
}
