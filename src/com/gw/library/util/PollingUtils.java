package com.gw.library.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.gw.library.base.C;

public class PollingUtils {

	// 开启轮询服务
	public static void startPollingService(Context context, int seconds,
			Class<?> cls, String action) {
		// 获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();

		// 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
				seconds * 1000, pendingIntent);
	}

	// 停止轮询服务
	public static void stopPollingService(Context context, Class<?> cls,
			String action) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 取消正在执行的服务
		manager.cancel(pendingIntent);
	}

	// 开启闹钟服务
	public static void startAlarmService(Context context, Class<?> cls,
			String action) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 00);
		// 获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 触发服务的起始时间
		long triggerAtTime = calendar.getTimeInMillis();
		long currenAtTime = System.currentTimeMillis();
		if (currenAtTime > triggerAtTime) {
			triggerAtTime += C.time.alarmTime;
		}
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
				C.time.alarmTime, pendingIntent);

	}
}