package gus.apps.pinsafe;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentManager;

public class ChangeLogDisplayer {
	static final String LAST_CHANGELOG_VERSION = "gus.apps.pinsafe.LAST_CHANGELOG_VERSION";
	public static final boolean IsLogShowRequired(Context context)
	{

		try {
			//get package version number
			int version = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionCode;
			
			int previousVersion = context.getSharedPreferences(ChangeLogDisplayer.class.toString(), 0)
					.getInt(LAST_CHANGELOG_VERSION, 0);
			
			return version > previousVersion;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static final void ShowComplete(Context context, FragmentManager fragmentManager)
	{
		if(IsLogShowRequired(context))
			ShowLog(context, fragmentManager);
	}
	public static final void ShowLog(Context context, FragmentManager fragmentManager)
	{
		ChangeLogDialogFragment dialog = new ChangeLogDialogFragment();
		dialog.show(fragmentManager, ChangeLogDisplayer.class.toString());
		int version;
		
		try {
			version = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		Editor edit = context.getSharedPreferences(ChangeLogDisplayer.class.toString(), 0).edit();
		edit.putInt(LAST_CHANGELOG_VERSION, version);
		edit.apply();
	}
}
