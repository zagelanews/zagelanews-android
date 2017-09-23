package org.zagelnews.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.constants.ServerConstants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EncodingUtils {
	public static byte [] BitMapToByteArray(Bitmap bmp){
		if(bmp==null){
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static Bitmap ByteArrayToBitMap(byte [] byteArray){
		if(byteArray==null){
			return null;
		}
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		return bmp;
	}

	public static Bitmap ImageIdToBitMap(String imageBuck, String imageId) {

		Bitmap bitmap = null;
		try {
			String urlBuff = getImageUrl(imageBuck, imageId);
			if(urlBuff!=null&&urlBuff.trim().length()>0){
				URL url = new URL(urlBuff);
				bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return bitmap;
	}

	public static String getImageUrl(String imageBuck, String imageId) {
		StringBuffer urlBuff = new StringBuffer();
		if(imageId!=null&&imageId.trim().length()>ImagesConstants.MIN_IMAGE_URL_SIZE){
			urlBuff = new StringBuffer(ServerConstants.S3_URL).append("/").append(imageBuck).append("/").append(imageId).append(ImagesConstants.SAMPLE_IMAGE_TAGE);
		}
		return urlBuff.toString();
	}
	
	public static String getFullSizeImageUrl(String imageBuck, String imageId) {
		StringBuffer urlBuff = new StringBuffer(ServerConstants.S3_URL).append("/").append(imageBuck).append("/").append(imageId).append(ImagesConstants.FULL_SIZE_IMAGE_TAGE);
		return urlBuff.toString();
	}
}
