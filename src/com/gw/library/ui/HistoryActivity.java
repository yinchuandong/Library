package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;

public class HistoryActivity extends BaseUiAuth {

	GwListView listView;
	BaseAdapter baseAdapter;
	LinkedList<String> data = new LinkedList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);
		pullToRefresh();
		
//		super.recommendBtn.setFocusable(true);
	}
	/**
	 * 下拉刷新
	 */
	public void pullToRefresh(){
		for (int i = 0; i < 10; i++) {
			data.add(String.valueOf(i));
		}
		listView = (GwListView)findViewById(R.id.history_list);
		
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
						data.addFirst( "12312312");
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
