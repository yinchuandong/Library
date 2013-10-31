package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.gw.library.R;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseTask;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnLoadMoreListener;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.list.RecommendList;
import com.gw.library.model.Recommend;
import com.gw.library.sqlite.RecommendSqlite;
import com.gw.library.util.AppUtil;

public class RecommendActivity extends BaseUiAuth {

	GwListView listView;
	ArrayList<Recommend> rcList;
	RecommendList rcListAdapter;
	RecommendSqlite rcSqlite;
	
	int listRows = 4; //每页显示的数目
	static boolean isLoaded = false; //该activity是否是第一次被加载
	static int preLoadedPage = 0; //上一次加载的页码
	int page;//现在的页码
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_recommend);
		
		this.setHandler(new RecommendHandler(this));//设置handler
		
		rcList = new ArrayList<Recommend>(); //实例化暂存list
		rcSqlite = new RecommendSqlite(this); //实例化数据库
		
		listView = (GwListView)findViewById(R.id.recommend_list);
		initData();
		bindEvent();
		
		//如果没有被加载，则第一次进入界面时就加载
		if (!isLoaded) {
			listView.displayHeader();
			HashMap<String, String> form = new HashMap<String, String>();
			form.put("studentNumber", user.getStudentNumber());
			form.put("password", user.getPassword());
			form.put("schoolId", user.getSchoolId());
			doTaskAsync(C.task.recommendList, C.api.recommendList, form);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initData(){
		ArrayList<HashMap<String, String>> mapList = rcSqlite.query(
				"select * from recommend where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			rcList = (ArrayList<Recommend>) AppUtil.hashMapToModel(
					"com.gw.library.model.Recommend", mapList);
			
			rcListAdapter = new RecommendList(this, rcList);
			listView.setAdapter(rcListAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void bindEvent(){
		//下拉刷新
		listView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", user.getStudentNumber());
				form.put("password", user.getPassword());
				form.put("schoolId", user.getSchoolId());
				doTaskAsync(C.task.recommendList, C.api.recommendList, form);
			}
		});
		
		//上拉加载更多
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				page = (int)Math.ceil(rcList.size()/listRows);
				preLoadedPage = page;
				page++;
				
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", user.getStudentNumber());
				form.put("password", user.getPassword());
				form.put("schoolId", user.getSchoolId());
				form.put("p", String.valueOf(page));
//				form.put("listRows", String.valueOf(listRows));
				doTaskAsync(C.task.recommendListPage, C.api.recommendList, form);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		switch (taskId) {
		//下拉刷新加载
		case C.task.recommendList:
			try {
				String whereSql = Recommend.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				rcSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				rcList = (ArrayList<Recommend>) message.getDataList("Recommend");
				for (Recommend recommend : rcList) {
					loadImage(recommend.getCover());
					rcSqlite.updateRecommend(recommend);
					Log.i("studentNumber", recommend.getTitle());
				}
				rcListAdapter.setData(rcList); // 必须调用这个方法来改变data，否者刷新无效
				rcListAdapter.notifyDataSetChanged();
				
			} catch (Exception e) {
				e.printStackTrace();
				toast(e.getMessage());
			}finally{
				listView.onRefreshComplete();
			}
			break;
			
		//数据分页加载
		case C.task.recommendListPage:
			try {
				ArrayList<Recommend> tempList = (ArrayList<Recommend>) message.getDataList("Recommend");
				for (Recommend recommend : tempList) {
					String whereSql = Recommend.COL_ID + "=?";
					String[] whereParams = new String[] { recommend.getId() };
					if (!rcSqlite.exists(whereSql, whereParams)) {
						loadImage(recommend.getCover());
						rcSqlite.updateRecommend(recommend);
						rcList.add(recommend);
					}
				}
				rcListAdapter.setData(rcList); // 必须调用这个方法来改变data，否者刷新无效
				rcListAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(page == preLoadedPage){
					toast("数据已经全部加载完成");
				}
				listView.onLoadMoreComplete();
			}
			break;
		}
		
	}
	
	@Override
	public void onNetworkError(int taskId){
		listView.onRefreshComplete();
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
			super.handleMessage(msg); //继承父类的handleMessage方法
			listView.onRefreshComplete();
			
			switch (msg.what) {
			case BaseTask.LOAD_IMAGE:
				rcListAdapter.notifyDataSetChanged();
				break;
			}
		}

	}
}
