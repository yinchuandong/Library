package com.gw.library.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.model.RemindTime;
import com.gw.library.model.User;
import com.gw.library.service.AlarmNotifyService;
import com.gw.library.sqlite.RemindTimeSqlite;
import com.gw.library.util.AppUtil;
import com.gw.library.util.PollingUtils;

public class SettingActivity extends BaseUiAuth {

	Button logoutBtn;
	Button saveBtn;
	Button backBtn;
	Spinner spinner;

	TimePicker timePicker;
	private int before_day;// 提前时间
	private int rHourOfDay;// 提醒时间
	private int rMinute;// 提醒时间

	SharedPreferences sharedPreferences;
	EditText editText;
	TextView timeEditText;
	RemindTimeSqlite remindTimeSqlite;
	ArrayList<RemindTime> tList;
	User user = BaseAuth.getUser();

	Calendar calendar = Calendar.getInstance();// 全局使用的calendar

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_settings);
		sharedPreferences = AppUtil.getSharedPreferences(SettingActivity.this);
		before_day = sharedPreferences.getInt("before_day", C.time.day);
		rHourOfDay = sharedPreferences.getInt("hourOfDay", C.time.hour);
		rMinute = sharedPreferences.getInt("minute", C.time.minute);

		// remindTimeSqlite = new RemindTimeSqlite(this);
		// before_day = getUserRemindTime(user);

		// timePicker = (TimePicker) this.findViewById(R.id.timepicker);
		logoutBtn = (Button) findViewById(R.id.logout);
		spinner = (Spinner) this.findViewById(R.id.select);
		backBtn = (Button) findViewById(R.id.s_back);
		saveBtn = (Button) this.findViewById(R.id.save);
		editText = (EditText) this.findViewById(R.id.s_number);
		timeEditText = (TextView) this.findViewById(R.id.time_EditText);// 显示时间
		// 显示系统数值
		spinner.setSelection(before_day);
		editText.setText(user.getStudentNumber());
		timeEditText.setText(rHourOfDay + "时" + rMinute + "分");
		bindEvent();
	}

	private void bindEvent() {
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = getIntent().getExtras();
				String lastActivity = bundle.getString("lastActivity");
				if (lastActivity.equals("RecommendActivity")) {
					forward(RecommendActivity.class);
				} else if (lastActivity.equals("HistoryActivity")) {
					forward(HistoryActivity.class);
				} else if (lastActivity.equals("RemindActivity")) {
					forward(RemindActivity.class);
				} else {
					forward(RemindActivity.class);
				}
			}
		});

		// 绑定退出事件
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseAuth.setLogin(false);
				BaseAuth.clearUserInfo(getContext());
				finish();
				// 停止之前的所有闹钟服务
				PollingUtils.stopAlarmService(LoginActivity.getLoginContext(),
						AlarmNotifyService.class, C.action.alarmAction);
				//恢复默认
				forward(LoginActivity.class);
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				before_day = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		saveBtn.setOnClickListener(new OnClickListener() {// 保存按钮统一保存

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// remindTimeSqlite.updateTime(tList.get(0));
				saveToAPP(before_day, rHourOfDay, rMinute);
				if (before_day == 0) {
					toast("提醒时间已经修改为当天");
				} else {
					toast("提醒时间已经修改为" + before_day + "天");
				}

			}
		});
		timeEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(SettingActivity.this, listener2,
						Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();// 调用对话框的TimerPicker
			}
		});

	}

	// 查询当前用户提醒的日期
	@SuppressWarnings("unchecked")
	public int getUserRemindTime(User user) {
		// String studentNumber = user.getStudentNumber();
		// String schoolId = user.getSchoolId();

		int day = 0;
		try {

			ArrayList<HashMap<String, String>> mapList = remindTimeSqlite
					.query("select * from RemindTime where studentNumber=?",
							new String[] { user.getStudentNumber() });
			if (mapList.size() > 0) {
				tList = (ArrayList<RemindTime>) AppUtil.hashMapToModel(
						"com.gw.library.model.RemindTime", mapList);
				Log.v("错误", "SettingActivity------------->>mapList.size()常"
						+ mapList.size());
			} else {
				Log.v("错误", "SettingActivity------------->>mapList.size()常"
						+ mapList.size());
			}
			day = Integer.parseInt(tList.get(0).getDay());
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("转化错误", "SettingActivity------------->>异常");
		}
		return day;
	}

	// 写到应用中区
	private void saveToAPP(int day, int hourOfDay, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		long settingTime = calendar.getTimeInMillis();
		long curTime = System.currentTimeMillis();
		long nextTime;
		if(settingTime < curTime){
			nextTime = settingTime + C.time.alarmTime;//修改上次提醒的时间
		}else{
			nextTime = settingTime;
		}
		Editor editor = sharedPreferences.edit();
		editor.putLong("nextRemindTime", nextTime);
		editor.putInt("before_day", day);
		editor.putInt("hourOfDay", hourOfDay);
		editor.putInt("minute", minute);
		editor.commit();

//		// 停止之前的闹钟服务
//		PollingUtils.stopAlarmService(LoginActivity.getLoginContext(),
//				AlarmNotifyService.class, C.action.alarmAction);
//		// 重新设置闹钟服务
//		PollingUtils.startAlarmService(LoginActivity.getLoginContext(),
//				AlarmNotifyService.class, C.action.alarmAction);
	}

	// 用于检测时间设定器的变化
	private TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);// 设置小时
			rHourOfDay = hourOfDay;
			calendar.set(Calendar.MINUTE, minute);// 调用分钟
			rMinute = minute;
			// 调用更新时间显示
			SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH时mm分");// 创建时间格式

			timeEditText.setText(simpleTimeFormat.format(calendar.getTime()));// 按照最新的calendar更新时间显示;//
		}
	};

	
		

}