package com.gw.library.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseDialog;
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

	BaseDialog baseDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);

		checkIsExistedCookie();
		sNumberText = (EditText) findViewById(R.id.studentNumber);
		pWordText = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);
		bindLoginEvent();
		Bundle bundle = new Bundle();
		bundle.putString("text", "正在登录。。。");
		baseDialog = new BaseDialog(this, bundle);
	}

	/**
	 * 绑定登陆框的click事件
	 */
	public void bindLoginEvent() {
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				baseDialog.show();
				
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
				BaseAuth.setLogin(true);
				BaseAuth.saveUserInfo(this, userInfo);
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
				baseDialog.close();
				toast(message.getInfo());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onNetworkError(int taskId){
		baseDialog.close();
	}
}
