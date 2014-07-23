package com.example.stethoscope;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mobileSensorDataManager";
 
    // Contacts table name
    private static final String TABLE_MOBILE_SENSOR = "MobileSensorData";
 
    // Contacts Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_ISMANUAL = "is_manual";
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_AUDIOFILENAME="audiofilename";
    
    private String[] allColumns = { KEY_ID,
    		KEY_ISMANUAL,
    		KEY_DATE_TIME,
    	      KEY_AUDIOFILENAME};
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i("MainActivity", "onCreate SQLiteDatabase");
        //create mobile sensor table
        String CREATE_MOBILE_SENSOR_TABLE ="CREATE TABLE " + TABLE_MOBILE_SENSOR + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ISMANUAL + " TEXT," + KEY_DATE_TIME + " TEXT,"
                + KEY_AUDIOFILENAME + " TEXT" + ")";
        
        db.execSQL(CREATE_MOBILE_SENSOR_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOBILE_SENSOR);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    
 
    // Adding new Entry
    long addContact(SensorData sd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ISMANUAL, sd.getIsManual());
        values.put(KEY_DATE_TIME, sd.getDateAndTime()); // Contact Date and Time
        values.put(KEY_AUDIOFILENAME, sd.getAudioFileName());
        
        // Inserting Row
        long newRowId = db.insert(TABLE_MOBILE_SENSOR, null, values);
        db.close(); // Closing database connection
        return newRowId;
    }
 
    // Getting single Entry
    SensorData getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_MOBILE_SENSOR, new String[] { KEY_ID,KEY_ISMANUAL,
                KEY_DATE_TIME, KEY_AUDIOFILENAME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
 
        SensorData sd = new SensorData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        // return contact
        return sd;
    }

    // Getting All Contacts
    public List<SensorData> getAllContacts() {
        List<SensorData> sensorDataList = new ArrayList<SensorData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOBILE_SENSOR;
        
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);
        
        Cursor cursor = db.query(TABLE_MOBILE_SENSOR,
                allColumns, null, null, null, null, null);
 
        // looping through all rows and adding to list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            
            	SensorData sd = new SensorData(cursor.getInt(0),cursor.getString(1), 
            			cursor.getString(2),cursor.getString(3));
                // Adding contact to list
                sensorDataList.add(sd);
                cursor.moveToNext();
        }
 
        // return list
        return sensorDataList;
    }
 
    // Updating single contact
    public int updateContact(SensorData sd) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ISMANUAL, sd.getIsManual());
        values.put(KEY_DATE_TIME, sd.getDateAndTime()); // Contact Date and TIme        
        values.put(KEY_AUDIOFILENAME, sd.getAudioFileName());
 
        // updating row
        return db.update(TABLE_MOBILE_SENSOR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sd.getID()) });
    }
 
    // Deleting single contact
    public void deleteContact(SensorData sd) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOBILE_SENSOR, KEY_ID + " = ?",
                new String[] { String.valueOf(sd.getID()) });
        db.close();
    }
 
    // Getting contacts Count
    public long getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOBILE_SENSOR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        long totalEntry=cursor.getCount();
        cursor.close();
        // return count
        return totalEntry;
    }
 
}
