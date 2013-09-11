package com.gw.library.model;

import com.gw.library.base.BaseModel;

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

}
