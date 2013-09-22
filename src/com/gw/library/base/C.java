package com.gw.library.base;

import android.R.integer;

public class C {

	/**
	 * 向服务器请求的url地址
	 * 
	 * @author yinchuandong
	 * 
	 */
	public static final class api {

		public static final String base = "http://lib.yinchuandong.com/index.php/Api/";
//		public static final String base = "http://192.168.233.101/libraryServer/index.php/Api/";

		public static final String historyList = "Loan/getHistoryList";
		public static final String loanList = "Loan/getLoanList";
		public static final String renew = "Loan/renew";
		public static final String login = "User/login";
		public static final String register = "User/register";
		public static final String schoolList = "School/getSchoolList";
	}

	/**
	 * 返回的错误的提示文本
	 * 
	 * @author yinchuandong
	 * 
	 */
	public static final class err {
		public static final String network = "网络错误";
		public static final String message = "消息错误";
		public static final String jsonFormat = "消息格式错误";
	}

	/**
	 * 定义任务类的ID
	 * 
	 * @author Kelink
	 * 
	 */
	public static final class task {

		public static final int index = 1001;
		public static final int login = 1002; //登陆
		public static final int historyList = 1003; //借阅历史
		public static final int loanList = 1004; //借阅列表
		public static final int notice = 1005; //通知
		public static final int schoolList = 1006; //获得学校的名称
		public static final int renew = 1007; //续借
	}
	
	
	public static final class dir{
		public static final String base = "/sdcard/library";
		public static final String cover = base + "/cover";
	}

}
