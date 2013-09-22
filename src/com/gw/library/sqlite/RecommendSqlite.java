package com.gw.library.sqlite;

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
				Recommend.COL_IMAGENAME
		};
		return columns;
	}

}
