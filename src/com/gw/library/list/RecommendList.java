package com.gw.library.list;

import java.util.ArrayList;

import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.model.Recommend;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RecommendList extends BaseList{

	BaseUi baseUi;
	ArrayList<Recommend> rList;
	
	public RecommendList(BaseUi baseUi, ArrayList<Recommend> rList){
		this.baseUi = baseUi;
		this.rList = rList;
	}
	
	@Override
	public int getCount() {
		return rList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return convertView;
	}
	
	public void setData(ArrayList<Recommend> rList){
		this.rList = rList;
	}

}
