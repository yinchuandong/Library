package com.gw.library.base;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.gw.library.base.BaseTask;
import com.gw.library.util.AppCache;
import com.gw.library.util.AppUtil;

public class BaseUi extends Activity {

	protected BaseApp app;
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// debug memory
		debugMemory("onCreate");
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
		// init application
		// this.app = (BaseApp) this.getApplicationContext();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// debug memory
		debugMemory("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// debug memory
		debugMemory("onPause");
	}

	@Override
	public void onStart() {
		super.onStart();
		// debug memory
		debugMemory("onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// debug memory
		debugMemory("onStop");
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// util method

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 不结束当前活动
	 * activity间的切换
	 * @param classObj
	 */
	public void overlay(Class<?> classObj) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		startActivity(intent);
		overridePendingTransition(0, 0); // 去掉activity切换时的动画效果
	}

	/**
	 * 不结束当前活动
	 * activity间的切换, 带参数
	 * @param classObj
	 * @param params
	 */
	public void overlay(Class<?> classObj, Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		intent.putExtras(params);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	/**
	 * 不结束当前活动
	 * activity间的切换 带动画
	 * @param classObj
	 * @param enterAnim
	 * @param exitAnim
	 */
	public void overlay(Class<?> classObj, int enterAnim, int exitAnim) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		startActivity(intent);
		overridePendingTransition(0, 0); // 去掉activity切换时的动画效果
	}

	/**
	 * 不结束当前活动
	 * activity间的切换 带参数 带动画
	 * @param classObj
	 * @param params
	 * @param enterAnim
	 * @param exitAnim
	 */
	public void overlay(Class<?> classObj, Bundle params, int enterAnim, int exitAnim) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		intent.putExtras(params);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	/**
	 * 原始的切换
	 * @param classObj
	 */
	public void forward(Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
		overridePendingTransition(0, 0);
	}
	
	/**
	 * activity 之间带动画的切换
	 * @param classObj
	 * @param enterAnim
	 * @param exitAnim
	 */
	public void forward(Class<?> classObj, int enterAnim, int exitAnim) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
		overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 带参数的切换
	 * @param classObj
	 * @param params
	 */
	public void forward(Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
		overridePendingTransition(0, 0);
	}
	
	/**
	 * 带动画而且带参数的切换 
	 * @param classObj
	 * @param params
	 * @param enterAnim
	 * @param exitAnim
	 */
	public void forward(Class<?> classObj, Bundle params, int enterAnim, int exitAnim) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
		overridePendingTransition(enterAnim, enterAnim);
	}

	public Context getContext() {
		return this;
	}

	public BaseHandler getHandler() {
		return this.handler;
	}

	public void setHandler(BaseHandler handler) {
		this.handler = handler;
	}

	public LayoutInflater getLayout() {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getLayout(int layoutId) {
		return getLayout().inflate(layoutId, null);
	}

	public View getLayout(int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}

	public BaseTaskPool getTaskPool() {
		return this.taskPool;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// logic method

	public void doFinish() {
		this.finish();
	}

	public void doLogout() {

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

	public void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	public void sendMessage(int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}

	public void sendMessage(int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
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

	public void doTaskAsync(int taskId, BaseTask baseTask, int delayTime) {
		taskPool.addTask(taskId, baseTask, delayTime);
	}

	public void doTaskAsync(int taskId, String taskUrl) {
		taskPool.addTask(taskId, taskUrl, new BaseTask() {
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

	public void doTaskAsync(int taskId, String taskUrl,
			HashMap<String, String> taskArgs) {
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

	// 任务回调方法，需要override
	public void onTaskComplete(int taskId, BaseMessage message) {

	}

	public void onTaskComplete(int taskId) {

	}

	// 网络异常
	public void onNetworkError(int taskId) {
		toast(C.err.network);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// debug method

	public void debugMemory(String tag) {
		if (this.showDebugMsg) {
			Log.w(this.getClass().getSimpleName(),
					tag + ":" + AppUtil.getUsedMemory());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// common classes

	public class BitmapViewBinder implements ViewBinder {
		//
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}
}