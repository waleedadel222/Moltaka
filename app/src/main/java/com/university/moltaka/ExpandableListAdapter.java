package com.university.moltaka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> english, computerScience, computerSkills, business;
    Context context;
    public static ArrayList<String> skillsArray;
    private HashMap<Integer, boolean[]> mChildCheckStates;

    public ExpandableListAdapter(Context context) {

        this.context = context;
        skillsArray = new ArrayList<String>();
        mChildCheckStates = new HashMap<Integer, boolean[]>();

        listDataHeader = new ArrayList<String>();
        listDataHeader.add("English");
        listDataHeader.add("Computer skills");
        listDataHeader.add("Computer science ");
        listDataHeader.add("Finance/business");

        english = new ArrayList<String>();
        english.add("Toefl");
        english.add("Toeic");
        english.add("Ielts");

        computerSkills = new ArrayList<String>();
        computerSkills.add("Word");
        computerSkills.add("Office");
        computerSkills.add("Powerpoint");
        computerSkills.add("Excel");
        computerSkills.add("Mailings");

        computerScience = new ArrayList<String>();
        computerScience.add("Mobile development");
        computerScience.add("Web development");
        computerScience.add("DeskTop development");
        computerScience.add("Design");
        computerScience.add("Network");

        business = new ArrayList<String>();
        business.add("Accounting");
        business.add("Financing");

        listDataChild = new HashMap<String, List<String>>();
        listDataChild.put(listDataHeader.get(0), english);
        listDataChild.put(listDataHeader.get(1), computerSkills);
        listDataChild.put(listDataHeader.get(2), computerScience);
        listDataChild.put(listDataHeader.get(3), business);
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
        txtListChild.setText(childText);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;
        checkBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            checkBox.setChecked(getChecked[mChildPosition]);
        } else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
            mChildCheckStates.put(mGroupPosition, getChecked);
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    skillsArray.add(childText);
                } else {
                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    if (skillsArray.size() > 0) {
                        for (int i = 0; i < skillsArray.size(); i++) {
                            if (skillsArray.get(i).equals(childText)) {
                                skillsArray.remove(i);
                            }
                        }
                    }
                }
            }
        });
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView listHeader = (TextView) convertView.findViewById(R.id.listHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
