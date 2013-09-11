package com.gw.library.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.util.AppUtil;

public class LoginActivity extends BaseUi {

	EditText sNumberText;
	EditText pWordText;
	Button loginBtn;

	String studentNumber;
	String password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 判断是否已经登录，跳转用户主页
		if (BaseAuth.isLogin()) {
			this.forward(HistoryActivity.class);
		}
		setContentView(R.layout.ui_login);

		// stuNumEditText = (EditText) this.findViewById(R.id.studentNumber);
		// pwdEditText = (EditText) this.findViewById(R.id.password);
		// settings = getPreferences(Context.MODE_PRIVATE);
		// mCheckBox = (CheckBox) this.findViewById(R.id.remember);
		// if (settings.getBoolean("remember", false)) {
		// // 设置checkbox的状态
		// stuNumEditText.setText(settings.getString("studentNumber", ""));
		// pwdEditText.setText(settings.getString("password", ""));
		// }
		// 为记住密码添加选中事件
		// mCheckBox
		// .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// // TODO Auto-generated method stub
		// SharedPreferences.Editor editor = settings.edit();
		// if (mCheckBox.isChecked()) {
		// editor.putBoolean("remember", true);
		// editor.putString("studentNumber", stuNumEditText
		// .getText().toString());
		// editor.putString("password", pwdEditText.getText()
		// .toString());
		// } else {
		// editor.putBoolean("remember", false);
		// editor.putString("studentNumber", "");
		// editor.putString("password", "");
		// }
		// editor.commit();
		// }
		// });
		// 为登录按钮添加事件
		// loginButton = (Button) this.findViewById(R.id.login);
		// loginButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// switch (v.getId()) {
		// case R.id.login:
		// // 联网进行登录
		// doTaskLogin();
		// break;
		// }
		//
		// }
		//
		// });
		// }

		// /**
		// * 登录
		// */
		// private void doTaskLogin() {
		// // 在输入不为空的情况下才进行登录
		// if (stuNumEditText.length() > 0 && pwdEditText.length() > 0) {
		// HashMap<String, String> urlParams = new HashMap<String, String>();
		// urlParams.put("studentNumber", stuNumEditText.getText().toString());
		// urlParams.put("password", pwdEditText.getText().toString());
		// urlParams.put("schoolId", "1");
		// try {
		// this.doTaskAsync(C.task.login, C.api.login, urlParams);
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// toast(e.getMessage());
		// }
		// } else {
		// toast(this.getString(R.string.login_empty));
		// }
		// }
		//
		// /****
		// * 登录回调
		// */
		//
		// @Override
		// public void onTaskComplete(int taskId, BaseMessage message) {
		// // TODO Auto-generated method stub
		// super.onTaskComplete(taskId, message);
		// switch (taskId) {
		// case C.task.login:
		// // 登录逻辑
		// User user = null;
		// try {
		// user = (User) message.getData("User");// 返回一个User对象
		// if (user.getUsername() != null) {
		// // 设置用户登录信息和状态
		// BaseAuth.setUser(user);
		// BaseAuth.setLogin(true);
		// } else {
		// BaseAuth.setUser(user);
		// BaseAuth.setLogin(false);
		// toast(this.getString(R.string.login_fail));
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// toast(e.getMessage());
		// }
		// // 切换到首页
		// if (BaseAuth.isLogin()) {
		// // 开启相应服务
		// // BaseService.start(this, null);
		// // 跳转用户主页
		// forward(LoginActivity.class);
		// }
		// break;
		//
		// default:
		// break;
		// }
		// }
		//
		// /**
		// * 网络错误回调
		// */
		// @Override
		// public void onNetworkError(int taskId) {
		// // TODO Auto-generated method stub
		// super.onNetworkError(taskId);
		// }

		// /**
		// * 按下BACK键的调用其他界面方法
		// */
		// @Override
		// public boolean onKeyDown(int keyCode, KeyEvent event) {
		// // TODO Auto-generated method stub
		// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		// {
		// doFinish();
		// }
		// return super.onKeyDown(keyCode, event);
		// =======

		// 检查以前是否有登陆过
		checkIsExistedCookie();
		sNumberText = (EditText) findViewById(R.id.studentNumber);
		pWordText = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);
		bindLoginEvent();
	}

	/**
	 * 绑定登陆框的click事件
	 */
	public void bindLoginEvent() {
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				studentNumber = sNumberText.getText().toString();
				password = pWordText.getText().toString();

				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", studentNumber);
				form.put("password", password);
				form.put("schoolId", "1");
				doTaskAsync(C.task.login, C.api.login, form);
			}
		});
	}

	/**
	 * 检查是否登陆过
	 */
	public void checkIsExistedCookie() {
		if (BaseAuth.isLogin()) {
			forward(HistoryActivity.class);
		} else {
			HashMap<String, String> userInfo = BaseAuth.getUserInfo(this);
			String spStudentNumber = userInfo.get("studentNumber");
			if (spStudentNumber != "" && !spStudentNumber.equals("")) {
				forward(HistoryActivity.class);
			}
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		try {
			if (message.getStatus().equals("1")) {
				JSONObject jsonObject = new JSONObject(message.getData());
				JSONObject userObject = jsonObject.getJSONObject("User");
				HashMap<String, String> userInfo = AppUtil
						.jsonObject2HashMap(userObject);
				// 因为密码在服务器上不保存，所以只能保存在本地
				userInfo.put("password", password);
				BaseAuth.setLogin(true);
				BaseAuth.saveUserInfo(this, userInfo);

				forward(HistoryActivity.class);
			} else {
				toast(message.getInfo());
			}
			// Log.i("loginactivity-->ontaskcomplete", message.getData());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
