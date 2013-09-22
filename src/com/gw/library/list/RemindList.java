package com.gw.library.list;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.UserDataHandler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gw.library.base.BaseAuth;
import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.base.C;
import com.gw.library.model.Loan;
import com.gw.library.model.Recommend;
import com.gw.library.model.User;
import com.gw.library.R;

public class RemindList extends BaseList{
	private BaseUi baseUi;
	private ArrayList<Loan> remindList;
	private LayoutInflater inflater;
	
	
	
	public final class RItem{
		public TextView remainDayView;
		public TextView titleView;
		public TextView authorView;
		public TextView rCloseView;
		public TextView renewView;
		public RelativeLayout itemMainLayout;
		public LinearLayout itemBottomLayout;
	}
	
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

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		RItem rItem = null;
		if (rItem == null) {
			convertView = inflater.inflate(R.layout.tpl_remind_item, null);
			rItem = new RItem();
			rItem.titleView = (TextView)convertView.findViewById(R.id.r_title);
			rItem.authorView = (TextView)convertView.findViewById(R.id.r_author);
			rItem.remainDayView = (TextView)convertView.findViewById(R.id.r_remain_day);
			rItem.rCloseView = (TextView)convertView.findViewById(R.id.r_close);
			rItem.renewView = (TextView)convertView.findViewById(R.id.renew);
			rItem.itemMainLayout = (RelativeLayout)convertView.findViewById(R.id.r_item_main);
			rItem.itemBottomLayout = (LinearLayout)convertView.findViewById(R.id.r_item_bottom);
			convertView.setTag(rItem);
		}else{
			rItem = (RItem)convertView.getTag();
		}
		
		
		Loan loan = remindList.get(position);
		String remainDays = getRemainsDay(loan.getReturnDate());
		String returnDate = getReturnDate(loan.getReturnDate());
		rItem.titleView.setText(loan.getTitle());
		rItem.authorView.setText("归还日期："+returnDate);
		rItem.remainDayView.setText(remainDays);
		
		bindEvent(position, rItem);
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
	
	public void bindEvent(final int position, final RItem rItem){
		//点击书弹出续借的框，再点一次收起
		rItem.itemMainLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (rItem.itemBottomLayout.getVisibility() == View.GONE) {
					rItem.itemBottomLayout.setVisibility(View.VISIBLE);
				}
				Log.i("remindlist-->onclick", ""+position);
			}
		});
		
		//关闭的框框
		rItem.rCloseView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("onclick",rItem.titleView.getText().toString());
				if (rItem.itemBottomLayout.getVisibility() == View.VISIBLE) {
					rItem.itemBottomLayout.setVisibility(View.GONE);
				}
			}
		});
		
		rItem.renewView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				baseUi.baseDialog.setData(0, null);
				baseUi.baseDialog.show();//显示对话框
				
				HashMap<String, String> form = new HashMap<String, String>();
				User user = BaseAuth.getUser();
				form.put("studentNumber", user.getStudentNumber());
				form.put("schoolId", user.getSchoolId());
				form.put("password", user.getPassword());
				form.put("books", remindList.get(position).getId());
				baseUi.doTaskAsync(C.task.renew, C.api.renew, form);
			}
		});
		
	}
	
}
