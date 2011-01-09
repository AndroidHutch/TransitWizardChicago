package com.hutchdesign.transitgenie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
//Edit Test
public class Request 
{
	private String server = "www.transitgenie.com:8080";
	private double version;
	private double originLongitude;
	private double originLatitude;
	private double destLongitude;
	private double destLatitude;
	private double queryTime;
	private int maxResults;
	private double walkingSpeed;
	private double sequenceNumber;
	private String streetMode;
	private String transitMode;
	private String currentDevice;
	private URL requestURL;

	//URL requestURL = new URL(requestString);
	public void buildURL() throws MalformedURLException
	{
		String requestString = "http://" + server + "/path_xml?version=" + version +
		"&origlon=" + originLongitude + "&origlat=" + originLatitude + "&destlon=" +
		destLongitude + "&destlat=" + destLatitude + "&dep_time=" + queryTime +
		"&max_results=" + maxResults + "&walking_speed=" + walkingSpeed + "&seqno=" +
		sequenceNumber + "&street_mode=%22" + streetMode + "%22&transit_mode=%22" +
		transitMode + "%22&udid=%22" + currentDevice + "%22";
		
		requestURL = new URL(requestString);
	}

	// Getter/Setters for all URL parts
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(double originLongitude) {
		this.originLongitude = originLongitude;
	}

	public double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public double getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(double destLongitude) {
		this.destLongitude = destLongitude;
	}

	public double getDestLatitude() {
		return destLatitude;
	}

	public void setDestLatitude(double destLatitude) {
		this.destLatitude = destLatitude;
	}

	public double getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(double queryTime) {
		this.queryTime = queryTime;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public double getWalkingSpeed() {
		return walkingSpeed;
	}

	public void setWalkingSpeed(double walkingSpeed) {
		this.walkingSpeed = walkingSpeed;
	}

	public double getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(double sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getStreetMode() {
		return streetMode;
	}

	public void setStreetMode(String streetMode) {
		this.streetMode = streetMode;
	}

	public String getTransitMode() {
		return transitMode;
	}

	public void setTransitMode(String transitMode) {
		this.transitMode = transitMode;
	}

	public String getCurrentDevice() {
		return currentDevice;
	}

	public void setCurrentDevice(String currentDevice) {
		this.currentDevice = currentDevice;
	}

	public URL getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(URL requestURL) {
		this.requestURL = requestURL;
	}
	
	
}


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