package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ColorCodeDto;
import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.utils.ClientConfiguration;


public class FeedTypeDto extends ZagelnewsDto{
	private Integer feedTypeKey;
	private Integer colorKey;
	private String feedTypeAdesc;
	private Integer feedTypeCode;
	private String feedTypeEdesc;
    private ColorCodeDto feedTypeColor;
	
	public FeedTypeDto() {
	}

	public Integer getFeedTypeKey() {
		return this.feedTypeKey;
	}

	public void setFeedTypeKey(Integer feedTypeKey) {
		this.feedTypeKey = feedTypeKey;
	}

	public Integer getColorKey() {
		return this.colorKey;
	}

	public void setColorKey(Integer colorKey) {
		this.colorKey = colorKey;
	}

	public String getFeedTypeAdesc() {
		return this.feedTypeAdesc;
	}

	public void setFeedTypeAdesc(String feedTypeAdesc) {
		this.feedTypeAdesc = feedTypeAdesc;
	}

	public Integer getFeedTypeCode() {
		return this.feedTypeCode;
	}

	public void setFeedTypeCode(Integer feedTypeCode) {
		this.feedTypeCode = feedTypeCode;
	}

	public String getFeedTypeEdesc() {
		return this.feedTypeEdesc;
	}

	public void setFeedTypeEdesc(String feedTypeEdesc) {
		this.feedTypeEdesc = feedTypeEdesc;
	}
	
	public ColorCodeDto getFeedTypeColor() {
		return feedTypeColor;
	}

	public void setFeedTypeColor(ColorCodeDto feedTypeColor) {
		this.feedTypeColor = feedTypeColor;
	}

	@Override
	public String toString() {
		if(ClientConfiguration.lang.contains("ar")){
			return feedTypeAdesc;	
		}else{
			return feedTypeEdesc;
		}
		
	}
}