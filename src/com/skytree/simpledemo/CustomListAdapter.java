package com.skytree.simpledemo;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final  ArrayList<String> chaptername;
	private  final ArrayList<String> datename;
	String[] arrchaptername;
	String[] arrdate;
	int Flag;
	public CustomListAdapter(Activity context, ArrayList<String> strcontent, ArrayList<String> strdate,int flag) {
		super(context, R.layout.mylist, strcontent);
		// TODO Auto-generated constructor stub

		this.context=context;
		this.chaptername=strcontent;
		this.datename=strdate;
		this.Flag=flag;
	}

	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.mylist, null,true);
		arrchaptername = chaptername.toArray(new String[chaptername.size()]);
		arrdate=datename.toArray(new String[datename.size()]);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
		TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

		txtTitle.setText(arrchaptername[position]);
		extratxt.setText(arrdate[position]);
		extratxt.setVisibility(view.VISIBLE);
		if (Flag==1)
		{
			extratxt.setVisibility(view.GONE);
		}
		return rowView;

	};
}