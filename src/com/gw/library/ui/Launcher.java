package com.gw.library.ui;

import com.gw.library.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tpl_history_item);
		//setContentView(R.layout.main_tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

}
