/*Created by: Sairin Sadique and Sarowary Khan*/

package com.qhubb.qhubb;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<AdatpterValues> searchArrayList;
	
	private LayoutInflater mInflater;

	public MyCustomBaseAdapter(Context context, ArrayList<AdatpterValues> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() { /*get size of array list*/
		return searchArrayList.size();	
	}

	public Object getItem(int position) { /*search array list for position*/
		return searchArrayList.get(position);
	}

	public long getItemId(int position) { /*get position*/
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.meetinglist, null);
			holder = new ViewHolder();
			holder.sstatus = (TextView) convertView.findViewById(R.id.topic); /*view status*/
			holder.sstime = (TextView) convertView.findViewById(R.id.stime); /*view time*/
			holder.ssdate = (TextView) convertView.findViewById(R.id.sdate); /*view date*/
			
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.sstatus.setText(searchArrayList.get(position).getstutus()); /*get status*/
		holder.sstime.setText(searchArrayList.get(position).getTime()); /*get time*/
		holder.ssdate.setText(searchArrayList.get(position).getDate()); /*get date*/
		


		return convertView;
	}

	static class ViewHolder {
	
		TextView sstatus; /*status*/
		TextView sstime; /*time*/
		TextView ssdate; /*date*/
		
		
	}
}
