package info.androidhive.loginandregistration.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "AndroidHiveLogin";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_ACCOUNT="Account";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	public void setAccount(String account){
		editor.putString(KEY_ACCOUNT,account);//設定帳戶(固定住)
		editor.commit();
	}
	public String getAccout(){
	return pref.getString(KEY_ACCOUNT,null);//(一開始回傳的帳戶的默認值為null)
	}
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}

}

