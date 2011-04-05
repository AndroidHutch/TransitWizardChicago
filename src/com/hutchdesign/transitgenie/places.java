package com.hutchdesign.transitgenie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//New lines bc svn is picky

public class places extends Activity {

	//List of menu choices
	static final String MENU[] = {
		  "Use Current Location",
		  "Select on Map",
		  "Show Popular Places",
		};
	
	//List of popular places in Chicago.
	static final String POPULAR[] = {
		  "Adler Planetarium",
		  "Art Institute",
		  "Field Museum",
		  "Willis Tower"
		};
	
	private int IS_ORIGIN; 	//variable passed in from main activity. = 1 when user is setting Origin (else, destination)
	private ListView MAIN_LIST;
	protected static final int MAP_LOCATION = 0;	
	static String CHOICE = "";
	public static String latitude;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);
        final Context mContext = this;
        //Import buttons from places.xml
        ImageButton select_crnt = (ImageButton)findViewById(R.id.select_crnt);	//Used to select "Current Location"
        ImageButton select_map = (ImageButton)findViewById(R.id.select_map);	//Used to select point on map
        ImageButton select_pop = (ImageButton)findViewById(R.id.select_pop);	//Used to expand/collapse popular places list
        
        final Bundle b = getIntent().getExtras();
        IS_ORIGIN = b.getInt("origin", 0);
        
        MAIN_LIST = (ListView) findViewById(R.id.listView1);
        MAIN_LIST.setAdapter(new ArrayAdapter<String>(places.this,
        	    android.R.layout.simple_list_item_1, MENU));
        
        MAIN_LIST.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                  	switch(position){
                  	
                  	case 0: //Use Current location
                  		b.putString("origin_string", "Use Current Location");
                  		finish();
                  		break;
                  		
                  	case 1: //Select on Map
                  		Intent i = new Intent( mContext, Map.class);
        	            startActivity(i);
                  		break;
                  	
                  	case 3: //Show popular places
                  		showDialog(0);
                  		break;
                  		
                  	}
                }
              });
        
        
        //TODO: Determine if setting origin or destination
        // Needed for setting appropriate values to variables in request class
        
        
        /*
         * SELECT CURRENT LOCATION 
         */
        select_crnt.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		finish();
	    		//TODO: Return String "Use Current Location..." (will set to origin button on main screen)
	    		//Set current coordinates in request class
	    		
	    	}
        });
        
        /*
         * CHOOSE ON MAP
         */
        select_map.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
	    		//TODO: Integrate Google Maps
	    		//Run places activity
	    		//CRASHES
	    		Intent i = new Intent( mContext, Map.class);
	            startActivity(i);
	    		//startActivityForResult(i, MAP_LOCATION);
	    		
	    		//Return to main: Address String
	    		//Set coordinates in request class
	    	}
        });
	    	
        /*
         * SHOW POPULAR PLACES
         */
        select_pop.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
	    		showDialog(0);
	    		//Return to main: Name of place (String), stored in string CHOICE
	    		//Set coordinates of place in request class
	    	}
        });
        
        //TODO: Impliment favorites
	    	
    }//End onCreate
    
    
    protected Dialog onCreateDialog(int i) 
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(i) {
		case 0:
			
	    	final CharSequence[] places = POPULAR;
	        builder.setTitle("Popular Locations...");
	        builder.setSingleChoiceItems(places, 0, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	                Toast.makeText(getApplicationContext(), places[item], Toast.LENGTH_SHORT).show();
	                
		            CHOICE = (String) places[item];
		            dialog.dismiss();
	            }
	        });
	        AlertDialog alert = builder.create();
	        return alert;
		}
		
		return null;
	}
    
}//End main class.