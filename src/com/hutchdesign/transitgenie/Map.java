package com.hutchdesign.transitgenie;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class Map extends MapActivity{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
