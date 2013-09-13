package com.gw.library.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gw.library.R;
import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.util.AppUtil;

public class SettingActivity extends BaseUiAuth{

	Button logoutBtn;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_settings);
		
		logoutBtn = (Button)findViewById(R.id.logout);
		bindEvent();
	}
	
	private void bindEvent(){
		//绑定退出事件
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseAuth.setLogin(false);
				BaseAuth.clearUserInfo(getContext());
				forward(LoginActivity.class);
			}
		});
	}
	
	
	
	
	
	
	
	
	
}
