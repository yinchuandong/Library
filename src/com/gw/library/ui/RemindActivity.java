package com.gw.library.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseModel;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.model.History;

public class RemindActivity extends BaseUiAuth{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remind);
		init();
	}
	
	
	
	public void init(){
		doTaskAsync(1, C.api.loanList + 
				"?studentNumber=20111003632&password=yin543211&schoolId=1"
		);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		Log.i("remindactivity====ontaskcomplete", taskId+"");
		try {
			ArrayList<History> hList = (ArrayList<History>)message.getDataList("History");
			Log.i("studentNumber", hList.get(0).getStudentNumber());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
