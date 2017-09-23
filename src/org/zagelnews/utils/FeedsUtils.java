package org.zagelnews.utils;

import java.util.List;

import org.zagelnews.constants.FeedsConstants.NotificationTypesConst;
import org.zagelnews.dtos.feeds.UserNotificationDto;


public class FeedsUtils {

	public static boolean hasNewNotifications(List<UserNotificationDto> notifications){
		boolean hasNewNotifications = false;
		if(notifications!=null && notifications.size()>0){
			for(UserNotificationDto dto: notifications){
				if(dto.getNotificationStatus()!=null&&dto.getNotificationStatus().equals(0)){
					if(NotificationTypesConst.FEEDS.equals(dto.getNotificationType())){
						hasNewNotifications = true;
						break;
					}
				}
			}
		}
		return hasNewNotifications; 
	}
}
