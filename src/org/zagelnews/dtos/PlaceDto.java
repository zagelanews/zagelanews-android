package org.zagelnews.dtos;

public class PlaceDto {
	
	private double lon = 0;
	private double lat = 0;
	private String addressDesc;
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getAddressDesc() {
		return addressDesc;
	}
	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}
	
	@Override
	public String toString() {
		return addressDesc;
	}
	
	
}
