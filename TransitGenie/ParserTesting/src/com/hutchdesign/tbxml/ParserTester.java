package com.hutchdesign.tbxml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import android.widget.TextView;

import com.hutchdesign.tbxml.TBXML;
import com.hutchdesign.tbxml.TBXML.TBXMLException;
import com.hutchdesign.transitgenie.R;
import com.hutchdesign.transitgenie.RequestHandler;

public class ParserTester {

	/**
	 */
	public static void main(String[] args) throws IOException, TBXMLException {
		RouteData routeData;
        URL url = new URL("http://www.transitgenie.com:8080/path_xml?version=2.3&origlon=-87.839341&origlat=41.823309&destlon=-87.635990&destlat=41.878884&dep_time=1279296315&max_results=1&walking_speed=1.300000&seqno=6&street_mode=%22walk%22&transit_mode=%22Both%22");

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

           
    } catch (Exception e) {
    	System.out.println("Parsing Error");
            /* Display any Error to the GUI. */
            //tv.setText("Error: " + e.getMessage());
            //Log.e(MY_DEBUG_TAG, "WeatherQueryError", e);
            //return null;
    }
		
	}

}
