package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseTask;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.service.NotifyService;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.AppUtil;

public class RemindActivity extends BaseUiAuth {

	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;

	RemindReceiver remindReceiver;
	private static final String REMIND_ACTION = NotifyService.SERVICE_REMIND_ACTION;

	public static boolean isLoaded = false; // 是否被加载的标志

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		listView = (GwListView) findViewById(R.id.remind_list);

		rSqlite = new RemindSqlite(this);

		// 注册remindReceiver
		remindReceiver = new RemindReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(REMIND_ACTION);
		registerReceiver(remindReceiver, filter);

		pullToRefresh();
		initData();

		pullToRefresh(); // 绑定下拉刷新的事件
		initData(); // 初始化数据

		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			doTaskAsync(
					1,
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
			// 无记录处理
			if (rList == null || rList.size() == 0) {
				toast("没有借阅记录,去library看看吧");
			}
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
		Log.i("remindactivity====ontaskcomplete", taskId + "");
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
			listView.onRefreshComplete(); // 刷新完成

			isLoaded = true;// 加载完成的标志设为true
		} catch (Exception e) {
			e.printStackTrace();
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
						C.api.loanList + "?studentNumber="
								+ user.getStudentNumber() + "&password="
								+ user.getPassword() + "&schoolId="
								+ user.getSchoolId());
			}
		});
	}

	/**
	 * 添加Receiver,接受来自后台的更新操作
	 */
	public class RemindReceiver extends BroadcastReceiver {

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 更新UI
			if (intent.getAction().equals(
					"com.gw.library.service.NotifyService.HISTORY")) {
				rList = (ArrayList<Loan>) intent.getSerializableExtra("rList");
				remindListAdapter.setData(rList); // 必须调用这个方法来改变data，否者刷新无效
				remindListAdapter.notifyDataSetChanged();
			}
		}

	}

}
