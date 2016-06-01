package com.sms.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sms.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sms.adapter.ConversationListAdapter;
import com.sms.base.BaseFragment;
import com.sms.bean.Conversation;
import com.sms.bean.Group;
import com.sms.dao.GroupDao;
import com.sms.dao.SimpleQueryHandler;
import com.sms.dao.ThreadGroupDao;
import com.sms.dialog.ConfirmDialog;
import com.sms.dialog.DeleteMsgDialog;
import com.sms.dialog.ListDialog;
import com.sms.dialog.ConfirmDialog.OnConfirmListener;
import com.sms.dialog.DeleteMsgDialog.OnDeleteCancelListener;
import com.sms.dialog.ListDialog.OnListDialogLietener;
import com.sms.globle.Constant;
import com.sms.ui.activity.ConversationDetailActivity;
import com.sms.ui.activity.NewMsgActivity;
import com.sms.utils.ToastUtils;

public class ConversationFragment extends BaseFragment {

	private Button bt_conversation_edit;
	private Button bt_conversation_new_msg;
	private Button bt_conversation_select_all;
	private Button bt_conversation_cancel_select;
	private Button bt_conversation_delete;
	private LinearLayout ll_conversation_edit_menu;
	private LinearLayout ll_conversation_select_menu;
	private ListView lv_conversation_list;
	private ConversationListAdapter adapter;
	private List<Integer> selectedConversationIds;

