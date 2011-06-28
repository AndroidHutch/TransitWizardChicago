/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * -----------------
 * Routes Activity
 * -----------------
 * Activity started after user has specified origin and destination (ie. Upon "Go" button pressed on TransitGenieMain Activity,
 * 		and server has sucessfully returned routes to (TransitGenieMain) Document[] allRoutes.	
 * 
 * Displays these routes retrived from main in a custom ListView ('RouteListView' from transit.xml)
 * 			-> See RouteDetail.java which translates ArrayList of SingleRoute's,
 * 			into the text/images (from row.xml) displayed in the list.
 * Contains additional methods utilized by ListView adapters (RouteAdapter, DetailAdapter) for proper variable display.
 * 
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        
        instr.setText("Click row for route details.\nUse clock button on top to adjust arrival/departure time.\nUse \"+\" button to add location to favorites.");	//Instructions for user
        orig.setText(b.getString("origin_string"));		//Origin in form of String (imported from TransitGenieMain)
        dest.setText(b.getString("destin_string"));		//Destination in form of String (imported from TransitGenieMain)
        
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
        
        
		
		
		//CREATE CUSTOM LIST VIEW	
        ListView routeListView = (ListView) findViewById(R.id.RouteListView);	//Load ListView from .xml       
        RouteList = new ArrayList<SingleRoute>();		//Create ArrayList of SingleRoutes.
        
        //Parse and separate Documents.
        for(int x=0; x<TransitGenieMain.allRoutes.length; ++x) 
        {
        	try{
        	RouteList.add(new SingleRoute(TransitGenieMain.allRoutes[x]));
        	}
        	catch(Exception e){
    			Toast.makeText(getApplicationContext(), "Error at Route: " + x, Toast.LENGTH_SHORT).show();
        		continue;}
        }
     
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
		
		
    }//End onCreate
    
    //Start RouteDetail Activity upon user selecting a specific route.
    public void getDetail(int p)
    {
    	Intent i = new Intent(getApplicationContext(), RouteDetail.class);
    	b.putInt("position", p);
    	i.putExtras(b);
        startActivity(i);
    }
    
    //------------------------------------------------------------------------------------------------------
    //ADDITIONAL METHODS...
    
    //setStepImage displays appropriate route image (in ImageView 'i') based on attribute String 'step'
    public static void setStepImage(ImageView i, String step)
    {
    	if(step == null){
    		i.setImageResource(R.drawable.filler);		//Blank Image
    		return; }
    	if(step.equals("walk")){
			i.setImageResource(R.drawable.walk_icon_small);
			return;	}
    	if(step.equals("PACE")){	
    		i.setImageResource(R.drawable.pace);	
    		return; }
    	if(step.equals("CTA")){ 	
    		i.setImageResource(R.drawable.cta_bus);	
    		return; }
    	if(step.equals("METRA")){ 	
    		i.setImageResource(R.drawable.metra);	
    		return; }
		
    	//Train Images
    	if(step.equals("Blue")){
    		i.setImageResource(R.drawable.cta_blue);	
    		return; }
    	if(step.equals("Pink")){
    		i.setImageResource(R.drawable.cta_pink);	
    		return; }
    	if(step.startsWith("P")){
    		i.setImageResource(R.drawable.cta_purple);	
    		return; }
    	if(step.startsWith("O")){
    		i.setImageResource(R.drawable.cta_orange);	
    		return; }
    	if(step.equals("R") || step.equals("Red")){
    		i.setImageResource(R.drawable.cta_red);	
    		return; }
    	if(step.startsWith("G")){
    		i.setImageResource(R.drawable.cta_green);	
    		return; }
    	if(step.startsWith("B")){
    		i.setImageResource(R.drawable.cta_brown);	
    		return; }
    	if(step.startsWith("Y")){
    		i.setImageResource(R.drawable.cta_yellow);	
    		return; }
		
		i.setImageResource(R.drawable.unknown_vehicle_icon);
    }
    
    //setStepText used to prevent NullPointerExceptions.
    public static void setStepText(TextView t, String s)
    {
    	if(s == null){
    		t.setText(""); 
    		return; }
    	t.setText(s);
    }
    
    //convertFromMeters returns value of x in either miles or feet in the form of a String.
    public static String convertFromMeters(double x)
    {
    	String ans = "";		//String to be returned
		double x_miles = (x / (1609.344));		//Convert x to miles.
		
		if(x_miles < 0.1)
		{
			int feet = (int) (x/3.2808399);		//Convert x to feet.
			ans = String.valueOf(feet) + "ft";
		}
		else
		{
			ans = String.valueOf(x_miles).substring(0, 3) + "mi";
		}
    	
    	return ans;
    }
    
    //Format milliseconds to time format mm:hh a (e.g. 12:00 pm)
    public static String formatMillis(long m)
    {
    	SimpleDateFormat date = new SimpleDateFormat("h:mm a");
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(m * 1000L);
		
		return "" + date.format(cal.getTime());
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
			        	   Double lat1 = TransitGenieMain.request.destinLatitude;
			        	   Double lon1 = TransitGenieMain.request.destinLongitude;
			        	   TransitGenieMain.addFavorite(name1, lat1, lon1); 
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