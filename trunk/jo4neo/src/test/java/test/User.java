package test;

import java.util.Collection;
import java.util.LinkedList;

import jo4neo.Nodeid;
import jo4neo.neo;


public class User {
	transient Nodeid neoid;
	
	@neo(index=true) String id;
	@neo String firstName;
	@neo String lastName;
	@neo Collection<Role> roles;
	
	public User() {
		roles = new LinkedList<Role>();
	}
	
	public boolean hasRole(Role role) {
		for (Role r : roles) {
			if (r.getClass().isAssignableFrom(role.getClass()))
				return true;
		}
		return false;
	}

}
