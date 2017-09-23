package org.zagelnews.dtos.groups;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.ImageDto;

/**
 * @author oomran
 * List of the groups that user are member of
 */
public class GroupDto extends ZagelnewsDto{
	
	private Integer groupId;
	private Integer groupOwnerId;
	private ImageDto profileImageRaw;
	private ImageDto coverImageRaw;
	private String groupName;
	private String groupDesc;
	private Integer membershipStatus;
	private String joinTime;
	private boolean selected;
	
	public GroupDto() {
	}
	
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getGroupOwnerId() {
		return groupOwnerId;
	}
	public void setGroupOwnerId(Integer groupOwnerId) {
		this.groupOwnerId = groupOwnerId;
	}
	
	public ImageDto getProfileImageRaw() {
		return profileImageRaw;
	}
	public void setProfileImageRaw(ImageDto profileImageRaw) {
		this.profileImageRaw = profileImageRaw;
	}
	public ImageDto getCoverImageRaw() {
		return coverImageRaw;
	}
	public void setCoverImageRaw(ImageDto coverImageRaw) {
		this.coverImageRaw = coverImageRaw;
	}
	public String getProfileSampleImageUrl() {
		return profileImageRaw==null?null:profileImageRaw.getSampleImageUrl();
	}

	public void setProfileSampleImageUrl(String profileSampleImageUrl) {
		if(profileImageRaw==null){
			profileImageRaw = new ImageDto();
		}
		this.profileImageRaw.setSampleImageUrl(profileSampleImageUrl);
	}

	public String getProfileFullImageUrl() {
		return profileImageRaw==null?null:profileImageRaw.getFullImageUrl();
	}

	public void setProfileFullImageUrl(String profileFullImageUrl) {
		if(profileImageRaw==null){
			profileImageRaw = new ImageDto();
		}
		this.profileImageRaw.setFullImageUrl(profileFullImageUrl);
	}

	public String getCoverSampleImageUrl() {
		return coverImageRaw==null?null:coverImageRaw.getSampleImageUrl();
	}

	public void setCoverSampleImageUrl(String coverSampleImageUrl) {
		if(coverImageRaw==null){
			coverImageRaw = new ImageDto();
		}
		this.coverImageRaw.setSampleImageUrl(coverSampleImageUrl);
	}

	public String getCoverFullImageUrl() {
		return coverImageRaw==null?null:coverImageRaw.getFullImageUrl();
	}

	public void setCoverFullImageUrl(String coverFullImageUrl) {
		if(coverImageRaw==null){
			coverImageRaw = new ImageDto();
		}
		this.coverImageRaw.setFullImageUrl(coverFullImageUrl);
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	
	public Integer getMembershipStatus() {
		return membershipStatus;
	}
	public void setMembershipStatus(Integer membershipStatus) {
		this.membershipStatus = membershipStatus;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString() {
		return groupName;
	}
}
