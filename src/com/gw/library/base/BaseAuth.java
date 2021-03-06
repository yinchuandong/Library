package com.gw.library.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gw.library.model.User;
import com.gw.library.util.AppUtil;

/**
 * 
 * @author KELINK 用户登录验证信息的基类
 * 
 */
public class BaseAuth {
	/**
	 * 获得用户单例
	 * 
	 * @return
	 */
	public static User getUser() {
		return User.getInstance();
	}

	public static void setLogin(boolean isLogin) {
		User.getInstance().setLogin(isLogin);
	}

	public static boolean isLogin() {
		return User.getInstance().getLogin();
	}

	/**
	 * 保存用户信息到sharepreferences
	 * 
	 * @param context
	 * @param data
	 */
	public static void saveUserInfo(Context context,
			HashMap<String, String> data) {
		User user = BaseAuth.getUser();
		SharedPreferences preferences = AppUtil.getSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = data.get(key);

			editor.putString(key, value);
			try {
				Field field = user.getClass().getDeclaredField(key);
				field.setAccessible(true);
				field.set(user, value);
			} catch (Exception e) {
				Log.e("BaseAuth-->saveUserInfo", "false");
				e.printStackTrace();
			}
		}
		editor.commit();
	}

	/**
	 * 从手机本地获取保存的用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static HashMap<String, String> getUserInfo(Context context) {
		HashMap<String, String> userInfo = new HashMap<String, String>();
		SharedPreferences preferences = AppUtil.getSharedPreferences(context);

		userInfo.put(User.COL_STUDENTNUMBER,
				preferences.getString(User.COL_STUDENTNUMBER, ""));
		userInfo.put(User.COL_SCHOOLID,
				preferences.getString(User.COL_SCHOOLID, ""));
		userInfo.put(User.COL_USERNAME,
				preferences.getString(User.COL_USERNAME, ""));
		userInfo.put(User.COL_ACADEMY,
				preferences.getString(User.COL_ACADEMY, ""));
		userInfo.put(User.COL_MAJOR, preferences.getString(User.COL_MAJOR, ""));
		userInfo.put(User.COL_PASSWORD,
				preferences.getString(User.COL_PASSWORD, ""));

		return userInfo;
	}

	public static void clearUserInfo(Context context) {
		SharedPreferences preferences = AppUtil.getSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString(User.COL_STUDENTNUMBER, "");
		editor.putString(User.COL_SCHOOLID, "");
		editor.putString(User.COL_USERNAME, "");
		editor.putString(User.COL_ACADEMY, "");
		editor.putString(User.COL_MAJOR, "");
		editor.putString(User.COL_PASSWORD, "");

		editor.commit();
	}

}
