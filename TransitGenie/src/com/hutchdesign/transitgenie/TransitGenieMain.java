package com.hutchdesign.transitgenie;
//edit new svn browser
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TransitGenieMain extends Activity 
{
	private static final String MY_DEBUG_TAG = "Parsing Error";
	float latitude;
	float longitude;
	URL serverRequest;
	String originCoordinates;
	String destinationCoordinates;
	EditText origin;
	EditText destination;
	TextView tv;
	RouteData routeData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button goButton = (Button)findViewById(R.id.Go);
        
        goButton.setOnClickListener(mAddListener);
        
    }
    
	private OnClickListener mAddListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{
                /* Create a URL we want to load some xml-data from. */
                URL url = new URL("http://www.anddev.org/images/tut/basic/parsingxml/example.xml");

				//serverRequest = new URL("http://google.com");
				//BufferedReader in = new BufferedReader(new InputStreamReader(serverRequest.openStream()));
				
				origin = (EditText)findViewById(R.id.Origin);
				if( origin.getText().toString().equals("Use Current Location"))
				{
					parseRequest(url);
	                /* Display the TextView. */
	                //setContentView(tv);
					//get current GPS location coords
					//Context context = getApplicationContext();
					//int duration = Toast.LENGTH_LONG;
					//String text = "got Here";
					//Toast toast = Toast.makeText(context, text, duration);
					//toast.show();

				}
				else // Extract location from entered text
				{
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_LONG;
				String text = "needInternet";
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				}
				originCoordinates = extractLatLong(origin);
				destination = (EditText)findViewById(R.id.Destination);
				destinationCoordinates = extractLatLong(destination);
			}
			catch (Exception ex)
			{
                Log.e(MY_DEBUG_TAG, "sad face :(", ex);
			}
		}
	};
	
	private String extractLatLong(EditText origin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void parseRequest( URL url )
	{
        try {

                /* Get a SAXParser from the SAXPArserFactory. */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                /* Get the XMLReader of the SAXParser we created. */
                XMLReader xr = sp.getXMLReader();
                /* Create a new ContentHandler and apply it to the XML-Reader*/
                RequestHandler requestHandler = new RequestHandler();
                xr.setContentHandler(requestHandler);
               
                /* Parse the xml-data from our URL. */
                xr.parse(new InputSource(url.openStream()));
                /* Parsing has finished. */

                /* Our ExampleHandler now provides the parsed data to us. */
                routeData = requestHandler.getParsedData();
				/* Create a new TextView to display the parsing result later. */
		        tv = (TextView)findViewById(R.id.Routes);
		        /* Set the result to be displayed in our GUI. */
                tv.setText(routeData.toString());
                //return routeData;
               
        } catch (Exception e) {
                /* Display any Error to the GUI. */
                tv.setText("Error: " + e.getMessage());
                Log.e(MY_DEBUG_TAG, "WeatherQueryError", e);
                //return null;
        }
	}
}
	
	
