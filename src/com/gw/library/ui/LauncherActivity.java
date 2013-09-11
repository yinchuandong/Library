package com.gw.library.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import com.gw.library.base.GwListView.OnRefreshListener;

import com.gw.library.R;
import com.gw.library.base.BaseUi;
import com.gw.library.base.GwListView;
import com.gw.library.list.HistoryList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LauncherActivity extends BaseUi {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_settings);
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
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}
	
	
}
