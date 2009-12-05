package test;

import java.util.Date;

import thewebsemantic.neo;

public class HotelReview extends ContentItem implements Taggable {

	@neo
	String reviewContent;
	
	@neo
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
