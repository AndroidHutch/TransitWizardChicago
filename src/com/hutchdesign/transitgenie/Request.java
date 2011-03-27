package com.hutchdesign.transitgenie;

import java.net.MalformedURLException;
import java.net.URL;
//Edit Test
public class Request 
{
	public String server = "www.transitgenie.com:8080";
	public Double version = 2.3;
	public double originLongitude = 0.0;
	public double originLatitude = 0.0;
	public double destLongitude = 0.0;
	public double destLatitude = 0.0;
	public double queryTime = 1234;
	public int maxResults = 10;
	public double walkingSpeed = 1.30;
	public double sequenceNumber = 6;
	public String streetMode = "walk";
	public String transitMode = "Both";
	public String currentDevice;
	public URL requestURL;

	//URL requestURL = new URL(requestString);
	public void buildURL() throws MalformedURLException
	{
		String requestString = "http://" + server + "/path_xml?version=" + version +
		"&origlon=" + originLongitude + "&origlat=" + originLatitude + "&destlon=" +
		destLongitude + "&destlat=" + destLatitude + "&dep_time=" + queryTime +
		"&max_results=" + maxResults + "&walking_speed=" + walkingSpeed + "&seqno=" +
		sequenceNumber + "&street_mode=%22" + streetMode + "%22&transit_mode=%22" +
		transitMode + "%22"; //&udid=%22" + currentDevice + "%22";
		
		requestURL = new URL(requestString);
	}
	
}
// Example URL
/*
http://www.transitgenie.com:8080/path_xml?version=2.3&origlon=-87.839341&origlat=41.823309
&destlon=-87.635990&destlat=41.878884&dep_time=1279296315&max_results=10&walking_speed=1.300000
&seqno=6&street_mode=%22walk%22&transit_mode=%22Both%22
*/
//// port 8080 adds coords of endpoint to returned route
//NSString *urlString = [NSString stringWithFormat: @"http://%@/path_xml?version=%@&origlon=%f&origlat=%f&destlon=%f&destlat=%f&%@_time=%d&max_results=10&walking_speed=%f&seqno=%d&street_mode=%%22%s%%22&transit_mode=%%22%@%%22&udid=%%22%@%%22",
//					   [delegate routeServer],
//					   [delegate version],
//					   [[[delegate origin] objectForKey:@"longitude"] doubleValue], 
//					   [[[delegate origin] objectForKey:@"latitude"] doubleValue], 
//					   [[[delegate destination] objectForKey:@"longitude"] doubleValue],
//					   [[[delegate destination] objectForKey:@"latitude"] doubleValue],
//					   (([delegate.mainView queryType]==QT_ARRIVAL)?@"arr":@"dep"),
//					   [delegate routeTime], 
//					   walking_speed,
//					   sequence_number,
//					   ((delegate.mainView.walkOrBike.selectedSegmentIndex==0)?"walk":"bike"),
//					   [delegate.mainView configuredTransitMode],
//					   [[UIDevice currentDevice] uniqueIdentifier]];
