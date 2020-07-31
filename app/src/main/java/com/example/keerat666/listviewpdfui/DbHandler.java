package com.example.keerat666.listviewpdfui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    SQLiteDatabase sqb;
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String file_name = "file_name";
    public static final String Word_Count = "Word_Count";
    public static final String audio_count = "audio_count";
    public static final String no_page = "no_page";
    public static final String time = "time";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqb) {





       // sqb = sqb.openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        sqb.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR" +
                " ," + "audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

        sqb.execSQL("CREATE TABLE IF NOT EXISTS folder(folderpath VARCHAR,folderName VARCHAR , type VARCHAR);");

        sqb.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR ,audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

        sqb.execSQL("CREATE TABLE IF NOT EXISTS folder(folderpath VARCHAR,folderName VARCHAR , type VARCHAR);");

        sqb.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");

        sqb.execSQL("CREATE TABLE IF NOT EXISTS bookmarks(fpath VARCHAR,page_number VARCHAR ,total_pages VARCHAR, type VARCHAR);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqb, int i, int i1) {
        sqb.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqb);

    }

















    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }


    public ArrayList<String> getallfilemaster() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(file_name)));
            res.moveToNext();
        }
        return array_list;
    }

}
