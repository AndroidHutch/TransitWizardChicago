/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * ------------
 * SingleRoute
 * ------------
 * Initialized with a DOM tree stored in a Document.
 * Used as an item in a custom ListView for Routes.java.
 * Parses DOM tree for variable data needed for RouteAdapter.
 * 	 ...RouteAdapter then 'translates' the variable data to widgets.
 * 
 */


package com.hutchdesign.transitgenie;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/*
 *  NOTE:
 * 	int seconds = (int) ((milliseconds / 1000) % 60);
 *	int minutes = (int) ((milliseconds / 1000) / 60);
 *
 */

public class SingleRoute 
{
	private Document DOC;
	String arrival = "";	//arrival time (Arrive by "xx:xx")
	String depart = "";		//initial depart time (Depart by "xx:xx")
	String leaveIn = ""; 	//initial depart time from current time (Leave in "x" Minutes)
	String[] steps = new String[4];
	String[] stepText = new String[4];
	NodeList allSteps;
	Element routeNode;
	
	public SingleRoute(Document i) {
        this.DOC = i;		//Import Document.
        routeNode = (Element) DOC.getElementsByTagName("route").item(0); //Element <route>
        allSteps = routeNode.getChildNodes();
	}
	
	
	/* SET IMMEDIATE DATA ------------------------------------------------------
	 * 
	 * Set variables needed for summary of route
	 * (Viewed on Routes screen)
	 * (Detailed info only needed for RouteDetail screen, 
	 *  additional detail is pulled from Document in RouteDetail Activity)
	 * 
	 */
	public void setImmediateData()
	{	
		SimpleDateFormat date = new SimpleDateFormat("h:mm a");
		Calendar cal = Calendar.getInstance();

		//DEPART TIME
		long d = Long.parseLong(routeNode.getAttribute("dep_time")); //Depart time now stored in milliseconds
			cal.setTimeInMillis(d);
		depart = "" + date.format(cal.getTime());
			
		//"LEAVE IN X MINUTES"
			//long now = System.currentTimeMillis();
			long diff = 0; //(cal.getTimeInMillis() - now) /(1000*60);
		leaveIn = diff  + " min";				//Store depart time (relative to current time, eg. "+5 min")
		
		//ARRIVAL TIME
		long a = Long.parseLong(routeNode.getAttribute("arr_time"));	//Arrival time now stored in milliseconds
			cal.setTimeInMillis(a);
		arrival = "" + date.format(cal.getTime());
		
		//Get appropriate images and labels for first 4 route steps.
		for(int x=0; x<4; ++x)
		{
			if(allSteps.item(x) != null)
			{
				String nodeName = allSteps.item(x).getNodeName();
				NamedNodeMap attr = allSteps.item(x).getAttributes();	//Get current Node's attributes
				
				if(nodeName.equals("transit")) //public transit node
				{
					String temp = attr.item(0).getNodeValue();	//Agency Id
					String type = attr.item(1).getNodeValue();	//Route type
					String rtid = attr.item(2).getNodeValue();  //Route Id
					
					if(temp.equals("CTA") && type.equals("1"))	//Route is a CTA train.
					{
						temp = rtid;	//Step is now the route ID corresponding to which train
						
						String temp2 = attr.item(3).getNodeValue();
						temp2.replace(' ', '\n');
						stepText[x] = temp2;	//Set step text to long id name (eg. "Green Line")
					}
					else 
					{
						stepText[x] = rtid;		//Set step text to id name (eg. bus number)
					}
					steps[x] = temp;
				}
				else //walk node
				{	
					steps[x] = nodeName;
					NodeList s = allSteps.item(x).getChildNodes();
					double num = 0;
					String temp = "0";
					
					for(int y=0; y<s.getLength(); ++y)
					{
						NamedNodeMap attr1 = s.item(y).getAttributes();
						if(s.item(y).getNodeName().equals("start"))
						{
							temp = attr1.item(0).getNodeValue();
						}
						else
						{
							temp = attr1.item(1).getNodeValue();
						}
						num += Integer.valueOf(temp);
					} 
					
					stepText[x] = Routes.convertFromMeters(num);	//Convert total length from meters to miles or feet.
				}
				
			}
		}

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
