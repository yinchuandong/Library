package com.gw.library.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.gw.library.base.BaseSqlite;
import com.gw.library.model.Loan;

public class RemindSqlite extends BaseSqlite{

	public RemindSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "loan";
	}

	@Override
	protected String[] tableColumns() {
		String[] tableColums = {
			Loan.COL_ID ,
			Loan.COL_SCHOOLID ,
			Loan.COL_STUDENTNUMBER ,
			Loan.COL_TITLE ,
			Loan.COL_AUTHOR ,
			Loan.COL_URL ,
//			Loan.COL_PUBLISHYEAR ,
			Loan.COL_RETURNDATE 
//			Loan.COL_PAYMENT ,
//			Loan.COL_LOCATION ,
//			Loan.COL_CALLNUMBER 
		};
		return tableColums;
	}

	public boolean updateloan(Loan loan){
		ContentValues values = new ContentValues();
		values.put(Loan.COL_ID, loan.getId());
		values.put(Loan.COL_STUDENTNUMBER, loan.getStudentNumber());
		values.put(Loan.COL_SCHOOLID, loan.getSchoolId());
		values.put(Loan.COL_TITLE, loan.getTitle());
		values.put(Loan.COL_AUTHOR, loan.getAuthor());
		values.put(Loan.COL_URL, loan.getUrl());
//		values.put(Loan.COL_PUBLISHYEAR, loan.getPublishYear());
//		values.put(Loan.COL_PAYMENT, loan.getPayment());
		values.put(Loan.COL_RETURNDATE, loan.getReturnDate());
//		values.put(Loan.COL_CALLNUMBER, loan.getCallNumber());
//		values.put(Loan.COL_LOCATION, loan.getLocation());
		
		// prepare sql
		String whereSql = Loan.COL_ID + "=?";
		String[] whereParams = new String[]{loan.getId()};
		try {
			if (exists(whereSql, whereParams)) {
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
