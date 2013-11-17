package com.gw.library.base;

import java.util.HashMap;

import com.gw.library.model.User;
import com.gw.library.ui.HomeActivity;
import com.gw.library.ui.LoginActivity;
import com.gw.library.util.AppCache;
import com.gw.library.util.AppUtil;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class BaseFragment extends Fragment{

	protected BaseTaskPool taskPool;
	protected BaseHandler handler;
	protected boolean showDebugMsg = true;
	public BaseDialog baseDialog; // 基类对话框
	public User user;
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if (!BaseAuth.isLogin()) {
			forward(LoginActivity.class);
		}else{
			user = BaseAuth.getUser();
		}
		handler = new BaseHandler(this);
		taskPool = new BaseTaskPool(getContext());
		baseDialog = new BaseDialog(getActivity());
		
	}
	
	/**
	 * 得到活动的context对象
	 * @return
	 */
	protected Context getContext(){
		return getActivity().getApplicationContext();
	}
	
	protected void setHandler(BaseHandler handler){
		this.handler = handler;
	}
	
	/**
	 * 原始的切换
	 * @param classObj
	 */
	public void forward(Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(getContext(), classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		getActivity().finish();
	}
	
	/**
	 * 给handler发送message
	 * @param what
	 */
	public void sendMessage(int what){
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	/**
	 * 给handler发送message
	 * @param what
	 * @param data
	 */
	public void sendMessage(int what, String data){
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		Message msg = new Message();
		msg.what = what;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	/**
	 * 给handler发送message
	 * @param what
	 * @param taskId
	 * @param data
	 */
	public void sendMessage(int what, int taskId, String data){
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		bundle.putInt("task", taskId);
		Message msg = new Message();
		msg.what = what;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	/**
	 * 异步加载图片
	 * @param url
	 */
	public void loadImage (final String url) {
		taskPool.addTask(0, new BaseTask(){
			@Override
			public void onComplete(){
				AppCache.getCachedImage(getContext(), url);
				sendMessage(BaseTask.LOAD_IMAGE);
			}
		}, 0);
	}
	
	/**
	 * 执行网络异步任务，get方法
	 * @param taskId
	 * @param taskUrl
	 */
	public void doTaskAsync(int taskId, String taskUrl){
		taskPool.addTask(taskId, taskUrl, new BaseTask(){
			@Override
			public void onComplete(String httpResult) {
				debugMemory("1");
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), error);
			}
		}, 0);
	}
	
	/**
	 * 执行网络异步任务，post方法
	 * @param taskId
	 * @param taskUrl
	 * @param taskArgs
	 */
	public void doTaskAsync(int taskId, String taskUrl,	HashMap<String, String> taskArgs) {
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask() {
			@Override
			public void onComplete(String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
	}
	
	public void doTaskAsync(int taskId, BaseTask baseTask, int delayTime) {
		taskPool.addTask(taskId, baseTask, delayTime);
	}
	
	public void doTaskAsync(int taskId, int delayTime) {
		taskPool.addTask(taskId, new BaseTask() {
			@Override
			public void onComplete() {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, delayTime);
	}
	
	/**
	 * 异步任务完成时的回调方法
	 * @param taskId
	 * @param message
	 */
	public void onTaskComplete(int taskId, BaseMessage message){
		
	}
	
	/**
	 * 异步任务完成时的回调方法
	 * @param taskId
	 */
	public void onTaskComplete(int taskId){
		
	}
	
	/**
	 * 执行异步任务，网络错误时的回调方法
	 * @param taskId
	 */
	public void onNetworkError(int taskId){
		
	}
	
	/**
	 * 从服务器加载数据，供外部调用
	 */
	public void loadDataFromServer(){
		
	}
	
	public void debugMemory(String tag) {
		if (this.showDebugMsg) {
			Log.w(this.getClass().getSimpleName(),
					tag + ":" + AppUtil.getUsedMemory());
		}
	}
	
	public void toast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	
	@SuppressLint("HandlerLeak")
	public class FragmentHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg){
			try {
				int taskId;
				String result;
				switch (msg.what) {
				//正常完成
				case BaseTask.TASK_COMPLETE:
					Bundle bundle = msg.getData();
					taskId = bundle.getInt("task");
					result = bundle.getString("data");
					if (result != null) {
						BaseMessage message = AppUtil.getMessage(result);
						onTaskComplete(taskId, message);
					}else if(!AppUtil.isEmptyInt(taskId)){
						onTaskComplete(taskId);
					}else{
						toast(C.err.message);
					}
					break;
				//网络错误时
				case BaseTask.NETWORK_ERROR:
					taskId = msg.getData().getInt("task");
					onNetworkError(taskId);
					break;
				case BaseTask.SHOW_TOAST:
					result = msg.getData().getString("data");
					toast(result);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				toast(e.getMessage());
			}
		}
	}
}
