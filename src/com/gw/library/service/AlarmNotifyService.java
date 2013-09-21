package com.gw.library.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseService;
import com.gw.library.base.C;
import com.gw.library.model.Loan;
import com.gw.library.model.User;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.ui.AlarmDetialActivity;
import com.gw.library.util.AppUtil;

public class AlarmNotifyService extends BaseService {

	// 用户
	private User user = BaseAuth.getUser();
	// 数据和数据库
	ArrayList<Loan> rList;
	RemindSqlite rSqlite;

	@Override
	public void onCreate() {
		super.onCreate();
		getReturnList();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Intent alarmIntent = new Intent(C.action.alarmReceiverAction);
		sendBroadcast(alarmIntent);
		showNotification();

	}

	/**
	 * 从数据库中读取归还时间符合要求的书籍
	 */
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	private void getReturnList() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date settingDate = new Date(System.currentTimeMillis());// 获取设置的时间
		String str = formatter.format(settingDate);

		ArrayList<HashMap<String, String>> mapList = rSqlite.query(
				"select * from loan where studentNumber=? and returnDate="
						+ str, new String[] { user.getStudentNumber() });
		if (mapList.size() > 0) {
			try {
				rList = (ArrayList<Loan>) AppUtil.hashMapToModel(
						"com.gw.library.model.Loan", mapList);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			rList = null;
		}
	}

	/**
	 * 通知栏提醒
	 */
	private void showNotification() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 定义通知栏展现的内容信息
		int icon = R.drawable.ic_launcher;// 提示图片
		// 定义下拉通知栏时要展现的内容信息
		CharSequence tickerText = "Library";
		Context context = getApplicationContext();
		CharSequence contentTitle = "Library Remind ";
		CharSequence contentText = "亲，你有即将到期的图书哟。。。赶紧的查看";

		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(AlarmNotifyService.this,
				AlarmDetialActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(2, notification);
	}
}
