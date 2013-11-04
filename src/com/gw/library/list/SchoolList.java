package com.gw.library.list;

import java.util.ArrayList;

import com.gw.library.R;
import com.gw.library.model.School;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SchoolList extends BaseAdapter implements SectionIndexer{
	private ArrayList<School> sList;
	private Context mContext;
	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}
	
	public SchoolList(Context context, ArrayList<School> sList){
		this.sList = sList;
		mContext = context;
	}
	
	public void updateData(ArrayList<School> sList){
		this.sList = sList;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return sList.size();
	}

	@Override
	public Object getItem(int position) {
		return sList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.tpl_school_item, null);
			holder.tvTitle = (TextView)view.findViewById(R.id.title);
			holder.tvLetter = (TextView)view.findViewById(R.id.catalog);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		School school = sList.get(position);
		holder.tvTitle.setText(school.getName());
		
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(school.getSortLetters());
		}else{
			holder.tvLetter.setVisibility(View.GONE);
		}
		
		return view;
	}


	/**
	 * 获得字符的ascii码
	 * @param position
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	@Override
	public int getSectionForPosition(int position) {
		return sList.get(position).getSortLetters().charAt(0);
	}
	
	/**
	 * 通过ascii码获得下标
	 * @param sectionIndex
	 * @return
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = sList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
