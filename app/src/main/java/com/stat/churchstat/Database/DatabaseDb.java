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
 * Created by sanniAdewale on 03/01/2017.
 */

public class DatabaseDb {

    DetailsHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseDb(Context context) {
        helper = new DetailsHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyPost(ArrayList<Details> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + DetailsHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Details current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.no_of_males);
            statement.bindString(3, current.no_of_females);
            statement.bindString(4, current.no_of_adult);
            statement.bindString(5, current.no_of_children);
            statement.bindString(6, current.date);
            statement.bindString(7, current.category);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Details> getAllMyDetails() {
        ArrayList<Details> currentData = new ArrayList<>();

        String[] columns = {
                DetailsHelper.COLUMN_ID,
                DetailsHelper.COLUMN_MALES,
                DetailsHelper.COLUMN_FEMALES,
                DetailsHelper.COLUMN_ADULT,
                DetailsHelper.COLUMN_CHILDREN,
                DetailsHelper.COLUMN_DATE,
                DetailsHelper.COLUMN_CATEGORY
        };
        Cursor cursor = sqLiteDatabase.query(DetailsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Details current = new Details();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_ID)));
                current.no_of_males = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_MALES));
                current.no_of_females = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_FEMALES));
                current.no_of_adult = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_ADULT));
                current.no_of_children = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_CHILDREN));
                current.date = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_DATE));
                current.category = cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_CATEGORY));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public int getLastId() {
        int id = 0;
        String[] columns = {
                DetailsHelper.COLUMN_ID,
                DetailsHelper.COLUMN_MALES,
                DetailsHelper.COLUMN_FEMALES,
                DetailsHelper.COLUMN_ADULT,
                DetailsHelper.COLUMN_CHILDREN,
                DetailsHelper.COLUMN_DATE,
                DetailsHelper.COLUMN_CATEGORY
        };
        Cursor cursor = sqLiteDatabase.query(DetailsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DetailsHelper.COLUMN_ID)));
            //cursor.close();
        }
        Log.e("gotID", "my id is " + id);
        return id;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(DetailsHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey, String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//DetailsHelper.COLUMN_STATUS
        sqLiteDatabase.update(DetailsHelper.TABLE_NAME_MYPOST, contentValues, DetailsHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(DetailsHelper.TABLE_NAME_MYPOST, DetailsHelper.COLUMN_ID + "=" + id, null);
    }


    public class DetailsHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "Details_db";
        private static final int DB_VERSION = 3;

        public static final String TABLE_NAME_MYPOST = "Details101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_MALES = "males";
        public static final String COLUMN_FEMALES = "females";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_CHILDREN = "children";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MALES + " TEXT," +
                COLUMN_FEMALES + " TEXT," +
                COLUMN_ADULT + " TEXT," +
                COLUMN_CHILDREN + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_CATEGORY + " TEXT" +
                ");";


        public DetailsHelper(Context context) {
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
