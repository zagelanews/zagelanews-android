package org.zagelnews.utils;

import org.zagelnews.constants.MapConstants;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class DraggableCircle {
	private Marker centerMarker;
	private Marker radiusMarker;
	private Circle circle;
	private Double radius;
	private boolean isDraggable;

	public DraggableCircle(GoogleMap googleMap, LatLng center, Double radius, boolean isDraggable) {
		//this.radius = radius/1000;//convert to km
		this.radius = radius;
		this.isDraggable = isDraggable;
		if(isDraggable){
			centerMarker = googleMap.addMarker(new MarkerOptions()
			.position(center)
			.draggable(true));
			radiusMarker = googleMap.addMarker(new MarkerOptions()
			.position(toRadiusLatLng(center, radius))
			.draggable(true)
			.icon(BitmapDescriptorFactory.defaultMarker(
					BitmapDescriptorFactory.HUE_AZURE)));
		}
		circle = googleMap.addCircle(new CircleOptions()
		.center(center)
		.radius(radius)
		.strokeWidth(7)
		.strokeColor(Color.BLUE)
		.fillColor(Color.TRANSPARENT));
	}
	public boolean onMarkerMoved(Marker marker) {
		if(isDraggable){
			if (marker.equals(centerMarker)) {
				circle.setCenter(marker.getPosition());
				radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
				return true;
			}
			if (marker.equals(radiusMarker)) {
				radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
				circle.setRadius(radius);
				return true;
			}
		}
		return false;
	}

	/** Generate LatLng of radius marker */
	private static LatLng toRadiusLatLng(LatLng center, Double radius) {
		Double radiusAngle = Math.toDegrees(radius / MapConstants.RADIUS_OF_EARTH_METERS) /
				Math.cos(Math.toRadians(center.latitude));
		return new LatLng(center.latitude, center.longitude + radiusAngle);
	}

	private static Double toRadiusMeters(LatLng center, LatLng radius) {
		float[] result = new float[1];
		Location.distanceBetween(center.latitude, center.longitude,
				radius.latitude, radius.longitude, result);
		return Double.valueOf(result[0]);
	}
	public Circle getCircle() {
		return circle;
	}
	public Double getRadius() {
		return radius;
	}
}
