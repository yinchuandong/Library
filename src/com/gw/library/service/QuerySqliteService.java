package com.gw.library.service;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseService;
import com.gw.library.model.History;
import com.gw.library.model.Loan;
import com.gw.library.model.User;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.AppUtil;

public class QuerySqliteService extends BaseService {
	/**
	 * 每次创建Activity的时候，都访问数据库的Service,整个应用中均存在
	 */

	// 用户
	private User user = BaseAuth.getUser();
	// 数据和数据库
	public ArrayList<History> hList;
	public HistorySqlite hSqlite;
	private ArrayList<Loan> rList;
	private RemindSqlite rSqlite;

	// 定义Binder 对象
	private QuerySqliteBinder binder = new QuerySqliteBinder();

	// 服务状态
	private boolean quit;

	public boolean isQuit() {
		if (quit) {
			return true;
		} else {
			return false;
		}
	}

	public class QuerySqliteBinder extends Binder {
		public ArrayList<History> getHList() {
			return hList;
		}

		public ArrayList<Loan> getRList() {
			return rList;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		// TODO 更新信息
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO 读取最新的数据库信息
		getHistoryList();
		getLoanList();
		super.onStart(intent, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.print("Service is UnBinded");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		quit = true;// 标记服务退出
		super.onDestroy();
	}

	// SQlite中获取历史消息
	@SuppressWarnings("unchecked")
	public void getHistoryList() {
		ArrayList<HashMap<String, String>> mapList = hSqlite.query(
				"select * from history where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			hList = (ArrayList<History>) AppUtil.hashMapToModel(
					"com.gw.library.model.History", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// SQlite中获取借阅消息
	@SuppressWarnings("unchecked")
	public void getLoanList() {
		ArrayList<HashMap<String, String>> mapList = rSqlite.query(
				"select * from loan where studentNumber=?",
				new String[] { user.getStudentNumber() });
		try {
			rList = (ArrayList<Loan>) AppUtil.hashMapToModel(
					"com.gw.library.model.Loan", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 服务的接收器
	public class APPReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

		}

	}

}
