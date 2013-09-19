package com.gw.library.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.gw.library.R;
import com.gw.library.base.BaseUi;
import com.gw.library.service.AlarmReceiver;

public class AlarmNotifyActivity extends BaseUi {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);
		Calendar calendar = Calendar.getInstance();
		// 设置闹钟的时�?
		calendar.set(calendar.YEAR, 2013);
		calendar.set(calendar.MONTH, calendar.AUGUST);// 0-11表示12个月�?表示�?���?
		calendar.set(calendar.DAY_OF_MONTH, 13);
		calendar.set(calendar.HOUR_OF_DAY, 23);
		calendar.set(calendar.MINUTE, 5);
		calendar.set(calendar.SECOND, 0);
		// calendar.set(2013,9,13,23,30,0);

		Intent intent = new Intent("com.gw.library.service.action");
		intent.setClass(this, AlarmReceiver.class);
		// 设置PendingIntent对象发�?广播
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		// 获取AlarmManger对象
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 设置时间到的时�?，执行PendingIntent,只是执行�?��
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pIntent);
		// 如果�?��重复执行，使用上面一行的setRepeating方法，�?数第二参数为间隔时间,单位为毫�?
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 10000, pIntent);

	}
}
