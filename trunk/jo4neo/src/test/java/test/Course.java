package test;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class Course {
	
	Nodeid neo;
	@neo String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
