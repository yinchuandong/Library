package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.service.RemoteService;
import com.gw.library.util.AppUtil;
import com.gw.library.util.PollingUtils;

public class LoginActivity extends BaseUi {

	EditText sNumberText;
	EditText pWordText;
	AutoCompleteTextView schoolText;
	Button loginBtn;

	String studentNumber;
	String password;

	String schoolName;

	ArrayList<String> schoolList = new ArrayList<String>();// 学校的列表
	ArrayAdapter<String> schoolAdapter; // 学校列表的适配器

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);

		checkIsExistedCookie();

		schoolText = (AutoCompleteTextView) findViewById(R.id.school);

		sNumberText = (EditText) findViewById(R.id.studentNumber);
		pWordText = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);
		bindLoginEvent();
		doTaskAsync(C.task.schoolList, C.api.schoolList);
	}

	/**
	 * 绑定登陆框的click事件
	 */
	public void bindLoginEvent() {
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				baseDialog.setData(0, null);
				baseDialog.show();

				studentNumber = sNumberText.getText().toString();
				password = pWordText.getText().toString();
				schoolName = schoolText.getText().toString();

				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", studentNumber);
				form.put("password", password);
				form.put("schoolName", schoolName);
				doTaskAsync(C.task.login, C.api.login, form);
			}
		});
	}

	/**
	 * 检查是否登陆过
	 */
	public void checkIsExistedCookie() {
		if (BaseAuth.isLogin()) {
			startService();// 开启服务
			forward(HistoryActivity.class);
		} else {
			HashMap<String, String> userInfo = BaseAuth.getUserInfo(this);
			String spStudentNumber = userInfo.get("studentNumber");
			if (spStudentNumber != "" && !spStudentNumber.equals("")) {
				BaseAuth.setLogin(true);
				BaseAuth.saveUserInfo(this, userInfo);
				startService();// 开启服务
				forward(HistoryActivity.class);
			}
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		switch (taskId) {
		case C.task.login: // 登陆的任务
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
					startService();// 开启服务
					forward(HistoryActivity.class);

				} else {
					baseDialog.setData(1, message.getInfo());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case C.task.schoolList: // 获得学校的列表
			try {
				JSONObject jsonObject = new JSONObject(message.getData());
				JSONArray jsonArray = jsonObject.getJSONArray("School");
				ArrayList<HashMap<String, String>> sTempList = AppUtil
						.jsonArray2ArrayList(jsonArray);
				for (HashMap<String, String> hashMap : sTempList) {
					schoolList.add(hashMap.get("schoolName"));
				}
			} catch (JSONException e) {
				e.printStackTrace();

			}
			schoolAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, schoolList);
			schoolText.setAdapter(schoolAdapter);
			break;
		}

	}

	@Override
	public void onNetworkError(int taskId) {
		baseDialog.close();
	}

	// 开启应用服务
	public void startService() {
		// TODO Auto-generated method stub
		PollingUtils.startPollingService(LoginActivity.this, C.time.pollTime,
				RemoteService.class, C.action.remoteAction);

	}
}
