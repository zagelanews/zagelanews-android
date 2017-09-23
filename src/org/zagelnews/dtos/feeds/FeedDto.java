package org.zagelnews.dtos.feeds;

import java.util.List;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.geo.PointDto;

public class FeedDto extends ZagelnewsDto{
	
	//feed author information
	private Integer authorId;
	private Integer refGroupId;
	private String authorFullName;
	private String authorSampleImageUrl;
	private String authorFullImageUrl;
	
	private String coverFullImageUrl;
	
	//if the feed is based on user interest
	private Integer interestId;
	private double interestCenterLatitude;
	private double interestCenterLongitude;
	private double interestRadious;
	private String interestName;
	
	//feed main information
	private Integer feedSeq;
	private String feedText;
	private String feedDate;
	private String formatedfeedDate;
	private List<PointDto> feedLocations;
	
	
	//feed other info
	private Integer feedAccuracy;
	private Integer commentCount;
	private Integer voteCount;
	private Integer requesterVoteAction;
	private List<ImageDto>feedImages;
	private String firstFullImageUrl;
	private String firstSampleImageUrl;
	
	private Integer feedAuthorType;
	private Integer feedType;
	
	
	private boolean isMyOwnFeed;
	
	
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	public Integer getRefGroupId() {
		return refGroupId;
	}
	public void setRefGroupId(Integer refGroupId) {
		this.refGroupId = refGroupId;
	}
	public String getAuthorFullName() {
		return authorFullName;
	}
	public void setAuthorFullName(String authorFullName) {
		this.authorFullName = authorFullName;
	}
	public String getAuthorSampleImageUrl() {
		return authorSampleImageUrl;
	}
	public void setAuthorSampleImageUrl(String authorSampleImageUrl) {
		this.authorSampleImageUrl = authorSampleImageUrl;
	}
	public String getAuthorFullImageUrl() {
		return authorFullImageUrl;
	}
	public void setAuthorFullImageUrl(String authorFullImageUrl) {
		this.authorFullImageUrl = authorFullImageUrl;
	}
	public Integer getInterestId() {
		return interestId;
	}
	public void setInterestId(Integer interestId) {
		this.interestId = interestId;
	}
	public double getInterestCenterLatitude() {
		return interestCenterLatitude;
	}
	public void setInterestCenterLatitude(double interestCenterLatitude) {
		this.interestCenterLatitude = interestCenterLatitude;
	}
	public double getInterestCenterLongitude() {
		return interestCenterLongitude;
	}
	public void setInterestCenterLongitude(double interestCenterLongitude) {
		this.interestCenterLongitude = interestCenterLongitude;
	}
	public double getInterestRadious() {
		return interestRadious;
	}
	public void setInterestRadious(double interestRadious) {
		this.interestRadious = interestRadious;
	}
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public Integer getFeedSeq() {
		return feedSeq;
	}
	public void setFeedSeq(Integer feedSeq) {
		this.feedSeq = feedSeq;
	}
	public String getFeedText() {
		return feedText;
	}
	public void setFeedText(String feedText) {
		this.feedText = feedText;
	}
	public String getFeedDate() {
		return feedDate;
	}
	public void setFeedDate(String feedDate) {
		this.feedDate = feedDate;
	}
	public String getFormatedfeedDate() {
		return formatedfeedDate;
	}
	public void setFormatedfeedDate(String formatedfeedDate) {
		this.formatedfeedDate = formatedfeedDate;
	}
	public Integer getFeedAccuracy() {
		return feedAccuracy;
	}
	public void setFeedAccuracy(Integer feedAccuracy) {
		this.feedAccuracy = feedAccuracy;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public Integer getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}
	public Integer getRequesterVoteAction() {
		return requesterVoteAction;
	}
	public void setRequesterVoteAction(Integer requesterVoteAction) {
		this.requesterVoteAction = requesterVoteAction;
	}
	public List<ImageDto> getFeedImages() {
		return feedImages;
	}
	public void setFeedImages(List<ImageDto> feedImages) {
		this.feedImages = feedImages;
	}
	public String getFirstFullImageUrl() {
		return firstFullImageUrl;
	}
	public void setFirstFullImageUrl(String firstFullImageUrl) {
		this.firstFullImageUrl = firstFullImageUrl;
	}
	public String getFirstSampleImageUrl() {
		return firstSampleImageUrl;
	}
	public void setFirstSampleImageUrl(String firstSampleImageUrl) {
		this.firstSampleImageUrl = firstSampleImageUrl;
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
	public List<PointDto> getFeedLocations() {
		return feedLocations;
	}
	public void setFeedLocations(List<PointDto> feedLocations) {
		this.feedLocations = feedLocations;
	}
	public boolean isMyOwnFeed() {
		return isMyOwnFeed;
	}
	public void setMyOwnFeed(boolean isMyOwnFeed) {
		this.isMyOwnFeed = isMyOwnFeed;
	}
	public String getCoverFullImageUrl() {
		return coverFullImageUrl;
	}
	public void setCoverFullImageUrl(String coverFullImageUrl) {
		this.coverFullImageUrl = coverFullImageUrl;
	}

}
