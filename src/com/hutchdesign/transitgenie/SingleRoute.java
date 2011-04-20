package com.hutchdesign.transitgenie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
	String[] steps = new String[5];
	
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
		int count = 0;
		//while(e.hasChildNodes()){
		//steps[0] = e.getChildNodes().item(0).getNodeName();
		//count++;
	//}

		
	}//End setImmediateData()
	
	/*
	 * Example request working with <routes> as root node (ie nodes.item(0) = <route>:
	 
	 
	NodeList nodes = rootElement.getChildNodes();
    System.out.println("getLocalName() = " + nodes.item(0).getLocalName());
    System.out.println("getNodeName() = " + nodes.item(0).getNodeName());
    System.out.println("getNodeValue() = " + nodes.item(0).getNodeValue());
    System.out.println("getNodeType() =" + nodes.item(0).getNodeType());
    NamedNodeMap map = nodes.item(0).getAttributes();
    System.out.println("Get Attribute 0 = " + map.item(0));
    System.out.println("Child = " + nodes.item(0).getChildNodes().item(0).getNodeName());
	
	Example request results:
	
	getLocalName() = null
	getNodeName() = route
	getNodeValue() = null
	getNodeType() =1
	Get Attribute 0 = arr_time="1279303255"
	Child = walk
	*/
	
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
