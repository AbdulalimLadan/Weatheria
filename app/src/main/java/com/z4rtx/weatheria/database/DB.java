package com.z4rtx.weatheria.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {

    private static String DB_NAME = "wheatheria_db";

    //TABLES
    private static String TABLE_SEARCH_HISTORY = "SEARCH_HISTORY";
    private static String TABLE_CURRENT_SEARCH = "CURRENT_SEARCH";

    private static final String TABLE_SEARCH_HISTORY_DROP_IF_EXIST = "drop table if exists " + TABLE_SEARCH_HISTORY;
    private static final String TABLE_CURRENT_SEARCH_DROP_IF_EXIST = "drop table if exists " + TABLE_CURRENT_SEARCH;

    public DB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_CURRENT_SEARCH + "(" +
                "" +Columns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" +Columns.CITY+" TEXT,"+
                "" +Columns.COUNTRY+" TEXT"+
                ")");
        db.execSQL("create table "+ TABLE_SEARCH_HISTORY + "(" +
                "" +Columns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" +Columns.CITY+" TEXT"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CURRENT_SEARCH_DROP_IF_EXIST);
        db.execSQL(TABLE_SEARCH_HISTORY_DROP_IF_EXIST);
        onCreate(db);
    }


    //Save a search 'city' and 'country'
    public void saveSearch(String city, String country){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.CITY, city);
        contentValues.put(Columns.COUNTRY, country);
        db.delete(TABLE_CURRENT_SEARCH, null, null);
        db.insert(TABLE_CURRENT_SEARCH, null,contentValues);
        storeIntoHistory(city);
    }

    //Save a search into history
    public void storeIntoHistory(String city){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.CITY, city);
        db.insert(TABLE_SEARCH_HISTORY, null,contentValues);
    }

    //Fetch CurrentSearch
    public String fetchCurrentSearch(){
        String q = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+Columns.CITY+", "+Columns.COUNTRY+" from "+TABLE_CURRENT_SEARCH, null);
        if(res.getCount() > 0){
            res.moveToFirst();
            q = res.getString(0) + ", " + res.getString(1);
        }
        res.close();
        return q;
    }

    private static class Columns{
        private static final String ID = "ID";
        private static final String CITY = "CITY";
        private static final String COUNTRY = "COUNTRY";
    }
}
