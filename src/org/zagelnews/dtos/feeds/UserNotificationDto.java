package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ZagelnewsDto;

/**
 * The notification Dto that hold the notification for the given user
 * @author oomran
 *
 */
public class UserNotificationDto extends ZagelnewsDto{
	
	private Integer forUserId;
	private Integer objectSeq;
	private String objectDescription;
	private Integer notificationClass;
	private String notificationClassDesc;
	private Integer notificationType;
	private String notificationTypeDesc;
	private Integer notificationCount;
	private String notificationDate;
	private String formattedNotificationDate;
	private Integer notificationStatus;
	
	public Integer getForUserId() {
		return forUserId;
	}
	public void setForUserId(Integer forUserId) {
		this.forUserId = forUserId;
	}
	public Integer getObjectSeq() {
		return objectSeq;
	}
	public void setObjectSeq(Integer objectSeq) {
		this.objectSeq = objectSeq;
	}
	public String getObjectDescription() {
		return objectDescription;
	}
	public void setObjectDescription(String objectDescription) {
		this.objectDescription = objectDescription;
	}
	public Integer getNotificationClass() {
		return notificationClass;
	}
	public void setNotificationClass(Integer notificationClass) {
		this.notificationClass = notificationClass;
	}
	public String getNotificationClassDesc() {
		return notificationClassDesc;
	}
	public void setNotificationClassDesc(String notificationClassDesc) {
		this.notificationClassDesc = notificationClassDesc;
	}
	public Integer getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}
	public String getNotificationTypeDesc() {
		return notificationTypeDesc;
	}
	public void setNotificationTypeDesc(String notificationTypeDesc) {
		this.notificationTypeDesc = notificationTypeDesc;
	}
	public Integer getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}
	public String getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(String notificationDate) {
		this.notificationDate = notificationDate;
	}
	public String getFormattedNotificationDate() {
		return formattedNotificationDate;
	}
	public void setFormattedNotificationDate(String formattedNotificationDate) {
		this.formattedNotificationDate = formattedNotificationDate;
	}
	public Integer getNotificationStatus() {
		return notificationStatus;
	}
	public void setNotificationStatus(Integer notificationStatus) {
		this.notificationStatus = notificationStatus;
	}



}
