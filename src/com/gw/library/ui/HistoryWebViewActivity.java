package com.gw.library.ui;

import android.os.Bundle;
import android.view.KeyEvent;
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
		toast(url);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		// 设置Web视图
		webView.setWebViewClient(new HistoryWebViewClient());
	}

	@Override
	// 设置回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return false;
	}

	// Web视图
	private class HistoryWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
