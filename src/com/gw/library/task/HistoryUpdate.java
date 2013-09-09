package com.gw.library.task;

import android.util.Log;

import com.gw.library.base.BaseTask;

public class HistoryUpdate extends BaseTask {
	private static final int id = 0001;
	private static final String name = "HISTORY_UPDATE";
	private static final String taskUrl = "http://www.baidu.com";

	public HistoryUpdate() {
		super();
	}

	public static String getTaskurl() {
		return taskUrl;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void onStart() {
		// TODO 开始任务

	}

	@Override
	public void onComplete() {
		// TODO 更新完成后更新UI
		Log.v("msg", "no message!");
	}

	@Override
	public void onComplete(String httpResult) {
		// TODO 回调处理httpResult
		// 输出获取的远程信息
		// Log.v("msg", httpResult);
		// 数据解析

	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		Log.v("msg", error);
	}

}
