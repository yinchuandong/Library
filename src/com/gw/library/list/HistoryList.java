package com.gw.library.list;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.model.History;

public class HistoryList extends BaseList {
	private BaseUi baseUi;
	private ArrayList<History> historyList;
	private LayoutInflater inflater;

	TextView titleView;
	TextView authorView;
	TextView returnTimeView;

	public HistoryList(BaseUi baseUi, ArrayList<History> historyList) {
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
		convertView = inflater.inflate(R.layout.tpl_history_item, null);
		titleView = (TextView) convertView.findViewById(R.id.hs_title);
		authorView = (TextView) convertView.findViewById(R.id.hs_author);
		returnTimeView = (TextView) convertView
				.findViewById(R.id.hs_return_time);

		History hs = historyList.get(position);
		titleView.setText(hs.getTitle());
		authorView.setText(hs.getAuthor());

		return convertView;
	}

	public void setData(ArrayList<History> historyList) {
		this.historyList = historyList;
	}

	public void addData(History history) {
		this.historyList.add(history);
	}
}
