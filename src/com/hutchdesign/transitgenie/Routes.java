package com.hutchdesign.transitgenie;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Routes extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit);
        
        ListView AllRoutes = (ListView) findViewById(R.id.RouteListView);
        
        
        List<SingleRoute> RouteList = new ArrayList<SingleRoute>();
        
        RouteList.add(new SingleRoute("12:34", new String[] {"one", "two"}));
			//Add all items from Document to RouteList
        
        RouteAdapter adapter = new RouteAdapter(this, RouteList);
        
        AllRoutes.setAdapter(adapter);
        
       	
    }//End onCreate
    
}//End main class.