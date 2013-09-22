package com.gw.library.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;

import com.gw.library.R;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseTask;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.RecommendList;
import com.gw.library.model.Recommend;
import com.gw.library.sqlite.RecommendSqlite;

public class RecommendActivity extends BaseUiAuth {

	GwListView listView;
	ArrayList<Recommend> rList;
	RecommendList rListAdapter;
	RecommendSqlite rSqlite;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_recommend);
		
		listView = (GwListView)findViewById(R.id.recommend_list);
	}
	
	
	public void bindEvent(){
		listView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
			}
		});
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		
	}
	
	@Override
	public void onNetworkError(int taskId){
		
	}

	/**
	 * 异步下载网络图片的回调方法
	 * 
	 * @author yinchuandong
	 * 
	 */
	private class RecommendHandler extends BaseHandler {

		public RecommendHandler(BaseUi ui) {
			super(ui);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case BaseTask.LOAD_IMAGE:
				break;

			default:
				break;
			}
		}

	}
}
