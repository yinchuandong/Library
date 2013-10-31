package com.gw.library.service;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gw.library.base.BaseAuth;
import com.gw.library.base.C;
import com.gw.library.util.PollingUtils;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 启动应用服务
		if (BaseAuth.isLogin()) {
			// 开启服务
			PollingUtils.startPollingService(context, C.time.pollTime,
					RemoteService.class, C.action.remoteAction);
		} else {
			HashMap<String, String> userInfo = BaseAuth.getUserInfo(context);
			String spStudentNumber = userInfo.get("studentNumber");
			if (spStudentNumber != "" && !spStudentNumber.equals("")) {
				BaseAuth.setLogin(true);
				BaseAuth.saveUserInfo(context, userInfo);
				// 开启服务
				PollingUtils.startPollingService(context, C.time.pollTime,
						RemoteService.class, C.action.remoteAction);
			}
		}
	}

}
