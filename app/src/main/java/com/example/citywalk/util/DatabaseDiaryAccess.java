package com.example.citywalk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDiaryAccess extends SQLiteOpenHelper {
    private static final String DB = "CityWalk.db";
    private static final String TABLE = "diary";
    private static final int VERSION_DB = 0;
    private static DatabaseDiaryAccess instance = null;

    private static final int POSITION = 1, TEXT = 2, PICTURE_PATH = 3;

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
        content.put("position", diary.getPosition());
        content.put("text", diary.getText());
        content.put("picture_path", diary.getPicture_path());
        return writeLink.insert(TABLE, null, content);
    }

    public List<EntryDiary> getByPosition(String position) {
        Cursor query = readLink.query(
                TABLE,
                new String[]{"position", "text", "picture_path"},
                "position=?",
                new String[]{position},
                null, null, null);
        List<EntryDiary> diaryList = new ArrayList<>();
        while (query.moveToNext()) {
            diaryList.add(new EntryDiary(
                    query.getString(POSITION),
                    query.getString(TEXT),
                    query.getString(PICTURE_PATH)
            ));
        }
        query.close();
        return diaryList;
    }

}
