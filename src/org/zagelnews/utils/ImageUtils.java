package org.zagelnews.utils;

import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.dtos.feeds.SampleFullImageDto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageUtils {
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
            int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
        }

    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
             }
         }
         return inSampleSize;
        }
    
    
	//decode and compress the photo
	public static SampleFullImageDto decodeFile(Context context, String filePath) {
		Bitmap fullSizeImage = BitmapFactory.decodeFile(filePath);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//fullSizeImage.compress(CompressFormat.JPEG, 100, bos);
		
		
		int sampleImageWidth = 
				Double.valueOf(
						context.
						getResources().
						getDisplayMetrics().
						widthPixels).
						intValue();
		Bitmap bitmap = decodeSampledBitmapFromPath(
				filePath, 
				sampleImageWidth,
				sampleImageWidth);
		
		ImageView img = new ImageView(context);
		img.setLayoutParams(new LayoutParams(ImagesConstants.FEED_BODY_SAMPLE_IMAGE_SIZE,ImagesConstants.FEED_BODY_SAMPLE_IMAGE_SIZE));
		img.setImageBitmap(bitmap);
		img.setPadding(ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding);
		//holder.linlaFooterProgress.addView(img);
		//feedImgsList.add(new SampleFullImageDto(img, fullSizeImage));
		 SampleFullImageDto imageDto = new SampleFullImageDto(img, fullSizeImage);
		 imageDto.setImagePath(filePath);
		return imageDto;

	}

	
	public static void scaleImage(ImageView view, int bounding)
	{
	    // Get the ImageView and its bitmap
	    Drawable drawing = view.getDrawable();
	    if (drawing == null) {
	        return; // Checking for null & return, as suggested in comments
	    }
	    Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    //int bounding = ImagesConstants.FEED_SAMPLE_IMAGE_SIZE;

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    width = scaledBitmap.getWidth(); // re-use
	    height = scaledBitmap.getHeight(); // re-use
	    BitmapDrawable result = new BitmapDrawable(scaledBitmap);

	    // Apply the scaled bitmap
	    view.setImageDrawable(result);

	    // Now change ImageView's dimensions to match the scaled image
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams(); 
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);

	}

/*	private int dpToPx(int dp)
	{
	    float density =getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}*/
}
