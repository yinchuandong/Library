package com.gw.library.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseService;
import com.gw.library.base.C;
import com.gw.library.model.History;
import com.gw.library.model.Loan;
import com.gw.library.model.User;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.sqlite.RemindSqlite;

public class NotifyService extends BaseService {
	/**
	 * 后台网络刷新服务，定时轮询刷新数据,保存到数据库
	 */
	private static final int when = 1000;// 轮询时间

	// 后台服务的action
	public static String SERVICE_HISTORY_ACTION = "com.gw.library.service.NotifyService.HISTORY";// history的action
	public static String SERVICE_REMIND_ACTION = "com.gw.library.service.NotifyService.LOAN";// loan的action
	public static String SERVICE_APP_ACTION = "com.gw.library.service.APPSERVER";
	// 用户
	private User user = BaseAuth.getUser();
	// 数据和数据库
	public ArrayList<History> hList;
	public HistorySqlite hSqlite;
	ArrayList<Loan> rList;
	RemindSqlite rSqlite;

	// 定时器
	private Timer timer = new Timer();
	// 广播接收器
	APPReceiver appReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 注册接收器
		appReceiver = new APPReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SERVICE_APP_ACTION);
		registerReceiver(appReceiver, filter);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if (intent.getAction().equals("")) {
			// 当登陆进入后，马上执行刷新
			getHistoryList();
			getLoanList();
		} else {
			// 定时刷新
			timer.schedule(new TimerSerivceTask(), when);
		}
	}

	// Timer 定时任务
	private class TimerSerivceTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getHistoryList();
			getLoanList();
		}

	}

	// 获取历史消息
	public void getHistoryList() {
		doTaskAsync(C.task.historyList, C.api.historyList + "?studentNumber="
				+ user.getStudentNumber() + "&password=" + user.getPassword()
				+ "&schoolId=" + user.getSchoolId());
	}

	// 获取借阅消息
	public void getLoanList() {
		doTaskAsync(C.task.loanList,
				C.api.loanList + "?studentNumber=" + user.getStudentNumber()
						+ "&password=" + user.getPassword() + "&schoolId="
						+ user.getSchoolId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// TODO Change to Model
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.historyList:
			try {
				String whereSql = History.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				hSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				hList = (ArrayList<History>) message.getDataList("History");
				for (History history : hList) {
					hSqlite.updateHistory(history);
					Log.i("studentNumber", history.getStudentNumber());
				}

				// 发送消息，通知刷新界面
				Intent sendIntent = new Intent();
				sendIntent.setAction(SERVICE_HISTORY_ACTION);
				sendIntent.putExtra("HList", hList);
				sendBroadcast(sendIntent);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			break;
		case C.task.loanList:
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

				// 发送消息，通知刷新界面
				Intent sendIntent = new Intent();
				sendIntent.setAction(SERVICE_REMIND_ACTION);
				sendIntent.putExtra("rList", rList);
				sendBroadcast(sendIntent);

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	// 服务的接收器
	public class APPReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

		}

	}

}
