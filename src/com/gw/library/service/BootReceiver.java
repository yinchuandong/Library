package com.gw.library.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gw.library.base.C;
import com.gw.library.util.PollingUtils;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 启动应用服务

		PollingUtils.startPollingService(context, C.time.pollTime,
				RemoteService.class, C.action.remoteAction);

	}
}
