/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * -----------------
 * Routes Activity
 * -----------------
 * Activity started immediately after user has specified origin and destination.
 * 		(ie. Upon "Go" button pressed on TransitGenieMain Activity)
 * 
 * Calls upon Request class (which sends/recieves info from server)
 * Takes recieved info from Request class and creates a list of SingleRoute's. (See SingleRoute.java)
 * Displays these routes in a custom ListView ('RouteListView' from transit.xml)
 * 			-> See RouteDetail.java which translates ArrayList of SingleRoute's,
 * 			into the text/images (from row.xml) displayed in the list.
 * 
 * Layout used: transit.xml
 * 
 * Includes: 
 * 
 * 		A) Origin & Destination in form of Strings (imported from TransitGenieMain via a Bundle)
 * 			Displayed at top of Activity.
 * 
 * 		B) ImageButton at top/right of screen with clock image (.../drawable/time_small.png). 
 * 			Allows user to edit destination/arrival times.
 * 
 * 		C) Custom ListView populated by SingleRoute's translated into images/text of row.xml via RouteAdapter.java.
 * 			row.xml includes arrival time, depart times, 
 * 			images of first four steps and their respective labels (eg. Bus number, walking distance)
 * 			Click listener leading to a detailed view of selected route (starts Activity 'RouteDetail')
 * 
 * 		D) Instructions at bottom of screen.
 * 
 * 
 * In a Nutshell: Custom List Views: (By Allie Curry)
 * 		Need:	
 * 			1. A ListView (type of Android View widget) stored in a .xml where list is to be displayed. 
 * 				-> A place to display the custom list.
 * 					[TransitGenie: R.id.RouteListView from transit.xml]
 * 
 * 			2. A list of objects to be displayed. (Can be any List type, eg. ArrayList or a custom List object) 
 * 				-> A list of objects in code form.
 * 					[TransitGenie: ArrayList<SingleRoute> RouteList from Routes Activity]
 * 
 * 			3. An .xml file defining layout of how each object is to be displayed on screen.
 * 				-> A visual layout template for each object.
 * 					[TransitGenie: row.xml]
 * 
 * 			4. An Adapter to translate list of objects (Step 2) into multiple iterations (= list.length) of template layout (Step 3),
 * 				and to send the visual list of objects to the ListView (Step 1).
 * 				(Setting onClickListener for the custom list happens here too, but is optional.)
 * 				-> A translator between objects' code and objects' display.
 * 					[TransitGenie: RouteAdapter.java]
 * 			
 */

package com.hutchdesign.transitgenie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Routes extends Activity {
	
	Bundle b;
	public static ArrayList<SingleRoute> RouteList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit);
        
        b = getIntent().getExtras();
        
        //Import TextViews to be updated
        TextView instr = (TextView) findViewById(R.id.route_instr);
        TextView orig = (TextView) findViewById(R.id.transit_origin1);
        TextView dest = (TextView) findViewById(R.id.transit_destn1);
        
        instr.setText("Click row for route details.\nUse button on top to adjust arrival/departure time preferences.");	//Instructions for user
        orig.setText(b.getString("origin_string"));				//Origin in form of String (imported from TransitGenieMain)
        dest.setText("To " +  b.getString("destin_string"));	//Destination in form of String (imported from TransitGenieMain)
        
        Document[] allRoutes = null;	//Array of DOM trees, each representing a singular route.
        try { //Call on Request class (initialized in TransitGenieMain) to build URL.
			TransitGenieMain.request.buildURL();	
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
        try { //Call on Request class (initialized in TransitGenieMain) to populate allRoutes array.
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
			Toast.makeText(getApplicationContext(), "Error: No Routes Found. Check internet connectivity.", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		
		else {
			
        ListView routeListView = (ListView) findViewById(R.id.RouteListView);	//Load ListView from .xml       
        RouteList = new ArrayList<SingleRoute>();		//Create ArrayList of SingleRoutes.
        
        //Parse and separate Documents.
        //for(int x=0; x<allRoutes.length; ++x) 
        //{
        	RouteList.add(new SingleRoute(allRoutes[0]));
        //}
     
        RouteAdapter adapter = new RouteAdapter(this, this, RouteList);		//Add ArrayList to adapter.
        routeListView.setAdapter(adapter);									//Set ListView adapter.
		
        /*
		 * Test to read dom tree new InputSource(url.openStream())); for(int k =
		 * 0; k < numberRequests; k++){ Element rootElement =
		 * documents[k].getDocumentElement();
		 * 
		 * NodeList nodes = rootElement.getChildNodes();
		 * 
		 * for( int i=0; i<nodes.getLength(); i++){ Node node = nodes.item(i);
		 * 
		 * if(node instanceof Element){ //a child element to process Element
		 * child = (Element) node; NamedNodeMap map = child.getAttributes();
		 * for( int j = 0; j < map.getLength(); j++)
		 * System.out.println(map.item(j).getNodeName());
		 * //System.out.println(attribute); } } }
		 */
		
		}
    }//End onCreate
    
    //Start RouteDetail Activity upon user selecting a specific route.
    public void getDetail(int p)
    {
    	Intent i = new Intent(getApplicationContext(), RouteDetail.class);
    	b.putInt("position", p);
    	i.putExtras(b);
        startActivity(i);
    }
    
}//End main class.