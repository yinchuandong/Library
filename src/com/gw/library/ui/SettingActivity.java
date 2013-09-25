package com.gw.library.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.model.RemindTime;
import com.gw.library.model.User;
import com.gw.library.sqlite.RemindTimeSqlite;
import com.gw.library.util.AppUtil;

public class SettingActivity extends BaseUiAuth {

	Button logoutBtn;
	Button saveBtn;
	Spinner spinner;
	TimePicker timePicker;
	private int before_day;// 提前时间
	SharedPreferences sharedPreferences;
	EditText editText;
	RemindTimeSqlite remindTimeSqlite;
	ArrayList<RemindTime> tList;
	User user = BaseAuth.getUser();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_settings);

		sharedPreferences = AppUtil.getSharedPreferences(SettingActivity.this);
		before_day = sharedPreferences.getInt("before_day", 0);
		// remindTimeSqlite = new RemindTimeSqlite(this);
		// before_day = getUserRemindTime(user);

		// timePicker = (TimePicker) this.findViewById(R.id.timepicker);
		logoutBtn = (Button) findViewById(R.id.logout);
		spinner = (Spinner) this.findViewById(R.id.select);
		spinner.setSelection(before_day);
		saveBtn = (Button) this.findViewById(R.id.save);
		timePicker = (TimePicker) this.findViewById(R.id.timerpicker);
		editText = (EditText) this.findViewById(R.id.s_number);
		editText.setText(user.getStudentNumber());
		bindEvent();
	}

	private void bindEvent() {
		// 绑定退出事件
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseAuth.setLogin(false);
				BaseAuth.clearUserInfo(getContext());
				finish();
				// 清理数据库
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
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// remindTimeSqlite.updateTime(tList.get(0));
				saveToAPP(before_day);
				if (before_day == 0) {
					toast("提醒时间已经修改为当天");
				} else {
					toast("提醒时间已经修改为" + before_day + "天");
				}

			}
		});

	}

	// 使用TimePicker
	private void createTimePicker(Context context) {
		TimePicker tpicker = new TimePicker(context);
		Calendar calendar = Calendar.getInstance();
		tpicker.setIs24HourView(true);// 设置是否为24小时制
		tpicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		tpicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		tpicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub

			}

		});
		tpicker.showContextMenu();
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
	private void saveToAPP(int day) {
		Editor editor = sharedPreferences.edit();
		editor.putInt("before_day", day);
		editor.commit();
	}

}
