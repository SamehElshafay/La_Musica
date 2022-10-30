package com.example.lamusica;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link second_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class second_frag extends Fragment {
    private String id ;
    public static MyDatabase3 myDatabase3 ;
    public ProgressTimer timer ;
    public ProgressTimer timer1 ;
    public static int position2 = 0 ;
    public int counter = -1 ;
//    public boolean isLooping = false ;
//    public boolean isPaused = false ;
//    public MyDatabase myDatabase ;
//    public MyDatabase2 myDatabase2 ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public second_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment second_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static second_frag newInstance(String param1, String param2) {
        second_frag fragment = new second_frag();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_frag, container, false);
        timer = Songs.timer ;
        timer1 = Songs.timer1 ;

        if(!first_frag.flag) {
            ((ImageView) view.findViewById(R.id.icon)).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
            myDatabase3 = new MyDatabase3(getActivity(), "playlists");
            myDatabase3.readMusics("2000000001");
            SongAdapter songAdapter = new SongAdapter(myDatabase3.getMusicName(), myDatabase3.getMusicArtist(), myDatabase3.getPlaylistIdM(), getActivity(), first_frag.iD);
            ((ListView) view.findViewById(R.id.songListPlaylist)).setAdapter(songAdapter);
        }

        ((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("222222222222222222222222222222" + first_frag.iD);
                if(((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).getCurrentItem() == 1 && first_frag.flag){
                    try {
                        ((TextView)view.findViewById(R.id.page)).setText(first_frag.name) ;
                        if(first_frag.number.equals("1")){
                            ((ImageView)view.findViewById(R.id.icon)).setBackground(getResources().getDrawable(R.drawable.ic_baseline_library_music_24));
                        }else {
                            ((ImageView)view.findViewById(R.id.icon)).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
                        }
                        myDatabase3 = new MyDatabase3(getActivity() , "playlists");
                        myDatabase3.readMusics(first_frag.iD);
                        SongAdapter songAdapter = new SongAdapter(myDatabase3.getMusicName() , myDatabase3.getMusicArtist() , myDatabase3.getPlaylistIdM() , getActivity() , first_frag.iD );
                        ((ListView)view.findViewById(R.id.songListPlaylist)).setAdapter(songAdapter);
                    }catch (Exception ex){

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ((ListView) view.findViewById(R.id.songListPlaylist)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position2 = position ;
                Songs.myDatabase.deleteAll();
                Songs.myDatabase.Add(myDatabase3.getMusicName().get(position2) , myDatabase3.getMusicArtist().get(position2) , myDatabase3.getMusicLocation().get(position2) , myDatabase3.getMusicAlbumName().get(position2) , myDatabase3.getMusicDuration().get(position2) , String.valueOf(myDatabase3.getMusicImagePath().get(position2)));
                runPlaylist(position);
            }
        });

        view.findViewById(R.id.backPlaylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewPager)getActivity().findViewById(R.id.playlistsViewPage)).setCurrentItem(0);
            }
        });

        ((ListView) view.findViewById(R.id.songListPlaylist)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new DialogEdit(getActivity() , (((ListView) view.findViewById(R.id.songListPlaylist)))).choise(position , first_frag.iD).show();
                return false;
            }
        });

        return view;
    }

    public void runPlaylist(int position){
        Songs.SongIn = 1 ;
        Uri URI  = Uri.parse(myDatabase3.getMusicLocation().get(position)) ;
        if(Songs.mediaPlayer.isPlaying()){
            Songs.mediaPlayer.pause();
            timer.pause();
            timer1.pause();
            changePauseOrStart(1);
            if(counter != position){
                Songs.mediaPlayer.stop();
                Songs.mediaPlayer.release();
                timer.resetTime();
                timer1.resetTime();
                changePauseOrStart(1);
                start(URI , position , myDatabase3.getMusicLocation().get(position));
                Songs.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onMusicComplite();
                    }
                });
            }
        }
        else if (counter == position){
            Songs.mediaPlayer.start();
            timer.start();
            timer1.start();
            changePauseOrStart(0);
        }
        else {
            Songs.mediaPlayer.stop();
            Songs.mediaPlayer.release();
            timer.resetTime();
            timer1.resetTime();
            changePauseOrStart(1);
            start(URI , position , myDatabase3.getMusicLocation().get(position));
            Songs.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onMusicComplite();
                }
            });
            counter = position ;
        }
    }

    public void start(Uri URI , int position , String location){
        Songs.mediaPlayer = MediaPlayer.create(getActivity(), URI) ;
        Songs.mediaPlayer.start();
        counter = position ;
        position2 = position ;
        changeNavigationButtomPlaylist(position , location);
        startTime(Long.parseLong(myDatabase3.getMusicDuration().get(position)));
        changePauseOrStart(0);
    }

    public void startTime(long millisecond){
        timer.setTimeLeftInMilliSeconds(millisecond);
        timer1.setTimeLeftInMilliSeconds(millisecond);

        timer.setFixedtime(millisecond);
        timer1.setFixedtime(millisecond);

        timer.start();
        timer1.start();
    }

    public void changePauseOrStart(int count){
        if(count == 0){
            getActivity().findViewById(R.id.PlayPause).setBackground(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
            getActivity().findViewById(R.id.PlayPause1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
        }else {
            getActivity().findViewById(R.id.PlayPause).setBackground(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
            getActivity().findViewById(R.id.PlayPause1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
        }
    }

    public void onMusicComplite(){
        if(Songs.myDatabase2.getAfterCompletion().equals("loop")){
            timer.resetTime();
            timer1.resetTime();
            Songs.myDatabase.deleteAll();
            Songs.myDatabase.Add(myDatabase3.getMusicName().get(position2) , myDatabase3.getMusicArtist().get(position2) , myDatabase3.getMusicLocation().get(position2) , myDatabase3.getMusicAlbumName().get(position2) , myDatabase3.getMusicDuration().get(position2) , String.valueOf(myDatabase3.getMusicImagePath().get(position2)));
            runPlaylist(position2);
        }else {
            next();
        }
    }

    public void next (){
        myDatabase3.readMusics(first_frag.iD);
        int s = (++position2) % myDatabase3.getMusicName().size() ;
        runPlaylist(s);
        Songs.myDatabase.deleteAll();
        Songs.myDatabase.Add(myDatabase3.getMusicName().get(s) , myDatabase3.getMusicArtist().get(s) , myDatabase3.getMusicLocation().get(s) , myDatabase3.getMusicAlbumName().get(s) , myDatabase3.getMusicDuration().get(s) , String.valueOf(myDatabase3.getMusicImagePath().get(s)));
    }

    public void changeNavigationButtomPlaylist(int position , String location){

        ((TextView)getActivity().findViewById(R.id.songName)).setText(myDatabase3.getMusicName().get(position));
        ((TextView)getActivity().findViewById(R.id.songName1)).setText(myDatabase3.getMusicName().get(position));

        MyDatabase3 myDatabase3 = new MyDatabase3(getActivity() , "playlists");

        myDatabase3.readFavoriteMusics();
        if(myDatabase3.getMusicLocation().contains(location)){
            changeFavoriteIcon(1);
        }else {
            changeFavoriteIcon(0);
        }

        myDatabase3.readMusics(first_frag.iD);
        int index = myDatabase3.getMusicLocation().indexOf(location);
        if(index >= 0){
            if(myDatabase3.getMusicArtist().get(position).equals("<unknown>")){
                ((TextView)getActivity().findViewById(R.id.songerName)).setText("unknown");
                ((TextView)getActivity().findViewById(R.id.songerName1)).setText("unknown");
            }else {
                ((TextView)getActivity().findViewById(R.id.songerName)).setText(myDatabase3.getMusicArtist().get(position));
                ((TextView)getActivity().findViewById(R.id.songerName1)).setText(myDatabase3.getMusicArtist().get(position));
            }

            if(myDatabase3.getMusicImagePath().get(position) != null) {
                Glide.with(this).load(myDatabase3.getMusicImagePath().get(position)).into(((CircleImageView) getActivity().findViewById(R.id.songImage)));
                Glide.with(this).load(myDatabase3.getMusicImagePath().get(position)).into(((CircleImageView) getActivity().findViewById(R.id.songImage1)));
            }else {

            }
        }
    }

    public void changeFavoriteIcon(int count){
        if(count == 0){
            getActivity().findViewById(R.id.favorite).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
            getActivity().findViewById(R.id.favorite1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
        }else {
            getActivity().findViewById(R.id.favorite).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
            getActivity().findViewById(R.id.favorite1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
        }
    }
}