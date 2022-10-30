package com.example.lamusica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class chooseAdapter extends BaseAdapter {
    private ArrayList<String> names = new ArrayList<>() ;
    private Context con ;

    chooseAdapter(ArrayList<String> names , Context con ){
        this.names = names  ;
        this.con = con ;
    }

    @Override
    public int getCount() {
        return names.size() ;
    }
    
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(con).inflate(R.layout.designcard1, parent , false);
        }

        ((TextView)convertView.findViewById(R.id.name)).setText(names.get(position));

        return convertView ;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
