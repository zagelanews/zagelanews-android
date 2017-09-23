package org.zagelnews.dtos.feeds;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class SampleFullImageDto {
	
	private ImageView sampleImg;
	private Bitmap fullSizeImage;
	private String imagePath;
	private String imageUrl;
	
	public SampleFullImageDto(ImageView sampleImg, Bitmap fullSizeImage) {
		super();
		this.sampleImg = sampleImg;
		this.fullSizeImage = fullSizeImage;
	}
	
	public ImageView getSampleImg() {
		return sampleImg;
	}
	public void setSampleImg(ImageView sampleImg) {
		this.sampleImg = sampleImg;
	}
	public Bitmap getFullSizeImage() {
		return fullSizeImage;
	}
	public void setFullSizeImage(Bitmap fullSizeImage) {
		this.fullSizeImage = fullSizeImage;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
