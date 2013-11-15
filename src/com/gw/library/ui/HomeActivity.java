package com.gw.library.ui;

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
import android.view.Menu;

public class HomeActivity extends FragmentActivity {

	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	ViewPageFragment viewPageFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_home);
		init();
	}
	
	
	private void init(){
		mSlidingMenu = (SlidingMenu)findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.frag_ui_left, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.frag_ui_right, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.frag_ui_viewpager, null));
		
	}
	
	
	
	
	
	
	

	

}