	static final int WHAT_DELETE_COMPLETE = 0;
	static final int WHAT_UPDATE_DELETE_PROGRESS = 1;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DELETE_COMPLETE:
				//�˳�ѡ��ģʽ����ʾ�༭�˵�
				adapter.setIsSelectMode(false);
				adapter.notifyDataSetChanged();
				showEditMenu();
				dialog.dismiss();
				break;
			case WHAT_UPDATE_DELETE_PROGRESS:
				dialog.updateProgressAndTitle(msg.arg1 + 1);
				break;

			}
		}
	};
	private DeleteMsgDialog dialog;
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��䲼���ļ�������view����
		View view = inflater.inflate(R.layout.fragment_conversation, null);
		
		lv_conversation_list = (ListView) view.findViewById(R.id.lv_conversation_list);
		
		bt_conversation_edit = (Button) view.findViewById(R.id.bt_conversation_edit);
		bt_conversation_new_msg = (Button) view.findViewById(R.id.bt_conversation_new_msg);
		bt_conversation_select_all = (Button) view.findViewById(R.id.bt_conversation_select_all);
		bt_conversation_cancel_select = (Button) view.findViewById(R.id.bt_conversation_cancel_select);
		bt_conversation_delete = (Button) view.findViewById(R.id.bt_conversation_delete);
		
		ll_conversation_edit_menu = (LinearLayout) view.findViewById(R.id.ll_conversation_edit_menu);
		ll_conversation_select_menu = (LinearLayout) view.findViewById(R.id.ll_conversation_select_menu);
		return view;
	}

	@Override
	public void initListener() {
		bt_conversation_edit.setOnClickListener(this);
		bt_conversation_new_msg.setOnClickListener(this);
		bt_conversation_select_all.setOnClickListener(this);
		bt_conversation_cancel_select.setOnClickListener(this);
		bt_conversation_delete.setOnClickListener(this);
		
		lv_conversation_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(adapter.getIsSelectMode()){
					//ѡ��ѡ��
					adapter.selectSingle(position);
				}
				else{
					//����Ự��ϸ
					Intent intent = new Intent(getActivity(), ConversationDetailActivity.class);
					//Я�����ݣ�address��thread_id
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation.createFromCursor(cursor);
					intent.putExtra("address", conversation.getAddress());
					intent.putExtra("thread_id", conversation.getThread_id());
					startActivity(intent);
				}
				
			}
		});
		
		lv_conversation_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor)adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				//�ж�ѡ�еĻỰ�Ƿ���������Ⱥ��
				if(ThreadGroupDao.hasGroup(getActivity().getContentResolver(), conversation.getThread_id())){
					//�ûỰ�Ѿ�����ӣ�����ConfirmDialog
					showExitDialog(conversation.getThread_id());
				}
				else{
					//�ûỰû�б���ӹ�������ListDialog���г�����Ⱥ��
					showSelectGroupDialog(conversation.getThread_id());
				}
				//���ѵ�����¼�������ᴫ�ݸ�OnItemClickListener
				return true;
			}
		});

	}

	@Override
	public void initData() {
		//����һ��CursorAdapter����
		adapter = new ConversationListAdapter(getActivity(), null);
		lv_conversation_list.setAdapter(adapter);
		
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		
		String[] projection = {
				"sms.body AS snippet",
				"sms.thread_id AS _id",
				"groups.msg_count AS msg_count",
				"address AS address",
				"date AS date"
		};
		//��ʼ�첽��ѯ
		//arg0��arg1����������Я��һ��int�ͺ�һ������
		//arg1:����Я��adapter���󣬲�ѯ��Ϻ��adapter����cursor
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, projection, null, null, "date desc");
//		Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_SMS_CONVERSATION, null, null, null, null);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_conversation_edit:
			showSelectMenu();
			//����ѡ��ģʽ
			adapter.setIsSelectMode(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_cancel_select:
			showEditMenu();
			//�˳�ѡ��ģʽ
			adapter.setIsSelectMode(false);
			adapter.cancelSelect();
			break;
		case R.id.bt_conversation_select_all:
			adapter.selectAll();
			break;
		case R.id.bt_conversation_delete:
			selectedConversationIds = adapter.getSelectedConversationIds();
			if(selectedConversationIds.size() == 0)
				return;
			showDeleteDialog();
//			deleteSms();
			break;
		case R.id.bt_conversation_new_msg:
			Intent intent = new Intent(getActivity(), NewMsgActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	/**
	 * ѡ��˵������ƶ����༭�˵������ƶ�
	 */
	private void showSelectMenu() {
		ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(ll_conversation_edit_menu.getHeight()).setDuration(200);
		//��ʱ200����ִ��run�����Ĵ���
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(0).setDuration(200);
			}
		}, 200);
		

	}
	
	private void showEditMenu() {
		ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(ll_conversation_edit_menu.getHeight()).setDuration(200);
		//��ʱ200����ִ��run�����Ĵ���
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(0).setDuration(200);
			}
		}, 200);

	}
	
	boolean isStopDelete = false;
	private void deleteSms() {
		dialog = DeleteMsgDialog.showDeleteDialog(getActivity(), selectedConversationIds.size(), new OnDeleteCancelListener() {
			
			@Override
			public void onCancel() {
				isStopDelete = true;
			}
		});
		
		Thread t = new Thread(){
			@Override
			public void run() {
				for(int i = 0; i < selectedConversationIds.size(); i++){
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//�ж�ɾ��
					if(isStopDelete){
						isStopDelete = false;
						break;
					}
					//ȡ�������еĻỰid,��id��Ϊwhere����ɾ�����з��������Ķ���
					String where = "thread_id = " + selectedConversationIds.get(i);
					getActivity().getContentResolver().delete(Constant.URI.URI_SMS, where, null);
					
					//������Ϣ����ɾ��������ˢ�£�ͬʱ�ѵ�ǰ��ɾ�����ȴ���������
					Message msg = handler.obtainMessage();
					msg.what = WHAT_UPDATE_DELETE_PROGRESS;
					//�ѵ�ǰɾ�����ȴ�����Ϣ��
					msg.arg1 = i;
					handler.sendMessage(msg);
				}
				//ɾ���Ự����ռ���
				selectedConversationIds.clear();
				handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
			}
		};
		t.start();

	}
	
	private void showDeleteDialog() {
		ConfirmDialog.showDialog(getActivity(), "��ʾ", "���Ҫɾ���Ự��", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				deleteSms();
			}
			
			@Override
			public void onCancel() {
			}
		}); 

	}
	
	private void showExitDialog(final int thread_id) {
		//��ͨ���Ựid��ѯȺ��id
		final int group_id= ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), thread_id);
		//ͨ��Ⱥ��id��ѯȺ������
		String name = GroupDao.getGroupNameByGroupId(getActivity().getContentResolver(), group_id);
		
		String message = "�ûỰ�Ѿ��������[" + name + "]Ⱥ�飬�Ƿ�Ҫ�˳���Ⱥ�飿";
		ConfirmDialog.showDialog(getActivity(), "��ʾ", message, new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				//��ѡ�еĻỰ��Ⱥ����ɾ��
				boolean isSuccess = ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), thread_id, group_id);
				ToastUtils.ShowToast(getActivity(), isSuccess? "�˳��ɹ�" : "�˳�ʧ��");
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	
	private void showSelectGroupDialog(final int thread_id) {
		//��ѯһ������ЩȺ�飬ȡ������ȫ������items
		final Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY, 
				null, null, null, null);
		if(cursor.getCount() == 0){
			ToastUtils.ShowToast(getActivity(), "��ǰû��Ⱥ�飬���ȴ���");
			return;
		}
		String[] items = new String[cursor.getCount()];
		//����cursor��ȡ������
		while(cursor.moveToNext()){
			Group group = Group.createFromCursor(cursor);
			//��ȡ����Ⱥ������֣�����Ⱥ����ȫ�����뵽һ��string���ϵ��С�
			items[cursor.getPosition()] = group.getName();
		}
		ListDialog.showDialog(getActivity(), "ѡ��Ⱥ��", items, new OnListDialogLietener() {
			
			@Override//�û�����ȷ����ť��
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//cursor���ǲ�ѯgroups��õ��ģ��������Ⱥ���������Ϣ
				cursor.moveToPosition(position);
				Group group = Group.createFromCursor(cursor);
				//��ָ���Ự����ָ��Ⱥ��
				boolean isSuccess = ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
				ToastUtils.ShowToast(getActivity(), isSuccess? "����ɹ�" : "����ʧ��");
			}
		});
	}
}
