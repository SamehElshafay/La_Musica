package com.example.lamusica;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class playlistLayout extends AppCompatActivity {
    private String id ;
    private MyDatabase3 myDatabase3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_layout2);
        ((TextView)findViewById(R.id.page)).setText(getIntent().getExtras().getString("name"  )) ;
        if(getIntent().getExtras().getString("number").equals("1")){
            ((ImageView)findViewById(R.id.icon)).setBackground(getResources().getDrawable(R.drawable.ic_baseline_library_music_24));
        }else {
            ((ImageView)findViewById(R.id.icon)).setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
        }
        myDatabase3 = new MyDatabase3(getApplicationContext() , "playlists");
        id = getIntent().getExtras().getString("id") ;
        myDatabase3.readMusics(id);
        SongAdapter songAdapter = new SongAdapter(myDatabase3.getMusicName() , myDatabase3.getMusicArtist() , myDatabase3.getPlaylistIdM() , this , id );
        ((ListView)findViewById(R.id.songListPlaylist)).setAdapter(songAdapter);

        findViewById(R.id.backPlaylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}