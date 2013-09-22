package com.gw.library.ui;

import android.os.Bundle;
import android.os.Message;

import com.gw.library.R;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseTask;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;

public class RecommendActivity extends BaseUiAuth {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_recommend);

	}

	/**
	 * 异步下载网络图片的回调方法
	 * 
	 * @author yinchuandong
	 * 
	 */
	private class RecommendHandler extends BaseHandler {

		public RecommendHandler(BaseUi ui) {
			super(ui);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case BaseTask.LOAD_IMAGE:
				break;

			default:
				break;
			}
		}

	}
}
