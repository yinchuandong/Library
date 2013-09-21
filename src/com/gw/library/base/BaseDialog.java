package com.gw.library.base;

import com.gw.library.R;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseDialog {

	private TextView mTextMessage;
	private ImageView mImageClose;
	private Dialog mDialog;

	public BaseDialog(Context context) {
		mDialog = new Dialog(context, R.style.base_dialog);
		mDialog.setContentView(R.layout.main_dialog);
		mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = 0;
		wl.alpha = 0.8f;
		window.setAttributes(wl);
//		window.setGravity(Gravity.CENTER);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.setLayout(350, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		
		
		
		mImageClose = (ImageView) mDialog.findViewById(R.id.cs_main_dialog_close);
		mImageClose.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
	}
	
	/**
	 * 设置对话框数据
	 * @param type 类型  1为加载完成; 0为正在加载，有个菊花在转
	 * @param text 提示的文字
	 */
	public void setData(int type, String text){
		switch (type) {
		case 1:
			mTextMessage = (TextView) mDialog.findViewById(R.id.cs_main_dialog_text);
			mTextMessage.setText(text);
			break;
		default:
			//显示loading的菊花
			break;
		}
	}

	public void show() {
		mDialog.show();
	}
	
	public void close(){
		mDialog.dismiss();
	}

}
