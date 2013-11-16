package com.gw.library.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gw.library.R;
import com.gw.library.base.BaseFragment;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.C;
import com.gw.library.list.HistoryList;
import com.gw.library.model.History;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.ui.HistoryActivity;
import com.gw.library.ui.HistoryWebViewActivity;
import com.gw.library.ui.HistoryActivity.HistoryReceiver;
import com.gw.library.util.AppUtil;
import com.gw.library.widget.GwListView;
import com.gw.library.widget.GwListView.OnLoadMoreListener;
import com.gw.library.widget.GwListView.OnLoadMoreViewState;
import com.gw.library.widget.GwListView.OnRefreshListener;

public class HistoryUi extends BaseFragment{
	
	private GwListView listView;
	private HistoryList hListAdapter; // Listview 的adapter
	private ArrayList<History> hList; // 具体数据

	HistorySqlite hSqlite;
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
		hSqlite = new HistorySqlite(getContext());
		initData();
		pullToRefresh();
		
		listView.setOnItemClickListener(new HSItemListener());
		
		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			doTaskAsync(
					C.task.historyList,
					C.api.historyList + "?studentNumber="
							+ user.getStudentNumber() + "&password="
							+ user.getPassword() + "&schoolId="
							+ user.getSchoolId());
		}
	}
	
	/**
	 * 从数据库里面加载数据
	 */
	@SuppressWarnings("unchecked")
	public void initData() {
		ArrayList<HashMap<String, String>> mapList = hSqlite.query(
				"select * from history where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			hList = (ArrayList<History>) AppUtil.hashMapToModel(
					"com.gw.library.model.History", mapList);

			hListAdapter = new HistoryList(getContext(), hList);
			listView.setAdapter(hListAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 异步线程完成之后的回调方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		Log.i("remindactivity====ontaskcomplete", taskId + "");
		switch (taskId) {
		case C.task.historyList:// 第一次加载或者下拉刷新
			try {
				String whereSql = History.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				hSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				hList = (ArrayList<History>) message.getDataList("History");
				for (History history : hList) {
					hSqlite.updateHistory(history);
//					Log.i("studentNumber", history.getStudentNumber());
					debugMemory(history.getStudentNumber());
				}
				hListAdapter.setData(hList); // 必须调用这个方法来改变data，否者刷新无效
				hListAdapter.notifyDataSetChanged();

			} catch (Exception e) {// 没有借阅历史的情况
				e.printStackTrace();
				debugMemory("is false====");
			} finally {
				debugMemory("is sucess====");
				isLoaded = true; // 加载完成的标志设为true
				listView.onRefreshComplete(); // 刷新完成
				loadMoreState = OnLoadMoreViewState.LMVS_NORMAL;
				listView.updateLoadMoreViewState(loadMoreState);// 设置显示加载更多

				//更新历史列表的isbn
				doTaskAsync(C.task.updateIsbn, C.api.updateIsbn
						+ "?studentNumber=" + user.getStudentNumber()
						+ "&schoolId=" + user.getSchoolId());

			}

			break;
		case C.task.historyListPage:// 翻页
			try {
				ArrayList<History> tempList = (ArrayList<History>) message
						.getDataList("History");
				for (History history : tempList) {
					String whereSql = History.COL_ID + "=?";
					String[] whereParams = new String[] { history.getId() };
					if (!hSqlite.exists(whereSql, whereParams)) {
						hList.add(history);
						hSqlite.updateHistory(history);
					}
					Log.i("studentNumber", history.getStudentNumber());
				}
				hListAdapter.setData(hList);
				hListAdapter.notifyDataSetChanged();
//				listView.setSelection(hList.size());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				listView.onLoadMoreComplete();
			}
			break;

		}

	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {

				doTaskAsync(C.task.historyList, C.api.historyList
						+ "?studentNumber=" + user.getStudentNumber()
						+ "&password=" + user.getPassword() 
						+ "&schoolId=" + user.getSchoolId()
						+ "&listRows=" + listRows
						);

			}
		});

		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				int page = (int) Math.ceil(hList.size() / (double)listRows);
				page++;
				doTaskAsync(C.task.historyListPage, C.api.historyList
						+ "?studentNumber=" + user.getStudentNumber()
						+ "&password=" + user.getPassword() 
						+ "&schoolId=" + user.getSchoolId() 
						+ "&p=" + page
						+ "&listRows=" + listRows
						);

			}
		});
	}

	/**
	 * History列表item被点击后的动作事件，逐项显示
	 */
	class HSItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent(getContext(),
					HistoryWebViewActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", hList.get(position - 1).getUrl());
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}
	
	
}
