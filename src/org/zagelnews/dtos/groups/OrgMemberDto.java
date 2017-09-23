package org.zagelnews.dtos.groups;

public class OrgMemberDto {
	private Long orgId;
	private Long userId;
	private String joinTime;
	private String imageId;
	private String imageBuck;
	private String userFullName;
	private Long memberStatus;
	
	boolean selected = false;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public Long getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(Long memberStatus) {
		this.memberStatus = memberStatus;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImageBuck() {
		return imageBuck;
	}
	public void setImageBuck(String imageBuck) {
		this.imageBuck = imageBuck;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
