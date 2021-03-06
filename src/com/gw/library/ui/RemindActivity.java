package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.AppUtil;
import com.gw.library.widget.GwListView;
import com.gw.library.widget.GwListView.OnLoadMoreListener;
import com.gw.library.widget.GwListView.OnLoadMoreViewState;
import com.gw.library.widget.GwListView.OnRefreshListener;

public class RemindActivity extends BaseUiAuth {

	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;

	RemindReceiver remindReceiver;
	public static boolean isLoaded = false; // 是否被加载的标志
	public static int loadMoreState = OnLoadMoreViewState.LMVS_FIRST;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		listView = (GwListView) findViewById(R.id.remind_list);
		listView.updateLoadMoreViewState(loadMoreState);
		rSqlite = new RemindSqlite(this);

		pullToRefresh(); // 绑定下拉刷新的事件
		initData(); // 初始化数据

		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			doTaskAsync(
					C.task.loanList,
					C.api.loanList + "?studentNumber="
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
		ArrayList<HashMap<String, String>> mapList = rSqlite.query(
				"select * from loan where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			rList = (ArrayList<Loan>) AppUtil.hashMapToModel(
					"com.gw.library.model.Loan", mapList);

			remindListAdapter = new RemindList(this, rList);
			listView.setAdapter(remindListAdapter);
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
		switch (taskId) {

		case C.task.loanList:
			try {
				String whereSql = Loan.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				rSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				rList = (ArrayList<Loan>) message.getDataList("Loan");
				for (Loan loan : rList) {
					rSqlite.updateloan(loan);
					Log.i("studentNumber", loan.getStudentNumber());
				}
				remindListAdapter.setData(rList); // 必须调用这个方法来改变data，否者刷新无效
				remindListAdapter.notifyDataSetChanged();

			} catch (Exception e) {// 没有需要提醒的情况
				e.printStackTrace();

			} finally {
				isLoaded = true;// 加载完成的标志设为true
				listView.onRefreshComplete(); // 刷新完成
				loadMoreState = OnLoadMoreViewState.LMVS_NORMAL;
				listView.updateLoadMoreViewState(loadMoreState);// 设置显示加载更多
				baseDialog.close();
			}
			break;

		case C.task.renew:
			baseDialog.setData(1, message.getInfo());
			baseDialog.show();
			break;
		}
	}

	@Override
	public void onNetworkError(int taskId) {
		toast(C.err.network);
	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				doTaskAsync(
						C.task.loanList,
						C.api.loanList + "?studentNumber="
								+ user.getStudentNumber() + "&password="
								+ user.getPassword() + "&schoolId="
								+ user.getSchoolId());
			}
		});
		// 加载更多
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// int page = (int) Math.ceil(rList.size() / 4.0);
				// page++;
				// doTaskAsync(
				// C.task.loanList,
				// C.api.loanList + "?studentNumber="
				// + user.getStudentNumber() + "&password="
				// + user.getPassword() + "&schoolId="
				// + user.getSchoolId() + "&p=" + page);
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册remindReceiver
		try {
			remindReceiver = new RemindReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(C.action.remindAction);
			this.registerReceiver(remindReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置加载更多
		listView.updateLoadMoreViewState(loadMoreState);
	}

	/**
	 * 添加Receiver,接受来自后台的更新操作
	 */
	public static class RemindReceiver extends BroadcastReceiver {

		public RemindReceiver() {

		}

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 显示有更新
			if (intent.getAction().equals(C.action.remindAction)) {
				// 取消闹钟
				// Intent intent2 = new Intent(C.action.alarmStopAction);
				// // sendBroadcast(intent2);
				// Activity activity = new Activity();
				// activity.sendBroadcast(intent2);
			}
			Log.i("RemindReceiver--->onreceive", intent.getAction());
		}
	}

	@Override
	public void onPause() {
		super.onStop();
		try {
			unregisterReceiver(remindReceiver);
		} catch (Exception e) {
			Log.i("Historyactivity-->onPause", "false");
		}

	}

}
