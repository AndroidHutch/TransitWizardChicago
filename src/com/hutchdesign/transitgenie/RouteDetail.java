package com.hutchdesign.transitgenie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RouteDetail extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit);
        
        TextView instr = (TextView) findViewById(R.id.route_instr);
        instr.setText("Click row for map of specific step\nor select map button.");
        
        
       	
    }//End onCreate
    
}//End main class.