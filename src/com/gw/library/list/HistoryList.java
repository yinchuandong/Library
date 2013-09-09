package com.gw.library.list;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gw.library.R;
import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.model.Recommend;

public class HistoryList extends BaseList{
	private BaseUi baseUi;
	private ArrayList<Recommend> historyList;
	private LayoutInflater inflater;
	
	public HistoryList(BaseUi baseUi, ArrayList<Recommend> historyList){
		this.baseUi = baseUi;
		this.historyList = historyList;
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.tpl_remind_item, null);
		return null;
	}
}
