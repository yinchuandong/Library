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
	public final static String COL_COVER = "cover";
	public final static String COL_SCHOOLID = "schoolId";
	public final static String COL_STUDENTNUMBER = "studentNumber";

	private String id;
	private String title;
	private String author;
	private String isbn;
	private String callNumber;
	private String url;
	private String recommendTime;
	private String cover;
	private String schoolId;
	private String studentNumber;
	
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
	
	public String getCover(){
		return cover;
	}
	
	public String getStudentNumber(){
		return studentNumber;
	}
	
	public String getSchoolId(){
		return schoolId;
	}
	
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setCover(String cover){
		this.cover = cover;
	}
	
}
