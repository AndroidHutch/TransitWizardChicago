package com.hutchdesign.transitgenie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class RouteDetail extends Activity {
	
	Bundle b;
	SingleRoute selectedRoute;	//the Route to be displayed in detail
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit);	
        //NOTE: R.layout.transit is same .xml template used for Routes.java 
        //		=> imported widgets have same id tags.
        
        //Get bundle from previous activity
        b = getIntent().getExtras();
        
        //Import widgets from transit.xml
        TextView instr = (TextView) findViewById(R.id.route_instr);
        TextView orig = (TextView) findViewById(R.id.transit_origin1);
        TextView dest = (TextView) findViewById(R.id.transit_destn1);
        ImageButton map = (ImageButton) findViewById(R.id.transit_button);
        
        map.setImageResource(R.drawable.americas_small);
        
        instr.setText("Click row for map of specific step\nor select map button.");		//Set instructions
        orig.setText(b.getString("origin_string"));				//Set origin title
        dest.setText("To " +  b.getString("destin_string"));	//Set destination title
        
        int position = b.getInt("position");			
       	selectedRoute = Routes.RouteList.get(position);	//Get SingleRoute from RouteList to be displayed in detailed form.
       	
        ListView routeListView = (ListView) findViewById(R.id.RouteListView);		//Load ListView from .xml
        DetailAdapter adapter = new DetailAdapter(this, this, selectedRoute.allSteps);	//Add NodeList to adapter.
        routeListView.setAdapter(adapter);											//Set ListView adapter.
        	
       	
    }//End onCreate

	public void showMap(int p) {
		
		Intent i = new Intent(getApplicationContext(), MapStep.class);
    	b.putInt("position", p);
    	i.putExtras(b);
        startActivity(i);
	}
    
}//End main class.