package com.hutchdesign.transitgenie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Routes extends Activity {
	
	Bundle b;
	public static List<SingleRoute> RouteList;
	
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
        
        instr.setText("Click row for route details.\nUse button on top to adjust arrival/departure time preferences.");
        orig.setText(b.getString("origin_string"));
        dest.setText("To " +  b.getString("destin_string"));
        
        Document[] allRoutes = null;
        try {
			TransitGenieMain.request.buildURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			allRoutes = TransitGenieMain.request.buildRoutes();
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
		if(allRoutes == null)
		{
			Toast.makeText(getApplicationContext(), "Error: No Routes Found.", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		
		else {
			
        ListView routeListView = (ListView) findViewById(R.id.RouteListView);	//Load ListView from .xml       
        RouteList = new ArrayList<SingleRoute>();				//Create ArrayList of Routes.
        
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
    
    public void getDetail(int p)
    {
    	Intent i = new Intent(getApplicationContext(), RouteDetail.class);
    	b.putInt("position", p);
    	i.putExtras(b);
        startActivity(i);
    }
    
}//End main class.