package com.gw.library.ui;

import java.util.ArrayList;

import android.os.Bundle;

import com.gw.library.R;
import com.gw.library.base.BaseUi;
import com.gw.library.base.GwListView;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.sqlite.RemindSqlite;

public class AlarmDetialActivity extends BaseUi {

	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_alarmdetial);
		Bundle bundle = getIntent().getExtras();
		rList = (ArrayList<Loan>) bundle.get("rList");

		listView = (GwListView) findViewById(R.id.remind_list);
		remindListAdapter = new RemindList(this, rList);
		listView.setAdapter(remindListAdapter);
	}
}
