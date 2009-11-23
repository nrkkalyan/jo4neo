package test;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Course {
	
	Neo neo;
	@Graph String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
