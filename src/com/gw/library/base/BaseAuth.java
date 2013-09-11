package com.gw.library.base;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.gw.library.model.User;

/**
 * 
 * @author KELINK 用户登录验证信息的基类
 * 
 */
public class BaseAuth {

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
}