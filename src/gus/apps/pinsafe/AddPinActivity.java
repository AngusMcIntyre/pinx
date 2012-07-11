package gus.apps.pinsafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author angus.mcintyre
 *
 */
public class AddPinActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);
        
        EditText t = (EditText)findViewById(R.id.editText_pinvalue);
        t.setOnEditorActionListener(new OnEditorActionListener(){

			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				onSubmit(arg0);
				return false;
			}
        });
    }   
    
    public void onSubmit(View view){
    	Intent data = this.getResultData();
    	if(data != null)	//do nothing if validation failed.
    	{
	    	super.setResult(RESULT_OK, data);
	    	super.finish();
    	}
    }
    
    /**
     * Gets the pin details as intent data for return result.
     * @return null if field validation fails.
     */
    Intent getResultData(){
    	Intent intent = new Intent();
    	
    	EditText view = (EditText)findViewById(R.id.editText_pinlabel);   	
    	if(view.length() == 0) return null;
    	intent.putExtra(PinsActivity.EXTRA_PINLABEL, view.getText().toString());
    	
    	view = (EditText)findViewById(R.id.editText_pinvalue);
    	if(view.length() == 0) return null;
    	
    	int value = 0;
    	try
    	{
    		value = Integer.parseInt(view.getText().toString());
    		intent.putExtra(PinsActivity.EXTRA_PINVALUE, value);
    	}
    	catch(Exception ex)
    	{
    		//validation failed.
    		return null;
    	}
    	
    	
    	return intent;
    }
}