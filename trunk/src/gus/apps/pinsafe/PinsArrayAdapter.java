package gus.apps.pinsafe;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PinsArrayAdapter extends ArrayAdapter<Pin> {

	public PinsArrayAdapter(Context context, int textViewResourceId,
			List<Pin> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater.from(super.getContext()).inflate(R.layout.pins_listview, parent);
		return super.getView(position, convertView, parent);
	}

}
