package com.scaninfotech.android.msmartlite;

import android.app.Activity;

public class AssetMangerActivity extends Activity {
	protected AssetRegistration getTaskManagerApplication() {
		AssetRegistration tma = (AssetRegistration)getApplication();
		return tma;
	}
}
