package com.gw.library.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.gw.library.base.BaseSqlite;
import com.gw.library.model.RemindTime;

public class RemindTimeSqlite extends BaseSqlite {

	public RemindTimeSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "RemindTime";
	}

	@Override
	protected String[] tableColumns() {
		String[] tableColums = { RemindTime.COL_ID, RemindTime.COL_SCHOOLID,
				RemindTime.COL_STUDENTNUMBER, RemindTime.COL_DAY };
		return tableColums;
	}

	public boolean updateTime(RemindTime remindTime) {
		ContentValues values = new ContentValues();
		values.put(RemindTime.COL_ID, remindTime.getId());
		values.put(RemindTime.COL_STUDENTNUMBER, remindTime.getStudentNumber());
		values.put(RemindTime.COL_SCHOOLID, remindTime.getSchoolId());
		values.put(RemindTime.COL_DAY, remindTime.getDay());

		String whereSql = RemindTime.COL_ID + "=?";
		String[] whereParams = new String[] { remindTime.getId() };
		try {
			if (exists(whereSql, whereParams)) {
				update(values, whereSql, whereParams);
			} else {
				create(values);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
