package com.example.shubham.reminders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shubham on 01-07-2017.
 */

public class reminderOpenHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "reminder";
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_DATE = "date";
    public final static String COLUMN_TIME = "time";
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_DETAILS = "details";

    public reminderOpenHelper(Context context) {
        super(context, "Reminder.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    String str="create table "+TABLE_NAME+" ( "+COLUMN_TITLE+" text, "+COLUMN_DATE+" text, "+COLUMN_TIME+" text, "+COLUMN_DETAILS+" text, "+COLUMN_ID+" integer primary key autoincrement);";
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
