package com.gw.library.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseService;
import com.gw.library.base.C;
import com.gw.library.model.Loan;
import com.gw.library.model.User;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.ui.AlarmDetialActivity;
import com.gw.library.ui.RemindActivity;
import com.gw.library.util.AppUtil;

/**
 * 闹钟服务不能又轮训服务开启,
 * 
 * @author Administrator
 * 
 */
public class AlarmNotifyService extends BaseService {

	// 用户
	private User user = BaseAuth.getUser();
	// 数据和数据库
	ArrayList<Loan> rList;
	RemindSqlite rSqlite;

	@Override
	public void onCreate() {
		super.onCreate();
		rList = new ArrayList<Loan>();
		rSqlite = new RemindSqlite(this);
		Log.v("alarm", "--------------->>>创建AlarmService成功！");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		try {
			getOverList();
			if (rList != null && rList.size() > 0) {
				Intent alarmIntent = new Intent(C.action.alarmReceiverAction);
				sendBroadcast(alarmIntent);
				showNotification();
				rList.clear();// 清除rList数据
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("AlarmNotifyServer-------------->",
					"Alarm闹钟异常--------------》error");
		}
		Log.v("alarm", "--------------->>>开启AlarmService成功！");
	}

	/**
	 * 从数据库中读取归还时间符合要求的书籍
	 */
	@SuppressWarnings("unchecked")
	private void getOverList() {

		ArrayList<HashMap<String, String>> mapList = rSqlite.query(
				"select * from loan where studentNumber=?",
				new String[] { user.getStudentNumber() });

		if (mapList.size() > 0) {
			try {
				ArrayList<Loan> temp = (ArrayList<Loan>) AppUtil
						.hashMapToModel("com.gw.library.model.Loan", mapList);

				for (int i = 0; i < temp.size(); i++) {
					if (getOverDay(temp.get(i).getReturnDate()) <= 0) {
						rList.add(temp.get(i));
					}

				}
				Log.v("remind", "有需要提醒的书籍，一共-----------》" + rList.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取应用设置中提前毫秒数
	 */
	private long getSettingDate() {
		SharedPreferences setting = AppUtil
				.getSharedPreferences(AlarmNotifyService.this);
		int day = setting.getInt("ahead_day", 0);
		return day * 24 * 60 * 60 * 1000;
	}

	/**
	 * 获得超期天数
	 * 
	 * @param resource
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public int getOverDay(String resource) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		int overDay = 0;

		try {
			Date date = format.parse(resource);
			Long nowTime = System.currentTimeMillis();
			overDay = (int) Math
					.ceil((double) ((date.getTime() - getSettingDate()) - nowTime)
							/ (24 * 60 * 60 * 1000));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return overDay;
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
		CharSequence contentText = "亲，你有即将到期的图书哟!!赶紧的查看,免得要扣费哟！";

		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;
		// 发送给RemindActivity的接收器

		// 点击消息后跳转页面
		Intent notificationIntent = new Intent(AlarmNotifyService.this,
				AlarmDetialActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("rList", rList);
		notificationIntent.putExtras(bundle);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(
				AlarmNotifyService.this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(2, notification);
		// Intent intent = new Intent(C.action.remindAction);
		// sendBroadcast(intent);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
