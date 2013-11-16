package com.gw.library.ui;

import com.gw.library.fragment.ViewPageFragment.MyPageChangeListener;
import com.gw.library.R;
import com.gw.library.R.layout;
import com.gw.library.R.menu;
import com.gw.library.fragment.LeftFragment;
import com.gw.library.fragment.RightFragment;
import com.gw.library.fragment.ViewPageFragment;
import com.gw.library.widget.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class HomeActivity extends FragmentActivity {

	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	ViewPageFragment viewPageFragment;
	public Bundle mSavedInstanceState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_home);
		this.mSavedInstanceState = savedInstanceState;
		init();
		initListener();
	}
	
	
	private void init(){
		mSlidingMenu = (SlidingMenu)findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.frag_tpl_left, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.frag_tpl_right, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.frag_tpl_center, null));
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		leftFragment = new LeftFragment();
		transaction.replace(R.id.left_frame, leftFragment);
		
		rightFragment = new RightFragment();
		transaction.replace(R.id.right_frame, rightFragment);
		
		viewPageFragment = new ViewPageFragment();
		transaction.replace(R.id.center_frame, viewPageFragment);
		
		transaction.commit();
		
		
	}
	
	private void initListener() {
		viewPageFragment.setMyPageChangeListener(new MyPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(viewPageFragment.isFirst()){
					mSlidingMenu.setCanSliding(true,false);
				}else if(viewPageFragment.isEnd()){
					mSlidingMenu.setCanSliding(false,true);
				}else{
					mSlidingMenu.setCanSliding(false,false);
				}
			}
		});
		
	}
	
	
	
	
	
	

	

}
