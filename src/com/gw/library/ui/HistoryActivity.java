package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.Bundle;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.HistoryList;
import com.gw.library.model.History;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.util.AppUtil;

public class HistoryActivity extends BaseUiAuth {

	GwListView listView;
//	BaseAdapter baseAdapter;
	HistoryList hListAdapter; //listview 的adapter
	ArrayList<History> hList; //具体数据
	
	LinkedList<String> data = new LinkedList<String>();
	HistorySqlite hSqlite;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);
		
		listView = (GwListView)findViewById(R.id.history_list);
		//实例化数据库
		hSqlite = new HistorySqlite(this);
		//初始化数据，打开页面的时候从手机数据库里面获取数据
		initData(); 
		pullToRefresh();
		
	}
	
	
	/**
	 * 从数据库里面加载数据
	 */
	@SuppressWarnings("unchecked")
	public void initData(){
		ArrayList<HashMap<String, String>> mapList = hSqlite.query("select * from history where studentNumber=?", new String[]{user.getStudentNumber()});
		try {
			hList = (ArrayList<History>)AppUtil.hashMapToModel("com.gw.library.model.History", mapList);
			hListAdapter = new HistoryList(this, hList);
			listView.setAdapter(hListAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 异步线程完成之后的回调方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		Log.i("remindactivity====ontaskcomplete", taskId+"");
		try {
			String whereSql = History.COL_STUDENTNUMBER + "=?";
			String[] whereParams = new String[]{user.getStudentNumber()};
			hSqlite.delete(whereSql, whereParams); //清空当前历史列表
			hList = (ArrayList<History>)message.getDataList("History");
			for (History history : hList) {
				hSqlite.updateHistory(history);
				Log.i("studentNumber", history.getStudentNumber());
			}
			hListAdapter.setData(hList); //必须调用这个方法来改变data，否者刷新无效
			hListAdapter.notifyDataSetChanged();
			listView.onRefreshComplete(); //刷新完成
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 下拉刷新
	 */
	public void pullToRefresh(){
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				doTaskAsync(1, C.api.historyList + 
						"?studentNumber=" + user.getStudentNumber()+
						"&password=" + user.getPassword() + 
						"&schoolId=" + user.getSchoolId()
				);
			}
		});
	}

}

