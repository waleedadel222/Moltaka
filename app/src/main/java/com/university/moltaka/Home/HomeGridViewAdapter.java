package com.university.moltaka.Home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.university.moltaka.R;

public class HomeGridViewAdapter extends ArrayAdapter<Object> {

	Context context;
	int resource;
	String[] colors;
	String gridRowContent[];

	public HomeGridViewAdapter(Context context, int resource, String gridRowContent[]) {
		super(context, resource, gridRowContent);

		this.context = context;
		this.resource = resource;
		this.gridRowContent = gridRowContent;
        colors = new String[] { "#40CCC2", "#F4D212", "#C84C55", "#FB724D","#6CB255", "#40CCC2"};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resource, parent, false);

			TextView tx = (TextView) convertView.findViewById(R.id.homeTextView);
			tx.setText(gridRowContent[position]);

			convertView.setBackgroundColor(Color.parseColor(colors[position]));

			convertView.setTag(tx);
		}
		return convertView;

	}

}
