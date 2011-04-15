package com.hutchdesign.transitgenie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//getElementsByTagName()

/*
 * 	int seconds = (int) ((milliseconds / 1000) % 60);
	int minutes = (int) ((milliseconds / 1000) / 60);
 * 
 */

public class SingleRoute 
{
	private Document DOC;
	String arrival;		//arrival time ("Arrive by xx:xx")
	String leaveIn; 	//initial depart time ("Leave in x Minutes")
	String step1, step2, step3, step4;
	
	public SingleRoute(Document i) {
        this.DOC = i;		//Import Document.
        setImmediateData();		//Set (some) variables.
	}
	
	
	/* SET IMMEDIATE DATA ------------------------------------------------------
	 * 
	 * Set variables needed for summary of route
	 * (Viewed on Routes screen)
	 * (Deatiled info only needed for RouteDetail scren, 
	 * so is only pulled from Document if user chooses route based on summary)
	 * 
	 */
	private void setImmediateData()
	{
		Element e = (Element) DOC.getElementsByTagName("route").item(0); //Element <route>
		
		int d = Integer.parseInt(e.getAttribute("dep_time"));	//Depart time now stored in milliseconds
			int dMin  = (int) (d / 1000) / 60;		//Convert to proper minute
		leaveIn = "" + dMin + "min";				//Store depart time
		
		int a = Integer.parseInt(e.getAttribute("arr_time"));	//Arrival time now stored in milliseconds
			int aHour = (int) (a / 1000) / 3600;	//Convert to proper hour
			int aMin  = (int) (a / 1000) / 60;		//Convert to proper minute
		arrival = "" + aHour + ":" + aMin;			//Store depart time
		
		step1 = e.getChildNodes().item(0).getLocalName();
		step2 = e.getChildNodes().item(1).getLocalName();
		step3 = e.getChildNodes().item(2).getLocalName();
		step4 = e.getChildNodes().item(3).getLocalName();
		
	}//End setImmediateData()
}
