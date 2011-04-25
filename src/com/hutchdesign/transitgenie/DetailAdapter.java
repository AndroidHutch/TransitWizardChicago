package com.hutchdesign.transitgenie;

import java.util.List;

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

    private List<SingleRoute> routeList;

    public DetailAdapter(Context context, List<SingleRoute> routeList) {
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
            
            convertView = inflater.inflate(R.layout.detail, null);
        }

       
        
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

    public void onClick(View view) 
    {
    	
    	//TODO: Go to map for this step.

    }
}
