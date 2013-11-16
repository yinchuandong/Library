/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gw.library.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.gw.library.R;
import com.gw.library.base.BaseFragment;
import com.gw.library.list.FragmentList;
import com.gw.library.ui.HistoryActivity;
import com.gw.library.ui.HomeActivity;
import com.gw.library.ui.RecommendActivity;
import com.gw.library.ui.RemindActivity;

public class ViewPageFragment extends BaseFragment {

	private FragmentList fragmentAdapter;
	private ViewPager mPager;
	private ArrayList<Fragment> pagerList = new ArrayList<Fragment>();
	private HomeActivity home;
	
	private ImageView cursorImg;//选项卡下方的游标
	private RadioButton remindBtn, recommendBtn, historyBtn;
	private int curIndex = 0;
	private int cursorWidth, cursorOffset;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_ui_viewpager, null);
		mPager = (ViewPager)view.findViewById(R.id.pager);
		initMainTab(view);
		initCursorView(view);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		home = (HomeActivity)getActivity();
		initPager();
		bindEvent();
	}
	
	private void initPager(){
		RemindUi remindUi = new RemindUi();
		HistoryUi historyUi = new HistoryUi();
		RecommendUi recommendUi = new RecommendUi();
		pagerList.add(remindUi);
		pagerList.add(recommendUi);
		pagerList.add(historyUi);
		fragmentAdapter = new FragmentList(getFragmentManager(), pagerList);
		mPager.setAdapter(fragmentAdapter);
	}
	
	/**
	 * 初始化tab
	 * @param view
	 */
	private void initMainTab(View view){
		remindBtn = (RadioButton)view.findViewById(R.id.tab_remind);
		recommendBtn = (RadioButton)view.findViewById(R.id.tab_recommend);
		historyBtn = (RadioButton)view.findViewById(R.id.tab_history);
		remindBtn.setOnClickListener(new MainTabListener(0));
		recommendBtn.setOnClickListener(new MainTabListener(1));
		historyBtn.setOnClickListener(new MainTabListener(2));
	}
	
	/**
	 * 初始化游标
	 * @param view
	 */
	private void initCursorView(View view){
		cursorImg = (ImageView)view.findViewById(R.id.cursor);
		cursorWidth = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		cursorOffset = (screenWidth / 3 - cursorWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(cursorOffset, 0);
		cursorImg.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 绑定viewpager切换事件
	 */
	private void bindEvent(){
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			int one = cursorOffset*2 + cursorWidth;
			@Override
			public void onPageSelected(int position) {
				if (myPageChangeListener != null){
					myPageChangeListener.onPageSelected(position);
				}
				Animation animation = new TranslateAnimation(curIndex*one, position*one, 0, 0);
				animation.setDuration(300);
				animation.setFillAfter(true);
				cursorImg.startAnimation(animation);
				curIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerList.size() - 1)
			return true;
		else
			return false;
	}
	

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {
		myPageChangeListener = l;
	}
	/**
	 * 页面改变的接口，留给activity调用
	 * @author Administrator
	 */
	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}
	
	private class MainTabListener implements OnClickListener{
		private int index = 0;
		public MainTabListener(int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index, true);
		}
		
	}

}
