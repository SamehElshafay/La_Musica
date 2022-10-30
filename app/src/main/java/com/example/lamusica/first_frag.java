package com.example.lamusica;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link first_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class first_frag extends Fragment {
    private MyDatabase3 myDatabase3 ;
    private ArrayList<String> names = new ArrayList<>() ;
    private playlistAdapter Adapter ;
    static String iD ;
    static String name ;
    static String number ;
    static boolean flag = false ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public first_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment first_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static first_frag newInstance(String param1, String param2) {
        first_frag fragment = new first_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_frag, container, false);
        myDatabase3 = new MyDatabase3(getActivity(), "playlists");

        myDatabase3.readPlaylists();

        Adapter = new playlistAdapter(myDatabase3.getPlaylistName(), getActivity());
        ((ListView) view.findViewById(R.id.listP)).setAdapter(Adapter);

        DialogEdit DE = new DialogEdit(getActivity(), ((ListView) view.findViewById(R.id.listP)));

        view.findViewById(R.id.newPlaylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DE.addPlaylist(Adapter).show();
            }
        });

        view.findViewById(R.id.favourites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_frag secondFrag = new  second_frag();
                final Bundle bundle = new Bundle();
                iD =  "2000000001" ;
                name = "Favourites" ;
                number = "2" ;
                flag = true ;
                ((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).setCurrentItem(1);
            }
        });

        ((ListView) view.findViewById(R.id.listP)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    iD = myDatabase3.getPlaylistId().get(position) ;
                    System.out.println("11111111111111111111111111111111111111" + myDatabase3.getPlaylistId().get(position));
                    name = myDatabase3.getPlaylistName().get(position) + " playlist" ;
                    number = "1" ;
                    flag = true ;
                    ((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).setCurrentItem(1);
                } catch (Exception ex) {
                    myDatabase3.readPlaylists();
                }
            }
        });

        ((ListView) view.findViewById(R.id.listP)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DE.deletePlaylist(Adapter, Integer.parseInt(myDatabase3.getPlaylistId().get(position))).show();
                return false;
            }
        });

//        ((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).getCurrentItem() == 0){
//
//                    System.out.println("======================");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        return view;
    }

    void refresh(View view) {
        myDatabase3.readPlaylists();
        Adapter.setNames(myDatabase3.getPlaylistName());
        ((ListView) view.findViewById(R.id.listP)).setAdapter(Adapter);
    }
}