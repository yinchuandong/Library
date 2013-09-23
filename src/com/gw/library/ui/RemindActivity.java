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
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.AppUtil;

public class RemindActivity extends BaseUiAuth {

	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;

	RemindReceiver remindReceiver;
	public static boolean isLoaded = false; // 是否被加载的标志

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		listView = (GwListView) findViewById(R.id.remind_list);

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

				isLoaded = true;// 加载完成的标志设为true
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				listView.onRefreshComplete(); // 刷新完成
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册remindReceiver
		remindReceiver = new RemindReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(C.action.remindAction);
		this.registerReceiver(remindReceiver, filter);

	}

	/**
	 * 添加Receiver,接受来自后台的更新操作
	 */
	public class RemindReceiver extends BroadcastReceiver {

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
			unregisterReceiver(remindReceiver);
		} catch (Exception e) {
			Log.i("Historyactivity-->onPause", "false");
		}

	}

}
