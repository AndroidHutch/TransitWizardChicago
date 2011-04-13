package com.hutchdesign.transitgenie;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

//TODO: Wrap .xml files in in Scroll Views

public class TransitGenieMain extends Activity {
    protected static final int ORIGIN_REQUEST = 0;

    Bundle b;	//Hodls data passed between main activity and places activity
    //Set up Request URL 
    Request request = new Request();
    Document[] routes;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        b = new Bundle();
        
        //Import Buttons from main.xml
        Button button_go = (Button)findViewById(R.id.button_go);			//"Go" button on main screen (=> User is ready for routes)
        ImageButton button_origin = (ImageButton)findViewById(R.id.button_origin);	//User wishes to choose origin.
        ImageButton button_destn = (ImageButton)findViewById(R.id.button_destn);		//User wishes to choose destination.
        
        button_go.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		try {
					request.buildURL();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					routes = request.buildRoutes();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		//Run Routes activity
				//TODO ALLIE: Send routes doc array to Routes.class to display
	    		Intent i = new Intent(getApplicationContext(), Routes.class);
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
	    		
	    		//Run places activity
	    		Intent i = new Intent(getApplicationContext(), places.class);
	    		
	    		b.putInt("origin", 0);	//Pass to Places activity that user is requesting origin.
	    		i.putExtras(b);
	    		
	    		startActivityForResult(i, 0);
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
	    		
	    		b.putInt("origin", 1);	//Pass to Places activity that user is requesting destination.
	    		i.putExtras(b);
	    		
	            startActivityForResult(i, 1);
	            
	            //TODO: Grab destination selection from places activity
	            //possible reference: http://thedevelopersinfo.wordpress.com/2009/10/15/passing-data-between-activities-in-android/
	            
	            //EditText destn = (EditText) findViewById(R.id.text_destn2);
	            //destn.setText( b.getString("destin_string") );
	            
	    	}
        }); 
       
        
        
    }//End onCreate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Bundle bundl = data.getExtras();
    	
        // If the request went well (OK) and the request was ORIGIN_REQUEST
        if (resultCode == Activity.RESULT_OK && requestCode == ORIGIN_REQUEST) {
        	
        	EditText origin = (EditText) findViewById(R.id.text_origin2);
        	origin.setText(bundl.getString("origin_string"));
        	
					            // Perform a query to the contact's content provider for the contact's name
								//  Cursor cursor = getContentResolver().query(data.getData(),
								//  new String[] {places.latitude}, null, null, null);
								//  if (cursor.moveToFirst()) { // True if the cursor is not empty
								//  int columnIndex = cursor.getColumnIndex(places.latitude);
								//  request.originLatitude = cursor.getString(columnIndex);
								//  request = null;// Do something with the selected contact's name...
								//  }
        }
     // If the request went well (OK) and the request was for destination
        else if (resultCode == Activity.RESULT_OK)
        {
        	EditText destn = (EditText) findViewById(R.id.text_destn2);
        	destn.setText(bundl.getString("destin_string"));
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