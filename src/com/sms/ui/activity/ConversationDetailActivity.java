package com.sms.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sms.R;
import com.sms.adapter.ConversationDetailAdapter;
import com.sms.base.BaseActivity;
import com.sms.dao.ContactDao;
import com.sms.dao.SimpleQueryHandler;
import com.sms.dao.SmsDao;
import com.sms.globle.Constant;

public class ConversationDetailActivity extends BaseActivity {

	private String address;
	private int thread_id;
	private SimpleQueryHandler queryHandler;
	private ConversationDetailAdapter adapter;
	private ListView lv_conversation_detail;
	private EditText et_conversation_detail;
	private Button bt_conversation_detail_send;

	@Override
	public void initView() {
		setContentView(R.layout.activity_conversation_detail);
		
		lv_conversation_detail = (ListView) findViewById(R.id.lv_conversation_detail);
		et_conversation_detail = (EditText) findViewById(R.id.et_conversation_detail);
		bt_conversation_detail_send = (Button) findViewById(R.id.bt_conversation_detail_send);
		
		//ֻҪListViewˢ�£��ͻỬ��
		lv_conversation_detail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	@Override
	public void initListener() {
		bt_conversation_detail_send.setOnClickListener(this);

	}

	@Override
	public void initData() {
		//�õ����ݹ���������
		Intent intent = getIntent();
		if(intent != null){
			address = intent.getStringExtra("address");
			thread_id = intent.getIntExtra("thread_id", -1);
			
			initTitleBar();
		}
		
		//���Ự��ϸ�����listview����adapter����ʾ�Ự�����ж���
		adapter = new ConversationDetailAdapter(this, null, lv_conversation_detail);
		lv_conversation_detail.setAdapter(adapter);
		
		//���ջỰid��ѯ���ڸûỰ�����ж���
		String[] projection = {
				"_id",
				"body",
				"type",
				"date"
		};
		String selection = "thread_id = " + thread_id;
		
		//�첽��ѯ����
		queryHandler = new SimpleQueryHandler(getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS, projection, selection, null, "date");
//		Cursor cursor = getContentResolver().query(Constant.URI.URI_SMS, projection, selection, null, null);
	}

	/**
	 * ��ʼ��������
	 */
	private void initTitleBar() {
		
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
		String name = ContactDao.getNameByAddress(getContentResolver(), address);
		((TextView)findViewById(R.id.tv_titlebar_title)).setText(TextUtils.isEmpty(name)? address : name);

	}
	
	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;
		case R.id.bt_conversation_detail_send:
			String body = et_conversation_detail.getText().toString();
			//�ж��û��Ƿ������������
			if(!TextUtils.isEmpty(body)){
				SmsDao.sendSms(this, address, body);
				et_conversation_detail.setText("");
			}
			break;
		}

	}

}
