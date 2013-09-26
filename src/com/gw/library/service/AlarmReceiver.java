package com.gw.library.service;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.util.Log;

import com.gw.library.base.C;

public class AlarmReceiver extends BroadcastReceiver {
	MediaPlayer alarmPlayer;

	/**
	 * 系统默认铃声进行提醒
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.v("AlarmReceiver-->onReceive", intent.getAction());
		
		if (intent.getAction().equals(C.action.alarmAction)) {
			alarmPlayer = new MediaPlayer();
			Log.v("message----------------->", "收到广播");
			try {
				alarmPlayer.setDataSource(context, RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//				alarmPlayer.prepare();
//				alarmPlayer.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (intent.getAction().equals(C.action.alarmStopAction)) {
			if (alarmPlayer != null) {
				alarmPlayer.stop();
				Log.v("message----------------->", "停止广播22222");
			}
			Log.v("message----------------->", "停止广播");
		}

	}
}
