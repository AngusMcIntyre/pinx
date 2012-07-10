package gus.apps.pinsafe;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PinDataBaseHandler extends SQLiteOpenHelper  {

	//Version
	private static final int DATABASE_VERSION = 1;
	
	//DB Name
	private static final String DATABASE_NAME = "PinSafeStorage";
	
	//Table names
	private static final String TABLE_PINS = "pins";
	
	//Column names
	private static final String KEY_ID = "id";
    private static final String KEY_NAME = "label";
    private static final String KEY_PIN = "pin";	
	
	public PinDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PINS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"
                + KEY_PIN + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PINS);
		this.onCreate(db);
	}
	
	public boolean addPin(String label, int value){
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label); // Contact Name
        values.put(KEY_PIN, value); // Contact Phone
 
        // Inserting Row
        long result = db.insert(TABLE_PINS, null, values);
        db.close(); // Closing database connection
        
        return result != -1;
	}
	
	public List<Pin> getAllPins(){
		List<Pin> result = new ArrayList<Pin>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PINS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pin val = new Pin();
                
                val.Id = cursor.getInt(0);
                val.Label = cursor.getString(1);
                val.Pin = cursor.getInt(2);
                
                // Adding contact to list
                result.add(val);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        
        // return contact list
        return result;
	}

	public int UpdatePin(Pin pin){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, pin.Label);
		values.put(KEY_PIN, pin.Pin);
		
		int result = db.update(TABLE_PINS, values, KEY_ID + " = ?", new String[]{String.valueOf(pin.Id)});
		
		db.close();
		
		return result;
	}
}