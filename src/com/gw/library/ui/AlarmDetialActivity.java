package com.gw.library.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.widget.GwListView;

public class AlarmDetialActivity extends BaseUi {
	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;
	Button setting;
	Button s_back;
	RadioButton recommendBtn;
	RadioButton remindBtn;
	RadioButton historyBtn;
	TextView mainTitle;
	TextView reNew;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		Bundle bundle = getIntent().getExtras();
		rList = (ArrayList<Loan>) bundle.getSerializable("rList");
		remindListAdapter = new RemindList(this, rList);
		listView = (GwListView) findViewById(R.id.remind_list);
		setting = (Button) this.findViewById(R.id.main_setting);
		reNew = (TextView) this.findViewById(R.id.renew);
		s_back = (Button) this.findViewById(R.id.s_back);

		listView.setAdapter(remindListAdapter);
		bindEvent();
	}

	private void bindEvent() {
		bindTabEvent();
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (BaseAuth.isLogin()) {
					Intent intent = new Intent(AlarmDetialActivity.this,
							SettingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("lastActivity", "AlarmDetialActivity");
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					DialogToLogin();
				}

			}
		});

		// 为每一个列表项添加动作事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast("你点击的是");
				Intent intent = new Intent(AlarmDetialActivity.this,
						HistoryWebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", rList.get(position - 1).getUrl());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	// 没有登录时候进行操作弹出对话警告
	private void DialogToLogin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("MY--REMIND--LIB");
		builder.setMessage("检测到当前您还没有登录，现在登录查看信息");
		builder.setPositiveButton("登录查看",
				new DialogInterface.OnClickListener() {// 设置确定按钮点击的事件Listener
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 登录
						if (BaseAuth.isLogin()) {
							forward(RemindActivity.class);
						} else {
							forward(LoginActivity.class);
						}

					}
				});
		builder.setNegativeButton("我知道了",
				new DialogInterface.OnClickListener() {// 设置取消按钮点击的事件Listener
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 取消
						if (BaseAuth.isLogin()) {
							forward(RemindActivity.class);
						} else {
							finish();
						}
					}
				});
		builder.create().show();
	}

	/**
	 * 绑定底部tab的事件
	 */
	public void bindTabEvent() {
		recommendBtn = (RadioButton) findViewById(R.id.tab_recommend);
		remindBtn = (RadioButton) findViewById(R.id.tab_remind);
		historyBtn = (RadioButton) findViewById(R.id.tab_history);

		mainTitle = (TextView) findViewById(R.id.main_title);

		if (recommendBtn == null || remindBtn == null || historyBtn == null) {
			Log.e("baseUiAuth--bindTabEvent", "false");
			return;
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tab_recommend:
					if (BaseAuth.isLogin()) {
						forward(RemindActivity.class);
					} else {
						DialogToLogin();
					}
					break;
				case R.id.tab_remind:
					if (BaseAuth.isLogin()) {
						forward(RemindActivity.class);
					} else {
						DialogToLogin();
					}
					break;
				case R.id.tab_history:
					if (BaseAuth.isLogin()) {
						forward(RemindActivity.class);
					} else {
						DialogToLogin();
					}
					break;
				}

			}
		};

		recommendBtn.setOnClickListener(onClickListener);
		historyBtn.setOnClickListener(onClickListener);
		remindBtn.setOnClickListener(onClickListener);

		// 设置main_tab 中 radiobutton 的 drawableTop的背景图片

		mainTitle.setText(R.string.remind);
		remindBtn.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.hover_remind, 0, 0);

	}

	public void onTaskComplete(int taskId, BaseMessage message) {
		switch (taskId) {
		case C.task.renew:
			baseDialog.setData(1, message.getInfo());
			baseDialog.show();
			break;
		}
	}

}
