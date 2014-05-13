package com.example.drivingapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DataORM {

    private static final String TAG = "DataORM";

    private static final String TABLE_NAME = "userdata";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_TIME_TYPE = "LONG PRIMARY KEY";
    private static final String COLUMN_TIME = "id";

    private static final String COLUMN_X_TYPE = "FLOAT";
    private static final String COLUMN_X = "accelX";

    private static final String COLUMN_Y_TYPE = "FLOAT";
    private static final String COLUMN_Y = "accelY";

    private static final String COLUMN_Z_TYPE = "FLOAT";
    private static final String COLUMN_Z = "accelZ";

    private static final String COLUMN_SPEED_TYPE = "FLOAT";
    private static final String COLUMN_SPEED = "speed";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_TIME + " " + COLUMN_TIME_TYPE + COMMA_SEP +
                COLUMN_X  + " " + COLUMN_X_TYPE + COMMA_SEP +
                COLUMN_Y + " " + COLUMN_Y_TYPE + COMMA_SEP +
                COLUMN_Z + " " + COLUMN_Z_TYPE + COMMA_SEP +
                COLUMN_SPEED + " " + COLUMN_SPEED_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    public static void insertData(Context context, DataStorageClass dsc) {
    	Log.i(TAG, "Insert data called...");
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        Log.i(TAG, "New database wrapper created...");
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();
        Log.i(TAG, "Got writable database...");
		ContentValues values = postToContentValues(dsc);
		Log.i(TAG, "Created values...");
		long postId = database.insert(DataORM.TABLE_NAME, "null", values);
		Log.i(TAG, "Inserted new Post with ID: " + postId);

        database.close();
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(DataStorageClass dsc) {
        ContentValues values = new ContentValues();
        values.put(DataORM.COLUMN_TIME, dsc.returnTime());
        values.put(DataORM.COLUMN_X, dsc.returnX());
        values.put(DataORM.COLUMN_Y, dsc.returnY());
        values.put(DataORM.COLUMN_Z, dsc.returnZ());
        values.put(DataORM.COLUMN_SPEED, dsc.returnSpeed());

        return values;
    }

    public static List<DataStorageClass> getData(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<DataStorageClass> postList = new ArrayList<DataStorageClass>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DataStorageClass dsc = cursorToPost(cursor);
                postList.add(dsc);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        database.close();

        return postList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static DataStorageClass cursorToPost(Cursor cursor) {
        DataStorageClass dsc = new DataStorageClass();
        dsc.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME)));
        dsc.setX(cursor.getFloat(cursor.getColumnIndex(COLUMN_Z)));
        dsc.setY(cursor.getFloat(cursor.getColumnIndex(COLUMN_Y)));
        dsc.setZ(cursor.getFloat(cursor.getColumnIndex(COLUMN_Z)));
        dsc.setSpeed(cursor.getFloat(cursor.getColumnIndex(COLUMN_SPEED)));

        return dsc;
    }
}