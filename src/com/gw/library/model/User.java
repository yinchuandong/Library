package com.gw.library.model;

import com.gw.library.base.BaseModel;

<<<<<<< HEAD
/**
 * 
 * @author KELINK 登录用户信息model
 */
public class User extends BaseModel {
	private String studentNumber;// 用户的学号
	private String username;// 用户名
	private String schoolId;// 学校的
	private String academy;// 学院
	private String major;// 专业
	private String email;// 邮件

	public User() {
		super();
	}

	public User(String studentNumber, String username, String schoolId,
			String academy, String major, String email) {
		super();
		this.studentNumber = studentNumber;
		this.username = username;
		this.schoolId = schoolId;
		this.academy = academy;
		this.major = major;
		this.email = email;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public String getUsername() {
		return username;
	}

	public String getSchoolID() {
		return schoolId;
	}

	public String getAcademy() {
		return academy;
	}

	public String getMajor() {
		return major;
	}

	public String getEmail() {
		return email;
	}

=======
public class User extends BaseModel{
	
	public final static String COL_STUDENTNUMBER = "studentNumber";
	public final static String COL_SCHOOLID = "schoolId";
	public final static String COL_USERNAME = "username";
	public final static String COL_ACADEMY = "academy";
	public final static String COL_MAJOR = "major";
	public final static String COL_PASSWORD = "password";
	
	private String studentNumber;
	private String schoolId;
	private String username;
	private String academy;
	private String major;
	private String password;
	private String email;
	
	private boolean isLogin = false;
	
	private static User user;
	
	public String getStudentNumber(){
		return studentNumber;
	}
	
	public String getSchoolId() {
		return schoolId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getAcademy(){
		return academy;
	}
	
	public String getMajor(){
		return major;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setLogin(boolean isLogin){
		this.isLogin = isLogin;
	}
	
	public boolean getLogin(){
		return isLogin;
	}
	
	
	public static User getInstance() {
		if (User.user == null) {
			User.user = new User();
		}
		return User.user;
	}
	
>>>>>>> de462430027292d72437ed02eb2d3a54da74389e
}
