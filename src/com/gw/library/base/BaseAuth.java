package com.gw.library.base;

<<<<<<< HEAD
import java.sql.Date;
import java.text.SimpleDateFormat;
=======
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gw.library.R.string;
import com.gw.library.model.User;
import com.gw.library.util.AppUtil;

>>>>>>> de462430027292d72437ed02eb2d3a54da74389e

import com.gw.library.model.User;

/**
 * 
 * @author KELINK 用户登录验证信息的基类
 * 
 */
public class BaseAuth {
<<<<<<< HEAD

	private static boolean is_login = false;// 默认没有登录
	private static String login_time;// 登录时间
	private static User user;// 当前用户

	public static boolean isLogin() {
		if (BaseAuth.is_login == false) {
			return false;
		} else {
			return true;
		}
	}

	public static String getLogin_time() {
		return login_time;
	}

	public static void setLogin_time(String login_time) {
		BaseAuth.login_time = login_time;
	}

	public static void setLogin(boolean state) {
		BaseAuth.is_login = state;
		if (BaseAuth.is_login == true) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String date = formatter.format(curDate);
			setLogin_time(date);
		}
	}

	public static void setUser(User user) {
		BaseAuth.user = user;
	}

	public static User getUser() {
		return BaseAuth.user;
	}
=======
	
	/**
	 * 获得用户单例
	 * @return
	 */
	public static User getUser(){
		return User.getInstance();
	}
	
	public static void setLogin(boolean isLogin){
		User.getInstance().setLogin(isLogin);
	}
	
	public static boolean isLogin(){
		return User.getInstance().getLogin();
	}
	
	/**
	 * 保存用户信息到sharepreferences
	 * @param context
	 * @param data
	 */
	public static void saveUserInfo(Context context, HashMap<String, String> data) {
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
	 * @param context
	 * @return
	 */
	public static HashMap<String, String> getUserInfo(Context context){
		HashMap<String, String> userInfo = new HashMap<String, String>();
		SharedPreferences preferences = AppUtil.getSharedPreferences(context);
		userInfo.put(User.COL_STUDENTNUMBER, preferences.getString(User.COL_STUDENTNUMBER, ""));
		userInfo.put(User.COL_SCHOOLID, preferences.getString(User.COL_SCHOOLID, ""));
		userInfo.put(User.COL_USERNAME, preferences.getString(User.COL_USERNAME, ""));
		userInfo.put(User.COL_ACADEMY, preferences.getString(User.COL_ACADEMY, ""));
		userInfo.put(User.COL_MAJOR,preferences.getString(User.COL_MAJOR, ""));
		
		return userInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
>>>>>>> de462430027292d72437ed02eb2d3a54da74389e
}