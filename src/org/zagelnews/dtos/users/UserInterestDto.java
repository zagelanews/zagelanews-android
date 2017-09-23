package org.zagelnews.dtos.users;

import org.zagelnews.dtos.ZagelnewsDto;

public class UserInterestDto extends ZagelnewsDto{
	
	
	private Integer userId;
	private Integer interestKey;
	private Integer interestStatus;
	private double latitude;
	private double longitude;
	private double radius;
	private String interestName;
	private String createTimestamp;
	
	boolean selected = false;
	
	public UserInterestDto() {
	}
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getInterestKey() {
		return interestKey;
	}
	public void setInterestKey(Integer interestKey) {
		this.interestKey = interestKey;
	}
	public Integer getInterestStatus() {
		return interestStatus;
	}
	public void setInterestStatus(Integer interestStatus) {
		this.interestStatus = interestStatus;
	}
	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getRadius() {
		return radius;
	}


	public void setRadius(double radius) {
		this.radius = radius;
	}


	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
