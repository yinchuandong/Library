package com.gw.library.model;

import com.gw.library.base.BaseModel;

/**
 * 由于当前借阅的列表我们无法改变
 * 因此不提供set方法
 * @author yinchuandong
 *
 */
public class Remind extends BaseModel{

	public final static String COL_ID = "id";
	public final static String COL_TITLE = "title";
	public final static String COL_AUTHOR = "author";
	public final static String COL_URL = "url";
	public final static String COL_PUBLISHYEAR = "publishYear";
	public final static String COL_RETURNDATE= "returnDate";
	public final static String COL_PAYMENT = "payment";
	public final static String COL_LOCATION = "location";
	public final static String COL_CALLNUMBER = "callNumber";
	
	private String id;  //书的id
	private String title; //标题
	private String author; //作者
	private String url; //链接的地址
	private String publishYear; //出版年限
	private String returnDate; //归还日期
	private String payment; //该书欠了多少钱
	private String location; //位于那里
	private String callNumber; //索书号
	
	/**
	 * 空构造函数
	 */
	public Remind(){
		
	}
	
	public String getId(){
		return id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getPublishYear(){
		return publishYear;
	}
	
	public String getReturnDate(){
		return returnDate;
	}
	
	
	public String getPayment(){
		return payment;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getCallNumber(){
		return callNumber;
	}
	
	
	
	
	
	
	
	
	
	
	
}
