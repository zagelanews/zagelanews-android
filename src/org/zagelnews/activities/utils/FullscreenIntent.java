package org.zagelnews.activities.utils;

import java.util.List;

import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.dtos.feeds.ImageDto;
import org.zagelnews.ui.images.ImageLoader;
import org.zagelnews.utils.GeneralUtils;
import org.zagelnews.utils.IntentHelper;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FullscreenIntent extends BaseActivity {
	
	private boolean fromCache = true;
	
	int feedImageWidth;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_screen_intent);
		
		feedImageWidth = Double.valueOf(getResources().getDisplayMetrics().widthPixels).intValue();
		
		//load images
		final String feedImages= getIntent().getStringExtra("feedImages");
		
		ImageLoader imageLoader = new ImageLoader
				(this.getApplicationContext(), feedImageWidth,LayoutParams.MATCH_PARENT
				,ImageLoader.stub_id_noimage);
		
		
		
		if(!GeneralUtils.isStringEmpty(feedImages)){
			List<String>feedImagesUrls = GeneralUtils.commaSeperatedToList(feedImages);
			
			//load feed images
			if(feedImagesUrls!=null&&feedImagesUrls.size()>0){
				
				HorizontalScrollView scrollFooterProgress = (HorizontalScrollView)findViewById(R.id.feedImgsScrollLayout);
				LinearLayout imagesLayout = (LinearLayout) scrollFooterProgress.findViewById(R.id.feedImgsListLayout);
				
				
				for(String imageUrl : feedImagesUrls){
					ImageView img = new ImageView(this);
					img.setPadding(ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding);
					img.setLayoutParams(new LinearLayout.LayoutParams(feedImageWidth, LayoutParams.MATCH_PARENT));
					imageLoader.DisplayImage(imageUrl, img, fromCache);
					imagesLayout.addView(img);
				}
			}
		}else{
			final List<ImageDto> feedImagesDtosList= (List<ImageDto>)IntentHelper.getObjectForKey("imagesDtosList");
			//load feed images
			if(feedImagesDtosList!=null&&feedImagesDtosList.size()>0){
				
				HorizontalScrollView scrollFooterProgress = (HorizontalScrollView)findViewById(R.id.feedImgsScrollLayout);
				LinearLayout imagesLayout = (LinearLayout) scrollFooterProgress.findViewById(R.id.feedImgsListLayout);
				
				
				for(ImageDto imageDto : feedImagesDtosList){
					ImageView img = new ImageView(this);
					img.setPadding(ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding, ImagesConstants.feedImagesPadding);
					img.setLayoutParams(new LinearLayout.LayoutParams(feedImageWidth, LayoutParams.MATCH_PARENT));
					imageLoader.DisplayImage(imageDto.getFullImageUrl(), img, fromCache);
					imagesLayout.addView(img);
				}
			}
		}

	}
	
	@Override
	public void getTaskResult(Object result, Integer taskId) {
	}
}
