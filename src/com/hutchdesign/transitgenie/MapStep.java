package com.hutchdesign.transitgenie;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapStep extends MapActivity {

	Double startLat, startLong, endLat, endLong;
	String type;
	private Projection projection;
	class MapOverlay extends com.google.android.maps.Overlay {
		public void draw(Canvas canvas, MapView mapv, boolean shadow){
			super.draw(canvas, mapv, shadow);
			//Toast.makeText( getApplicationContext(), Integer.toString((int) TransitGenieMain.request.queryTime), Toast.LENGTH_SHORT).show();
			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(2);
			if(type.equals("walk"))
			{
				for(int i = 1; i < RouteDetail.walkPointArray.size(); i++)
				{
					startLat = Double.parseDouble(RouteDetail.walkPointArray.get(i - 1).getAttributes().item(0).getNodeValue());
					startLong = Double.parseDouble(RouteDetail.walkPointArray.get(i - 1).getAttributes().item(1).getNodeValue());
					endLat = Double.parseDouble(RouteDetail.walkPointArray.get(i).getAttributes().item(0).getNodeValue());
					endLong = Double.parseDouble(RouteDetail.walkPointArray.get(i).getAttributes().item(1).getNodeValue());
					GeoPoint gP1 = new GeoPoint((int)(startLat * 1E6), (int)(startLong * 1E6));//((int)(TransitGenieMain.request.originLatitude * 1E6), (int) (TransitGenieMain.request.originLongitude * 1E6));
					GeoPoint gP2 = new GeoPoint((int)(endLat * 1E6), (int)(endLong * 1E6));//((int)(TransitGenieMain.request.destLatitude * 1E6), (int) (TransitGenieMain.request.destLongitude * 1E6));

					Point p1 = new Point();
					Point p2 = new Point();

					Path path = new Path();

					projection.toPixels(gP1, p1);
					projection.toPixels(gP2, p2);
					//canvas.drawPoint(p1.x, y, paint)
					path.moveTo(p2.x, p2.y);
					path.lineTo(p1.x,p1.y);

					canvas.drawPath(path, mPaint);
				}
			}
			else{
				GeoPoint gP1 = new GeoPoint((int)(startLat * 1E6), (int)(startLong * 1E6));
				Point p1 = new Point();
				projection.toPixels(gP1, p1);
				canvas.drawCircle(p1.x, p1.y, 5 ,mPaint);
				
				mPaint.setColor(Color.BLUE);
				GeoPoint gP2 = new GeoPoint((int)(endLat * 1E6), (int)(endLong * 1E6));
				Point p2 = new Point();
				projection.toPixels(gP2, p2);
					
				canvas.drawCircle(p2.x, p2.y, 5 ,mPaint);
				
				mPaint.setColor(Color.BLACK);
				mPaint.setStrokeWidth(0.5f);
				canvas.drawText("Alight", p2.x+5, p2.y+5, mPaint);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle b = getIntent().getExtras();
		type = b.getString("type");
		startLat = Double.parseDouble(b.getString("startLat"));
		startLong = Double.parseDouble(b.getString("startLong"));
		endLat = Double.parseDouble(b.getString("endLat"));
		endLong = Double.parseDouble(b.getString("endLong"));
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		MapController mc = mapView.getController();


		// String coordinates[] = {"1.352566007", "103.78921587"};
		double lat = startLat; // =
		// Double.parseDouble(coordinates[0]);
		double lng = startLong;// = Double.parseDouble(coordinates[1]);

		GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		mc.animateTo(p);
		mc.setZoom(16);

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

