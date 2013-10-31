package com.gw.library.model;

import com.gw.library.base.BaseModel;

public class RemindTime extends BaseModel {

	public final static String COL_STUDENTNUMBER = "studentNumber";
	public final static String COL_SCHOOLID = "schoolId";
	public final static String COL_ID = "Id";
	public final static String COL_DAY = "day";

	private String studentNumber;
	private String schoolId;
	private String Id;
	private String day;

	public String getStudentNumber() {
		return studentNumber;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public String getId() {
		return Id;
	}

	public String getDay() {
		return day;
	}

}
