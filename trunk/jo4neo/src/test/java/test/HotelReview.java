package test;

import java.util.Date;

import thewebsemantic.Graph;

public class HotelReview extends ContentItem implements Taggable {

	@Graph
	String reviewContent;
	
	@Graph
	Date reviewDate;

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
}
