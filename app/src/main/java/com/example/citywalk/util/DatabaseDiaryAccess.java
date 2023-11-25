package com.example.citywalk.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseDiaryAccess extends SQLiteOpenHelper {
    private static final String DB = "CityWalk.db";
    private static final String TABLE = "diary";
    private static final int VERSION_DB = 0;
    private static DatabaseDiaryAccess instance = null;

    private SQLiteDatabase readLink = null, writeLink = null;

    private DatabaseDiaryAccess(Context context) {
        super(context, DB, null, VERSION_DB);
    }

    public static DatabaseDiaryAccess getInstance(Context context) {
        if (null == instance)
        {
            return instance = new DatabaseDiaryAccess(context);
        }
        else return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE +"(" +
                " id_table integer primary key autoincrement not null," +
                " position varchar not null, text varchar, picture_path varchar" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase openRead() {
        readLink = instance.getReadableDatabase();
        return readLink;
    }

    public SQLiteDatabase openWrite() {
        writeLink = instance.getWritableDatabase();
        return writeLink;
    }

    public void closeLink() {
        if (null != readLink && readLink.isOpen())
        {
            readLink.close();
            readLink = null;
        }
        if (null != writeLink && writeLink.isOpen())
        {
            writeLink.close();
            writeLink = null;
        }
    }

}
