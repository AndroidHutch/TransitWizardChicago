package com.hutchdesign.transitgenie;

import java.util.List;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Map extends MapActivity {
	int ORIGIN;

	class MapOverlay extends com.google.android.maps.Overlay {

		public boolean onTap(GeoPoint p, MapView mapView) {
			if (ORIGIN == 0) {
				TransitGenieMain.request.originLatitude = (p.getLatitudeE6() / 1E6);
				TransitGenieMain.request.originLongitude = (p.getLongitudeE6() / 1E6);
			} else {
				TransitGenieMain.request.destLatitude = (p.getLatitudeE6() / 1E6);
				TransitGenieMain.request.destLongitude = (p.getLongitudeE6() / 1E6);
			}
			// TODO Make pop up of "Select this location?", return when verified
			//Reset GPS Flag
    		TransitGenieMain.DEST_CURRENT_LOCATION = 0;
    		TransitGenieMain.ORIGIN_CURRENT_LOCATION = 0;
			setResult(RESULT_OK);
			finish();

			return true;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle b = getIntent().getExtras();
		ORIGIN = b.getInt("origin", 0);
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		MapController mc = mapView.getController();
		// String coordinates[] = {"1.352566007", "103.78921587"};
		double lat = 41.874929479660025; // =
											// Double.parseDouble(coordinates[0]);
		double lng = -87.64549255371094;// = Double.parseDouble(coordinates[1]);

		GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		mc.animateTo(p);
		mc.setZoom(13);

		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		mapView.invalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
