package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ZagelnewsDto;



/**
 * Wrapper for the image
 * @author oomran
 *
 */
public class ImageDto extends ZagelnewsDto{
	
	private byte[] sampleImage;
	private byte[] fullImage;
	private Integer imageKey;
	private String fullImageUrl;
	private String sampleImageUrl;
	
	public ImageDto() {
	}
	public ImageDto(byte[] sampleImage, byte[] fullImage) {
		this.sampleImage = sampleImage;
		this.fullImage = fullImage;
	}
	
	public ImageDto(Integer imageKey, String sampleImageUrl, String fullImageUrl) {
		this.sampleImageUrl = sampleImageUrl;
		this.fullImageUrl = fullImageUrl;
		this.imageKey = imageKey;
	}	
	
	public byte[] getSampleImage() {
		return sampleImage;
	}
	public void setSampleImage(byte[] sampleImage) {
		this.sampleImage = sampleImage;
	}
	public byte[] getFullImage() {
		return fullImage;
	}
	public void setFullImage(byte[] fullImage) {
		this.fullImage = fullImage;
	}
	public Integer getImageKey() {
		return imageKey;
	}
	public void setImageKey(Integer imageKey) {
		this.imageKey = imageKey;
	}
	public String getFullImageUrl() {
		return fullImageUrl;
	}
	public void setFullImageUrl(String fullImageUrl) {
		this.fullImageUrl = fullImageUrl;
	}
	public String getSampleImageUrl() {
		return sampleImageUrl;
	}
	public void setSampleImageUrl(String sampleImageUrl) {
		this.sampleImageUrl = sampleImageUrl;
	}
	
}
