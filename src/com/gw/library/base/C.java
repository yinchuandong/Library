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
//		 public static final String base =
//		 "http://192.168.233.15/libraryServer/index.php/Api/";

		public static final String historyList = "Loan/getHistoryList";
		public static final String loanList = "Loan/getLoanList";
		public static final String recommendList = "Recommend/getRecommendList";
		public static final String renew = "Loan/renew";
		public static final String updateIsbn = "Loan/updateIsbn";// 更新历史列表的isbn
		public static final String login = "User/login";
		public static final String register = "User/register";
		public static final String schoolList = "School/getSchoolList";

		public static final String imgBase = "http://lib.yinchuandong.com/Upload/";
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
		public static final int login = 1002; // 登陆
		public static final int historyList = 1003; // 借阅历史
		public static final int loanList = 1004; // 借阅列表
		public static final int notice = 1005; // 通知
		public static final int schoolList = 1006; // 获得学校的名称
		public static final int renew = 1007; // 续借

		public static final int historyListPage = 1008; // 借阅历史-->翻页
		public static final int loanListPage = 1009; // 借阅历史-->翻页
		public static final int updateIsbn = 1010; // 借阅历史-->更新isbn
		public static final int recommendList = 1011; // 图书推荐
		public static final int recommendListPage = 1012;// 图书推荐-->翻页

	}

	public static final class dir {
		public static final String base = "/sdcard/gwlibrary";
		public static final String cover = base + "/cover";
	}

	/**
	 * 定义接收器的属性
	 * 
	 * @author kelink
	 * 
	 */
	public static final class action {
		public static final String remoteAction = "com.gw.library.service.action.remote";
		public static final String remindAction = "com.gw.library.service.action.remind";
		public static final String historyAction = "com.gw.library.service.action.history";
		public static final String alarmAction = "com.gw.library.service.action.alarmreceiver";
		public static final String alarmStopAction = "com.gw.library.service.action.alarmStop";
		public static final String alarmReceiverAction = "com.gw.library.service.action.alarmreceiver";

	}

	/**
	 * 定义服务时间
	 */
	public static final class time {

		public static final long pollTime = 12 * 60 * 60 * 1000;
		public static final long alarmTime = 24 * 60 * 60 * 1000;
		public static final int day = 0;
		public static final int hour = 8;
		public static final int minute = 30;
	}

}
