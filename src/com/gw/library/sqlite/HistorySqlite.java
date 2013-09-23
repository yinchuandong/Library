package com.gw.library.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.gw.library.base.BaseSqlite;
import com.gw.library.model.History;

public class HistorySqlite extends BaseSqlite{

	public HistorySqlite(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String tableName() {
		return "history";
	}

	@Override
	protected String[] tableColumns() {
		String[] tableColumns = {
				History.COL_ID ,
				History.COL_STUDENTNUMBER ,
				History.COL_SCHOOLID ,
				History.COL_TITLE ,
				History.COL_AUTHOR ,
				History.COL_URL ,
//				History.COL_PUBLISHYEAR ,
//				History.COL_PAYMENT ,
//				History.COL_LIMITTIME ,
//				History.COL_RETURNTIME ,
//				History.COL_LOCATION ,
		};
		return tableColumns;
	}
	
	public boolean updateHistory(History history){
		ContentValues values = new ContentValues();
		values.put(History.COL_ID, history.getId());
		values.put(History.COL_STUDENTNUMBER, history.getStudentNumber());
		values.put(History.COL_SCHOOLID	, history.getSchoolId());
		values.put(History.COL_TITLE, history.getTitle());
		values.put(History.COL_AUTHOR, history.getAuthor());
		values.put(History.COL_URL, history.getUrl());
//		values.put(History.COL_PUBLISHYEAR, history.getPublishYear());
//		values.put(History.COL_PAYMENT, history.getPayment());
//		values.put(History.COL_LIMITTIME, history.getLimitTime());
//		values.put(History.COL_RETURNTIME, history.getReturnTime());
//		values.put(History.COL_LOCATION, history.getLocation());
		
		// prepare sql
		String whereSql = History.COL_ID + "=?";
		String[] whereParams = new String[]{history.getId()};
		try {
			if (exists(whereSql, whereParams)) { //因为ig每次不一样，所以这里不会执行
				update(values, whereSql, whereParams);
			}else {
				create(values);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	

}
