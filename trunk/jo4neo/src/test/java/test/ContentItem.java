package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class ContentItem {
	Nodeid neo;
	@neo
	Collection<Tag> tags = new ArrayList<Tag>();

	public Collection<Tag> getTags() {
		return tags;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}
	
	public String toString() {
		return getClass().getName();
	}
}
