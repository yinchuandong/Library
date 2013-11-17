package com.gw.library.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gw.library.R;
import com.gw.library.base.BaseFragment;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseTask;
import com.gw.library.base.C;
import com.gw.library.fragment.HistoryUi.HSItemListener;
import com.gw.library.list.HistoryList;
import com.gw.library.list.RecommendList;
import com.gw.library.model.History;
import com.gw.library.model.Recommend;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.sqlite.RecommendSqlite;
import com.gw.library.ui.HistoryWebViewActivity;
import com.gw.library.util.AppUtil;
import com.gw.library.widget.GwListView;
import com.gw.library.widget.GwListView.OnLoadMoreListener;
import com.gw.library.widget.GwListView.OnLoadMoreViewState;
import com.gw.library.widget.GwListView.OnRefreshListener;

public class RecommendUi extends BaseFragment{

	private GwListView listView;
	private RecommendList rcListAdapter; // Listview 的adapter
	private ArrayList<Recommend> rcList; // 具体数据

	RecommendSqlite rcSqlite;
	double listRows = 10.0; //每页显示的数目
	public static boolean isLoaded = false; // 是否被加载的标志

	public static int loadMoreState = OnLoadMoreViewState.LMVS_FIRST;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ui_history, null);
		listView = (GwListView) view.findViewById(R.id.history_list);
		listView.updateLoadMoreViewState(loadMoreState);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		rcSqlite = new RecommendSqlite(getContext());
		initData();
		pullToRefresh();
	}
	
	
	/**
	 * 从数据库里面加载数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		ArrayList<HashMap<String, String>> mapList = rcSqlite.query(
				"select * from recommend where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			rcList = (ArrayList<Recommend>) AppUtil.hashMapToModel(
					"com.gw.library.model.Recommend", mapList);

			rcListAdapter = new RecommendList(getContext(), rcList);
			listView.setAdapter(rcListAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 从服务器加载数据，供外部调用
	 */
	public void loadDataFromServer(){
		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			HashMap<String, String> form = new HashMap<String, String>();
			form.put("studentNumber", user.getStudentNumber());
			form.put("password", user.getPassword());
			form.put("schoolId", user.getSchoolId());
			doTaskAsync(C.task.recommendList, C.api.recommendList, form);
		}
	}

	/**
	 * 下拉刷新
	 */
	private void pullToRefresh() {
		//下拉刷新
		listView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", user.getStudentNumber());
				form.put("password", user.getPassword());
				form.put("schoolId", user.getSchoolId());
				form.put("listRows", String.valueOf(listRows));
				doTaskAsync(C.task.recommendList, C.api.recommendList, form);
			}
		});
		

		
		//上拉加载更多
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				Log.i("recommendactivity-->bindevent","=====");
				int page = (int) Math.ceil(rcList.size() / (double)listRows);
				page++;
				Log.i("recommendactivity-->bindevent",page+"=====");
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", user.getStudentNumber());
				form.put("password", user.getPassword());
				form.put("schoolId", user.getSchoolId());
				form.put("p", String.valueOf(page));
				form.put("listRows", String.valueOf(listRows));
				doTaskAsync(C.task.recommendListPage, C.api.recommendList, form);
			}
		});
	}
	
	/**
	 * 异步线程完成之后的回调方法
	 */
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
				isLoaded = true;
				listView.onRefreshComplete();
				loadMoreState = OnLoadMoreViewState.LMVS_NORMAL;
				listView.updateLoadMoreViewState(loadMoreState);// 设置显示加载更多
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

		public RecommendHandler(BaseFragment frag) {
			super(frag);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg); //继承父类的handleMessage方法
			listView.onRefreshComplete();
			
			switch (msg.what) {
			case BaseTask.LOAD_IMAGE:
				debugMemory("image");
				rcListAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

}
