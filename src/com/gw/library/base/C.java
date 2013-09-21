package com.gw.library.base;

public class C {

	/**
	 * 向服务器请求的url地址
	 * 
	 * @author yinchuandong
	 * 
	 */
	public static final class api {

		public static final String base = "http://lib.yinchuandong.com/index.php/Api/";
		// public static final String base =
		// "http://192.168.233.101/libraryServer/index.php/Api/";

		public static final String historyList = "Loan/getHistoryList";
		public static final String loanList = "Loan/getLoanList";
		public static final String login = "User/login";
		public static final String register = "User/register";
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
		public static final int login = 1002;
		public static final int historyList = 1003;
		public static final int loanList = 1004;
		public static final int notice = 1005;
	}

	public static final class dir {
		public static final String base = "/sdcard/library";
		public static final String cover = base + "/cover";
	}

	public static final class action {
		public static final String remoteAction = "com.gw.library.service.action.remote";
		public static final String remindAction = "com.gw.library.service.action.remind";
		public static final String historyAction = "com.gw.library.service.action.history";
		public static final String alarmAction = "com.gw.library.service.action.alarm";
		public static final String alarmReceiverAction = "com.gw.library.service.action.alarmreceiver";

	}

}
