package gus.apps.pinsafe;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PinsActivity extends Activity {

	static final int ADD_PIN_REQUEST = 1;
	static final String EXTRA_PINVALUE = "gus.apps.pinsafe.EXTRA_PINVALUE";
	static final String EXTRA_PINLABEL = "gus.apps.pinsafe.EXTRA_PINLABEL";
	
	int mMasterKey = 0;
	
	PinDataBaseHandler mDBHandler = null;
	ArrayAdapter<Pin> mAdapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //check version and surpress screenshots >= honeycomb
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        
        setContentView(R.layout.activity_pins);
        
        //get masterkey
        mMasterKey = getIntent().getExtras().getInt(Constants.EXTRA_MASTERKEY);
        
        //grab the database handler
        mDBHandler = new PinDataBaseHandler(this, mMasterKey);
        
        List<Pin> allPins = mDBHandler.getAllPins();
        
        // TODO -  read all the pins and apply to listview
        ListView lv = (ListView)findViewById(R.id.listview_pins);
        mAdapter = new ArrayAdapter<Pin>(this, android.R.layout.simple_list_item_1, allPins);
        lv.setAdapter(mAdapter);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ADD_PIN_REQUEST && resultCode == RESULT_OK && data != null)
		{
			//extract data from intent
			String pinLabel = data.getStringExtra(EXTRA_PINLABEL);
			int pinValue = data.getIntExtra(EXTRA_PINVALUE, 0);
			
			Pin newPin = mDBHandler.addPin(pinLabel, pinValue);
			mAdapter.add(newPin);
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pins, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_addpin:
            	addPin();
            	return true;
            case R.id.menu_deleteall:
            	mDBHandler.clearAllPins();
            	mAdapter.clear();
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    void addPin(){
    	Intent intent = new Intent(this, AddPinActivity.class);
    	super.startActivityForResult(intent, ADD_PIN_REQUEST);
    }
}
