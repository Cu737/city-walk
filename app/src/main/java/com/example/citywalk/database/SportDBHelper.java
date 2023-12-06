package com.example.citywalk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.citywalk.enity.Sportinformation;

import java.util.ArrayList;
import java.util.List;

public class SportDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "userSport.db";
    private static final int DB_VERSION = 1;
    private static SportDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase wRDB = null;

    private SportDBHelper(Context context)
    {
        super(context,DB_NAME,null, DB_VERSION);

    }

    public static SportDBHelper getInstance(Context context)
    {
        if (mHelper == null)
        {
            mHelper = new SportDBHelper(context);
        }
        return mHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table sport"+"(" +
                "date TEXT PRIMARY KEY," +
                "    sportnum double" +
                ");";
        sqLiteDatabase.execSQL(sql);
        System.out.println("建立运动表");


    }

    //建立数据库读链接
    public SQLiteDatabase openReadLink()
    {
        if (mRDB == null ||!mRDB.isOpen())
        {
            mRDB =mHelper.getReadableDatabase();
        }

        return mRDB;

    }

    //建立数据库写链接

    public SQLiteDatabase openWriteLink()
    {
        if (wRDB == null ||!wRDB.isOpen())
        {
            wRDB =mHelper.getWritableDatabase();
        }

        return wRDB;

    }

    //关闭数据库链接
    public void closeLink()
    {
        if (mRDB != null&&mRDB.isOpen())
        {
            mRDB.close();
            mRDB = null;
        }
        if (wRDB != null &&wRDB.isOpen())
        {
            wRDB.close();
            wRDB = null;
        }
    }

    //增加
    public void insertSportinformaiton(Sportinformation Sportinformaiton)
    {
        ContentValues values = new ContentValues();
        //Log.w("insert_sport",Sportinformaiton.date);
        //Log.w("insert_sport", String.valueOf(Sportinformaiton.sportnum));

        values.put("date",Sportinformaiton.date);
        values.put("sportnum",Sportinformaiton.sportnum);

        wRDB.insert("sport",null,values);
    }

    //取所有
    public List<Sportinformation> queryAllSportinformaiton()
    {
        List<Sportinformation> l= new ArrayList<>();
        Cursor cursor = mRDB.query("sport",null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            Sportinformation p= new Sportinformation();
            p.date = cursor.getString(0);
            p.sportnum = cursor.getDouble(1);
            l.add(p);
        }
        return l;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
