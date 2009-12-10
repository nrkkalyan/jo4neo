package example.model;

import java.util.Collection;
import java.util.LinkedList;

public class Tag {
	
	private String name;
	private Collection<Post> members = new LinkedList<Post>();

	public Tag() {}
	
	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String s) {
		name = s;
	}

	public Collection<Post> getMembers() {
		return members;
	}

	public void setMembers(Collection<Post> members) {
		this.members = members;
	}
	
	public void addMember(Post p) {
		members.add(p);
	}

}
