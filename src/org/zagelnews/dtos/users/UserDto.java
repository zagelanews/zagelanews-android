package org.zagelnews.dtos.users;


import java.util.Date;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.ImageDto;
import org.zagelnews.dtos.geo.PointDto;

public class UserDto extends ZagelnewsDto{
	
	private Integer profileType;
	private Integer sessionId;
	private Integer userId;
	private Integer credId;
	private String password;
	private String salt;
	private String email;
	private Integer userStatus;
	private Integer userCredStatus;
	private String fullName;
	private ImageDto profileImageRaw;
	private ImageDto coverImageRaw;
	private Integer cntCode;
	private Date birthDate;
	private Integer userGender;
	private String token;
	
	private PointDto pointOfInterest;
	


	/**
	 * Get the user id and name
	 * @param credId
	 * @param userId
	 * @param fullName
	 */
	public UserDto(Integer credId, Integer userId, String fullName) {
		this.credId = credId;
		this.userId = userId;
		this.fullName = fullName;
	}
	
	/**
	 * Get the user authentication
	 * @param userId
	 * @param password
	 * @param salt
	 * @param email
	 * @param userCredStatus
	 * @param userStatus
	 */
	public UserDto(Integer userId, Integer credId, String password, String salt, String email, Integer userCredStatus, Integer userStatus) {
		this.userId = userId;
		this.credId = credId;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.userCredStatus = userCredStatus;
		this.userStatus = userStatus;
	}
	
	
	/**
	 * Get the authenticated user information
	 * @param userId
	 * @param email
	 * @param fullName
	 * @param profileImageUrl
	 * @param coverImageUrl
	 * @param cntCode
	 */
	public UserDto(Integer userId, 
			String email, 
			String fullName, 
			String profileSampleImageUrl, 
			String profileFullImageUrl, 
			String coverSampleImageUrl, 
			String coverFullImageUrl, 
			Integer cntCode,
			Date birthDate,
			Integer userGender) {
		this.userId = userId;
		this.email = email;
		this.fullName = fullName;
		this.setProfileSampleImageUrl(profileSampleImageUrl);
		this.setProfileFullImageUrl(profileFullImageUrl);
		this.setCoverSampleImageUrl(coverSampleImageUrl);
		this.setCoverFullImageUrl(coverFullImageUrl);
		this.cntCode = cntCode;
		this.birthDate = birthDate;
		this.userGender = userGender;
	}
	
	/**
	 * Get the non-authenticated user information
	 * @param userId
	 * @param email
	 * @param fullName
	 * @param profileImageUrl
	 * @param coverImageUrl
	 * @param cntCode
	 */
	public UserDto(Integer profileType,
			Integer userId, 
			String fullName, 
			String profileSampleImageUrl, 
			String profileFullImageUrl, 
			String coverSampleImageUrl, 
			String coverFullImageUrl) {
		this.profileType = profileType;
		this.userId = userId;
		this.fullName = fullName;
		this.setProfileSampleImageUrl(profileSampleImageUrl);
		this.setProfileFullImageUrl(profileFullImageUrl);
		this.setCoverSampleImageUrl(coverSampleImageUrl);
		this.setCoverFullImageUrl(coverFullImageUrl);
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getCredId() {
		return credId;
	}

	public void setCredId(Integer credId) {
		this.credId = credId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public UserDto() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}



	public Integer getCntCode() {
		return cntCode;
	}

	public void setCntCode(Integer cntCode) {
		this.cntCode = cntCode;
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

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getUserCredStatus() {
		return userCredStatus;
	}

	public void setUserCredStatus(Integer userCredStatus) {
		this.userCredStatus = userCredStatus;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getUserGender() {
		return userGender;
	}

	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}
	
	public PointDto getPointOfInterest() {
		return pointOfInterest;
	}

	public void setPointOfInterest(PointDto pointOfInterest) {
		this.pointOfInterest = pointOfInterest;
	}

	public Integer getProfileType() {
		return profileType;
	}

	public void setProfileType(Integer profileType) {
		this.profileType = profileType;
	}
}
