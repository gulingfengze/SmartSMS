package com.sms.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initListener();
		initData();
	}
	
	public abstract void initView();
	public abstract void initListener();
	public abstract void initData();
	public abstract void processClick(View v);
	
	@Override
	public void onClick(View v) {
		processClick(v);
		
	}
}
