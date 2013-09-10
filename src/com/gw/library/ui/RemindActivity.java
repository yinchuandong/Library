package com.gw.library.ui;

import android.os.Bundle;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;

public class RemindActivity extends BaseUiAuth{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		init();
	}
	
	
	
	public void init(){
		doTaskAsync(1, C.api.historyList + 
				"?studentNumber=20111003632&password=yin543211&school_id=1"
		);
	}
	
	public void onTaskComplete(int taskId, BaseMessage message) {
		Log.i("remindactivity====ontaskcomplete", taskId+"");
	}

}
