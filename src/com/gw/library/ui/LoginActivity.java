package com.gw.library.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.model.User;

public class LoginActivity extends BaseUi {
	
	EditText sNumberText;
	EditText pWordText;
	Button loginBtn;
	
	String studentNumber;
	String password;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);
		
		sNumberText = (EditText)findViewById(R.id.studentNumber);
		pWordText = (EditText)findViewById(R.id.password);
		loginBtn = (Button)findViewById(R.id.login);
		
	}
	
	public void bindLoginEvent(){
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				studentNumber = sNumberText.getText().toString();
				password = pWordText.getText().toString();
				
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", studentNumber);
				form.put("password", password);
				form.put("schoolId", "1");
				doTaskAsync(C.task.login,
					C.api.login,
					form
				);
			}
		});
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		try {
			User user = (User)message.getData("User");
			Log.i("loginactivity-->ontaskcomplete", user.getStudentNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
