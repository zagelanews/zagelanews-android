package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.geo.PointDto;

public class FeedsSummaryDto extends ZagelnewsDto{
	
	private Integer feedSeq;
	private PointDto feedLocation;
	private Integer feedAuthorType;
	private Integer feedType;
	private String feedText;
	public String getFeedText() {
		return feedText;
	}
	public void setFeedText(String feedText) {
		this.feedText = feedText;
	}
	public Integer getFeedSeq() {
		return feedSeq;
	}
	public void setFeedSeq(Integer feedSeq) {
		this.feedSeq = feedSeq;
	}
	public PointDto getFeedLocation() {
		return feedLocation;
	}
	public void setFeedLocation(PointDto feedLocation) {
		this.feedLocation = feedLocation;
	}
	public Integer getFeedAuthorType() {
		return feedAuthorType;
	}
	public void setFeedAuthorType(Integer feedAuthorType) {
		this.feedAuthorType = feedAuthorType;
	}
	public Integer getFeedType() {
		return feedType;
	}
	public void setFeedType(Integer feedType) {
		this.feedType = feedType;
	}
	

}
