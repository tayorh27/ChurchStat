package com.stat.churchstat.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 10/02/2017.
 */

public class RatingDb {

    RatingsHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public RatingDb(Context context) {
        helper = new RatingsHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertRating(ArrayList<Ratings> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + RatingsHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Ratings current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.cat);
            statement.bindString(3, current.star);
            statement.bindString(4, current.comment);
            statement.bindString(5, current.improves);
            statement.bindString(6, current.date);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Ratings> getAllMyRatings() {
        ArrayList<Ratings> currentData = new ArrayList<>();

        String[] columns = {
                RatingsHelper.COLUMN_ID,
                RatingsHelper.COLUMN_CATEGORY,
                RatingsHelper.COLUMN_STAR,
                RatingsHelper.COLUMN_COMMENT,
                RatingsHelper.COLUMN_IMPROVES,
                RatingsHelper.COLUMN_DATE
        };
        Cursor cursor = sqLiteDatabase.query(RatingsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Ratings current = new Ratings();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_ID)));
                current.cat = cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_CATEGORY));
                current.star = cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_STAR));
                current.comment = cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_COMMENT));
                current.improves = cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_IMPROVES));
                current.date = cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_DATE));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public int getLastId() {
        int id = 0;
        String[] columns = {
                RatingsHelper.COLUMN_ID,
                RatingsHelper.COLUMN_CATEGORY,
                RatingsHelper.COLUMN_STAR,
                RatingsHelper.COLUMN_COMMENT,
                RatingsHelper.COLUMN_IMPROVES,
                RatingsHelper.COLUMN_DATE
        };
        Cursor cursor = sqLiteDatabase.query(RatingsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RatingsHelper.COLUMN_ID)));
            //cursor.close();
        }
        Log.e("gotID", "my id is " + id);
        return id;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(RatingsHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey, String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//RatingsHelper.COLUMN_STATUS
        sqLiteDatabase.update(RatingsHelper.TABLE_NAME_MYPOST, contentValues, RatingsHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(RatingsHelper.TABLE_NAME_MYPOST, RatingsHelper.COLUMN_ID + "=" + id, null);
    }


    public class RatingsHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "Ratings_db";
        private static final int DB_VERSION = 2;

        public static final String TABLE_NAME_MYPOST = "Ratings101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_CATEGORY = "cat";
        public static final String COLUMN_STAR = "star";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_IMPROVES = "improves";
        public static final String COLUMN_DATE = "date";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_STAR + " TEXT," +
                COLUMN_COMMENT + " TEXT," +
                COLUMN_IMPROVES + " TEXT," +
                COLUMN_DATE + " TEXT" +
                ");";


        public RatingsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_MYPOST);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(" DROP TABLE " + TABLE_NAME_MYPOST + " IF EXISTS;");
                onCreate(sqLiteDatabase);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }
}
