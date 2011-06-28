/* Transit Genie Android 
 * v. 1.0
 * Code by Mike Hutcheson and Allie Curry
 * 
 * -------------
 * RouteAdapter
 * -------------
 * Adapter for custom ListView in Routes.java.
 * Initialized with a List of SingleRoute's.
 * Each SingleRoute in the List becomes a "row" in the custom ListView.
 * 		-> Variables from a SingleRoute are displayed in widgets stored in row.xml.
 * Ensures that on user click, detailed version of route is displayed.
 * 		-> Send SingleRoute from list to RouteDetail Activity.
 */

package com.hutchdesign.transitgenie;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity parent;

    private List<SingleRoute> routeList;
    
    public RouteAdapter(Context context, Activity p, List<SingleRoute> routeList) {
        this.context = context;
        this.routeList = routeList;
        this.parent = p;
    }

    public int getCount() {
        return routeList.size();
    }

    public Object getItem(int position) {
        return routeList.get(position);
    }
    
    public boolean moreThanOneSteps()
    {
    	for(int x=0; x<getCount(); ++x)
    	{
    		if(routeList.get(x).allSteps.getLength() > 0)
    		{
    			return true;
    		}
    	}
		return false;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	
        SingleRoute curr = routeList.get(position);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            convertView = inflater.inflate(R.layout.row, null);
        }

        curr.setImmediateData();	//Set variables needed
        
        //Show arrival time
        TextView arrival = (TextView) convertView.findViewById(R.id.row_arriveBy_time);
        arrival.setText(curr.arrival);
        
        //Show first depart time
        TextView dep1 = (TextView) convertView.findViewById(R.id.row_departBy_time1);
        dep1.setText(curr.depart);
        
        //Show "Leave in x min"
        TextView leave = (TextView) convertView.findViewById(R.id.row_leaveIn_time);
        leave.setText(curr.leaveIn);
        
        //Import ImageViews which represent modes of transportation for steps #1-4
        ImageView step1 = (ImageView) convertView.findViewById(R.id.row_step1);
        ImageView step2 = (ImageView) convertView.findViewById(R.id.row_step2);
        ImageView step3 = (ImageView) convertView.findViewById(R.id.row_step3);
        ImageView step4 = (ImageView) convertView.findViewById(R.id.row_step4);
        
        //Detmine & Set proper image
        Routes.setStepImage(step1, curr.steps[0]);
        Routes.setStepImage(step2, curr.steps[1]);
        Routes.setStepImage(step3, curr.steps[2]);
        Routes.setStepImage(step4, curr.steps[3]);
        
        //Import TextViews which show length of walk or bus number
        TextView txt1 = (TextView) convertView.findViewById(R.id.row_step1_text);
        TextView txt2 = (TextView) convertView.findViewById(R.id.row_step2_text);
        TextView txt3 = (TextView) convertView.findViewById(R.id.row_step3_text);
        TextView txt4 = (TextView) convertView.findViewById(R.id.row_step4_text);
        
        //Set Text under images
        Routes.setStepText(txt1, curr.stepText[0]);
        Routes.setStepText(txt2, curr.stepText[1]);
        Routes.setStepText(txt3, curr.stepText[2]);
        Routes.setStepText(txt4, curr.stepText[3]);
        
        //View position is stored for use in onClick
        TextView pos = (TextView) convertView.findViewById(R.id.row_pos);
        pos.setText(String.valueOf(position));
        
        convertView.setOnClickListener(this);
        return convertView;
    }

    public void onClick(View view) {
    	
    	//Go to Route Detail for this route
    	TextView pos = (TextView) view.findViewById(R.id.row_pos);
        int p = Integer.valueOf((String) pos.getText());

    	Routes a  = (Routes) parent;
    	a.getDetail(p);

    }
}
