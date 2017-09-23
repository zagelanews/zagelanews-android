package org.zagelnews.constants;

import com.google.android.gms.maps.GoogleMap;

public interface MapConstants {
	
	int MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;

	public int MAX_AUTO_COMPLETE_RESULTS = 10;

	//for google maps circle demo	
	float GOOGLE_MAPS_ZOOM = 13.0f;
	float GOOGLE_MAPS_EXTRA_ZOOM = 15.0f;
	final double DEFAULT_RADIUS = 300;
	final double RADIUS_OF_EARTH_METERS = 6371009;
	final long SERVER_REFRESH_RATE = 30000;
	
}
