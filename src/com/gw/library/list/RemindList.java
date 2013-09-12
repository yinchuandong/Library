package com.gw.library.list;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.model.Loan;
import com.gw.library.model.Recommend;
import com.gw.library.R;

public class RemindList extends BaseList{
	private BaseUi baseUi;
	private ArrayList<Loan> remindList;
	private LayoutInflater inflater;
	
	TextView remainDayView;
	TextView titleView;
	TextView authorView;
	
	public RemindList(BaseUi baseUi, ArrayList<Loan> remindList){
		this.baseUi = baseUi;
		this.remindList = remindList;
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return remindList.size();
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
		titleView = (TextView)convertView.findViewById(R.id.r_title);
		authorView = (TextView)convertView.findViewById(R.id.r_author);
		remainDayView = (TextView)convertView.findViewById(R.id.r_remain_day);
		
		Loan loan = remindList.get(position);
		String remainDays = getRemainsDay(loan.getReturnDate());
		titleView.setText(loan.getTitle());
		authorView.setText(loan.getAuthor());
		remainDayView.setText(remainDays);
		
		return convertView;
	}
	
	public void setData(ArrayList<Loan> remindList){
		this.remindList = remindList;
	}
	
	public String getRemainsDay(String resource){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String remainDay = "";
		try {
			Date date = format.parse(resource);
			Long nowTime = System.currentTimeMillis();
			int days = (int)Math.ceil((double)(date.getTime() - nowTime)/(24*60*60*1000));
//			Log.i("remindList",days + "/"+nowTime+"/"+date.getTime());
			remainDay = String.valueOf(days);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return remainDay;
	}
	
}
