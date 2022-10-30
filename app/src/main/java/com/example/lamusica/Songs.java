package com.example.lamusica;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Songs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Songs extends Fragment {
    private final int PERMISSION_REQUEST = 1 ;
    public ArrayList<String> names = new ArrayList<>() ;
    public ArrayList<String> artists = new ArrayList<>() ;
    public ArrayList<String> locations = new ArrayList<>() ;
    public ArrayList<Uri> imageUriPath = new ArrayList<>() ;
    public ArrayList<String> dataAdded = new ArrayList<>() ;
    public ArrayList<String> albumName = new ArrayList<>() ;
    public ArrayList<String> duration = new ArrayList<>() ;
    public ArrayList<String> newLocation = new ArrayList<>();
    public static MediaPlayer mediaPlayer ;
    public static ProgressTimer timer ;
    public static ProgressTimer timer1 ;
    public boolean isPaused = false ;
    public int counter = -1 ;
    public int position2 = 0 ;
    public boolean isLooping = false ;
    public static MyDatabase myDatabase ;
    public static MyDatabase2 myDatabase2 ;
    String numberOfSong = "" ;
    String rule = "" ;
    static int SongIn = 0 ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Songs() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Songs.
     */
    // TODO: Rename and change types and number of parameters
    public static Songs newInstance(String param1, String param2) {
        Songs fragment = new Songs();
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
        getActivity().findViewById(R.id.mainDrawer).setVisibility(View.INVISIBLE);
        View view1 = inflater.inflate(R.layout.fragment_songs, container, false);

        ((TextView) getActivity().findViewById(R.id.songName)).setSelected(true);
        ((TextView) getActivity().findViewById(R.id.songName1)).setSelected(true);


        myDatabase = new MyDatabase(getActivity() , "music" , "onlySong");
        myDatabase2 = new MyDatabase2(getActivity() , "mediarules" , "rules") ;

        timer = new ProgressTimer();
        timer1 = new ProgressTimer();

        rule = myDatabase2.getAfterCompletion() ;

        if(rule.equals("loop")){
            changeLoopIcon(0);
        }else {
            changeLoopIcon(1);
        }

        if(permissionAccepted()){
            numberOfSong = String.valueOf(getMusic(view1)) ;
        }


        if(myDatabase2.getFirstTime().equals("true")){
            myDatabase2.changeFirstTime();
        }

        getActivity().findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.mainDrawer).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.secondPage).setVisibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.bottomnavigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.mainDrawer).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.secondPage).setVisibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.drawer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.mainDrawer).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.secondPage).setVisibility(View.VISIBLE);
            }
        });

        getActivity().findViewById(R.id.mainDrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.mainDrawer).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.secondPage).setVisibility(View.VISIBLE);
            }
        });

        ((SeekBar)getActivity().findViewById(R.id.musicBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    long milli = ( progress * mediaPlayer.getDuration() ) / 100 ;
                    mediaPlayer.seekTo((int)milli);
                    timer.setTimeLeftInMilliSeconds(mediaPlayer.getDuration()-milli);
                    timer1.setTimeLeftInMilliSeconds(mediaPlayer.getDuration()-milli);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                timer.stop();
                timer1.stop();
                mediaPlayer.pause();
                changePauseOrStart(1);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
                timer.start();
                timer1.start();
                changePauseOrStart(0);
            }
        });

        timer = new ProgressTimer(0, ((TextView) getActivity().findViewById(R.id.countdown)), ((ProgressBar) getActivity().findViewById(R.id.progress)) , ((SeekBar)getActivity().findViewById(R.id.musicBar)));
        timer1 = new ProgressTimer(0, ((TextView) getActivity().findViewById(R.id.countdown1)), ((ProgressBar) getActivity().findViewById(R.id.progress1)) ,((SeekBar)getActivity().findViewById(R.id.musicBar1)));

        myDatabase.readAll();

        try {
            if(locations.contains(myDatabase.getLocations())){
                int position = locations.indexOf(myDatabase.getLocations());
                Uri URI  = Uri.parse(locations.get(position));
                mediaPlayer = MediaPlayer.create(getActivity(), URI) ;

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onMusicComplite();
                    }
                });

                mediaPlayer.seekTo(Integer.parseInt(myDatabase.getLastMills()));
                mediaPlayer.start();
                mediaPlayer.pause();

                changeNavigationButtom(position);

                ((SeekBar)getActivity().findViewById(R.id.musicBar)).setProgress(((Integer.parseInt(myDatabase.getLastMills()) * 100)/ Integer.parseInt(duration.get(position))));
                ((SeekBar)getActivity().findViewById(R.id.musicBar1)).setProgress(((Integer.parseInt(myDatabase.getLastMills()) * 100)/ Integer.parseInt(duration.get(position))));

                timer.setFixedtime(Integer.parseInt(duration.get(position)));
                timer1.setFixedtime(Integer.parseInt(duration.get(position)));

                if(Integer.parseInt(myDatabase.getLastMills()) < 0){
                    timer.setTimeLeftInMilliSeconds(0);
                    timer1.setTimeLeftInMilliSeconds(0);
                }
                else {
                    timer.setTimeLeftInMilliSeconds(Integer.parseInt(duration.get(position)) - Integer.parseInt(myDatabase.getLastMills()));
                    timer1.setTimeLeftInMilliSeconds(Integer.parseInt(duration.get(position)) - Integer.parseInt(myDatabase.getLastMills()));
                }

                timer.start();
                timer1.start();

                timer.pause();
                timer1.pause();
            }
            else {
                Uri URI  = Uri.parse(locations.get(0));
                mediaPlayer = MediaPlayer.create(getActivity(), URI) ;
                mediaPlayer.start();
                counter = 0 ;
                changeNavigationButtom(0);
                startTime(Long.parseLong(duration.get(0)));
            }
        }catch (Exception ex){
            try {
                Uri URI  = Uri.parse(locations.get(0));
                mediaPlayer = MediaPlayer.create(getActivity(), URI) ;
                mediaPlayer.start();
                counter = 0 ;
                changeNavigationButtom(0);
                startTime(Long.parseLong(duration.get(0)));
            }catch (Exception e){

            }
        }

        ((SeekBar)getActivity().findViewById(R.id.musicBar1)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    long milli = ( progress * mediaPlayer.getDuration() ) / 100 ;
                    mediaPlayer.seekTo((int)milli);
                    timer.setTimeLeftInMilliSeconds(mediaPlayer.getDuration()-milli);
                    timer1.setTimeLeftInMilliSeconds(mediaPlayer.getDuration()-milli);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                timer.stop();
                timer1.stop();
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
                timer.start();
                timer1.start();
            }
        });

        ((ListView) view1.findViewById(R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position2 = position ;
                myDatabase.deleteAll();
                myDatabase.Add(names.get(position) , artists.get(position) , locations.get(position) , albumName.get(position) , duration.get(position) , String.valueOf(imageUriPath.get(position)));
                run(position);
            }
        });

        ((ListView) view1.findViewById(R.id.list)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new DialogEdit(getActivity() , ((ListView) view1.findViewById(R.id.list)) ).selectPlaylist( names.get(position) , artists.get(position) , locations.get(position) , albumName.get(position) , duration.get(position) , String.valueOf(imageUriPath.get(position)) , dataAdded.get(position) ).show();
                return true;
            }
        });

        getActivity().findViewById(R.id.Loop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDatabase2.getAfterCompletion().equals("loop")) {
                    changeLoopIcon(1);
                    mediaPlayer.setLooping(false);
                    myDatabase2.continu();
                }else {
                    changeLoopIcon(0);
                    mediaPlayer.setLooping(true);
                    myDatabase2.loop();
                }
            }
        });
        getActivity().findViewById(R.id.Loop1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDatabase2.getAfterCompletion().equals("loop")) {
                    changeLoopIcon(1);
                    mediaPlayer.setLooping(false);
                    myDatabase2.continu();
                }else {
                    changeLoopIcon(0);
                    mediaPlayer.setLooping(true);
                    myDatabase2.loop();
                }
            }
        });


        getActivity().findViewById(R.id.PlayPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused){
                    mediaPlayer.pause();
                    changePauseOrStart(1);
                    timer.pause();
                    timer1.pause();
                    isPaused = true ;
                }else {
                    mediaPlayer.start();
                    changePauseOrStart(0);
                    timer.start();
                    timer1.start();
                    isPaused = false ;
                }
            }
        });
        getActivity().findViewById(R.id.PlayPause1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused){
                    mediaPlayer.pause();
                    changePauseOrStart(1);
                    timer.pause();
                    timer1.pause();
                    isPaused = true ;
                }else {
                    mediaPlayer.start();
                    changePauseOrStart(0);
                    isPaused = false ;
                    timer.start();
                    timer1.start();
                }
            }
        });

        getActivity().findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        getActivity().findViewById(R.id.Next1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        getActivity().findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pravious();
            }
        });
        getActivity().findViewById(R.id.previous1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pravious();
            }
        });

        MyDatabase3 myDatabase3 = new MyDatabase3(getActivity() , "playlists");

        getActivity().findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavorite(myDatabase3);
            }
        });
        getActivity().findViewById(R.id.favorite1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavorite(myDatabase3);
            }
        });

        ((SearchView)getActivity().findViewById(R.id.search)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                newLocation.clear();
                int i = 0 ;
                if(newText.equals("")){
                    getMusic(view1) ;
                }
                else {
                    scan(view1) ;
                    for (String name : names) {
                        if (name.toLowerCase().contains(newText.toLowerCase())) {
                            newLocation.add(locations.get(i));
                        }
                        i++;
                    }
                    searchGetMusic(view1, newLocation);
                }
                return false;
            }
        });

        getActivity().findViewById(R.id.menuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(v);
            }
        });

        return view1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    getMusic(getView());
                }
            } else {
                Toast.makeText(getActivity(), "no Permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(permissionAccepted()){
            myDatabase.AddLastMills( String.valueOf(Integer.parseInt(duration.get(position2)) - timer.getTimeLeftInMilliSeconds()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(permissionAccepted()){
            myDatabase.AddLastMills( String.valueOf(Integer.parseInt(duration.get(position2)) - timer.getTimeLeftInMilliSeconds()));
        }
    }

    public int getMusic(View view){
        names.clear() ; artists.clear() ; locations.clear() ; albumName.clear() ; duration.clear() ; imageUriPath.clear() ;
        Cursor songCursor = getActivity().getContentResolver().query( /*songUri*/ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,
                null , null , null , null );

        if(songCursor != null && songCursor.moveToFirst()){
            do {
                if(myDatabase2.getSkip().equals("true")){
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 60000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }else {
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 3000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }
            } while (songCursor.moveToNext());
            MyAdapter Adapter = new MyAdapter(names, artists, getActivity());
            ((ListView) view.findViewById(R.id.list)).setAdapter(Adapter);
        }
        sort();
        return names.size();
    }

    private void addToList(Cursor songCursor) {
        names.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        artists.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        locations.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        albumName.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        duration.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
        imageUriPath.add(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
        dataAdded.add(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
    }

    public void sort(){
        for(int i = 1 ; i < names.size() ;i++){
            for(int x = 0 ; x < names.size() - i ; x++){
                if(myDatabase2.getMethodSort().compareTo("by character") == 0 && names.get(x).compareTo(names.get(x+1)) < 0){
                    swapAll(x);
                }
                if(myDatabase2.getMethodSort().compareTo("by data added") == 0 && timer.ConvertMilli(Long.parseLong(dataAdded.get(x))).compareTo(timer.ConvertMilli(Long.parseLong(dataAdded.get(x+1)))) < 0){
                    swapAll(x);
                }
                if(myDatabase2.getMethodSort().compareTo("by time") == 0 && timer.ConvertMilli(Long.parseLong(duration.get(x))).compareTo(timer.ConvertMilli(Long.parseLong(duration.get(x+1)))) > 0){
                    swapAll(x);
                }
            }
        }
    }

    public void swapAll(int position){
        swap(names , position);
        swap(artists , position);
        swap(locations , position);
        swap(albumName , position);
        swap(duration , position);
        swapUri(imageUriPath , position);
        swap(dataAdded , position);
    }

    public void swap(ArrayList<String> arrayList , int position){
        String temp = "" ;
        temp = arrayList.get(position) ;
        arrayList.set(position ,arrayList.get(position+1));
        arrayList.set(position+1 ,temp);
    }

    public void swapUri(ArrayList<Uri> arrayList , int position){
        Uri temp = Uri.parse("");
        temp = arrayList.get(position) ;
        arrayList.set(position ,arrayList.get(position+1));
        arrayList.set(position+1 ,temp);
    }

    public int scan(View view){
        names.clear() ; artists.clear() ; locations.clear() ; albumName.clear() ; duration.clear() ; imageUriPath.clear() ;
        Cursor songCursor = getActivity().getContentResolver().query( /*songUri*/ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,
                null , null , null , null );
        if(songCursor != null && songCursor.moveToFirst()){
            do {
                if(myDatabase2.getSkip().equals("true")){
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 60000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }else {
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 3000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }
            } while (songCursor.moveToNext());
        }
        return locations.size();
    }

    public void searchGetMusic(View view , ArrayList<String> locationForSearch ){
        names.clear() ; artists.clear() ; locations.clear() ; albumName.clear() ; duration.clear() ; imageUriPath.clear() ;
        Cursor songCursor = getActivity().getContentResolver().query( /*songUri*/ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,
                null , null , null , null );
        if(songCursor != null && songCursor.moveToFirst()){
            do {
                if(myDatabase2.getSkip().equals("true")){
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 60000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }else {
                    if (Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) > 3000 && Integer.parseInt(songCursor.getString(/* index */ songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))) <= 82800000) {
                        addToList(songCursor);
                    }
                }
            } while (songCursor.moveToNext());
            MyAdapter Adapter = new MyAdapter(names, artists, getActivity());
            ((ListView) view.findViewById(R.id.list)).setAdapter(Adapter);
        }
    }

    public void changeNavigationButtomPlaylist(int position , String ID){
        System.out.println(position + " ==============================================================================");
        ((TextView)getActivity().findViewById(R.id.songName)).setText(second_frag.myDatabase3.getMusicName().get(position));
        ((TextView)getActivity().findViewById(R.id.songName1)).setText(second_frag.myDatabase3.getMusicName().get(position));

        MyDatabase3 myDatabase3 = new MyDatabase3(getActivity() , "playlists");
        myDatabase3.readFavoriteMusics();
        ArrayList<String> location = myDatabase3.getMusicLocation() ;
        myDatabase3.readMusics(ID);

        if(location.contains(myDatabase3.getMusicLocation().get(position))){
            changeFavoriteIcon(1);
        }else {
            changeFavoriteIcon(0);
        }

        for(int i = 0 ; i < second_frag.myDatabase3.readMusics(ID) ; i++){
            System.out.println(second_frag.myDatabase3.getMusicName().get(i) + "llllllllllllllllllllllllllllllllllllll");
        }

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

    public void changeNavigationButtom(int position){
        ((TextView)getActivity().findViewById(R.id.songName)).setText(names.get(position));
        ((TextView)getActivity().findViewById(R.id.songName1)).setText(names.get(position));

        MyDatabase3 myDatabase3 = new MyDatabase3(getActivity() , "playlists");
        myDatabase3.readFavoriteMusics();
        if(myDatabase3.getMusicLocation().contains(locations.get(position2))){
            changeFavoriteIcon(1);
        }else {
            changeFavoriteIcon(0);
        }

        if(artists.get(position).equals("<unknown>")){
            ((TextView)getActivity().findViewById(R.id.songerName)).setText("unknown");
            ((TextView)getActivity().findViewById(R.id.songerName1)).setText("unknown");
        }else {
            ((TextView)getActivity().findViewById(R.id.songerName)).setText(artists.get(position));
            ((TextView)getActivity().findViewById(R.id.songerName1)).setText(artists.get(position));
        }

        if(imageUriPath.get(position) != null) {
            Glide.with(this).load(imageUriPath.get(position)).into(((CircleImageView) getActivity().findViewById(R.id.songImage)));
            Glide.with(this).load(imageUriPath.get(position)).into(((CircleImageView) getActivity().findViewById(R.id.songImage1)));
        }else {

        }
    }

    public void run(int position){
        SongIn = 0 ;
        Uri URI  = Uri.parse(locations.get(position)) ;
        if (mediaPlayer == null){
            start(URI , position);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onMusicComplite();
                }
            });
        }
        else if(position != counter){
            mediaPlayer.stop();
            mediaPlayer.release();
            timer.resetTime();
            timer1.resetTime();
            changePauseOrStart(1);
            start(URI , position);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onMusicComplite();
                }
            });
        }
        else if(counter == position){
            if(!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                timer.start();
                changePauseOrStart(0);
            }else{
                mediaPlayer.pause();
                timer.pause();
                changePauseOrStart(1);
            }
        }
    }

    public void runPlaylist(int position , String ID){
        SongIn = 1 ;
        Uri URI  = Uri.parse(second_frag.myDatabase3.getMusicLocation().get(position)) ;
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            timer.pause();
            timer1.pause();
            changePauseOrStart(1);
            if(counter != position){
                Songs.mediaPlayer.stop();
                Songs.mediaPlayer.release();
                timer.resetTime();
                timer1.resetTime();
                changePauseOrStart(1);
                startPlaylist(URI , position , ID);
                Songs.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onMusicComplite();
                    }
                });
            }
        }
        else if (counter == position){
            mediaPlayer.start();
            timer.start();
            timer1.start();
            changePauseOrStart(0);
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            timer.resetTime();
            timer1.resetTime();
            changePauseOrStart(1);
            startPlaylist(URI , position , ID);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onMusicComplite();
                }
            });
            counter = position ;
        }
    }

    public void start(Uri URI , int position){
        mediaPlayer = MediaPlayer.create(getActivity(), URI) ;
        mediaPlayer.start();
        counter = position ;
        position2 = position ;
        changeNavigationButtom(position);
        startTime(Long.parseLong(duration.get(position)));
        changePauseOrStart(0);
    }

    public void startPlaylist(Uri URI , int position , String ID){
        mediaPlayer = MediaPlayer.create(getActivity(), URI) ;
        mediaPlayer.start();
        counter = position ;
        position2 = position ;
        changeNavigationButtomPlaylist(position , ID);
        startTime(Long.parseLong(second_frag.myDatabase3.getMusicDuration().get(position)));
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
        if(myDatabase2.getAfterCompletion().equals("loop")){
            timer.resetTime();
            timer1.resetTime();
            myDatabase.deleteAll();
            myDatabase.Add(names.get(position2) , artists.get(position2) , locations.get(position2) , albumName.get(position2) , duration.get(position2) , String.valueOf(imageUriPath.get(position2)));
            run(position2);
        }else {
            next();
        }
    }

    public void next (){
//        if(((ViewPager)getActivity().findViewById(R.id.view_pager)).getCurrentItem() == 0) {
        if(SongIn == 0) {
            run((++position2) % names.size());
            myDatabase.deleteAll();
            myDatabase.Add(names.get(position2) , artists.get(position2) , locations.get(position2) , albumName.get(position2) , duration.get(position2) , String.valueOf(imageUriPath.get(position2)));
        }else {
            second_frag.myDatabase3.readMusics(first_frag.iD);
            int index = (++second_frag.position2) % second_frag.myDatabase3.getMusicName().size() ;
            runPlaylist(index , first_frag.iD);

            Songs.myDatabase.deleteAll();
            Songs.myDatabase.Add(second_frag.myDatabase3.getMusicName().get(index) , second_frag.myDatabase3.getMusicArtist().get(index) , second_frag.myDatabase3.getMusicLocation().get(index) , second_frag.myDatabase3.getMusicAlbumName().get(index) , second_frag.myDatabase3.getMusicDuration().get(index) , String.valueOf(second_frag.myDatabase3.getMusicImagePath().get(index)));
        }
    }

    public void pravious (){
        if(position2 == 0) {
            position2 = names.size();
            run(--position2);
        }
        else {
            run(--position2);
        }
        myDatabase.deleteAll();
        myDatabase.Add(names.get(position2) , artists.get(position2) , locations.get(position2) , albumName.get(position2) , duration.get(position2) , String.valueOf(imageUriPath.get(position2)));
    }

    public void showPopMenu(View v){
        PopupMenu popupMenu = new PopupMenu( getActivity() , v , Gravity.TOP);
        popupMenu.getMenuInflater().inflate(R.menu.mymenu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.item1) {
                    Intent intent = new Intent(getActivity() , setting.class);
                    intent.putExtra("numberOfSong" , numberOfSong );
                    startActivity(intent);
                }
                return true ;
            }
        });
        popupMenu.show();
    }

    public void showPopMenuMusic(View v){
        PopupMenu popupMenu = new PopupMenu( getActivity() , v , Gravity.TOP);
        popupMenu.getMenuInflater().inflate(R.menu.mymenu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.item1) {

                }
                return true ;
            }
        });
        popupMenu.show();
    }

    public void changeFavorite(MyDatabase3 myDatabase3){
        myDatabase3.readFavoriteMusics();
        if(myDatabase3.getMusicLocation().contains(locations.get(position2))){
            myDatabase3.deleteUsingLocation(locations.get(position2));
            changeFavoriteIcon(0);
        }else {
            myDatabase3.addMusic(2000000001 , 2000000001 , names.get(position2) , artists.get(position2) , locations.get(position2) , albumName.get(position2) , duration.get(position2) , String.valueOf(imageUriPath.get(position2)) , dataAdded.get(position2));
            changeFavoriteIcon(1);
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

    public void changeLoopIcon(int count){
        if(count == 0) {
            getActivity().findViewById(R.id.Loop).setBackground(getResources().getDrawable(R.drawable.ic_baseline_autorenew_24));
            getActivity().findViewById(R.id.Loop1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_autorenew_24));
        }else {
            getActivity().findViewById(R.id.Loop).setBackground(getResources().getDrawable(R.drawable.ic_baseline_compare_arrows_24));
            getActivity().findViewById(R.id.Loop1).setBackground(getResources().getDrawable(R.drawable.ic_baseline_compare_arrows_24));
        }
    }

    public boolean permissionAccepted(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
            return false ;
        } else {
            return true ;
        }
    }

}