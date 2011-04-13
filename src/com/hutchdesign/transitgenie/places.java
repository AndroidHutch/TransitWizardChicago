package com.hutchdesign.transitgenie;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
		  "Hide Popular Places",
		  "\tAdler Planetarium",
		  "\tArt Institute",
		  "\tField Museum",
		  "\tWillis Tower"
		};
	
	private int ORIGIN; 	//variable passed in from main activity. = 1 when user is setting Origin (else, destination)
	private ListView MAIN_LIST;
	protected static final int MAP_LOCATION = 0;	
	static String CHOICE = "";
	public static String latitude;
	
	boolean isPopShowing = false;
	ArrayList<String> LIST = new ArrayList<String>();
	ArrayAdapter<String> ADAP;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);
        final Context mContext = this;
        final Bundle b = getIntent().getExtras();
        
        //Import header (TextView) from places.xml
        TextView header = (TextView) findViewById(R.id.header1);
        
        if(ORIGIN == 0)
        {
        	header.setText("Choose Origin");
        }
        else
        {
        	header.setText("Choose Destination");
        }
        
        
        ORIGIN = b.getInt("origin", 0);		//Retrieve variable origin from main class
        
        LIST.addAll(Arrays.asList(MENU));
        MAIN_LIST = (ListView) findViewById(R.id.listView1);
        ADAP = (new ArrayAdapter<String>(places.this,
        	    android.R.layout.simple_list_item_1, LIST));
        MAIN_LIST.setAdapter(ADAP);
        
        MAIN_LIST.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                  	switch(position){
                  	
                  	//********************
                  	//USE CURRENT LOCATION
                  	case 0:
                  		if(ORIGIN == 0)	{ b.putString("origin_string", "Use Current Location"); }
                  		else			{ b.putString("destin_string", "Use Current Location"); }
                  		finish();
                  		break;
                  	
                  	//********************
                  	//SELECT ON MAP
                  	case 1:
                  		Intent i = new Intent( mContext, Map.class);
        	            startActivity(i);
                  		break;
                  		
                  	//********************
                  	//SHOW OR HIDE POPULAR PLACES
                  	case 2:
                  		if(!isPopShowing) //Popular Places are NOT displayed. -> User wants them to be.
                  		{
	                  		LIST.remove(2);
	                  		LIST.addAll(Arrays.asList(POPULAR));
	                  		ADAP.notifyDataSetChanged();
	                  		isPopShowing = true;
                  		}
                  		else //Popular Places are diplayed. -> User wants to hide them.
                  		{
                  			LIST.clear();
                  			LIST.addAll(Arrays.asList(MENU));
                  			ADAP.notifyDataSetChanged();
                  			isPopShowing = false;
                  		}
                  		break;
                  	
                  	//********************
                  	//ADLER PLANETARIUM
                  	case 3:
                  		if(ORIGIN == 0)	{ b.putString("origin_string", "Adler Planetarium"); }
                  		else			{ b.putString("destin_string", "Adler Planetarium"); }
                  		
                  		//TODO: Set Latitude/Logitude for Popular Places.
                  		
                  		Intent intent = getIntent();
                  		intent.putExtras(b);
                  		setResult(RESULT_OK, intent);
                  		finish();
                  		break;
                  	}
                }
              });
        
        
        //TODO: Determine if setting origin or destination
        // Needed for setting appropriate values to variables in request class
        
      
        
        //TODO: Impliment favorites
	    	
    }//End onCreate
    
}//End main class.