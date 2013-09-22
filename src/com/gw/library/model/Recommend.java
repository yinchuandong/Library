package com.gw.library.model;

import com.gw.library.base.BaseModel;

public class Recommend extends BaseModel{
	
	public final static String COL_ID = "id";
	public final static String COL_TITLE = "title";
	public final static String COL_AUTHOR = "author";
	public final static String COL_ISBN = "isbn";
	public final static String COL_CALLNUMBER = "callNumber";
	public final static String COL_URL = "url";
	public final static String COL_RECOMMENDTIME = "recommendTime";
	public final static String COL_IMAGENAME = "imageName";

	private String id;
	private String title;
	private String author;
	private String isbn;
	private String callNumber;
	private String url;
	private String recommendTime;
	private String imageName;
	
	public String getId(){
		return id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getIsbn(){
		return isbn;
	}
	
	public String getCallNumber(){
		return callNumber;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getRecommendTime(){
		return recommendTime;
	}
	
	public String getImageName(){
		return imageName;
	}
	
}
