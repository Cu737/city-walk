package com.example.citywalk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.citywalk.enity.Position;

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
        String sql = "create table postion"+"(" +
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
    public void insert(Position position)
    {
        ContentValues values = new ContentValues();
        values.put("latitude",position.latitude);
        values.put("longitude",position.longitude);

        wRDB.insert("position",null,values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
