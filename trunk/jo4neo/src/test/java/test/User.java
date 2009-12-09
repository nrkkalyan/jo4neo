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
	@neo("role") Collection<Role> roles;
	
	public User() {
		roles = new LinkedList<Role>();
	}
	

}
