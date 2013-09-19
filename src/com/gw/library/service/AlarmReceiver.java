package com.gw.library.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 广播接受到之后的处理
		System.out.print("接受到了相应的广播");
		Toast.makeText(context, "接受到了相应的广播", Toast.LENGTH_SHORT).show();
		Intent itIntent = new Intent(context, AlarmNotifyActivity.class);
		itIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(itIntent);
	}
}
