package example.model;

import java.util.Collection;
import java.util.LinkedList;
import jo4neo.*;

public class Tag extends NeoBean<Tag> {
	
	public static final String index = "tag.name";
	
	@neo(index=true) public String name;
	@neo private Collection<Post> members = new LinkedList<Post>();

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
