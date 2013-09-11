package com.gw.library.model;

import com.gw.library.base.BaseModel;

public class User extends BaseModel {

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

	public String getStudentNumber() {
		return studentNumber;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public String getUsername() {
		return username;
	}

	public String getAcademy() {
		return academy;
	}

	public String getMajor() {
		return major;
	}

	public String getPassword() {
		return password;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean getLogin() {
		return isLogin;
	}

	public static User getInstance() {
		if (User.user == null) {
			User.user = new User();
		}
		return User.user;
	}

}
