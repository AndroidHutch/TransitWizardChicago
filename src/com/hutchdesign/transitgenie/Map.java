package com.hutchdesign.transitgenie;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Map extends MapActivity{

		class MapOverlay extends com.google.android.maps.Overlay
		{
			public boolean onTap(GeoPoint p, MapView mapView)
			{
				Geocoder geoCoder = new Geocoder(
	                    getBaseContext(), Locale.getDefault());
				// Currently Displays address from tap coords on every tap
				// Could decrease lag by showing "Select this Location" and
				// parsing coords only when Select is hit instead of every tap
	                try {
	                    List<Address> addresses = geoCoder.getFromLocation(
	                        p.getLatitudeE6()  / 1E6, 
	                        p.getLongitudeE6() / 1E6, 1);
	 
	                    String add = "";
	                    if (addresses.size() > 0) 
	                    {
	                        for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
	                             i++)
	                           add += addresses.get(0).getAddressLine(i) + "\n";
	                    }
	 //TODO Make pop up of address, return Geopoint if address selected
	                    
	                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
	                }
	                catch (IOException e) {                
	                    e.printStackTrace();
	                }   
	                return true;
			}

		}
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.map);
	    mapView.setBuiltInZoomControls(true);
	    MapController mc = mapView.getController();
        //String coordinates[] = {"1.352566007", "103.78921587"};
        double lat = 41.874929479660025; //= Double.parseDouble(coordinates[0]);
        double lng = -87.64549255371094;// = Double.parseDouble(coordinates[1]);
 
        GeoPoint p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(13); 
        
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);      
        mapView.invalidate();
	
	}
	
	
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
}
