package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class Tag {
	Nodeid neo;
	
	@neo(index="tagname")
	String name;
	
	@neo
	Collection<Taggable> items = new ArrayList<Taggable>();
	
	public Collection<Taggable> getItems() {
		return items;
	}
	public void setItems(Collection<Taggable> items) {
		this.items = items;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
