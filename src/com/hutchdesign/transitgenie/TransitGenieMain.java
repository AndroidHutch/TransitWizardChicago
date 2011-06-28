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

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class TransitGenieMain extends Activity {
    protected static final int ORIGIN_REQUEST = 0;
	private static final int TIME_DIALOG = 1;
    public static int ORIGIN_CURRENT_LOCATION;	//if = 0: Latitude/Long. was manually set for origin.
    											//if = 1: Grab GPS data for origin.
    											//if = 2: Resolve the address input.
    public static int DESTIN_CURRENT_LOCATION;	//See above (in context of destination).
    public static LocationManager mlocManager;
    public static LocationListener mlocListener;
    public static Request request;
    Calendar c = Calendar.getInstance();
    public static SQLHelper SQL_HELPER;
    public static Cursor CURSOR;
    Bundle b;	//Holds data passed between main activity and places activity
    Geocoder geocoder;
    EditText origin_text;
    EditText destin_text;
    
	private int mHour;
	private int mMinute;
	private boolean from_places_origin;	//Has the user just returned with an origin selection from the places Activity?
										//Used to determine if user has typed their input or if it was selected from places.java.
	private boolean from_places_destin;	//See above, in context of destination selection.
	
	//GPS & Location Variables
	private double currentLat;		//Last known latitude.
    private double currentLon;		//Last known longitude.
	
	public static Document[] allRoutes;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        geocoder = new Geocoder(this.getApplicationContext());
        request = new Request();
        
        //Initialize SQL Database helper and cursor
        SQL_HELPER = new SQLHelper(this);
        CURSOR = getFavorites();
        
        //Set up GPS location manager
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();      
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, mlocListener);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 2, mlocListener);
  
        //Initialize Bundle
        b = new Bundle();
        from_places_origin = false;
        from_places_destin = false;
        currentLat = 0;
        currentLon = 0;

        
        //Import Buttons from main.xml
        Button button_go = (Button)findViewById(R.id.button_go);					//"Go" button on main screen (=> User is ready for routes)
        ImageButton button_origin = (ImageButton)findViewById(R.id.button_origin);	//Selected when user wishes to choose origin.
        ImageButton button_destn = (ImageButton)findViewById(R.id.button_destn);	//Selected when user wishes to choose destination.
        origin_text = (EditText)findViewById(R.id.text_origin2);
        destin_text = (EditText)findViewById(R.id.text_destn2);
        
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
         * Edit Text Listeners For Origin and Destination Input  *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        origin_text.addTextChangedListener(new TextWatcher()
        {
        	public void  afterTextChanged (Editable s) { 
        		if(!from_places_origin){	//Do not set custom location flag (ORIGIN_CURRENT_LOCATION) if user just returned from 'places' Activity.
        			ORIGIN_CURRENT_LOCATION = 2;	/* => On "GO," program will parse origin box for the custom location. 
               									 	* (Uses geocoder to determine what the user meant) 
               									 	*/
        		} else if(origin_text.getText().toString().equalsIgnoreCase("Use Current Location")){
        			ORIGIN_CURRENT_LOCATION = 1;
        		} else {
        			ORIGIN_CURRENT_LOCATION = 0;
        		}
        		from_places_origin = false;
			} 
			public void  beforeTextChanged  (CharSequence s, int start, int count, int after) { } 	//Do Nothing (Required method)
			public void  onTextChanged  	(CharSequence s, int start, int before, int count){ }	//Do Nothing (Required methid)
        });
        destin_text.addTextChangedListener(new TextWatcher()
        {
        	public void  afterTextChanged (Editable s) { 
        		if(!from_places_destin){	//Do not set custom location flag (DESTIN_CURRENT_LOCATION) if user just returned from 'places' Activity.
        			DESTIN_CURRENT_LOCATION = 2;	/* => On "GO," program will parse destination box for the custom location. 
               									 	* (Uses geocoder to determine what the user meant) 
               									 	*/
        			Toast.makeText( getApplicationContext(),
						    "I was edited",
						    Toast.LENGTH_SHORT).show();
        		} else if(destin_text.getText().toString().equalsIgnoreCase("Use Current Location")){
        			DESTIN_CURRENT_LOCATION = 1;
        		} else {
        			DESTIN_CURRENT_LOCATION = 0;
        		}
        		
        		from_places_destin = false;
			} 
			public void  beforeTextChanged  (CharSequence s, int start, int count, int after) { } 	//Do Nothing (Required method)
			public void  onTextChanged  	(CharSequence s, int start, int before, int count){ }	//Do Nothing (Required methid)
        });

        /* * * * * * * * * * * * * 
         * "GO" Button Listener  *
         * * * * * * * * * * * * */
        button_go.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
	    		EditText destin = (EditText) findViewById(R.id.text_destn2);
	    		EditText origin = (EditText) findViewById(R.id.text_origin2);
	    		
	    		//Check if there is text in origin/destination boxes
	    		if(origin.getText().length() <= 0) {
	    			Toast.makeText(getApplicationContext(), "Please input a location for Origin.", Toast.LENGTH_SHORT).show();
	    			return;
	    		}
	    		if(destin.getText().length() <= 0) {
	    			Toast.makeText(getApplicationContext(), "Please input a location for Destination.", Toast.LENGTH_SHORT).show();
	    			return; 
	    		}
	    		
	    		Address address;
	    		
	    		switch(ORIGIN_CURRENT_LOCATION) {	//Resolve origin latitude and longitude
	    			//case 0: => Latitude & Longitude are already set in request.java.
	    			case 0: break;
	    			case 1:	//Grab GPS data for origin (user has selected 'Use Current Location').
	    				if(!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	    					Toast.makeText( getApplicationContext(),
								    "Please enable GPS to utilize current location for Origin.",
								    Toast.LENGTH_SHORT).show();
	    					return;
	    				}
	    				else if(DESTIN_CURRENT_LOCATION == 1) {
	    					Toast.makeText( getApplicationContext(),
								    "Origin and Destination are both your current location. Please alter one.",
								    Toast.LENGTH_SHORT).show();
	    					return;
	    				}
	    				else {
	    					//current = mlocManager.getLastKnownLocation(PROVIDER);
	    					if(currentLat > 0.0) {
	    						request.originLatitude = currentLat;	
		    					request.originLongitude = currentLon;
		    					b.putString("origin_string", "My Location");
	    					} else {
	    						Toast.makeText( getApplicationContext(),
	    							    "No current loction found for Origin.\nPlease check GPS status.",
	    							    Toast.LENGTH_SHORT).show();
	    						return;
	    					}
	    				}
	    				
	    				break;
	    			
	    			case 2:	//Resolve address of input.
	    				try {
							address = geocoder.getFromLocationName(origin_text.getText().toString(), 2).get(0);		//Resolve address
							if(address == null) {
								Toast.makeText( getApplicationContext(),
									    "Can not resolve Origin Address. Try searching on map or re-type.",
									    Toast.LENGTH_SHORT).show();
								return;
							}
							request.originLatitude = address.getLatitude();		//set Latitude based on resolved address
	    					request.originLongitude = address.getLongitude();	//set Logitude based on resolved address
	    					b.putString("origin_string", origin_text.getText().toString());
						} catch (IOException e1) {
							Toast.makeText( getApplicationContext(),
								    "Can not resolve Origin Address. Try searching on map or re-type.",
								    Toast.LENGTH_SHORT).show();	//TODO: change from Toast to dialog.
							return;
						}		
	    				break;
	    			default: break;
	    		} //End switch ORIGIN_CURRENT_LOCATION
	    		switch(DESTIN_CURRENT_LOCATION) {	//Resolve destination latitude and longitude
    			//case 0: => Latitude & Longitude are already set in request.java.
	    		case 0:  break;
    			case 1:	//Grab GPS data for destination (user has selected 'Use Current Location').
    				if(!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
    					Toast.makeText( getApplicationContext(),
    							"Please enable GPS to utilize current location for Destination.",
							    Toast.LENGTH_SHORT).show();
    					return;
    				} else {
    					//current = mlocManager.getLastKnownLocation(PROVIDER);
    					if(currentLat > 0.0) {
    						request.destinLatitude = currentLat;	
        					request.destinLongitude = currentLon;
        					b.putString("destin_string", "My Location");
    					} else {
    						Toast.makeText( getApplicationContext(),
    							    "No current loction found for Destination.\nPlease check GPS status.",
    							    Toast.LENGTH_SHORT).show();
    						return;
    					}
    				}
    				
    				break;
    			
    			case 2:	//Resolve address of input.
    				try {
						address = geocoder.getFromLocationName(origin_text.getText().toString(), 2).get(0);		//Resolve address
						
						if(address == null) {
							Toast.makeText( getApplicationContext(),
								    "Can not resolve Destination Address. Try searching on map or re-type.",
								    Toast.LENGTH_SHORT).show();
						}
						
						request.destinLatitude = address.getLatitude();		//set Latitude based on resolved address
    					request.destinLongitude = address.getLongitude();	//set Logitude based on resolved address
    					b.putString("destin_string", destin_text.getText().toString());
					} catch (IOException e1) {
						Toast.makeText( getApplicationContext(),
							    "Can not resolve Destination Address. Try searching on map or re-type.",
							    Toast.LENGTH_SHORT).show();	//TODO: change from Toast to dialog.
						return;
					}		
    				break;
    			default: break;
    		} //End switch DESTIN_CURRENT_LOCATION*/\
	    		
    			 /* * * * * * * * * * * * * * * * * * * * * * * * *
    			  * ASYNC TASK
    			  * 	Used to request server data.
    			  * 	Progress bar is diplayed while loading.
    			  * * * * * * * * * * * * * * * * * * * * * * * * */
    			 AsyncTask<String, Void, String> sendRequest = new AsyncTask<String, Void, String>() {
				    Dialog progress;

				    @Override
				    protected void onPreExecute() {
				        progress = ProgressDialog.show(TransitGenieMain.this, 
				                "Loading Data", "Please Wait...");
				        super.onPreExecute();
				    }

				    @Override
				    protected String doInBackground(String... params) {
				    	//RETRIEVE DOCUMENT
				        allRoutes = null;	//Array of DOM trees, each representing a singular route.
				        
				        try {
							allRoutes = TransitGenieMain.request.buildRoutes();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace(); 
						}
						if(allRoutes == null)	//If no routes were added to array.
						{
							return "error";
						}
				        return "";
				    }

				    @Override
				    protected void onPostExecute(String result) {
				    	super.onPostExecute(result);
				        progress.dismiss();
				        
				    	if(!result.equals("error")) //Run Routes activity if no error occured
				    	{
				    		Intent i = new Intent(getApplicationContext(), Routes.class);
				    		i.putExtras(b);		// Bundle needed in next Activity to utilize Strings representing origin and destination.
				            startActivity(i);
				    	}  
				    	else
				    	{
				    		Toast.makeText(getApplicationContext(), 
				    				"Error: No Routes Found.\n" + request.originLatitude + "\n" + request.originLongitude, 
				    				Toast.LENGTH_SHORT).show();
				    	}
				    }
				};
    			sendRequest.execute();	//Request data form server via Async. task
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
	    	}
        }); 
       
    }//End onCreate
    
    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {
    	public void onLocationChanged(Location loc)
	    {	
		    currentLat = loc.getLatitude();
			currentLon = loc.getLongitude();
			
			/*Toast.makeText( getApplicationContext(),
    				"UPDATE\n" + currentLat + "\n" + currentLon,
    			    Toast.LENGTH_SHORT ).show();*/
	    }

	    public void onProviderDisabled(String provider) { }
	    public void onProviderEnabled(String provider) { }
	    public void onStatusChanged(String provider, int status, Bundle extras) { }
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
        	
        	from_places_origin = true;
        	EditText origin = (EditText) findViewById(R.id.text_origin2);
        	origin.setText(bundl.getString("origin_string"));
        	b.putString("origin_string", bundl.getString("origin_string"));
        	
        	request.originLatitude = bundl.getDouble("origin_lat");
        	request.originLongitude = bundl.getDouble("origin_lon");
	
        }
        
        //If the request went well (OK) and the request was for destination
        //Note: requestCode = 1 => Destination Request
        else if(resultCode == Activity.RESULT_OK && requestCode == 1)  {
        	
        	from_places_destin = true;
        	EditText destn = (EditText) findViewById(R.id.text_destn2);
        	destn.setText(bundl.getString("destin_string"));
        	b.putString("destin_string", bundl.getString("destin_string"));
        	
        	request.destinLatitude = bundl.getDouble("destin_lat");
        	request.destinLongitude = bundl.getDouble("destin_lon");
        }
        
        else if(resultCode == Activity.RESULT_CANCELED) { }

    }
    
    //Custom Menu
    //	Utilizes manu_main.xml
    //	Contains selections: 'Settings', 'Depart Time', 'About'
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_main, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId())
    	{
	    	case R.id.menu_settings:
	    		//Intent i = new Intent(getApplicationContext(), MainMenu.class);
	    		
	    		//b.putStringArrayList("favs", getFavoritesArrayList());
	    		//b.putInt("origin", 1);	//Set in Bundle 'b' that user is requesting destination.
	    		//i.putExtras(b);			//Pass Bundle 'b' to Places activity via Intent 'i'.
	            //startActivity(i);
				return true;
				
			case R.id.menu_time:
		        
		        mHour = c.get(Calendar.HOUR_OF_DAY);
		        mMinute = c.get(Calendar.MINUTE);
				showDialog(TIME_DIALOG);
				return true;
			
			case R.id.menu_about:
				String url = "http://www.transitgenie.com/";
				Intent iweb = new Intent(Intent.ACTION_VIEW);
				iweb.setData(Uri.parse(url));
				startActivity(iweb);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
    	
    	}    	
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG:
            return new TimePickerDialog(this,
                    mTimeSetListener, mHour, mMinute, false);
        }
        return null;
    }
 // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                c.set(Calendar.HOUR_OF_DAY, mHour);
                c.set(Calendar.MINUTE, mMinute);
                request.queryTime = c.getTimeInMillis()/1000L;
                Log.i("Time", Long.toString(c.getTimeInMillis()/1000L));
                Log.i("Current", Long.toString(System.currentTimeMillis()));
            }
        };
   
        
    //-----------------------------------------------------------
    //METHODS NEEDED FOR SQL DATABASE

    //Retrieve all favorite locations from the database
    private Cursor getFavorites() {
    	SQLiteDatabase db = SQL_HELPER.getReadableDatabase();
    	Cursor cursor = db.query(SQLHelper.TABLE, null, null, null, null, null, null);
  
    	startManagingCursor(cursor);
    	return cursor;
    }
    
    //Retrieve all favorite locations from the database in the form of an array list
    private ArrayList<String> getFavoritesArrayList() {
    	ArrayList<String> allNames = new ArrayList<String>();
    	CURSOR.moveToFirst();
    	
    	while (CURSOR.moveToNext())
    		allNames.add(CURSOR.getString(1));

    	return allNames;
    }
    
  //Add a favorite location to the database
    public static void addFavorite(String name, double latitude, double longitude) {
      SQLiteDatabase db = SQL_HELPER.getWritableDatabase();
      ContentValues values = new ContentValues();
      
      //Add variables to SQLHelper
      values.put(SQLHelper.NAME, name);
      values.put(SQLHelper.LAT, latitude);
      values.put(SQLHelper.LON, longitude);
      
      //Add current data in SQLHelper to database
      db.insert(SQLHelper.TABLE, null, values);
    }
    
    //Delete favorite based on route name
	public static void deleteFavoriteByName(String name) {
    	SQLiteDatabase db = SQL_HELPER.getReadableDatabase();
    	db.delete(SQLHelper.TABLE, SQLHelper.NAME + "=?", new String[]{name});
    }
    
    //Delete favoirte based on database row id
    public void deleteFavoriteById(long id) {
    	SQLiteDatabase db = SQL_HELPER.getReadableDatabase();
    	db.delete(SQLHelper.TABLE, "_id=?", new String[]{String.valueOf(id)});
    }
    
    /* Request updates at startup */
	/*@Override
	protected void onResume() {
		super.onResume();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, mlocListener);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 2, mlocListener);
	}*/

	/* Remove the locationlistener updates when Activity is paused */
	/*@Override
	protected void onPause() {
		super.onPause();
		mlocManager.removeUpdates(mlocListener);	
	}*/
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	if(CURSOR != null)
    		CURSOR.close();
    	if(SQL_HELPER != null)
    		SQL_HELPER.close();
    }  
    				
}//End main class.


