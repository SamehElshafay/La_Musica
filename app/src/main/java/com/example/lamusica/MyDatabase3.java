/**
 * DDl : date defination language
 * ------
 * create database  => X
 * create table
 * drop db    => X
 * drop table
 * truncate table
 * ==========================================
 * Dml: data manipulation language
 * -----
 * add -> insert
 * delete -> delete
 * edit -> update
 * view -> select
 * ==========================================
 * DCl : Data control language
 * -----
 * security
 **/

package com.example.lamusica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MyDatabase3 extends SQLiteOpenHelper {
    private Context con ;
    private String databaseName ;
    private String playlistTable ;
    private String musicTable ;

    private ArrayList<String> playlistName = new ArrayList<>();
    private ArrayList<String> playlistId   = new ArrayList<>();

    private ArrayList<String> playlistIdM = new ArrayList<>();
    private ArrayList<String> musicId = new ArrayList<>();
    private ArrayList<String> musicName = new ArrayList<>();
    private ArrayList<String> musicArtist = new ArrayList<>();
    private ArrayList<String> musicLocation = new ArrayList<>();
    private ArrayList<String> musicAlbumName = new ArrayList<>();
    private ArrayList<String> musicDuration = new ArrayList<>();
    private ArrayList<String> musicImagePath = new ArrayList<>();
    private ArrayList<String> musicDataAdded = new ArrayList<>();

    MyDatabase3(Context con , String databaseName){
        super( con , databaseName , null  , 1 );
        this.con = con ;
        this.databaseName = databaseName ;
        playlistTable = "playlist" ;
        musicTable = "musics" ;
    }

    boolean addMusic (int playlistid , int id , String name , String artist , String location , String albumName , String duration , String imageUriPath , String dataAdded){
        try {
            ContentValues data = new ContentValues();
            data.put("playlistid", playlistid);
            data.put("id", id);
            data.put("name", name);
            data.put("artist", artist);
            data.put("locations", location);
            data.put("albumName", albumName);
            data.put("duration", duration);
            data.put("imageUriPath", imageUriPath);
            data.put("dataAdded", dataAdded);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(musicTable , null, data);
            db.close();
            return true ;
        }catch (Exception ex){
            return false ;
        }
    }

    boolean addPlayList (int playlistid1 , String playlistName1){
        try {
            ContentValues data = new ContentValues();
            data.put("playlistid", playlistid1);
            data.put("playlistName", playlistName1);
            playlistName.add(playlistName1);
            playlistId.add(String.valueOf(playlistid1));
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(playlistTable , null, data);
            db.close();
            return true ;
        }catch (Exception ex){
            return false ;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE "+playlistTable+"(" +
                "playlistID int ,"  +
                "playlistName VARCHAR(2000) "  +
            ");"
        );
        db.execSQL(
            "CREATE TABLE "+musicTable+"(" +
                "playlistid int ,"  +
                "id int ,"  +
                "name VARCHAR(2000) ,"  +
                "artist VARCHAR(2000) , "  +
                "locations VARCHAR(2000) ,"  +
                "albumName VARCHAR(2000) ,"  +
                "duration VARCHAR(2000) ,"  +
                "imageUriPath VARCHAR(2000) ,"  +
                "dataAdded VARCHAR(2000) "  +
            ");"
        );
    }

    void readPlaylists(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + playlistTable , null);
        cursorCourses.moveToFirst();
        playlistId.clear();
        playlistName.clear();
        try{
            do{
                playlistId.add(cursorCourses.getString(0));
                playlistName.add(cursorCourses.getString(1));
            }while (cursorCourses.moveToNext());
        }catch (Exception ex){

        }
        
        return;
    }

    int readMusics(String id) {
        clear();
        int size = 0 ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + musicTable, null);
        if (id != null){
            try {
                cursorCourses.moveToFirst();
                do {
                    if (id.equals(cursorCourses.getString(0))){
                        size++;
                        playlistIdM.add(cursorCourses.getString(0));
                        musicId.add(cursorCourses.getString(1));
                        musicName.add(cursorCourses.getString(2));
                        musicArtist.add(cursorCourses.getString(3));
                        musicLocation.add(cursorCourses.getString(4));
                        musicAlbumName.add(cursorCourses.getString(5));
                        musicDuration.add(cursorCourses.getString(6));
                        musicImagePath.add(cursorCourses.getString(7));
                        musicDataAdded.add(cursorCourses.getString(8));
                    }
                } while (cursorCourses.moveToNext());
                
            } catch (Exception ex) { }
        }
        return size ;
    }

    int readAllMusics() {
        clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + musicTable, null);
        int songNumber = 0;
        try {
            cursorCourses.moveToFirst();
            do {
                songNumber++;
                playlistIdM.add(cursorCourses.getString(0));
                musicId.add(cursorCourses.getString(1));
                musicName.add(cursorCourses.getString(2));
                musicArtist.add(cursorCourses.getString(3));
                musicLocation.add(cursorCourses.getString(4));
                musicAlbumName.add(cursorCourses.getString(5));
                musicDuration.add(cursorCourses.getString(6));
                musicImagePath.add(cursorCourses.getString(7));
                musicDataAdded.add(cursorCourses.getString(8));
            } while (cursorCourses.moveToNext());

        } catch (Exception ex) { }
        return songNumber;
    }
    void readFavoriteMusics() {
        clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + musicTable, null);
        try {
            cursorCourses.moveToFirst();
            do {
                if ("2000000001".equals(cursorCourses.getString(0))){
                    playlistIdM.add(cursorCourses.getString(0));
                    musicId.add(cursorCourses.getString(1));
                    musicName.add(cursorCourses.getString(2));
                    musicArtist.add(cursorCourses.getString(3));
                    musicLocation.add(cursorCourses.getString(4));
                    musicAlbumName.add(cursorCourses.getString(5));
                    musicDataAdded.add(cursorCourses.getString(6));
                    musicImagePath.add(cursorCourses.getString(7));
                }
            } while (cursorCourses.moveToNext());
            
        } catch (Exception ex) { }

        return;
    }

    boolean deletePlaylist(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(
                    "delete from "+ playlistTable +" where playlistID ='" + id + "';"
            );
            return true;
        }catch (Exception ex){
            return false ;
        }
    }

    void clear(){
        playlistIdM.clear();musicId.clear();musicName.clear();musicArtist.clear();musicLocation.clear();
        musicAlbumName.clear();musicDuration.clear();musicImagePath.clear();musicDataAdded.clear();
    }

    public void setPlaylistName(ArrayList<String> playlistName) {
        this.playlistName = playlistName;
    }

    public ArrayList<String> getPlaylistName() {
        return playlistName;
    }

    public ArrayList<String> getPlaylistId() {
        return playlistId;
    }

    public ArrayList<String> getMusicAlbumName() {
        return musicAlbumName;
    }

    public ArrayList<String> getMusicArtist() {
        return musicArtist;
    }

    public ArrayList<String> getMusicDataAdded() {
        return musicDataAdded;
    }

    public ArrayList<String> getMusicDuration() {
        return musicDuration;
    }

    public ArrayList<String> getMusicId() {
        return musicId;
    }

    public ArrayList<String> getMusicImagePath() {
        return musicImagePath;
    }

    public ArrayList<String> getMusicLocation() {
        return musicLocation;
    }

    public ArrayList<String> getMusicName() {
        return musicName;
    }

    public ArrayList<String> getPlaylistIdM() {
        return playlistIdM;
    }

    boolean deleteUsingLocation (String location){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(
                    "delete from musics where locations = '" + location + "' ;"
            );
            return true;
        }catch (Exception ex){
            return false ;
        }
    }

    boolean deleteMusicFromPlaylist (String id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(
                    "delete from musics where id = '" + id + "' ;"
            );
            return true;
        }catch (Exception ex){
            return false ;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("");
    }

}
