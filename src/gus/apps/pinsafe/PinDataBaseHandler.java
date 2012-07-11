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
    
    int mMasterKey = 0;
	
	public PinDataBaseHandler(Context context, int masterKey) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mMasterKey = masterKey;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PINS + "("
                + KEY_ID + " INTEGER NOT NULL PRIMARY KEY," 
				+ KEY_NAME + " TEXT NOT NULL,"
                + KEY_PIN + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PINS);
		this.onCreate(db);
	}
	
	public Pin addPin(String label, int value){
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label); // Contact Name
        values.put(KEY_PIN, PinEncoder.EncodePin(mMasterKey, value)); // Contact Phone
 
        // Inserting Row
        long rowid = db.insert(TABLE_PINS, null, values);
        db.close(); // Closing database connection
        
        String select = "SELECT * FROM " + TABLE_PINS + " WHERE ROWID = ?";
        		
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{ String.valueOf(rowid) });       
        
        Pin result = new Pin();
        
        if(cursor.moveToFirst())
        {
	        result.id = cursor.getInt(0);
	        result.Label = cursor.getString(1);
	        result.value = PinEncoder.DecodePin(mMasterKey, cursor.getInt(2));
        }
        
        cursor.close();
        db.close();
        
        return result;
	}
	
	public List<Pin> getAllPins(){
		List<Pin> result = new ArrayList<Pin>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PINS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pin val = new Pin();
                
                val.id = cursor.getInt(0);
                val.Label = cursor.getString(1);
                val.value = PinEncoder.DecodePin(mMasterKey, cursor.getInt(2));
                
                // Adding contact to list
                result.add(val);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        
        // return contact list
        return result;
	}

	public int updatePin(Pin pin){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, pin.Label);
		values.put(KEY_PIN, PinEncoder.EncodePin(mMasterKey, pin.value));
		
		int result = db.update(TABLE_PINS, values, KEY_ID + " = ?", new String[]{String.valueOf(pin.id)});
		
		db.close();
		
		return result;
	}

	public boolean removePin(Pin pinToRemove){		
		SQLiteDatabase db = this.getWritableDatabase();
		
		int result = db.delete(TABLE_PINS, KEY_ID + " = ?", new String[] {String.valueOf(pinToRemove.id)} );
		
		db.close();
		
		return result > 0;
	}
	
	public void clearAllPins(){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PINS, null, null );
		
		db.close();
	}
}