package com.example.manep.cinema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int databaseversion = 1;
    public static final String DATA_BASE = "logindetails.db";
    public static final String TABLE_NAME = "tablename";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    public String CREATE_QUERY = " CREATE TABLE " + TABLE_NAME + "(" + USER_NAME + " TEXT, " + PASSWORD + " Text "+");";
    public  String DROP = "DROP TABLE IF EXISTS"+ TABLE_NAME;


    public DataBaseHelper(Context context) {
        super(context,DATA_BASE,null,databaseversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP);
        onCreate(db);
    }
    public Cursor getInfromation(){
        SQLiteDatabase sq =this.getReadableDatabase();
        String[] columns ={USER_NAME, PASSWORD};
        Cursor cr = sq.query(TABLE_NAME,columns,null,null,null,null,null);
        return cr;

    }
    public void delInformation(String name,String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor c = db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + "(" + USER_NAME + " = " + name + " AND "+ PASSWORD + " = " + pass +");",null);
        //if (c.moveToFirst()) {
            //db.execSQL(" DELETE FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = " + name + " AND " + PASSWORD + " = " + pass);
        //}
        db.delete(TABLE_NAME, USER_NAME + "='" + name + "'", null);
        //db.delete (TABLE_LABELS, KEY_NAME + "='" + label + "'", null);
        db.close();
        //db.delete( TABLE_NAME  , USER_NAME + " = " + name + " and " + PASSWORD + " = " + pass, null);
        Log.d("delete","deleted");
    }
    public void putInformation(String name, String pass){
       SQLiteDatabase db = this.getWritableDatabase();//So, context is null when you pass it into MyDBHandler, so when you call this.getWritableDatabase(), this is null. As a result, you get the NullPointerException, as you can see in the log:
        //Cursor cr = db.rawQuery("select USER_NAME from TABLE_NAME where USER_NAME = '"+ name +"'",null);
       // if (cr == null) {
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME,name);
            cv.put(PASSWORD,pass);
            db.insert(TABLE_NAME,null,cv);
         //   return true;
        //}else
       //     return false;
    }
    public Cursor getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TABLE_NAME ,null);
        return c;
    }
}
