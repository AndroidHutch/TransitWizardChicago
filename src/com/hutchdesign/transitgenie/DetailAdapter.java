/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * --------------
 * DetailAdapter
 * --------------
 * Adapter for custom ListView in RouteDetail.java.
 * Initialized with a List of Nodes corresponding to each step in the current route.
 * 		e.g. Node walk -> Node transit -> Node walk
 * Each Node in the List becomes a "row" in the custom ListView.
 * 		-> Attributes from a Node are displayed in widgets stored in detail.xml.
 * Ensures that on user click, map corresponding to step is displayed.
 * 
 */

package com.hutchdesign.transitgenie;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity parentActivity;
    private NodeList stepList;
    
    public DetailAdapter(Context context, Activity parentActivity, NodeList stepList) {
        this.context = context;
        this.parentActivity = parentActivity;
        this.stepList = stepList;
    }

    public int getCount() {
        return stepList.getLength();
    }

    public Object getItem(int position) {
        return stepList.item(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	
        Node curr = stepList.item(position);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detail, null);
        }
        
        //Import widgets needed from detail.xml
        ImageView stepImage = (ImageView) convertView.findViewById(R.id.detail_step);	//Image of walking, bus, or train color
        TextView stepTag = (TextView) convertView.findViewById(R.id.detail_length);  	//Image Tag (tag = walk length OR bus number OR train line)
        
        TextView stepFrom = (TextView) convertView.findViewById(R.id.detail_text_from);	//Starting point for this step
        TextView stepTo = (TextView) convertView.findViewById(R.id.detail_text_to);		//Arrival point for this step
        TextView stepFor = (TextView) convertView.findViewById(R.id.detail_text_for);	//Length of step OR board & alight times
        TextView stepForTitle = (TextView) convertView.findViewById(R.id.detail_title_for); //default: "FOR", change to "BOARD" for bus/train
        
       
        //SET IMAGE AND TAG  -----------------------------------------------------------------------
        //(tag = walk length OR bus number OR train line)
        
        String nodeName = curr.getNodeName();		//Get name of Node (e.g. "walk" or "transit")
        NamedNodeMap attr = curr.getAttributes();	//Get current Node's attributes

        if(nodeName.equals("transit")) //public transit node
		{
			String temp = attr.item(0).getNodeValue();	//Agency Id
			String type = attr.item(1).getNodeValue();	//Route type
			String rtid = attr.item(2).getNodeValue();  //Route Id
			
			stepFrom.setText(attr.item(6).getNodeValue());	//"From" is attribute board_stop
			stepTo.setText(attr.item(14).getNodeValue());	//"To" is attribute alight_stop
			
			if(temp.equals("CTA") && type.equals("1"))	//Route is a CTA train.
			{
				temp = rtid;	//Step is now the route ID corresponding to which train

				String temp2 = attr.item(3).getNodeValue();
				temp2.replace(' ', '\n');
				stepTag.setText(temp2);	//Set step label (text) to long id name (eg. "Green Line")
			}
			else 
			{
				stepTag.setText(rtid);		//Set step text to id name (eg. bus number)
			}
			
			Routes.setStepImage(stepImage, temp);		//Set image corresponding to step
		}
		else //walk node
		{	
			Routes.setStepImage(stepImage, nodeName);		//Set image corresponding to step
			NodeList s = curr.getChildNodes();
			double num = 0;
			String temp = "0";
			boolean fromIsSet = false;
			
			for(int y=0; y<s.getLength(); ++y)
			{
				NamedNodeMap attr1 = s.item(y).getAttributes();
				String walkNodeName = s.item(y).getNodeName();	//= "street" or "start" or "end"
				
				if(!fromIsSet) //Set starting point to first street name.
				{
					stepFrom.setText(attr1.item(0).getNodeValue());
					fromIsSet = true; //starting point is set
				}
				
				
				if(walkNodeName.equals("start") || walkNodeName.equals("end"))
				{
					temp = attr1.item(0).getNodeValue();	//grab length of step in meters
					fromIsSet = false; //starting point needs to be reset
				}
				else
				{
					temp = attr1.item(1).getNodeValue();
					stepTo.setText(attr1.item(0).getNodeValue());
				}
				
				num += Integer.valueOf(temp);
			} 
			
			stepTag.setText(Routes.convertFromMeters(num));
			
		}
        //END SET IMAGE AND TAG  -------------------------------------------------------------------

        //View position is stored for use in onClick
        TextView pos = (TextView) convertView.findViewById(R.id.row_pos);
        pos.setText(String.valueOf(position));
        
        convertView.setOnClickListener(this);
        return convertView;
    }
    
    public void onClick(View view) 
    {
    	
    	//TODO: Go to map for this step.

    	//stepList.
    	TextView pos = (TextView) view.findViewById(R.id.row_pos);
        
    	int p = Integer.valueOf((String) pos.getText());
    	RouteDetail a  = (RouteDetail) parentActivity;
    	a.showMap(p);
    }
}
