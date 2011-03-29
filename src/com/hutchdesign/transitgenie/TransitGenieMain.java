package com.hutchdesign.transitgenie;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//TODO: Wrap .xml files in in Scroll Views

public class TransitGenieMain extends Activity {
    protected static final int ORIGIN_REQUEST = 0;

  //Set up Request URL 
    Request request = new Request();
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Import Buttons from main.xml
        Button button_go = (Button)findViewById(R.id.button_go);			//"Go" button on main screen (=> User is ready for routes)
        Button button_origin = (Button)findViewById(R.id.button_origin);	//User wishes to choose origin.
        Button button_destn = (Button)findViewById(R.id.button_destn);		//User wishes to choose destination.
        
        
        
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
	            startActivity(i);
	    		//startActivityForResult(i, ORIGIN_REQUEST);
	            
	            //TODO: Grab origin selection from places activity
	            //possible reference: http://thedevelopersinfo.wordpress.com/2009/10/15/passing-data-between-activities-in-android/
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
	            startActivity(i);
	            
	            //TODO: Grab destination selection from places activity
	            //possible reference: http://thedevelopersinfo.wordpress.com/2009/10/15/passing-data-between-activities-in-android/
	    	}
        }); 
       
        
        
    }//End onCreate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request went well (OK) and the request was ORIGIN_REQUEST
        if (resultCode == Activity.RESULT_OK && requestCode == ORIGIN_REQUEST) {
            // Perform a query to the contact's content provider for the contact's name
            Cursor cursor = getContentResolver().query(data.getData(),
            new String[] {places.latitude}, null, null, null);
            if (cursor.moveToFirst()) { // True if the cursor is not empty
                int columnIndex = cursor.getColumnIndex(places.latitude);
                request.originLatitude = cursor.getString(columnIndex);
                //request = null;// Do something with the selected contact's name...
            }
        }
    }
    //Customize Menu
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
    
    
}//End main class.