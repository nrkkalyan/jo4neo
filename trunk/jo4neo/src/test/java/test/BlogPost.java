package test;

import java.util.Date;

import thewebsemantic.Graph;

public class BlogPost extends ContentItem implements Taggable {
	
	@Graph
	String title;
	@Graph
	Date publishDate;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}
