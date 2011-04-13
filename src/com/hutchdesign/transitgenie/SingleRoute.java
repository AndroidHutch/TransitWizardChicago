package com.hutchdesign.transitgenie;

public class SingleRoute 
{
	private String arrival;	//arrival time
	private String[] depart; //depart times
	
	
	public SingleRoute(String a, String[] d) {
        arrival = a;
        depart = d;
	}
	
	public String getArrival()
	{
		return arrival;
	}
	
	public String getDepart(int x)
	{
		return depart[x];
	}

}
