package org.zagelnews.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zagelnews.constants.MapConstants;
import org.zagelnews.dtos.PlaceDto;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


public class PlacesAutoCompleteAdapter extends ArrayAdapter<PlaceDto> implements Filterable {
	private List<PlaceDto> resultList = new ArrayList<PlaceDto>();
	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public PlaceDto getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the auto complete results.
					resultList = autocomplete(constraint.toString());

					if(resultList!=null){
						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
				else {
					notifyDataSetInvalidated();
				}
			}};
			return filter;
	}


	private List<PlaceDto> autocomplete(String input) {
		List<PlaceDto> addresses = new ArrayList<PlaceDto>();
		if(input!=""){
			// Following adapted from Conder and Darcey, pp.321 ff.		
			Geocoder gcoder = new Geocoder(getContext());

			try{
				List<Address> results = null;
				if(Geocoder.isPresent()){
					results = gcoder.getFromLocationName(input,MapConstants.MAX_AUTO_COMPLETE_RESULTS);
				} else {
					return null;
				}
				Iterator<Address> locations = results.iterator();
				String country;

				while(locations.hasNext()){
					PlaceDto placeDto = new PlaceDto();
					Address location = locations.next();
					placeDto.setLat(location.getLatitude());
					placeDto.setLon(location.getLongitude());
					country = location.getCountryName();
					if(country == null) {
						country = "";
					} else {
						country =  ", "+country;
					}
					placeDto.setAddressDesc(location.getAddressLine(0)+", "+location.getAddressLine(1)+country);
					addresses.add(placeDto);
				}
			} catch (IOException e){
			}
		}
		return addresses;
	}
}