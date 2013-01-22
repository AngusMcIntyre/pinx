package gus.apps.pinsafe;

import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;

public class KeyPadWidgetProvider extends AppWidgetProvider {
	static final String TAG = "KeyPadWidgetProvider";
	static final String EXTRA_KEYPADVALUE = "gus.apps.pinsafe.EXTRA_KEYPADVALUE";
	static final String EXTRA_KEYPADFINAL_VALUE = "gus.apps.pinsafe.EXTRA_KEYPAD_FINAL_VALUE";
	final int DEFAULT_VALUE = -1;
	static int[] buttonMappings = 
		{
			R.id.keypad_zero,
			R.id.keypad_one,
			R.id.keypad_two,
			R.id.keypad_three,
			R.id.keypad_four,
			R.id.keypad_five,
			R.id.keypad_six,
			R.id.keypad_seven,
			R.id.keypad_eight,
			R.id.keypad_nine,
		};

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		
		Log.i(TAG, "All instances of widget removed, removing stored value");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		
		if(action == "gus.apps.pinsafe.KEYPADWIDGET_KEYPRESS"){
				if(intent.hasExtra(EXTRA_KEYPADVALUE))
				{
					this.onButtonPressed(context, intent.getIntExtra(EXTRA_KEYPADVALUE, 0));
					this.UpdateWidget(context);
				}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(TAG, "Update callback");
		
		UpdateWidget(context);
	}
	
	public void onButtonPressed(Context context, int newValue)
	{
		Log.i(TAG, "Keypad button pressed");
		
		SharedPreferences prefs = context.getSharedPreferences(KeyPadWidgetProvider.class.toString(), 0);
		Editor editor = prefs.edit();
		
		int val = DEFAULT_VALUE;
		if(newValue == DEFAULT_VALUE) {
			Log.i(TAG, "Pressed button was 'Clear' - resetting values");
			editor.putInt(EXTRA_KEYPADFINAL_VALUE, DEFAULT_VALUE);
		}
		else {	
			val = prefs.getInt(EXTRA_KEYPADFINAL_VALUE, DEFAULT_VALUE);
			
			//if there is a value, multiply by 10
			if(val == DEFAULT_VALUE)
				val = 0;
			
			val *= 10;
			//add new value
			val += newValue;
			
			if(Integer.toString(val).length() == 4)
			{
				Log.i(TAG, "Got 4 digits. Starting activity");
				Intent intent = new Intent(context, PinsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //flag required because current context is not an activity context
		    	intent.putExtra(Constants.EXTRA_MASTERKEY, val);
		    	
				context.startActivity(intent);
				
				val = DEFAULT_VALUE;
			}
			
			editor.putInt(EXTRA_KEYPADFINAL_VALUE, val);		
		}
		
		editor.commit();
		
		Log.i(TAG, String.format("B" +
				"Button value was %s. Updated pin is %s", newValue, val));	}

	RemoteViews getViews(Context context){

		//get the layout to use
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_keypad_tall);
        
        int value = context.getSharedPreferences(KeyPadWidgetProvider.class.toString(), 0)
        		.getInt(EXTRA_KEYPADFINAL_VALUE, DEFAULT_VALUE);
        
        //if there is a value, set the title to the current input
        if(value > DEFAULT_VALUE)
        {
        	//views.setTextViewText(R.id.keypadwidget_title, Integer.toString(value));
        	
        	//use a placeholder character 
        	char[] charArray = new char[Integer.toString(value).length()];
        	Arrays.fill(charArray, '\u2022'); //• character
        	String str = new String(charArray);
        	
        	views.setTextViewText(R.id.keypadwidget_title, str);
        }
        else 
        	views.setTextViewText(R.id.keypadwidget_title, context.getString(R.string.app_name));
        
        //Set an onclick listener for each button
        for(int b = 0; b < 10; b++){
        	Intent intent = new Intent()
    		.setClass(context, KeyPadWidgetProvider.class)
    		.setAction("gus.apps.pinsafe.KEYPADWIDGET_KEYPRESS");

        	intent.putExtra(EXTRA_KEYPADVALUE, b);
        	
        	//use request code in otherwise only the first pending intent will be used, 
        	//the action is a unique key, duplicates before send are ignored
        	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, b, intent, 0); 
        	
        	views.setOnClickPendingIntent(buttonMappings[b], pendingIntent);
        }
        
        //add the intent for the 'clear' button
        Intent intent = new Intent()
		.setClass(context, KeyPadWidgetProvider.class)
		.setAction("gus.apps.pinsafe.KEYPADWIDGET_KEYPRESS");

    	intent.putExtra(EXTRA_KEYPADVALUE, DEFAULT_VALUE);
    	
    	//use request code in otherwise only the first pending intent will be used, 
    	//the action is a unique key, duplicates before send are ignored
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DEFAULT_VALUE, intent, 0); 
    	
    	views.setOnClickPendingIntent(R.id.keypad_clear, pendingIntent);
        
        return views;
	}

	void UpdateWidget(Context context){
		RemoteViews updateViews = this.getViews(context);
		
		//Update widget instances
		ComponentName thisWidget = new ComponentName(context, KeyPadWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, updateViews);
	}
	
	void ResetInput(Context context){
		Log.i(TAG, "Resetting master pin input");
		//reset stored value
		Editor editor = context.getSharedPreferences(KeyPadWidgetProvider.class.toString(), 0).edit();
		editor.remove(EXTRA_KEYPADFINAL_VALUE);
		editor.apply();
	}
	
}
