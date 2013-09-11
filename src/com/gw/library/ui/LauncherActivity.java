package com.gw.library.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.gw.library.R;
import com.gw.library.base.BaseUi;

public class LauncherActivity extends BaseUi {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
<<<<<<< HEAD
		// setContentView(R.layout.ui_history);
		// setContentView(R.layout.ui_login);
		// setContentView(R.layout.main_tab);

		// Intent intent = new Intent(LauncherActivity.this,
		// LoginActivity.class);
		// this.startActivity(intent);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(400);
					Log.i("launcheer", "1");
					forward(LoginActivity.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

=======
		setContentView(R.layout.ui_history);
		//setContentView(R.layout.main_tab);
		
//		new Thread(){
//			public void run(){
//				try{
//					Thread.sleep(400);
//					Log.i("launcheer", "1");
//					forward(HistoryActivity.class);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		}.start();
		forward(LoginActivity.class);
		
		
>>>>>>> de462430027292d72437ed02eb2d3a54da74389e
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

}
