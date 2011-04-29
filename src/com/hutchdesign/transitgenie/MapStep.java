package com.hutchdesign.transitgenie;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapStep extends MapActivity {
	
	int stepNumber;
	private Projection projection;
	class MapOverlay extends com.google.android.maps.Overlay {
		 public void draw(Canvas canvas, MapView mapv, boolean shadow){
		        super.draw(canvas, mapv, shadow);
		        Toast.makeText( getApplicationContext(), Integer.toString((int) TransitGenieMain.request.queryTime), Toast.LENGTH_SHORT).show();
		        Paint mPaint = new Paint();
		        mPaint.setDither(true);
		        mPaint.setColor(Color.RED);
		        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		        mPaint.setStrokeJoin(Paint.Join.ROUND);
		        mPaint.setStrokeCap(Paint.Cap.ROUND);
		        mPaint.setStrokeWidth(2);
		        GeoPoint gP1 = new GeoPoint((int)(TransitGenieMain.request.originLatitude * 1E6), (int) (TransitGenieMain.request.originLongitude * 1E6));
		        GeoPoint gP2 = new GeoPoint((int)(TransitGenieMain.request.destLatitude * 1E6), (int) (TransitGenieMain.request.destLongitude * 1E6));

		        Point p1 = new Point();
		        Point p2 = new Point();

		        Path path = new Path();

		        projection.toPixels(gP1, p1);
		        projection.toPixels(gP2, p2);

		        path.moveTo(p2.x, p2.y);
		        path.lineTo(p1.x,p1.y);

		        canvas.drawPath(path, mPaint);
		    }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle b = getIntent().getExtras();
		stepNumber = b.getInt("position");
		
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
		projection = mapView.getProjection();
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

