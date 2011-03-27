package com.hutchdesign.transitgenie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

//New lines bc svn is picky

public class PopularPlaces extends Activity {
	
	//List of popular places in Chicago.
	static final String popular[] = {
		  "Adler Planetarium",
		  "Art Institute",
		  "Field Museum",
		  "Willis Tower"
		};
	
	static String CHOICE = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);
        
        //Import buttons from places.xml
        ImageButton select_crnt = (ImageButton)findViewById(R.id.select_crnt);	//Used to select "Current Location"
        ImageButton select_map = (ImageButton)findViewById(R.id.select_map);	//Used to select point on map
        ImageButton select_pop = (ImageButton)findViewById(R.id.select_pop);	//Used to expand/collapse popular places list
        
        //TODO: Determine if setting origin or destination
        // Needed for setting appropriate values to variables in request class
        
        
        /*
         * SELECT CURRENT LOCATION 
         */
        select_crnt.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		
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
			
	    	final CharSequence[] places = popular;
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