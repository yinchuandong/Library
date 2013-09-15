package com.gw.library.list;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
	RelativeLayout itemMainLayout;
	RelativeLayout itemBottomLayout;
	
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
		itemMainLayout = (RelativeLayout)convertView.findViewById(R.id.r_item_main);
		itemBottomLayout = (RelativeLayout)convertView.findViewById(R.id.r_item_bottom);
		
		Loan loan = remindList.get(position);
		String remainDays = getRemainsDay(loan.getReturnDate());
		String returnDate = getReturnDate(loan.getReturnDate());
		titleView.setText(loan.getTitle());
		authorView.setText("归还日期："+returnDate);
		remainDayView.setText(remainDays);
		
//		bindEvent(position,convertView);
		return convertView;
	}
	
	public void setData(ArrayList<Loan> remindList){
		this.remindList = remindList;
	}
	
	/**
	 * 获得倒计时
	 * @param resource
	 * @return
	 */
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
	
	/**
	 * 把yyyyMMdd 转为 yyyy-MM-dd的格式
	 * 获得归还的日期
	 * @param date
	 * @return
	 */
	public String getReturnDate(String date){
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String result = "";
		
		try {
			result = format2.format(format1.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void bindEvent(final int position, final View view){
		itemMainLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (itemBottomLayout.getVisibility() == View.GONE) {
					itemBottomLayout.setVisibility(View.VISIBLE);
				}else{
//					itemBottomLayout.setVisibility(View.GONE);
					TextView textView = (TextView)view.findViewById(R.id.r_renew);
					textView.setText("12312");
				}
				Log.i("remindlist-->onclick", ""+position);
			}
		});
		
	}
	
}
