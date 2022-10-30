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

public class MyDatabase2 extends SQLiteOpenHelper {
    private Context con ;
    private String databaseName ;
    private String tableName ;



    // afterCompletion /*loop*/  ,  methodSort , skip
    MyDatabase2(Context con , String databaseName , String tableName ){
        super( con , databaseName /*databasename*/ , null  , 1 /*1 - inf*/ );
        this.con = con ;
        this.databaseName = databaseName ;
        this.tableName = tableName ;
    }

    boolean Add (){
        try {
            ContentValues data = new ContentValues();
            data.put("id", 1);
            data.put("firstTime", "true");
            data.put("afterCompletion", "continue");
            data.put("methodSort", "by character");
            data.put("skip", "true");
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(tableName , null, data);
            return true ;
        }catch (Exception ex){
            return false ;
        }
    }

    public void changeFirstTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tableName + " SET firstTime = 'false' ");

    }

    public void continu() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tableName + " SET afterCompletion = 'continue'");
    }

    public void loop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tableName + " SET afterCompletion = 'loop'");
    }

    public void changeAfterCompletion(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tableName + " SET afterCompletion = '" + value + "'");
    }

    public void changeMethodSort(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tableName + " SET methodSort = '" + value + "'");
        
    }

    public void changeSkip(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+tableName+" SET skip = '" + value + "'");
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE "+tableName+"(" +
                "id int ,"  +
                "firstTime VARCHAR(2000) ,"  +
                "afterCompletion VARCHAR(2000) , "  +
                "methodSort VARCHAR(2000) ,"  +
                "skip VARCHAR(2000) "  +
            ");"
        );
        System.out.println("------------------------------------------------------------------------------------------------------");
        ContentValues data = new ContentValues();
        data.put("id" , 1);
        data.put("firstTime" , "true");
        data.put("afterCompletion" , "continue");
        data.put("methodSort" , "by character");
        data.put("skip" , "true");
        db.insert(tableName , null, data);
        

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("");
    }

    public void getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        do{

        }while (cursorCourses.moveToNext());
        cursorCourses.close();
        return;
    }

    public String getFirstTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        String firstTime = cursorCourses.getString(1);
        cursorCourses.close();
        return firstTime;
    }

    public String getAfterCompletion(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        String afterCompletion = cursorCourses.getString(2);
        cursorCourses.close();
        return afterCompletion ;
    }

    public String getMethodSort(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        String methodSort = cursorCourses.getString(3);
        cursorCourses.close();
        return methodSort ;
    }

    public String getSkip(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + tableName, null);
        cursorCourses.moveToFirst();
        String skip = cursorCourses.getString(4);
        cursorCourses.close();
        return skip ;
    }

}
