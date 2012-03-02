package com.roly.GiveMeMyMoney2;

import android.content.Intent;
import greendroid.app.GDApplication;

public class G3MApplication extends GDApplication{

	@Override
	public Class<?> getHomeActivityClass() {
		return G3MActivity.class;
	}
	
	@Override
	public Intent getMainApplicationIntent() {
		return null;
	}
}
