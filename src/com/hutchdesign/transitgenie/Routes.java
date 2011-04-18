package com.hutchdesign.transitgenie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Routes extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit);
        
        Document[] allRoutes = null;
        
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
			finish();
		}
		
		
        ListView AllRoutes = (ListView) findViewById(R.id.RouteListView);	//Load ListView from .xml       
        List<SingleRoute> RouteList = new ArrayList<SingleRoute>();			//Create ArrayList of Routes.
        
        //Parse and separate Documents.
        for(int x=0; x<allRoutes.length; ++x) 
        {
        	RouteList.add(new SingleRoute(allRoutes[x]));
        	
        }
     
        RouteAdapter adapter = new RouteAdapter(this, RouteList);			//Add ArrayList to adapter.
        AllRoutes.setAdapter(adapter);										//Set ListView adapter.
		
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
    
}//End main class.