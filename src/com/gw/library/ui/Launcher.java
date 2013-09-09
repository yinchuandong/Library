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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Launcher extends BaseUi {

	GwListView listView;
	BaseAdapter baseAdapter;
	LinkedList<String> data = new LinkedList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);
		//setContentView(R.layout.main_tab);
		
		pullToRefresh();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}
	
	/**
	 * 下拉刷新
	 */
	public void pullToRefresh(){
		for (int i = 0; i < 10; i++) {
			data.add(String.valueOf(i));
		}
		listView = (GwListView)findViewById(R.id.historyList);
		baseAdapter = new BaseAdapter() {
			
			public View getView(int position, View convertView, ViewGroup parent) {
				 convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tpl_history_item, null);
				 TextView textView = (TextView)convertView.findViewById(R.id.test);
				 textView.setText(data.get(position));
				return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return data.get(position);
			}

			public int getCount() {
				return data.size();
			}
		};
		
		listView.setAdapter(baseAdapter);
		
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						data.addFirst("12312312");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						baseAdapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}

}
