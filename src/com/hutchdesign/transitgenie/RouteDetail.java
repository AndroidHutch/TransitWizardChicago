package com.hutchdesign.transitgenie;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    
}//End main class.