package com.hutchdesign.transitgenie;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RouteAdapter extends BaseAdapter implements OnClickListener {
    private Context context;

    private List<SingleRoute> listPhonebook;

    public RouteAdapter(Context context, List<SingleRoute> listPhonebook) {
        this.context = context;
        this.listPhonebook = listPhonebook;
    }

    public int getCount() {
        return listPhonebook.size();
    }

    public Object getItem(int position) {
        return listPhonebook.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        SingleRoute curr = listPhonebook.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
        }
        TextView arrival = (TextView) convertView.findViewById(R.id.row_arriveBy_time);
        arrival.setText(curr.getArrival());

        return convertView;
    }

    public void onClick(View view) {
    	//Go to Route Detail for this route

    }

}
