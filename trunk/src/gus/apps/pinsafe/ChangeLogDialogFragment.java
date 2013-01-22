package gus.apps.pinsafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChangeLogDialogFragment extends DialogFragment {
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())  
	        .setMessage(R.string.ChangeLog_Body)
	        .setTitle(R.string.ChangeLog_Title)
	        .setPositiveButton("OK", null);
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
