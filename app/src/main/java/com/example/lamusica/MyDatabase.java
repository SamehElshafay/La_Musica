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

public class MyDatabase extends SQLiteOpenHelper {
    private Context con ;
    private String databaseName ;
    private String tableName ;
    private String name ;
    private String artist ;
    private String locations ;
    private String albumName ;
    private String duration ;
    private String imageUriPath ;
    private String lastMills ;

    MyDatabase(Context con , String databaseName , String tableName ){
        super( con , databaseName /*databasename*/ , null  , 1 /*1 - inf*/ );
        this.con = con ;
        this.databaseName = databaseName ;
        this.tableName = tableName ;
    }

    Cursor getAll(){
        SQLiteDatabase x = this.getReadableDatabase();
        return x.rawQuery("SELECT * FROM " + tableName ,null) ; /* return type : Cursor */
    }

    boolean Add (String name , String artist , String locations , String albumName , String duration , String imageUriPath ){
        try {
            ContentValues data = new ContentValues();
            data.put("names", name);
            data.put("artists", artist);
            data.put("locations", locations);
            data.put("albumName", albumName);
            data.put("duration", duration);
            data.put("imageUriPath", imageUriPath);
            data.put("lastMills", 0 );
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(tableName , null, data);

            return true ;
        }catch (Exception ex){
            return false ;
        }
    }

    boolean delete (String name){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(
                    "delete from "+tableName+" where names='" + name + "';"
            );
            return true;
        }catch (Exception ex){
            return false ;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE "+tableName+"(" +
                "names VARCHAR(2000)," +
                "artists VARCHAR(2000)," +
                "locations VARCHAR(2000)," +
                "albumName VARCHAR(2000)," +
                "duration VARCHAR(2000)," +
                "imageUriPath VARCHAR(2000)," +
                "lastMills VARCHAR(2000)" +
            ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("");
    }
    void readAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        try {
            do {
                name = cursorCourses.getString(0);
                artist = cursorCourses.getString(1);
                locations = cursorCourses.getString(2);
                albumName = cursorCourses.getString(3);
                duration = cursorCourses.getString(4);
                imageUriPath = cursorCourses.getString(5);
                lastMills = cursorCourses.getString(6);
            } while (cursorCourses.moveToNext());
        }catch (Exception ex){ }
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,null,null);
        db.execSQL("delete from "+ tableName);
        db.close();
    }

    public void AddLastMills(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+tableName+" SET lastMills = "+value;
        db.execSQL(strSQL);
        db.close();
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getLocations() {
        return locations;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUriPath() {
        return imageUriPath;
    }

    public String getLastMills() {
        return lastMills;
    }
}
