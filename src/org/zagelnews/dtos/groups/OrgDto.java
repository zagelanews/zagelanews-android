package org.zagelnews.dtos.groups;

public class OrgDto {
	private Long orgId;
	private Long userId;
	private String orgName;
	private String orgDesc;
	private String imageId;
	private String imageBuck;
	private String orgCreateTime;
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
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgDesc() {
		return orgDesc;
	}
	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}
	public String getOrgCreateTime() {
		return orgCreateTime;
	}
	public void setOrgCreateTime(String orgCreateTime) {
		this.orgCreateTime = orgCreateTime;
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
}
