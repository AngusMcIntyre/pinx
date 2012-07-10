package gus.apps.pinsafe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PinsActivity extends Activity {

	int mMasterKey = 0;
	
	PinDataBaseHandler mDBHandler = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pins);
        
        //get masterkey
        mMasterKey = getIntent().getExtras().getInt(Constants.EXTRA_MASTERKEY);
        
        Pin[] pins = { new Pin("Visa", 1), new Pin("Debit", 11), new Pin("Sky+", 111) };
        
        //grab the database handler
        mDBHandler = new PinDataBaseHandler(this);
        mDBHandler.addPin(new SimpleDateFormat("E HH:mm:ss").format(new Date()), 1234);
        
        List<Pin> allPins = mDBHandler.getAllPins();
        
        // TODO -  read all the pins and apply to listview
        ListView lv = (ListView)findViewById(R.id.listview_pins);
        ArrayAdapter<?> arrAdapter = new ArrayAdapter<Pin>(this, android.R.layout.simple_list_item_1, allPins);
        lv.setAdapter(arrAdapter);
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
            	//TODO - get new pin fron activity
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
