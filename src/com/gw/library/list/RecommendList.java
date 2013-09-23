package com.gw.library.list;

import java.util.ArrayList;

import com.gw.library.base.BaseList;
import com.gw.library.base.BaseUi;
import com.gw.library.list.RemindList.RItem;
import com.gw.library.model.Recommend;
import com.gw.library.util.AppCache;
import com.gw.library.R;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendList extends BaseList{

	BaseUi baseUi;
	ArrayList<Recommend> rcList;
	LayoutInflater inflater;
	
	public final class RcItem{
		public ImageView coverView;
		public TextView authorView;
		public TextView titleView;
	}
	
	public RecommendList(BaseUi baseUi, ArrayList<Recommend> rcList){
		this.baseUi = baseUi;
		this.rcList = rcList;
		inflater = LayoutInflater.from(baseUi);
	}
	
	@Override
	public int getCount() {
		return rcList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RcItem rcItem = null;
		if (rcItem == null) {
			convertView = inflater.inflate(R.layout.tpl_recommend_item, null);
			rcItem = new RcItem();
			rcItem.authorView = (TextView)convertView.findViewById(R.id.rc_author);
			rcItem.titleView = (TextView)convertView.findViewById(R.id.rc_title);
			rcItem.coverView = (ImageView)convertView.findViewById(R.id.rc_cover);
		}else{
			rcItem = (RcItem)convertView.getTag();
		}
		Recommend model = rcList.get(position);
		rcItem.authorView.setText(model.getAuthor());
		rcItem.titleView.setText(model.getTitle());
//		rcItem.coverView.setImageResource(R.drawable.cover_1);
		Bitmap cover = AppCache.getImage(model.getCover());
		if (cover != null) {
			rcItem.coverView.setImageBitmap(cover);
		}
		
		return convertView;
	}
	
	public void setData(ArrayList<Recommend> rcList){
		this.rcList = rcList;
	}

}
