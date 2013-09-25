package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnLoadMoreListener;
import com.gw.library.base.GwListView.OnLoadMoreViewState;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.HistoryList;
import com.gw.library.model.History;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.util.AppUtil;

public class HistoryActivity extends BaseUiAuth {

	public GwListView listView;
	// BaseAdapter baseAdapter;
	public HistoryList hListAdapter; // Listview 的adapter
	public ArrayList<History> hList; // 具体数据

	HistorySqlite hSqlite = new HistorySqlite(this);

	public static boolean isLoaded = false; // 是否被加载的标志
	private boolean isFromLoadMore = false;// 标记加载 更多
	HistoryReceiver historyReceiver;
	public static int loadMoreState = OnLoadMoreViewState.LMVS_FIRST;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);

		listView = (GwListView) findViewById(R.id.history_list);
		listView.updateLoadMoreViewState(loadMoreState);
		// 实例化数据库
		hSqlite = new HistorySqlite(this);
		// 初始化数据，打开页面的时候从手机数据库里面获取数据
		initData();
		pullToRefresh();
		// 为每一个列表项添加动作事件
		listView.setOnItemClickListener(new HSItemListener());

		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			doTaskAsync(
					1,
					C.api.historyList + "?studentNumber="
							+ user.getStudentNumber() + "&password="
							+ user.getPassword() + "&schoolId="
							+ user.getSchoolId());
		}

	}

	/**
	 * 从数据库里面加载数据
	 */
	@SuppressWarnings("unchecked")
	public void initData() {
		ArrayList<HashMap<String, String>> mapList = hSqlite.query(
				"select * from history where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			hList = (ArrayList<History>) AppUtil.hashMapToModel(
					"com.gw.library.model.History", mapList);

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
		Log.i("remindactivity====ontaskcomplete", taskId + "");
		try {
			String whereSql = History.COL_STUDENTNUMBER + "=?";
			String[] whereParams = new String[] { user.getStudentNumber() };
			hSqlite.delete(whereSql, whereParams); // 清空当前历史列表
			hList = (ArrayList<History>) message.getDataList("History");
			for (History history : hList) {
				hSqlite.updateHistory(history);
				Log.i("studentNumber", history.getStudentNumber());
			}
			if (hList == null || hList.size() == 0) {// 无记录处理
				toast("没有借阅记录,去library看看吧");
			} else {
				hListAdapter.setData(hList); // 必须调用这个方法来改变data，否者刷新无效
				hListAdapter.notifyDataSetChanged();
				listView.onRefreshComplete(); // 刷新完成

				isLoaded = true; // 加载完成的标志设为true
				if (isLoaded) {
					loadMoreState = OnLoadMoreViewState.LMVS_NORMAL;
					listView.updateLoadMoreViewState(loadMoreState);
				}
				if (isFromLoadMore) {// 数据都加载完毕
					loadMoreState = OnLoadMoreViewState.LMVS_OVER;
					listView.updateLoadMoreViewState(loadMoreState);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			toast(e.getMessage());
		}
	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {

				doTaskAsync(
						1,
						C.api.historyList + "?studentNumber="
								+ user.getStudentNumber() + "&password="
								+ user.getPassword() + "&schoolId="
								+ user.getSchoolId());

			}
		});

		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				doTaskAsync(
						1,
						C.api.historyList + "?studentNumber="
								+ user.getStudentNumber() + "&password="
								+ user.getPassword() + "&schoolId="
								+ user.getSchoolId());
				isFromLoadMore = true;
			}
		});
	}

	/**
	 * History列表item被点击后的动作事件，逐项显示
	 */
	class HSItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent(HistoryActivity.this,
					HistoryWebViewActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", hList.get(position - 1).getUrl());
			intent.putExtras(bundle);
			startActivity(intent);

		}

	}

	@Override
	public void onResume() {

		super.onResume();
		// 注册historyReceiver
		historyReceiver = new HistoryReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(C.action.historyAction);
		this.registerReceiver(historyReceiver, filter);
		// 设置加载更多
		listView.updateLoadMoreViewState(loadMoreState);
	}

	/**
	 * 添加Receiver,接受来自后台的更新操作
	 */
	public class HistoryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 显示有更新
			toast("" + intent.getIntExtra("numb", 5));
		}
	}

	@Override
	public void onPause() {
		super.onStop();
		try {
			unregisterReceiver(historyReceiver);
		} catch (Exception e) {
			Log.i("Historyactivity-->onstop", "false");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
