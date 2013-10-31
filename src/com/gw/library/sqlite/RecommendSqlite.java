package com.gw.library.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.gw.library.base.BaseSqlite;
import com.gw.library.model.Recommend;

public class RecommendSqlite extends BaseSqlite{

	public RecommendSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "recommend";
	}

	@Override
	protected String[] tableColumns() {
		String [] columns = new String[]{
				Recommend.COL_ID,
				Recommend.COL_TITLE,
				Recommend.COL_AUTHOR,
				Recommend.COL_ISBN,
				Recommend.COL_CALLNUMBER,
				Recommend.COL_RECOMMENDTIME,
				Recommend.COL_URL,
				Recommend.COL_COVER,
				Recommend.COL_SCHOOLID,
				Recommend.COL_STUDENTNUMBER
		};
		return columns;
	}
	
	public boolean updateRecommend(Recommend recommend){
		ContentValues values = new ContentValues();
		values.put(Recommend.COL_ID, recommend.getId());
		values.put(Recommend.COL_STUDENTNUMBER, recommend.getStudentNumber());
		values.put(Recommend.COL_SCHOOLID	, recommend.getSchoolId());
		values.put(Recommend.COL_TITLE, recommend.getTitle());
		values.put(Recommend.COL_AUTHOR, recommend.getAuthor());
		values.put(Recommend.COL_URL, recommend.getUrl());
		values.put(Recommend.COL_COVER, recommend.getCover());
		values.put(Recommend.COL_ISBN, recommend.getIsbn());
		values.put(Recommend.COL_CALLNUMBER, recommend.getCallNumber());
		values.put(Recommend.COL_RECOMMENDTIME, recommend.getRecommendTime());
		
		// prepare sql
		String whereSql = Recommend.COL_ID + "=?";
		String[] whereParams = new String[]{recommend.getId()};
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
