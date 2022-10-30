package com.example.lamusica;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class DialogEdit {
    private ListView listView ;
    private Context con ;
    private EditText input ;
    private MyDatabase3 myDatabase3 ;

    DialogEdit(Context con , ListView listView){
        this.con = con ;
        this.listView = listView ;
    }

    AlertDialog addPlaylist(playlistAdapter PlaylistAdapter){
        myDatabase3 = new MyDatabase3(con ,"playlists");
        AlertDialog.Builder builder = new AlertDialog.Builder(con) ;
        builder.setMessage("playlist name");
        input = new EditText(con) ;
        input.setBackgroundResource(R.drawable.edit_text_design);
        input.setPadding(50 , 50 ,50 , 50 );
        builder.setView(input);

        builder.setPositiveButton("add" , new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().equals("")) {
                    myDatabase3.addPlayList((int) Math.round(Math.random() * 200000000), input.getText().toString());
                    myDatabase3.readPlaylists();
                    PlaylistAdapter.setNames(myDatabase3.getPlaylistName());
                    listView.setAdapter(PlaylistAdapter);
                    Toast.makeText(con , "playlist created" , Toast.LENGTH_SHORT).show() ;
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create() ;
        return ad ;
    }

    AlertDialog deletePlaylist(playlistAdapter PlaylistAdapter , int id){
        myDatabase3 = new MyDatabase3(con ,"playlists");
        AlertDialog.Builder builder = new AlertDialog.Builder(con) ;
        builder.setMessage("Do you want to delete the playlist ?");

        builder.setPositiveButton("yes" , new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabase3.deletePlaylist(id);
                myDatabase3.readPlaylists();
                PlaylistAdapter.setNames(myDatabase3.getPlaylistName());
                listView.setAdapter(PlaylistAdapter);
                Toast.makeText(con , "playlist deleted" , Toast.LENGTH_SHORT).show() ;
            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create() ;
        return ad ;
    }

    AlertDialog selectPlaylist(String name , String artist , String location , String albumName , String duration , String imageUriPath , String dataAdded){
        myDatabase3 = new MyDatabase3(con ,"playlists");
        AlertDialog.Builder builder = new AlertDialog.Builder(con , R.style.AlertDialogCustom) ;
        builder.setMessage("playlist name");

        ListView listView = new ListView(con) ;
        builder.setView(listView);

        myDatabase3 = new MyDatabase3(con , "playlists");
        myDatabase3.readPlaylists();
        myDatabase3.readMusics(null);

        playlistAdapter Adapter = new playlistAdapter(myDatabase3.getPlaylistName() , con);
        listView.setAdapter(Adapter);
        AlertDialog ad = builder.create() ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDatabase3.readMusics(myDatabase3.getPlaylistId().get(position)) ;
                if(myDatabase3.getMusicLocation().contains(location)){
                    Toast.makeText(con , "song is already in the playlist" , Toast.LENGTH_LONG).show();
                }else {
                    myDatabase3.addMusic(Integer.parseInt(myDatabase3.getPlaylistId().get(position)) , (int) Math.round(Math.random()*200000000) , name ,artist ,location , albumName ,duration , imageUriPath , dataAdded) ;
                    Toast.makeText(con , "added in playlist" , Toast.LENGTH_LONG).show();
                }
                ad.dismiss();
            }
        });
        return ad ;
    }

    AlertDialog choise(int musicPosition , String iD){
        myDatabase3 = new MyDatabase3(con ,"musics");
        myDatabase3.readMusics(null);
        AlertDialog.Builder builder = new AlertDialog.Builder(con , R.style.AlertDialogCustom) ;
//        builder.setMessage("");

        ListView listView = new ListView(con) ;
        builder.setView(listView);

        ArrayList<String> choose = new ArrayList() ;
        choose.add("remove frome playlist") ;
        chooseAdapter Adapter = new chooseAdapter( choose , con);
        listView.setAdapter(Adapter);
        AlertDialog ad = builder.create() ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDatabase3 = new MyDatabase3(con , "playlists");
                myDatabase3.readMusics(first_frag.iD);
                myDatabase3.deleteMusicFromPlaylist(myDatabase3.getMusicId().get(musicPosition));
                ad.dismiss();
            }
        });
        return ad ;
    }

}
