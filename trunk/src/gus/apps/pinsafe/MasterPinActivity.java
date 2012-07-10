package gus.apps.pinsafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MasterPinActivity extends Activity {

	final String TAG = "gus.apps.pinsafe.MasterPinActivity"; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_pin);
        
        final EditText et = (EditText)this.findViewById(R.id.edittext_masterpin);
        et.setOnEditorActionListener(new OnEditorActionListener(){

			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
		            	ViewPins();
		            return true;
		        }
				return false;
			}   	
        });
        
        et.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				if(s.length() == 4)
					ViewPins();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {			
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}
        
        });
    }   
    
        
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		EditText et = (EditText)super.findViewById(R.id.edittext_masterpin);
		et.setText("");	//empty the password
		
		//this feels like a hack
		//InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
		//imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
	}

	public void onGoPressed(View view){
    	
    	ViewPins();
	}
	
	public void ViewPins(){
		//get master key
    	EditText et = (EditText)super.findViewById(R.id.edittext_masterpin);
    	String val = et.getText().toString();
    	
    	if(val == null || val.length() == 0)
    		return;
    		
    	int keyVal = 0;
    	try
    	{
    		keyVal = Integer.parseInt(et.getText().toString());
    	}
    	catch(NumberFormatException ex)
    	{
    		Log.e(TAG, ex.getStackTrace().toString());
    		return;
    	}
    	
    	//setup intent
    	Intent intent = new Intent(this, PinsActivity.class);
    	intent.putExtra(Constants.EXTRA_MASTERKEY, keyVal);    	
    	super.startActivity(intent);
	}
}
