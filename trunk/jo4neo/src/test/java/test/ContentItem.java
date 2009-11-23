package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class ContentItem {
	Neo neo;
	@Graph
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
