package com.scaninfotech.android.msmartlite;

import android.app.Activity;

public class AssetRegistrationActivity extends Activity
{
	public AssetRegistrationActivity()
	{
		super();
	}

	protected AssetRegistration getStuffApplication()
	{
		return (AssetRegistration)getApplication();
	}
}
