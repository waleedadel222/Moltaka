package com.university.moltaka.Drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.university.moltaka.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adv_Adapter extends ArrayAdapter<HashMap<String, String>> {

    private ArrayList<HashMap<String, String>> Data;

    int resourceId;
    Context context;

    public Adv_Adapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resourceId = resource;
        this.Data = objects;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;

        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adv_row, parent, false);
        }

       TextView job_name = (TextView) view.findViewById(R.id.job_name);
       TextView company_name = (TextView) view.findViewById(R.id.company_name);
       TextView description = (TextView) view.findViewById(R.id.description);
       TextView contacting = (TextView) view.findViewById(R.id.contacts);

        company_name.setText( Data.get(i).get("company_name"));
        job_name.setText( Data.get(i).get("job_name"));
        contacting.setText(Data.get(i).get("contacts"));
        description.setText(Data.get(i).get("description"));

       return view;
   }

}
