package com.sms.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.sms.R;
import com.sms.R.id;
import com.sms.R.layout;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sms.adapter.MainPagerAdapter;
import com.sms.base.BaseActivity;
import com.sms.ui.fragment.ConversationFragment;
import com.sms.ui.fragment.GroupFragment;
import com.sms.ui.fragment.SearchFragment;
import com.sms.utils.LogUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {


	private ViewPager viewPager;
	private List<Fragment> fragments;
	private TextView tv_tab_conversation;
	private TextView tv_tab_group;
	private TextView tv_tab_search;
	private MainPagerAdapter adapter;
	private LinearLayout ll_tab_conversation;
	private LinearLayout ll_tab_group;
	private LinearLayout ll_tab_search;
	private View v_indicate_line;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		
		//拿到布局文件中的组件
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		
		tv_tab_conversation = (TextView) findViewById(R.id.tv_tab_conversation);
		tv_tab_group = (TextView) findViewById(R.id.tv_tab_group);
		tv_tab_search = (TextView) findViewById(R.id.tv_tab_search);
		
		ll_tab_conversation = (LinearLayout) findViewById(R.id.ll_tab_conversation);
		ll_tab_group = (LinearLayout) findViewById(R.id.ll_tab_group);
		ll_tab_search = (LinearLayout) findViewById(R.id.ll_tab_search);
		
		
	}

	@Override
	public void initListener() {
		//viewpager界面切换时会触发
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//切换完成后调用，传入的参数是切换后的界面的索引
			@Override
			public void onPageSelected(int position) {
				textLightAndScale();
			}
			
			//滑动过程不断调用
			//如果滑动过程中出现两个界面，position是前一个的索引
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
//				System.out.println(positionOffsetPixels);
				//计算红线位移的距离
				int distance = positionOffsetPixels / 3;
				
				//持续时间为0，立刻生效，因为红线的移动需要与用户滑动同步
				ViewPropertyAnimator.animate(v_indicate_line).translationX(distance + position * v_indicate_line.getWidth()).setDuration(0);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		});
		
		//给3个选项卡设置点击事件
		ll_tab_conversation.setOnClickListener(this);
		ll_tab_group.setOnClickListener(this);
		ll_tab_search.setOnClickListener(this);
		
		v_indicate_line = findViewById(R.id.v_indicate_line);
	}

	@Override
	public void initData() {
		fragments = new ArrayList<Fragment>();
		//创建Fragment对象，存入集合
		ConversationFragment fragment1 = new ConversationFragment();
		GroupFragment fragment2 = new GroupFragment();
		SearchFragment fragment3 = new SearchFragment();
		fragments.add(fragment1);
		fragments.add(fragment2);
		fragments.add(fragment3);
		adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
		
		textLightAndScale();
		
		//设置红线的宽度
		computeIndicateLineWidth();
		
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_tab_conversation:
			//改变ViewPager界面
			viewPager.setCurrentItem(0);
			break;
		case R.id.ll_tab_group:
			viewPager.setCurrentItem(1);
			break;
		case R.id.ll_tab_search:
			viewPager.setCurrentItem(2);
			break;

		}
	}

	/**
	 * 改变选项卡的文本的颜色和大小
	 */
	private void textLightAndScale() {
		//获取viewPager当前显示界面的索引
		int item = viewPager.getCurrentItem();
		//根据viewPager的界面索引决定选项卡颜色
		tv_tab_conversation.setTextColor(item == 0? Color.WHITE : 0xaa666666);
		tv_tab_group.setTextColor(item == 1? Color.WHITE : 0xaa666666);
		tv_tab_search.setTextColor(item == 2? Color.WHITE : 0xaa666666);

		//                        要操作的对象                                         改变宽度至指定比例
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleX(item == 0? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleX(item == 1? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleX(item == 2? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(item == 0? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleY(item == 1? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleY(item == 2? 1.2f : 1).setDuration(200);
	}

	/**
	 * 指定红线宽度为屏幕1/3
	 */
	private void computeIndicateLineWidth() {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		v_indicate_line.getLayoutParams().width = width / 3;
	}
    
}
