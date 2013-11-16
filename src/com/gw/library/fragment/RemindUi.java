package com.gw.library.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gw.library.R;
import com.gw.library.base.BaseFragment;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.C;
import com.gw.library.list.RemindList;
import com.gw.library.model.Loan;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.AppUtil;
import com.gw.library.widget.GwListView;
import com.gw.library.widget.GwListView.OnLoadMoreListener;
import com.gw.library.widget.GwListView.OnLoadMoreViewState;
import com.gw.library.widget.GwListView.OnRefreshListener;

public class RemindUi extends BaseFragment{
	
	GwListView listView;
	RemindList remindListAdapter;
	RemindSqlite rSqlite;
	ArrayList<Loan> rList;
	int listRows = 10; //每页显示的数目
	private static boolean isLoaded = false; // 是否被加载的标志
	private static int loadMoreState = OnLoadMoreViewState.LMVS_FIRST;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ui_remind, null);
		listView = (GwListView) view.findViewById(R.id.remind_list);
		listView.updateLoadMoreViewState(loadMoreState);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		rSqlite = new RemindSqlite(getContext());
		initData(); // 初始化数据
		pullToRefresh(); // 绑定下拉刷新的事件
		
		if (!isLoaded) {// 如果是第一次进入页面，则开始从服务器上获取数据
			listView.displayHeader();
			doTaskAsync(
					C.task.loanList,
					C.api.loanList + "?studentNumber="
							+ user.getStudentNumber() + "&password="
							+ user.getPassword() + "&schoolId="
							+ user.getSchoolId() + "&listRows=" + listRows);
		}
	}
	
	/**
	 * 从数据库里面加载数据
	 */
	@SuppressWarnings("unchecked")
	public void initData() {
		ArrayList<HashMap<String, String>> mapList = rSqlite.query(
				"select * from loan where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			rList = (ArrayList<Loan>) AppUtil.hashMapToModel(
					"com.gw.library.model.Loan", mapList);

			remindListAdapter = new RemindList(getContext(),this, rList);
			listView.setAdapter(remindListAdapter);
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
		switch (taskId) {

		case C.task.loanList:
			try {
				String whereSql = Loan.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				rSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				rList = (ArrayList<Loan>) message.getDataList("Loan");
				for (Loan loan : rList) {
					rSqlite.updateloan(loan);
					Log.i("studentNumber", loan.getStudentNumber());
				}
				remindListAdapter.setData(rList); // 必须调用这个方法来改变data，否者刷新无效
				remindListAdapter.notifyDataSetChanged();

			} catch (Exception e) {// 没有需要提醒的情况
				e.printStackTrace();

			} finally {
				isLoaded = true;// 加载完成的标志设为true
				listView.onRefreshComplete(); // 刷新完成
				loadMoreState = OnLoadMoreViewState.LMVS_NORMAL;
				listView.updateLoadMoreViewState(loadMoreState);// 设置显示加载更多
				baseDialog.close();
			}
			break;

		case C.task.renew:
			baseDialog.setData(1, message.getInfo());
			baseDialog.show();
			break;
		}
	}

	@Override
	public void onNetworkError(int taskId) {
		toast(C.err.network);
	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				doTaskAsync(
						C.task.loanList,
						C.api.loanList + "?studentNumber="
								+ user.getStudentNumber() + "&password="
								+ user.getPassword() + "&schoolId="
								+ user.getSchoolId() + "&listRows=" + listRows);
			}
		});
		// 加载更多
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				int page = (int) Math.ceil(rList.size() / listRows);
				page++;
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("studentNumber", user.getStudentNumber());
				form.put("password", user.getPassword());
				form.put("schoolId", user.getSchoolId());
				form.put("p", String.valueOf(page));
				form.put("listRows", String.valueOf(listRows));
				doTaskAsync(C.task.loanList, C.api.loanList, form);
			}

		});

	}

}
