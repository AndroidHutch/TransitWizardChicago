/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * ---------------------
 * RouteDetail Activity
 * ---------------------
 * Created upon selection of route from Routes Activity.
 * Displays detailed version of selected route.
 * 		-> Achieved with a custom ListView.
 * Shows Google map upon user selection of a specific step
 * 		or shows Google map of all steps if map button pressed.
 * 
 */

package com.hutchdesign.transitgenie;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        orig.setText(b.getString("origin_string"));		//Set origin title
        dest.setText(b.getString("destin_string"));		//Set destination title
        
        int position = b.getInt("position");			
       	selectedRoute = Routes.RouteList.get(position);	//Get SingleRoute from RouteList to be displayed in detailed form.
       	
        ListView routeListView = (ListView) findViewById(R.id.RouteListView);		//Load ListView from .xml
        DetailAdapter adapter = new DetailAdapter(this, this, selectedRoute.allSteps);	//Add NodeList to adapter.
        routeListView.setAdapter(adapter);											//Set ListView adapter.
        
        //-------------------------------------------------------
        //Import Add to Favorites Buttons
        Button fav_origin = (Button) findViewById(R.id.button_fav_origin);
        Button fav_dest = (Button) findViewById(R.id.button_fav_dest);
        
        //Add onClickListeners to add to favorites buttons.
        fav_origin.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		showDialog(0);
	    	}
        });
        fav_dest.setOnClickListener(new View.OnClickListener(){	
	    	public void onClick(View v){
	    		showDialog(1);
	    	}
        });
        //-------------------------------------------------------
       	
    }//End onCreate

	public void showMap(int p) {
		Node selectedNode = selectedRoute.allSteps.item(p);
		String nodeName = selectedNode.getNodeName();		//Get name of Node (e.g. "walk" or "transit")
        NamedNodeMap attr = selectedNode.getAttributes();	//Get current Node's attributes
        String startLat, startLong, endLat, endLong;
        startLat = startLong = endLat = endLong = "0";
        //endLat = "";
        if(nodeName.equals("transit")) //public transit node
		{
			startLat = attr.item(12).getNodeValue();	//Stop Latitude
			startLong = attr.item(13).getNodeValue();	//Stop Longitude

		}
		else //walk node
		{	
			
			NodeList s = selectedNode.getChildNodes().item(0).getChildNodes().item(0).getChildNodes(); // Get Points
			NamedNodeMap startPoints = s.item(0).getAttributes();
			NamedNodeMap endPoints = s.item(s.getLength() - 1).getAttributes();
			startLat = startPoints.item(0).getNodeValue();
			startLong = startPoints.item(1).getNodeValue();
			endLat = endPoints.item(0).getNodeValue();
			endLong = endPoints.item(1).getNodeValue();
		} 
        
		Intent i = new Intent(getApplicationContext(), MapStep.class);
    	b.putString("type", nodeName);
    	b.putString("startLat", startLat);
    	b.putString("startLong", startLong);
    	b.putString("endLat", endLat);
    	b.putString("endLong", endLong);
    	i.putExtras(b);
        startActivity(i);
	}
	
	//DIALOG CREATION
    protected Dialog onCreateDialog(int i) 
	{
		switch(i) {
		case 0:	//Origin		 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final String name = b.getString("origin_string");
			
			builder.setMessage("Add " + name + " to favorites?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //ADD TO DATABASE
			        	   Double lat = TransitGenieMain.request.originLatitude;
			        	   Double lon = TransitGenieMain.request.originLongitude;
			        	   TransitGenieMain.addFavorite(name, lat, lon); 
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			break;
			
		case 1:	//Destination		 
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			final String name1 = b.getString("destin_string");
			
			builder1.setMessage("Add " + name1 + " to favorites?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //ADD TO DATABASE
			        	   Double lat = TransitGenieMain.request.destLatitude;
			        	   Double lon = TransitGenieMain.request.destLongitude;
			        	   TransitGenieMain.addFavorite(name1, lat, lon); 
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert1 = builder1.create();
			alert1.show();
			break;
		}
		
		return null;
	}//END DIALOG CREATION
    
}//End main class.