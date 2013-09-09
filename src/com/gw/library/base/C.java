package com.gw.library.base;

public class C {
	
	/**
	 * 向服务器请求的url地址
	 * @author yinchuandong
	 *
	 */
	public static final class api {
		public static final String base = "http://lib.yinchuandong.com/index.php/Api/";
		public static final String historyList = "Loan/getHistoryList";
		public static final String loanList = "Loan/getLoanList";
		public static final String login = "User/login";
		public static final String register = "User/register";
	}
	
	/**
	 * 返回的错误的提示文本
	 * @author yinchuandong
	 *
	 */
	public static final class err {
		public static final String network			= "网络错误";
		public static final String message			= "消息错误";
		public static final String jsonFormat		= "消息格式错误";
	}
}
