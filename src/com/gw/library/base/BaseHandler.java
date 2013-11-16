package com.gw.library.base;

import com.gw.library.util.AppUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BaseHandler extends Handler {
	
	protected BaseUi ui = null;
	protected BaseFragment frag = null;
	
	public BaseHandler (BaseUi ui) {
		this.ui = ui;
	}
	
	public BaseHandler(BaseFragment fragment){
		this.frag = fragment;
	}
	
	public BaseHandler (Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
			switch (msg.what) {
				case BaseTask.TASK_COMPLETE:
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					if (result != null) {
						if(ui != null){
							ui.onTaskComplete(taskId, AppUtil.getMessage(result));
						}
						if(frag != null){
							frag.onTaskComplete(taskId, AppUtil.getMessage(result));
						}
					} else if (!AppUtil.isEmptyInt(taskId)) {
						if(ui != null){
							ui.onTaskComplete(taskId);
						}
						if (frag != null) {
							frag.onTaskComplete(taskId);
						}
					} else {
						if(ui != null){
							ui.onTaskComplete(taskId);
						}
						if (frag != null) {
							frag.onTaskComplete(taskId);
						}
						ui.toast(C.err.message);
					}
					break;
				case BaseTask.NETWORK_ERROR:
					taskId = msg.getData().getInt("task");
					if(ui != null){
						ui.onNetworkError(taskId);
					}
					if (frag != null) {
						frag.onNetworkError(taskId);
					}
					break;
				case BaseTask.SHOW_LOADBAR:
					break;
				case BaseTask.HIDE_LOADBAR:
					break;
				case BaseTask.SHOW_TOAST:
					result = msg.getData().getString("data");
					if(ui != null){
						ui.toast(result);
					}
					if (frag != null) {
						frag.toast(result);
					}
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(ui != null){
				ui.toast(e.getMessage());
			}
			if (frag != null) {
				frag.toast(e.getMessage());
			}
		}
	}
	
}