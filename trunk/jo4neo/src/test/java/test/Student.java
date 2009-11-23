package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Student {
	Neo neo;
	
	@Graph 
	Collection<Course> courses = new ArrayList<Course>();

	@Graph 
	String name;
	
	public Collection<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(Collection<Course> courses) {
		this.courses = courses;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
