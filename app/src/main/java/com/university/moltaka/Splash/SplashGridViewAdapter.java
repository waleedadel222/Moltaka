package com.university.moltaka.Splash;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.university.moltaka.R;

public class SplashGridViewAdapter extends ArrayAdapter<Object>{

	Context context;
	int resource;
	String[] numbers;
	Integer[] imgResources;

	public SplashGridViewAdapter(Context context, int resource, String[] numbers, Integer[] imgResources) {
		super(context, resource, numbers);
		this.context=context;
		this.resource=resource;
		this.numbers=numbers;
		this.imgResources=imgResources;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resource, parent, false);
			
			TextView tx= (TextView)convertView.findViewById(R.id.textView);
			tx.setText(numbers[position]);
			
			ImageView img=(ImageView)convertView.findViewById(R.id.imageView);
			img.setImageResource(imgResources[position]);
			
			convertView.setTag(tx);
		}
		return convertView;
		
	}
	

}
