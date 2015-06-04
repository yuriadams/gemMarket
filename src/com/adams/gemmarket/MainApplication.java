package com.adams.gemmarket;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

public class MainApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Parse.enableLocalDatastore(this);
			Parse.initialize(this, "9k9NDex9y9WATuGZ9T74mWzHcDTnWO6Uq3EoekDc", "gxRgeRxOl5fs75JInm2oCeikk2M1tHU5b58sL38Y");
		} catch (Exception e) {
			Log.i("Parse ERROR: ", e.getMessage());
		}
	}

}
