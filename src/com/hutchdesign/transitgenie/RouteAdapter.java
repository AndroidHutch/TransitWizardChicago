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
        setStepImage(step1, curr.steps[0]);
        setStepImage(step2, curr.steps[1]);
        setStepImage(step3, curr.steps[2]);
        setStepImage(step4, curr.steps[3]);
        
        //Import TextViews which show length of walk or bus number
        TextView txt1 = (TextView) convertView.findViewById(R.id.row_step1_text);
        TextView txt2 = (TextView) convertView.findViewById(R.id.row_step2_text);
        TextView txt3 = (TextView) convertView.findViewById(R.id.row_step3_text);
        TextView txt4 = (TextView) convertView.findViewById(R.id.row_step4_text);
        
        //Set Text under images
        setStepText(txt1, curr.stepText[0]);
        setStepText(txt2, curr.stepText[1]);
        setStepText(txt3, curr.stepText[2]);
        setStepText(txt4, curr.stepText[3]);
        
        //Store index of route
        TextView pos = (TextView) convertView.findViewById(R.id.row_pos);
        pos.setText(String.valueOf(position));
        
        convertView.setOnClickListener(this);
        return convertView;
    }
    
    private void setStepImage(ImageView i, String step)
    {
    	if(step == null){
    		i.setImageResource(R.drawable.filler);		//Blank Image
    		return; }
    	if(step.equals("walk")){
			i.setImageResource(R.drawable.walk_icon_small);
			return;	}
    	if(step.equals("PACE")){	
    		i.setImageResource(R.drawable.pace);	
    		return; }
    	if(step.equals("CTA")){ 	
    		i.setImageResource(R.drawable.cta_bus);	
    		return; }
    	if(step.equals("METRA")){ 	
    		i.setImageResource(R.drawable.metra);	
    		return; }
		
    	//Train Images
    	if(step.equals("G")){
    		i.setImageResource(R.drawable.cta_green);	
    		return; }
    	//TODO: Figure out remaining tags & add proper images.
		
		i.setImageResource(R.drawable.unknown_vehicle_icon);
    }
    
    private void setStepText(TextView t, String s)
    {
    	if(s == null){
    		t.setText(""); 
    		return; }
    	t.setText(s);
    }

    public void onClick(View view) {
    	
    	//Go to Route Detail for this route
    	TextView pos = (TextView) view.findViewById(R.id.row_pos);
        int p = Integer.valueOf((String) pos.getText());

    	Routes a  = (Routes) parent;
    	a.getDetail(p);

    }
}
