package com.gw.library.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.deamon.Deamon;
import com.gw.library.list.SchoolList;
import com.gw.library.model.School;
import com.gw.library.service.RemoteService;
import com.gw.library.util.AppUtil;
import com.gw.library.util.CharacterParser;
import com.gw.library.util.PollingUtils;
import com.gw.library.widget.SideBar;
import com.gw.library.widget.SideBar.OnTouchingLetterChangedListener;

public class LoginActivity extends BaseUi {

	EditText sNumberText;
	EditText pWordText;
	AutoCompleteTextView schoolText;
	Button loginBtn;
	RelativeLayout schoolListLayout;
	ListView schoolListView; //弹出的学校列表框
	SideBar sideBar; //listview的字母索引
	
	String studentNumber;
	String password;
	String schoolName;
	
	private CharacterParser characterParser;

	
	ArrayList<School> schoolList = new ArrayList<School>();// 学校的列表
	SchoolList schoolAdapter; // 学校列表的适配器

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);
		loginContext = getContext();
		characterParser = CharacterParser.getInstance();
		checkIsExistedCookie();

		sideBar = (SideBar)findViewById(R.id.sidebar);
		schoolText = (AutoCompleteTextView) findViewById(R.id.school);
		schoolListLayout = (RelativeLayout)findViewById(R.id.school_list_layout);
		schoolListView = (ListView)findViewById(R.id.school_list);
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
		
		//绑定学校框的focus事件
		schoolText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean isFocus) {
				Log.i("setOnFocusChangeListener", isFocus+"");
				if (isFocus) {
					if (schoolListLayout.getVisibility() == View.GONE) {
						schoolListLayout.setVisibility(View.VISIBLE);
					}else{
						schoolListLayout.setVisibility(View.GONE);
					}
				}
				
			}
		});
		//绑定学校框的click事件
		schoolText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (schoolListLayout.getVisibility() == View.GONE) {
					schoolListLayout.setVisibility(View.VISIBLE);
				}else{
					schoolListLayout.setVisibility(View.GONE);
				}
			}
		});
		
		//绑定弹出学校listvie的item事件
		schoolListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				School school = (School)schoolAdapter.getItem(position);
				schoolText.setText(school.getName());
				schoolListLayout.setVisibility(View.GONE);
			}
		});
		
		//字母索引框绑定事件
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = schoolAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					schoolListView.setSelection(position);
				}
			}
		});
		
		schoolText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}
	
	@SuppressLint("DefaultLocale")
	private ArrayList<School> sortList(ArrayList<String> list){
		ArrayList<School> mList = new ArrayList<School>();
		int len = list.size();
		for (int i = 0; i < len; i++) {
			School school = new School();
			school.setName(list.get(i));
			
			String pinyin = characterParser.getSelling(list.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			if (sortString.matches("[A-Z]")) {
				school.setSortLetters(sortString.toUpperCase());
			}else{
				school.setSortLetters("#");
			}
			mList.add(school);
		}
		
		Collections.sort(mList,new School());
		return mList;
	}

	private void filterData(String filterStr){
		ArrayList<School> list = new ArrayList<School>();
		if (TextUtils.isEmpty(filterStr)) {
			list = schoolList;
		}else{
			list.clear();
			for (School school : schoolList) {
				String name = school.getName();
				if (name.indexOf(filterStr) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					list.add(school);
				}
			}
		}
		Collections.sort(list, new School());
		schoolAdapter.updateData(list);
	}
	
	/**
	 * 检查是否登陆过
	 */
	public void checkIsExistedCookie() {
		if (BaseAuth.isLogin()) {
			startService();// 开启服务
			forward(RemindActivity.class);
		} else {
			HashMap<String, String> userInfo = BaseAuth.getUserInfo(this);
			String spStudentNumber = userInfo.get("studentNumber");
			if (spStudentNumber != "" && !spStudentNumber.equals("")) {
				BaseAuth.setLogin(true);
				BaseAuth.saveUserInfo(this, userInfo);
				startService();// 开启服务
				forward(RemindActivity.class);
			}
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		switch (taskId) {
		case C.task.login: // 登陆的任务
			try {
				if (message.getStatus().equals("1")) {
					baseDialog.close();
					JSONObject jsonObject = new JSONObject(message.getData());
					JSONObject userObject = jsonObject.getJSONObject("User");
					HashMap<String, String> userInfo = AppUtil
							.jsonObject2HashMap(userObject);
					// 因为密码在服务器上不保存，所以只能保存在本地
					userInfo.put("password", password);
					BaseAuth.setLogin(true);
					BaseAuth.saveUserInfo(this, userInfo);
					startService();// 开启服务
					forward(RemindActivity.class);

					Deamon.setJNIEnv();
					Deamon.mainThread();
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
				ArrayList<String> tempList = new ArrayList<String>();
				for (HashMap<String, String> hashMap : sTempList) {
					tempList.add(hashMap.get("schoolName"));
				}
				schoolList = sortList(tempList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			schoolAdapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_dropdown_item_1line, schoolList);
//			schoolText.setAdapter(schoolAdapter);
//			schoolAdapter = new ArrayAdapter<String>(this,
//					R.layout.tpl_school_item, schoolList);
			schoolAdapter = new SchoolList(getContext(), schoolList);
			schoolListView.setAdapter(schoolAdapter);
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

	private static Context loginContext;
	public static Context getLoginContext() {
		return loginContext;
	}
}
