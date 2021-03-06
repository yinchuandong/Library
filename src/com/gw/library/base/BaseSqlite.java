package com.gw.library.base;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.gw.library.model.History;
import com.gw.library.model.Loan;
import com.gw.library.model.Recommend;
import com.gw.library.model.RemindTime;

public abstract class BaseSqlite {

	private static final String DB_NAME = "library.db";
	private static final int DB_VERSION = 6;

	private DbHelper dbh = null;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;

	public BaseSqlite(Context context) {
		dbh = new DbHelper(context, DB_NAME, null, DB_VERSION);
	}

	public void create(ContentValues values) {
		try {
			db = dbh.getWritableDatabase();
			db.insert(tableName(), null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void update(ContentValues values, String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.update(tableName(), values, where, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void delete(String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.delete(tableName(), where, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public ArrayList<HashMap<String, String>> query(String sql, String[] args) {
		ArrayList<HashMap<String, String>> rowList = new ArrayList<HashMap<String, String>>();
		try {
			db = dbh.getReadableDatabase();
			cursor = db.rawQuery(sql, args);
			while (cursor.moveToNext()) {
				HashMap<String, String> colList = new HashMap<String, String>();
				int len = cursor.getColumnCount();
				for (int i = 0; i < len; i++) {
					colList.put(cursor.getColumnName(i), cursor.getString(i));
				}
				rowList.add(colList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return rowList;
	}

	public ArrayList<HashMap<String, String>> select(String where,
			String[] params) {
		ArrayList<HashMap<String, String>> rowList = new ArrayList<HashMap<String, String>>();
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), where, params, null,
					null, null);

			while (cursor.moveToNext()) {
				HashMap<String, String> colList = new HashMap<String, String>();
				int len = cursor.getColumnCount();
				for (int i = 0; i < len; i++) {
					colList.put(cursor.getColumnName(i), cursor.getString(i));
				}
				rowList.add(colList);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			cursor.close();
			db.close();
		}

		return rowList;

	}

	public int count(String where, String[] params) {
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), where, params, null,
					null, null);
			return cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return 0;
	}

	public boolean exists(String where, String[] params) {
		boolean result = false;
		try {
			int count = this.count(where, params);
			if (count > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			cursor.close();
		}
		return result;
	}

	abstract protected String tableName();

	abstract protected String[] tableColumns();

	protected class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createLoanSql());
			db.execSQL(createHisotrySql());
			db.execSQL(createRecommendSql());
			db.execSQL(createRemindTimeSql());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(upgradeLoanSql());
			db.execSQL(upgradeHistorySql());
			db.execSQL(upgradeRecommendSql());
			db.execSQL(upgradeRemindTimeSql());
			onCreate(db);
		}
	}

	protected String createLoanSql() {
		String sql = "CREATE TABLE loan (" + Loan.COL_ID
				+ " text PRIMARY KEY, " + Loan.COL_SCHOOLID + " text, "
				+ Loan.COL_STUDENTNUMBER + " text, " + Loan.COL_TITLE
				+ " text, " + Loan.COL_AUTHOR + " text, " + Loan.COL_URL
				+ " text, " +
				// Loan.COL_PUBLISHYEAR + " text, " +
				Loan.COL_RETURNDATE + " text" +
				// Loan.COL_PAYMENT + " text, " +
				// Loan.COL_LOCATION + " text, " +
				// Loan.COL_CALLNUMBER + " text" +
				")";
		return sql;
	}

	protected String createHisotrySql() {
		String sql = "CREATE TABLE history (" + History.COL_ID
				+ " text PRIMARY KEY, " + History.COL_STUDENTNUMBER + " text, "
				+ History.COL_SCHOOLID + " text, " + History.COL_TITLE
				+ " text, " + History.COL_AUTHOR + " text, " + History.COL_URL
				+ " text" +
				// History.COL_PUBLISHYEAR + " text, "+
				// History.COL_PAYMENT + " text, "+
				// History.COL_LIMITTIME + " text, "+
				// History.COL_RETURNTIME + " text, "+
				// History.COL_LOCATION + " text" +
				")";
		return sql;
	}

	protected String createRecommendSql() {
		String sql = "CREATE TABLE recommend (" + 
				Recommend.COL_ID + " text PRIMARY KEY, " + 
				Recommend.COL_TITLE + " text, "	+ 
				Recommend.COL_AUTHOR + " text, " + 
				Recommend.COL_ISBN	+ " text, " + 
				Recommend.COL_CALLNUMBER + " text, "+ 
				Recommend.COL_RECOMMENDTIME + " text, " + 
				Recommend.COL_URL + " text, " + 
				Recommend.COL_COVER + " text, "	+ 
				Recommend.COL_SCHOOLID + " text, "+
				Recommend.COL_STUDENTNUMBER + " text" + 
//				Recommend.COL_INTRO + " text"+
				")";
		return sql;
	}

	protected String createRemindTimeSql() {
		String sql = "CREATE TABLE RemindTime (" + RemindTime.COL_ID
				+ " text PRIMARY KEY, " + RemindTime.COL_SCHOOLID + " text, "
				+ RemindTime.COL_STUDENTNUMBER + " text," + RemindTime.COL_DAY
				+ " text " + ")";
		return sql;
	}

	protected String upgradeLoanSql() {
		return "DROP TABLE IF EXISTS loan";
	}

	protected String upgradeHistorySql() {
		return "DROP TABLE IF EXISTS history";
	}

	protected String upgradeRecommendSql() {
		return "DROP TABLE IF EXISTS recommend";
	}

	protected String upgradeRemindTimeSql() {
		return "DROP TABLE IF EXISTS RemindTime";
	}

}