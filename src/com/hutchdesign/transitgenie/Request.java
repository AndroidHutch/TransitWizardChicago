package com.hutchdesign.transitgenie;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;



/* 
 * Holds variables for fetching XML from server, and parses and returns server responses in dom trees.
 * Currently returns all requested documents at once.
 * TODO: Return and show documents as server responds with each instead of waiting until end.
 * 
 */

public class Request {
	public String server = "www.transitgenie.com:8080";
	public Double version = 2.3;
	// User Defined:
	public double originLongitude = -87.839341;
	public double originLatitude = 41.823309;
	public double destinLongitude = -87.635990;
	public double destinLatitude = 41.878884;
	public long queryTime = System.currentTimeMillis() / 1000L;
	public int maxResults = 4;
	public double walkingSpeed = 1.30;
	public double sequenceNumber = 6;
	public String streetMode = "walk";
	public String transitMode = "Both";
	public String currentDevice;
	public URL requestURL;

	// URL requestURL = new URL(requestString);
	public void buildURL() throws MalformedURLException {
		//queryTime = (System.currentTimeMillis() / 1000L);
		String requestString = "http://" + server + "/path_xml?version="
				+ version + "&origlon=" + originLongitude + "&origlat="
				+ originLatitude + "&destlon=" + destinLongitude + "&destlat="
				+ destinLatitude + "&dep_time=" + queryTime + "&max_results="
				+ maxResults + "&walking_speed=" + walkingSpeed + "&seqno="
				+ sequenceNumber + "&street_mode=%22" + streetMode
				+ "%22&transit_mode=%22" + transitMode + "%22"; // &udid=%22" + currentDevice + "%22";
		Log.i("URL", requestString);
		requestURL = new URL(requestString);
//		String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (attribute*1000));
//	    System.out.println(date);
	}

	public Document[] buildRoutes() throws IOException,
			ParserConfigurationException, SAXException {
		buildURL();
//		URL url = new URL(
//				"http://www.transitgenie.com:8080/path_xml?version=2.3&origlon=-87.839341&origlat=41.823309&destlon=-87.635990&destlat=41.878884&dep_time=1279296315&max_results=3&walking_speed=1.300000&seqno=6&street_mode=%22walk%22&transit_mode=%22Both%22");
//		// try {
		//int numberRequests = 1;
		// /* Get a SAXParser from the SAXPArserFactory. */
		// SAXParserFactory spf = SAXParserFactory.newInstance();
		// SAXParser sp = spf.newSAXParser();
		//
		// /* Get the XMLReader of the SAXParser we created. */
		// XMLReader xr = sp.getXMLReader();
		// /* Create a new ContentHandler and apply it to the XML-Reader*/
		// RequestHandler requestHandler = new RequestHandler();
		// xr.setContentHandler(requestHandler);
		InputStream in = requestURL.openStream();
		Writer writer = new StringWriter();

		char[] buffer = new char[1024];

		Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, n);
		}

		in.close();

		String xmlBody = (writer.toString());

		// xmlBody = xmlBody.substring(xmlBody.indexOf("<?xml"),
		// xmlBody.indexOf("/routes>") + 1);
		String[] xmlSplit = xmlBody.split("(?<=/routes>)");
		for (int i = 0; i < xmlSplit.length; i++)
			System.out.println(xmlSplit[i] + "Done");
		/* Parse the xml-data from our URL. */
		// xr.parse(new InputSource(new
		// ByteArrayInputStream(xmlBody.getBytes("utf-8"))));
		// xr.parse(new InputSource(url.openStream()));
		/* Parsing has finished. */

		/* Our ExampleHandler now provides the parsed data to us. */
		// routeData = requestHandler.getParsedData();

		// String xmlBody;// = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document documents[] = new Document[maxResults];

		// Trim off excess beginnings from regex, last string is junk
		// for (int i = 0; i < xmlSplit.length - 1; i++){
		// xmlSplit[i] = xmlSplit[i].substring(xmlSplit[i].indexOf("<?xml"),
		// xmlSplit[i].lastIndexOf(">") + 1);
		// System.out.println(xmlSplit[i]);
		// }
		// System.out.println("Split OK");
		for (int i = 0; i < maxResults; i++) {
			try{
			documents[i] = builder
					.parse(new InputSource(new ByteArrayInputStream(xmlSplit[i]
							.substring(xmlSplit[i].indexOf("<?xml"),
									xmlSplit[i].lastIndexOf(">") + 1).getBytes(
									"utf-8"))));
			}
			catch(Exception e){break;}
		}
		return documents;
		
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
}
// Example URL
/*
 * http://www.transitgenie.com:8080/path_xml?version=2.3&origlon=-87.839341&origlat=41.823309&destlon=-87.635990&destlat=41.878884&dep_time=1279296315&max_results=1&walking_speed=1.300000&seqno=6&street_mode=%22walk%22&transit_mode=%22Both%22
 */
// // port 8080 adds coords of endpoint to returned route
// NSString *urlString = [NSString stringWithFormat:
// @"http://%@/path_xml?version=%@&origlon=%f&origlat=%f&destlon=%f&destlat=%f&%@_time=%d&max_results=10&walking_speed=%f&seqno=%d&street_mode=%%22%s%%22&transit_mode=%%22%@%%22&udid=%%22%@%%22",
// [delegate routeServer],
// [delegate version],
// [[[delegate origin] objectForKey:@"longitude"] doubleValue],
// [[[delegate origin] objectForKey:@"latitude"] doubleValue],
// [[[delegate destination] objectForKey:@"longitude"] doubleValue],
// [[[delegate destination] objectForKey:@"latitude"] doubleValue],
// (([delegate.mainView queryType]==QT_ARRIVAL)?@"arr":@"dep"),
// [delegate routeTime],
// walking_speed,
// sequence_number,
// ((delegate.mainView.walkOrBike.selectedSegmentIndex==0)?"walk":"bike"),
// [delegate.mainView configuredTransitMode],
// [[UIDevice currentDevice] uniqueIdentifier]];
