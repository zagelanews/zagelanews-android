package org.zagelnews.dtos.feeds;

import org.zagelnews.adapters.VoteReasonsAdapter;
import org.zagelnews.dtos.ZagelnewsDto;

import android.widget.Button;
import android.widget.TextView;

public class VoteInputDto extends ZagelnewsDto{

	private TextView accuracyPercentage;
	private Button likeMe;
	private Button unLikeMe;
	private TextView voteCountTv;
	private VoteReasonsAdapter voteReasonsAdapter;
	private Integer feedSeq;
	private Integer voteType;
	private Integer voteReason;
	private FeedDto uf;
	
	public TextView getAccuracyPercentage() {
		return accuracyPercentage;
	}
	public void setAccuracyPercentage(TextView accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}
	public Button getLikeMe() {
		return likeMe;
	}
	public void setLikeMe(Button likeMe) {
		this.likeMe = likeMe;
	}
	public Button getUnLikeMe() {
		return unLikeMe;
	}
	public void setUnLikeMe(Button unLikeMe) {
		this.unLikeMe = unLikeMe;
	}
	public TextView getVoteCountTv() {
		return voteCountTv;
	}
	public void setVoteCountTv(TextView voteCountTv) {
		this.voteCountTv = voteCountTv;
	}
	public VoteReasonsAdapter getVoteReasonsAdapter() {
		return voteReasonsAdapter;
	}
	public void setVoteReasonsAdapter(VoteReasonsAdapter voteReasonsAdapter) {
		this.voteReasonsAdapter = voteReasonsAdapter;
	}
	public Integer getFeedSeq() {
		return feedSeq;
	}
	public void setFeedSeq(Integer feedSeq) {
		this.feedSeq = feedSeq;
	}
	public Integer getVoteType() {
		return voteType;
	}
	public void setVoteType(Integer voteType) {
		this.voteType = voteType;
	}
	public Integer getVoteReason() {
		return voteReason;
	}
	public void setVoteReason(Integer voteReason) {
		this.voteReason = voteReason;
	}
	public FeedDto getUf() {
		return uf;
	}
	public void setUf(FeedDto uf) {
		this.uf = uf;
	}
	
	

}
