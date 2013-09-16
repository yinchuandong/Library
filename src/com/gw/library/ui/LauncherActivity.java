package com.gw.library.ui;

import android.os.Bundle;
import android.view.Menu;

import com.gw.library.R;
import com.gw.library.base.BaseUi;

public class LauncherActivity extends BaseUi {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tpl_remind_item);
		// setContentView(R.layout.main_tab);

		// new Thread(){
		// public void run(){
		// try{
		// Thread.sleep(400);
		// Log.i("launcheer", "1");
		// forward(HistoryActivity.class);
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		// }.start();
		forward(LoginActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

}
