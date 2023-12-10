package com.example.citywalk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDiaryAccess extends SQLiteOpenHelper {
    private static final String DB = "user.db";
    private static final String TABLE = "diary";
    private static final int VERSION_DB = 1;
    private static DatabaseDiaryAccess instance = null;

    private static final int LATITUDE = 1, LONGITUDE = 2, TEXT = 3, PICTURE_PATH = 4;
    private static final double eps = 0.1;

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
                " latitude double, longitude double, text varchar, picture_path varchar" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase openRead() {
        if (readLink == null || !readLink.isOpen()) {
            readLink = instance.getReadableDatabase();
        }
        return readLink;
    }

    public SQLiteDatabase openWrite() {
        if (writeLink == null || !writeLink.isOpen()) {
            writeLink = instance.getWritableDatabase();
        }
        return writeLink;
    }

    @Override
    public void close() {
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

    public long insert(EntryDiary diary) {
        ContentValues content = new ContentValues();
        content.put("latitude", diary.getLatitude());
        content.put("longitude", diary.getLongitude());
        content.put("text", diary.getText());
        content.put("picture_path", diary.getPicture_path());
        return writeLink.insert(TABLE, null, content);
    }

    public List<EntryDiary> getByPosition(double latitude, double longitude) {
        Cursor query = readLink.query(
                TABLE,
                new String[]{"latitude", "longitude", "text", "picture_path"},
                "(latitude - ?) * (latitude - ?) + (longitude - ?) * (longitude - ?) < ?",
                new String[]{
                        Double.toString(latitude),Double.toString(latitude),
                        Double.toString(longitude),Double.toString(longitude),
                        Double.toString(eps)
                },
                null, null, null);
        List<EntryDiary> diaryList = new ArrayList<>();
        while (query.moveToNext()) {
            diaryList.add(new EntryDiary(
                    query.getDouble(LATITUDE),
                    query.getDouble(LONGITUDE),
                    query.getString(TEXT),
                    query.getString(PICTURE_PATH)
            ));
        }
        query.close();
        return diaryList;
    }

}
