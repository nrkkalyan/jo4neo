package test;

import java.util.Collection;

import jo4neo.Nodeid;
import jo4neo.neo;


public class Role {
	transient Nodeid id;
	
	@neo(index=true) String name;
	@neo Collection<Role> includes;
	
	
}
