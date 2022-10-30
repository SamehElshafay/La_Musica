package com.example.lamusica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<String> names = new ArrayList<>() ;
    private ArrayList<String> artists = new ArrayList<>() ;
    private Context con ;
    MyAdapter(ArrayList<String> names , ArrayList<String> artists , Context con ){
        this.names = names  ;
        this.artists = artists ;
        this.con = con ;
    }

    @Override
    public int getCount() {
        return artists.size() ;
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
            convertView = LayoutInflater.from(con).inflate(R.layout.designcard, parent , false);
        }

        ((TextView)convertView.findViewById(R.id.name)).setText(names.get(position));
        if(artists.get(position).equals("<unknown>"))
            ((TextView)convertView.findViewById(R.id.artist)).setText("unknown");
        else
            ((TextView)convertView.findViewById(R.id.artist)).setText(artists.get(position));

        return convertView ;
    }


}
