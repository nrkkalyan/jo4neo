package test;

import java.util.Collection;

import jo4neo.Nodeid;
import jo4neo.neo;


public class Role {
	transient Nodeid id;
	
	@neo(index=true) String name;
	@neo Collection<Role> includes;	
	@neo Role parent;
	@neo(inverse="role") Collection<User> users;
	
	public Role() {		
	}
	
	public Role(String name) {
		this.name = name;
	}
	
	public boolean hasRole(Role r) {
		Role candidate = this;
		while(candidate != null) {
			if (candidate.equals(r))
				return true;
			if (candidate == candidate.parent)
				break;
			candidate = candidate.parent;
		}
		return false;
	}

	public boolean equals(Object o) {
		if (o==null || !(o instanceof Role))
			return false;
		return equals((Role)o);
	}

	public boolean equals(Role r) {
		if (name==null)
			return r==null;
		return name.equals(r.name);
	}
}
