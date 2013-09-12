package com.gw.library.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gw.library.R;
import com.gw.library.base.BaseUiAuth;

public class HistoryWebViewActivity extends BaseUiAuth {
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history_webview);
		webView = (WebView) this.findViewById(R.id.hSwebview);
		String url = (String) this.getIntent().getExtras().get("url");
		// 支持JS
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置适应屏幕大小的加载模式
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl(url);
		// 支持缩放
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// 设置Web视图
		webView.setWebViewClient(new HistoryWebViewClient());
	}

	@Override
	// 设置回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 重写OnTouch以适应两手缩放屏幕
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float oldX1 = 0, oldX2 = 0;
		float oldY1 = 0, oldY2 = 0;
		float newX1 = 0, newX2 = 0;
		float newY1 = 0, newY2 = 0;
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_POINTER_2_DOWN:
			if (event.getPointerCount() == 2) {
				for (int i = 0; i < event.getPointerCount(); i++) {
					if (i == 0) {
						oldX1 = event.getX(i);
						oldY1 = event.getY();
					} else if (i == 1) {
						oldX2 = event.getX();
						oldY2 = event.getY();
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 2) {
				for (int i = 0; i < event.getPointerCount(); i++) {
					if (i == 0) {
						newX1 = event.getX(i);
						newY1 = event.getY();
					} else if (i == 1) {
						newX2 = event.getX();
						newY2 = event.getY();
					}
				}
				float disOld = (float) Math
						.sqrt((Math.pow(oldX2 - oldX1, 2) + Math.pow(oldY2
								- oldY1, 2)));
				float disNew = (float) Math
						.sqrt((Math.pow(newX2 - newX1, 2) + Math.pow(newY2
								- newY1, 2)));
				Log.d("onTouch", "disOld=" + disOld + "|disNew=" + disNew);
				if (disOld - disNew >= 25) {
					// 缩小
					webView.zoomOut();

				} else if (disNew - disOld >= 25) {
					// 放大
					webView.zoomIn();
				}
				oldX1 = newX1;
				oldX2 = newX2;
				oldY1 = newY1;
				oldY2 = newY2;
			}

		}
		return super.onTouchEvent(event);
	}

	class HistoryWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
}
