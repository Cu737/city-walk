package com.example.citywalk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.citywalk.enity.Position;

import java.util.ArrayList;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase wRDB = null;

    private UserDBHelper(Context context)
    {
        super(context,DB_NAME,null, DB_VERSION);

    }

    public static UserDBHelper getInstance(Context context)
    {
        if (mHelper == null)
        {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table position"+"(" +
                "ID INTEGER PRIMARY KEY," +
                "    latitude double NOT NULL," +
                "longitude double NOT NULL," +
                "    primary key(ID)" +
                ");";
        sqLiteDatabase.execSQL(sql);

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
    public void insertPosition(Position position)
    {
        ContentValues values = new ContentValues();
        values.put("latitude",position.latitude);
        values.put("longitude",position.longitude);

        wRDB.insert("position",null,values);
    }

    //取所有
    public List<Position> queryAllPosition()
    {
        List<Position> l= new ArrayList<>();
        Cursor cursor = mRDB.query("position",null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            Position p= new Position();
            p.ID = cursor.getInt(0);
            p.latitude = cursor.getDouble(1);
            p.longitude = cursor.getDouble(2);
            l.add(p);
        }
        return l;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
