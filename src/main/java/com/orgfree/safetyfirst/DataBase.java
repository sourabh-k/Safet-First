package com.orgfree.safetyfirst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sourabh on 22-Jul-16.
 */
public class DataBase extends SQLiteOpenHelper {
    SQLiteDatabase db;
    String dbname="Contacts";
    public DataBase(Context context) {
        super(context,"Contacts", null, 1);
        db = context.openOrCreateDatabase(dbname,SQLiteDatabase.CREATE_IF_NECESSARY,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry= "CREATE TABLE Details (name TEXT, number TEXT)";
        db.execSQL(qry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getData()
    {
        db = this.getReadableDatabase();
        Cursor c= db.query("Details",null,null,null,null,null,null,null);
        return  c;

    }
    public long addRecord(String name,String number)
    {
        db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("name",name);
        cv.put("number",number);
        long temp = db.insert("Details",null,cv);
        return temp;
    }
    public  int deleteData(String s)
    {
        db= this.getWritableDatabase();
        int temp= db.delete("Details",null,null);
        return temp;
    }
    public void updateRecord(String s){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number",s);
        int temp=db.update("Details",cv,"name=Mine",null);
    }
}
