package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.widget.ListView;

import com.gw.library.R;
import com.gw.library.base.BaseUi;

public class HistoryActivity extends BaseUi {

	private ListView historyListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		historyListView = (ListView) this.findViewById(R.layout.ui_history);

		// 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("book_title", "book_title");
			map.put("book_author", "book_author");
			map.put("book_return_time", "book_return_time");
			list.add(map);
		}
		// SimpleAdapter historyAdapter=new SimpleAdapter(this, list,
		// R.layout.tpl_historyitem, new String[] { "book_title",
		// "book_author","book_return_time" , new
		// int[]{R.id.book_title,R.id.book_author,R.id.book_return_time});

		// 添加并且显示
		// historyListView.setAdapter(historyAdapter);
	}
}
