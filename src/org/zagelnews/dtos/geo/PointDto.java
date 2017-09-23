package org.zagelnews.dtos.geo;


import org.zagelnews.dtos.ZagelnewsDto;

public class PointDto extends ZagelnewsDto{
	
	private Double latitude;
	private Double longitude;
	
	
	public PointDto(){
		
	}
	public PointDto(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public static PointDto getPointDto(String location) {
		Double latitude = null;
		Double longitude = null;

		String latlng = location.substring(location.indexOf('(')+1, location.indexOf(')'));

		String latlngArr [] = latlng.split(" ");

		latitude = Double.valueOf(latlngArr[0]);
		longitude = Double.valueOf(latlngArr[1]);

		PointDto pointDto = new PointDto(latitude, longitude);
		return pointDto;
	}
}
