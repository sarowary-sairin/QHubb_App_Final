/*Created by: Sairin Sadique and Sarowary Khan*/

package com.qhubb.qhubb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<MessageDetails> _data;
    Context _c;

    CustomAdapter (ArrayList<MessageDetails> data, Context c){
        _data = data;
        _c = c;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        if (v == null) /*if view is empty*/
        {
            LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_message, null); /*xml to list item message for timeline for twitter*/
        }

        ImageView image = (ImageView) v.findViewById(R.id.icon); /*view icon*/
        TextView timeView = (TextView)v.findViewById(R.id.time); /*view time*/
        TextView descView = (TextView)v.findViewById(R.id.description); /*view description*/


        MessageDetails msg = _data.get(position); /*position of message*/

        descView.setText(msg.desc); /*view description of message*/
        timeView.setText(msg.time); /*view time of message*/

        return v; /*show timeline/view*/
    }
}