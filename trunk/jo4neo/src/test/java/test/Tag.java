package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Tag {
	Neo neo;
	
	@Graph(index="tagname")
	String name;
	
	@Graph
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
