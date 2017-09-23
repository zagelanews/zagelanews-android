package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ZagelnewsDto;

/**
 * dto for the feed comment
 * @author oomran
 *
 */
public class FeedCommentDto extends ZagelnewsDto{

	
	private Integer feedSeq;
	private Integer commentSeq;
	private Integer userIdWhoComment;
	private String commentText;
	private String commentDate;
	private String formatedCommentDate;
	private String sampleImageUrlForUserWhoComment;
	private String fullImageUrlForUserWhoComment;
	private String userFullNameWhoComment;
	
	public FeedCommentDto() {
	}
	
	
	public FeedCommentDto(Integer feedSeq, Integer commentSeq,
			Integer userIdWhoComment, String commentText, String commentDate,
			String userFullNameWhoComment, String sampleImageUrlForUserWhoComment,
			String fullImageUrlForUserWhoComment) {
		super();
		this.feedSeq = feedSeq;
		this.commentSeq = commentSeq;
		this.userIdWhoComment = userIdWhoComment;
		this.commentText = commentText;
		this.commentDate = commentDate;
		this.sampleImageUrlForUserWhoComment = sampleImageUrlForUserWhoComment;
		this.fullImageUrlForUserWhoComment = fullImageUrlForUserWhoComment;
		this.userFullNameWhoComment = userFullNameWhoComment;
	}
	public Integer getFeedSeq() {
		return feedSeq;
	}
	public void setFeedSeq(Integer feedSeq) {
		this.feedSeq = feedSeq;
	}
	public Integer getCommentSeq() {
		return commentSeq;
	}
	public void setCommentSeq(Integer commentSeq) {
		this.commentSeq = commentSeq;
	}
	public Integer getUserIdWhoComment() {
		return userIdWhoComment;
	}
	public void setUserIdWhoComment(Integer userIdWhoComment) {
		this.userIdWhoComment = userIdWhoComment;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public String getFormatedCommentDate() {
		return formatedCommentDate;
	}
	public void setFormatedCommentDate(String formatedCommentDate) {
		this.formatedCommentDate = formatedCommentDate;
	}
	public String getSampleImageUrlForUserWhoComment() {
		return sampleImageUrlForUserWhoComment;
	}
	public void setSampleImageUrlForUserWhoComment(
			String sampleImageUrlForUserWhoComment) {
		this.sampleImageUrlForUserWhoComment = sampleImageUrlForUserWhoComment;
	}
	public String getFullImageUrlForUserWhoComment() {
		return fullImageUrlForUserWhoComment;
	}
	public void setFullImageUrlForUserWhoComment(
			String fullImageUrlForUserWhoComment) {
		this.fullImageUrlForUserWhoComment = fullImageUrlForUserWhoComment;
	}
	public String getUserFullNameWhoComment() {
		return userFullNameWhoComment;
	}
	public void setUserFullNameWhoComment(String userFullNameWhoComment) {
		this.userFullNameWhoComment = userFullNameWhoComment;
	}
}
