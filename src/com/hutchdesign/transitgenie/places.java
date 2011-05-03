package com.hutchdesign.transitgenie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//New lines bc svn is picky

public class places extends Activity {

	// List of menu choices
	private static final String MENU[] = { "Use Current Location", "Select on Map",
			"Show Popular Places", };

	// List of popular places in Chicago.
	private static final String POPULAR[] = { "Hide Popular Places",
			"\tAdler Planetarium", "\tBuckingham Fountain",
			"\tChicago Art Institute", "\tField Museum",
			"\tJohn Hancock Center", "\tLicoln Park Zoo", "\tMagnificent Mile",
			"\tMcCormick Place", "\tMidway Airport", "\tMillennium Park",
			"\tNavy Pier", "\tNorth Avenue Beach", "\tOak Street Beach",
			"\tUniversity of Illinois\n\t at Chicago", "\tWillis Tower" };
	
    // List of "latitude,longitude" of popular places
    static final String POPULAR_LOC[] = { "0,0", // First spot is empty since 1st element of POPULAR is = "Hide Popular Places"
		"41.866334,-87.606526",	// Adler Planetarium
		"41.875803,-87.618347",	// Buckingham Fountain
		"41.879579,-87.623885",	// Chicago Art Institute
		"41.866178,-87.617712",	// Field Museum
		"41.898803,-87.623616",	// John Hancock Center
		"41.920752,-87.632828",	// Licoln Park Zoo
		"41.899594,-87.624421",	// Magnificent Mile
		"41.852811,-87.611718",	// McCormick Place
		"41.78831,-87.740692",	// Midway Airport
		"41.882105,-87.622957",	// Millennium Park
		"41.891416,-87.609959",	// Navy Pier
		"41.914199,-87.624497",	// North Avenue Beach
		"41.902893,-87.622722",	// Oak Street Beach
		"41.869622,-87.649481",	// University of Illinois Chicago
		"41.87886,-87.635837"	// Willis Tower
    };
	private int ORIGIN; // variable passed in from main activity. = 1 when user
						// is setting Origin (else, destination)
	private ListView MAIN_LIST;
	protected static final int MAP_LOCATION = 0;
	static String CHOICE = "";
	public static String latitude;
	Bundle b;
	boolean isPopShowing = false;
	ArrayList<String> LIST = new ArrayList<String>();
	ArrayList<String> FAVORITES;
	ArrayAdapter<String> ADAP;
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places);
		final Context mContext = this;
		
		b = getIntent().getExtras();	
		ORIGIN = b.getInt("origin", 0); 			// Retrieve variable origin from main class
		FAVORITES = b.getStringArrayList("favs"); 	//Retrieve favorite route names taken from local database.

		//Import header (TextView) from places.xml
		TextView header = (TextView) findViewById(R.id.header1);

		if (ORIGIN == 0) {
			header.setText("Choose Origin");
		} else {
			header.setText("Choose Destination");
		}

		LIST.addAll(Arrays.asList(MENU));
		LIST.addAll(FAVORITES);
		MAIN_LIST = (ListView) findViewById(R.id.listView1);
		ADAP = (new ArrayAdapter<String>(places.this,
				android.R.layout.simple_list_item_1, LIST));
		MAIN_LIST.setAdapter(ADAP);

		MAIN_LIST.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;

				switch (position) {

				// ********************
				// USE CURRENT LOCATION
				case 0:
					if (ORIGIN == 0) {
						b.putString("origin_string", "Use Current Location");
						TransitGenieMain.ORIGIN_GPS = 1;
					} else {
						TransitGenieMain.DEST_GPS = 1;
						b.putString("destin_string", "Use Current Location");
					}

					intent = getIntent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
					break;

				// *************
				// SELECT ON MAP
				case 1:
					Intent i = new Intent(mContext, Map.class);
					b.putInt("origin", ORIGIN); // Pass to Places activity that
												// user is requesting origin.
					i.putExtras(b);
					startActivityForResult(i, 0);

					break;

				// ***************************
				// SHOW OR HIDE POPULAR PLACES
				case 2:
					if (!isPopShowing) // Popular Places are NOT displayed. ->
										// User wants them to be.
					{
						LIST.remove(2);
						LIST.addAll(2, Arrays.asList(POPULAR));
						ADAP.notifyDataSetChanged();
						isPopShowing = true;
					} else // Popular Places are diplayed. -> User wants to hide
							// them.
					{
						LIST.clear();
						LIST.addAll(Arrays.asList(MENU));
						LIST.addAll(FAVORITES);
						ADAP.notifyDataSetChanged();
						isPopShowing = false;
					}
					break;

				// *****************************
				// ANY POPULAR PLACE OR FAVORITE
				default:
					if (!isPopShowing) // Popular Places are NOT displayed. -> User selected a favorite
					{
						
						
					}
					
					//Send location name (String) to Bundle
					if (ORIGIN == 0) {
						TransitGenieMain.ORIGIN_GPS = 0;
						b.putString("origin_string", LIST.get(position).trim());
					} else {
						TransitGenieMain.DEST_GPS = 0;
						b.putString("destin_string", LIST.get(position).trim());
					}

					// TODO: Return Latitude/Logitude for Popular Places.
					// b.putString("latitude", "...");
					// b.putString("longitude", "...");

					intent = getIntent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
					break;
				}
			}
		});

		// TODO: Determine if setting origin or destination
		// Needed for setting appropriate values to variables in request class

		// TODO: Impliment favorites

	}// End onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			Geocoder geoCoder = new Geocoder(getBaseContext(),
					Locale.getDefault());
			double mapLat, mapLong;
			if (ORIGIN == 0) {
				mapLat = TransitGenieMain.request.originLatitude;
				mapLong = TransitGenieMain.request.originLongitude;
			} else {
				mapLat = TransitGenieMain.request.destLatitude;
				mapLong = TransitGenieMain.request.destLongitude;
			}
			// List<Address> addresses = geoCoder.getFromLocation(
			// p.getLatitudeE6() / 1E6,
			// p.getLongitudeE6() / 1E6, 1);
			//
			String add = "";

			List<Address> addresses;
			try {
				addresses = geoCoder.getFromLocation(mapLat, mapLong, 1);

				if (addresses.size() > 0)
					add = addresses.get(0).getAddressLine(0);
				// {
				// for (int count=0;
				// count<addresses.get(0).getMaxAddressLineIndex();
				// count++)
				// add += addresses.get(0).getAddressLine(count) + "\n";
				// }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (ORIGIN == 0) {
				b.putString("origin_string", add);
			} else {
				b.putString("destin_string", add);
			}
			// Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
			Intent i = getIntent();
			i.putExtras(b);
			setResult(RESULT_OK, i);
			finish();
		}

		else if (resultCode == Activity.RESULT_CANCELED) {
		}

	}

	// User hits back button -> Doesn't alter origin/destination.
	// DO NOT REMOVE or back button will cause crash.
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			Intent intent = getIntent();
			setResult(RESULT_CANCELED, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}// End main class.