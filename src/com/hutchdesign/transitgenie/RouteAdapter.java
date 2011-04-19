package com.hutchdesign.transitgenie;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteAdapter extends BaseAdapter implements OnClickListener {
    private Context context;

    private List<SingleRoute> routeList;

    public RouteAdapter(Context context, List<SingleRoute> routeList) {
        this.context = context;
        this.routeList = routeList;
    }

    public int getCount() {
        return routeList.size();
    }

    public Object getItem(int position) {
        return routeList.get(position);
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
        
        TextView arrival = (TextView) convertView.findViewById(R.id.row_arriveBy_time);
        arrival.setText(curr.arrival);
        
        TextView leave = (TextView) convertView.findViewById(R.id.row_leaveIn_time);
        leave.setText(curr.leaveIn);
        
        ImageView step1 = (ImageView) convertView.findViewById(R.id.row_step1);
        setStepImage(step1, curr.steps[0]);
        
        
        return convertView;
    }

    public void onClick(View view) {
    	//Go to Route Detail for this route

    }
    
    private void setStepImage(ImageView i, String step)
    {
    	if(step.equals("PACE")){	
    		i.setImageResource(R.drawable.pace);	
    		return; }
    	if(step.equals("CTA")){ 	
    		i.setImageResource(R.drawable.cta0);	
    		return; }
		if(step.equals("walk")){
			i.setImageResource(R.drawable.walk_icon_small);
			return;	}
		
		i.setImageResource(R.drawable.unknown_vehicle_icon);
    }

}
