package com.qhubb.qhubb;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* PLEASE DESCRIBE THE CONTENTS OF THIS JAVA CLASS */

public class MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<AdatpterValues> searchArrayList;
	
	private LayoutInflater mInflater;

	public MyCustomBaseAdapter(Context context, ArrayList<AdatpterValues> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return searchArrayList.size();	
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.meetinglist, null);
			holder = new ViewHolder();
			holder.sstatus = (TextView) convertView.findViewById(R.id.topic);
			holder.sstime = (TextView) convertView.findViewById(R.id.stime);
			holder.ssdate = (TextView) convertView.findViewById(R.id.sdate);
			
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.sstatus.setText(searchArrayList.get(position).getstutus());
		holder.sstime.setText(searchArrayList.get(position).getTime());
		holder.ssdate.setText(searchArrayList.get(position).getDate());
		


		return convertView;
	}

	static class ViewHolder {
	
		TextView sstatus;
		TextView sstime;
		TextView ssdate;
		
		
	}
}
