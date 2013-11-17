package com.gw.library.list;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class FragmentList extends FragmentPagerAdapter{

	ArrayList<Fragment> fList;
	public FragmentList(FragmentManager fm) {
		super(fm);
	}
	
	public FragmentList(FragmentManager fm, ArrayList<Fragment> fList){
		super(fm);
		this.fList = fList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position < fList.size()){
			fragment = fList.get(position);
		}else{
			fragment = fList.get(0);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return fList.size();
	}
	
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {		
//		super.instantiateItem(container, position);
//		container.addView(fList.get(position).getView(), 0);
//		return fList.get(position).getView();
//	}

}
