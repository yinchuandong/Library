package com.gw.library.service;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseModel;
import com.gw.library.base.BaseService;
import com.gw.library.base.C;
import com.gw.library.model.History;
import com.gw.library.model.Loan;
import com.gw.library.model.User;
import com.gw.library.sqlite.HistorySqlite;
import com.gw.library.sqlite.RemindSqlite;
import com.gw.library.util.PollingUtils;

public class RemoteService extends BaseService {
	/**
	 * 后台网络刷新服务，定时轮询刷新数据,保存到数据库
	 */

	// 用户
	private User user = BaseAuth.getUser();
	// 数据和数据库
	public ArrayList<History> hList;

	ArrayList<Loan> rList;
	RemindSqlite rSqlite = new RemindSqlite(this);
	HistorySqlite hSqlite = new HistorySqlite(this);
	boolean updateFlag = false;// 标记更新情况
	boolean alarmFlag = false;// 标记Alarm开启情况
	// 更新的条目
	private int numb;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("Remote", "--------------->>>创建RemoteService成功！");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		update();
		Log.v("login Statue", "登录状态-----------------》" + BaseAuth.isLogin());

		if (updateFlag == true && alarmFlag == false && BaseAuth.isLogin()) {
			PollingUtils.startAlarmService(RemoteService.this,
					AlarmNotifyService.class, C.action.alarmAction);
			alarmFlag = true;// 仅仅创建 一次
			Log.v("alarm", "--------------->>>尝试创建AlarmService成功！");
		}
		Log.v("alarm", "--------------->>>开启RemoteService成功！");
		return START_REDELIVER_INTENT;
	}

	// 更新
	public void update() {
		getLoanList();
		getHistoryList();
//		calculateRecommend();
		
		updateFlag = true;
		Log.v("alarm", "--------------->>>更新成功！alarmFlag为" + alarmFlag);
	}

	// 远程获取历史消息
	private void getHistoryList() {
		doTaskAsync(C.task.historyList, 
				C.api.historyList 
				+ "?studentNumber="	+ user.getStudentNumber() 
				+ "&password=" + user.getPassword()
				+ "&schoolId=" + user.getSchoolId());
	}

	// 远程获取借阅消息
	private void getLoanList() {
		doTaskAsync(
				C.task.loanList,
				C.api.loanList 
				+ "?studentNumber=" + user.getStudentNumber()
				+ "&password=" + user.getPassword() 
				+ "&schoolId=" + user.getSchoolId()
			);
	}
	
	private void calculateRecommend(){
		doTaskAsync(
				C.task.calculateRecommend, 
				C.api.calculateRecommend
				+ "?studentNumber=" + user.getStudentNumber()
				+ "&schoolId=" + user.getSchoolId() 
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// TODO Change to Model
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.historyList:

			try {
				String whereSql = History.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				hSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				hList = (ArrayList<History>) message.getDataList("History");
				if (hList.size() > 0) {
					for (History history : hList) {
						hSqlite.updateHistory(history);
						Log.i("studentNumber", history.getStudentNumber());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			break;
		case C.task.loanList:
			Log.i("remindactivity====ontaskcomplete", taskId + "");
			try {
				String whereSql = Loan.COL_STUDENTNUMBER + "=?";
				String[] whereParams = new String[] { user.getStudentNumber() };
				// 判断是否有更新
				ArrayList<HashMap<String, String>> mapList = rSqlite.query(
						"select * from loan where studentNumber=?",
						new String[] { user.getStudentNumber() });
				rSqlite.delete(whereSql, whereParams); // 清空当前历史列表
				rList = (ArrayList<Loan>) message.getDataList("Loan");
				if (mapList.size() > rList.size()) {
					numb = mapList.size() - rList.size();
					sendIntent(rList);
				}
				if (rList.size() > 0) {
					for (Loan loan : rList) {
						rSqlite.updateloan(loan);
						Log.i("studentNumber", loan.getStudentNumber());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.v("Loan Eception", "Loan中没有需要提醒的书本" + e.getMessage());
			}

			break;
		default:
			break;
		}
	}

	// 有更新信息，发送消息(用于以后)
	private void sendIntent(ArrayList<? extends BaseModel> list) {
		// 发送消息，通知本地的Service数据库已经修改
		Intent sendIntent = new Intent();

		if (list.get(0).getClass().getName().equals("History")) {
			sendIntent.setAction(C.action.historyAction);
			sendIntent.putExtra("numb", numb);
		} else if (list.get(0).getClass().getName().equals("Loan")) {
			sendIntent.setAction(C.action.remindAction);
			sendIntent.putExtra("numb", numb);
		}
		sendBroadcast(sendIntent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent localIntent = new Intent();

		localIntent.setClass(this, RemoteService.class); // 销毁时重新启动当前的Service
		this.startService(localIntent);

	}

}
