/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * -------------------------------------
 * Transit Genie Android Main Activity
 * -------------------------------------
 * Simple screen for users to see their selected origin/destination.
 * Starts upon application launch.
 * 
 * Layout used: main.xml
 * 
 * Includes: 
 * 
 * 		A) Two EditText boxes holding String version of current origin and destination selections.
 * 			Default values are "Use Current Location" for both EditText boxes.
 * 			Users may also directly type into the boxes (e.g. An address, Landmark).
 * 
 * 		B) Two "..." (aka "more") buttons to start 'places' Activity, (See .xml for clarification)
 * 			where users select their origin/destination from a list of popular places/favorites,
 * 			or via a Google Map.
 * 
 * 		C) "Go" button which starts 'Routes' Activity.
 * 			Which then implements the process of requesting routes from server.
 * 
 */

package com.hutchdesign.transitgenie;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TransitGenieMain extends Activity {
    protected static final int ORIGIN_REQUEST = 0;
    public static int ORIGIN_GPS = 1;
    public static int DEST_GPS = 1;
    public static Request request = new Request();
    
    public static SQLHelper SQL_HELPER;
    Cursor CURSOR;
    Bundle b;	//Holds data passed between main activity and places activity
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Initialize SQL Database helper and cursor
        SQL_HELPER = new SQLHelper(this);
        CURSOR = getFavorites();
        
        //Set up GPS location manager
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        //mlocManager.getLastKnownLocation(LOCATION_SERVICE);
        
        //Initialize Bundle and set default values
        b = new Bundle();
        b.putString("origin_string", "Current Location");
        b.putString("destin_string", "Current Location");
        
        //Import Buttons from main.xml
        Button button_go = (Button)findViewById(R.id.button_go);					//"Go" button on main screen (=> User is ready for routes)
        ImageButton button_origin = (ImageButton)findViewById(R.id.button_origin);	//Selected when user wishes to choose origin.
        ImageButton button_destn = (ImageButton)findViewById(R.id.button_destn);	//Selected when user wishes to choose destination.
        

        /* * * * * * * * * * * * * 
         * "GO" Button Listener  *
         * * * * * * * * * * * * */
        button_go.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
	    		//Run Routes activity
	    		Intent i = new Intent(getApplicationContext(), Routes.class);
	    		i.putExtras(b);		// Bundle needed in next Activity to utilize Strings representing origin and destination.
	            startActivity(i);
	    	}
        });
        
        /*
         * SET ORIGIN
         * User wishes to choose origin.
         * Switch to new activity -> "places" (uses places.xml)
         * Retrieve user choice from places activity and set to their origin.
         */
        button_origin.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		// reset GPS Flag
	    		ORIGIN_GPS = 0;
	    		//Run places activity
	    		Intent i = new Intent(getApplicationContext(), places.class);
	    		
	    		b.putStringArrayList("favs", getFavoritesArrayList());
	    		b.putInt("origin", 0);	//Set in Bundle 'b' that user is requesting origin.
	    		i.putExtras(b);			//Pass Bundle 'b' to Places activity via Intent 'i'.
	    		startActivityForResult(i, 0);
	    	}
        });
        
        /*
         * SET DESTINATION
         * User wishes to choose destination.
         * Switch to new activity -> "places" (uses places.xml)
         * Retrieve user choice from places activity and set to their destination.
         */
        button_destn.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
	    		//Run places activity
	    		Intent i = new Intent(getApplicationContext(), places.class);
	    		
	    		b.putStringArrayList("favs", getFavoritesArrayList());
	    		b.putInt("origin", 1);	//Set in Bundle 'b' that user is requesting destination.
	    		i.putExtras(b);			//Pass Bundle 'b' to Places activity via Intent 'i'.
	            startActivityForResult(i, 1);
	            
	            //TODO: Grab destination selection from places activity
	            //possible reference: http://thedevelopersinfo.wordpress.com/2009/10/15/passing-data-between-activities-in-android/
	            
	            //EditText destn = (EditText) findViewById(R.id.text_destn2);
	            //destn.setText( b.getString("destin_string") );
	            
	    	}
        }); 
       
    }//End onCreate
    
    /* Class My Location Listener */

    public class MyLocationListener implements LocationListener
    {
    	public void onLocationChanged(Location loc)
	    {
		    loc.getLatitude();
		
		    loc.getLongitude();
		
		    String Text = "My current location is: " +
		
		    "Latitud = " + loc.getLatitude() +
		
		    "Longitud = " + loc.getLongitude();
		
		
		    Toast.makeText( getApplicationContext(),
		    Text,
		    Toast.LENGTH_SHORT).show();
	
	    }
	
	
	    public void onProviderDisabled(String provider)
	    {
	    	Toast.makeText( getApplicationContext(),
		    "Gps Disabled",
		    Toast.LENGTH_SHORT ).show();
	
	    }
	
	
	    public void onProviderEnabled(String provider)
	    {
	
	    	Toast.makeText( getApplicationContext(),
		    "Gps Enabled",
		    Toast.LENGTH_SHORT).show();
	
	    }
	
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {
	
	
	    }
	    
    }//End MyLocationListener
    
    /*
    *	OnActivityResult is called upon the places.class Activity finishing.
    *	Used to receive Bundle from places,
    *   And set appropriate text in origin or destination editText (imported from main.xml).
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Bundle bundl = data.getExtras();
    	
        // If the request went well (OK) and the request was ORIGIN_REQUEST
        if(resultCode == Activity.RESULT_OK && requestCode == ORIGIN_REQUEST) {
        	
        	EditText origin = (EditText) findViewById(R.id.text_origin2);
        	origin.setText(bundl.getString("origin_string"));
        	b.putString("origin_string", bundl.getString("origin_string"));
        	
					            // Perform a query to the contact's content provider for the contact's name
								//  Cursor cursor = getContentResolver().query(data.getData(),
								//  new String[] {places.latitude}, null, null, null);
								//  if (cursor.moveToFirst()) { // True if the cursor is not empty
								//  int columnIndex = cursor.getColumnIndex(places.latitude);
								//  request.originLatitude = cursor.getString(columnIndex);
								//  request = null;// Do something with the selected contact's name...
								//  }
        }
        
        //If the request went well (OK) and the request was for destination
        //Note: requestCode = 1 => Destination Request
        else if(resultCode == Activity.RESULT_OK && requestCode == 1) 
        {
        	EditText destn = (EditText) findViewById(R.id.text_destn2);
        	destn.setText(bundl.getString("destin_string"));
        	b.putString("destin_string", bundl.getString("destin_string"));
        }
        
        else if(resultCode == Activity.RESULT_CANCELED)
        {
        }
        
    }
    
    //Custom Menu
    //	Utilizes manu_main.xml
    //	Contains selections: 'Settings', 'Depart Time', 'About'
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_main, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {/*
    	switch (item.getItemId())
    	{
	    	case R.id.menu_settings:
				
				return true;
			case R.id.menu_time:
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
    	
    	}*/
    	
		return true;
    }
    
    //-----------------------------------------------------------
    //METHODS NEEDED FOR SQL DATABASE

    //Retrieve all favorite locations from the database
    private Cursor getFavorites() 
    {
    	SQLiteDatabase db = SQL_HELPER.getReadableDatabase();
    	Cursor cursor = db.query(SQLHelper.TABLE, null, null, null, null, null, null);
  
    	startManagingCursor(cursor);
    	return cursor;
    }
    
    //Retrieve all favorite locations from the database in the form of an array list
    private ArrayList<String> getFavoritesArrayList() 
    {
    	ArrayList<String> allNames = new ArrayList<String>();
    	CURSOR.moveToFirst();
    	
    	while (CURSOR.moveToNext())
    		allNames.add(CURSOR.getString(1));

    	return allNames;
    }
    
    
    @Override
    public void onDestroy() 
    {
    	super.onDestroy();
    	
    	if(CURSOR != null)
    		CURSOR.close();
    	if(SQL_HELPER != null)
    		SQL_HELPER.close();
    }  
}//End main class.