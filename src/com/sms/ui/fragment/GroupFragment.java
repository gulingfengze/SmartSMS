package com.sms.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.sms.R;
import com.sms.adapter.GroupListAdapter;
import com.sms.base.BaseFragment;
import com.sms.bean.Group;
import com.sms.dao.GroupDao;
import com.sms.dao.SimpleQueryHandler;
import com.sms.dialog.InputDialog;
import com.sms.dialog.ListDialog;
import com.sms.dialog.InputDialog.OnInputDialogListener;
import com.sms.dialog.ListDialog.OnListDialogLietener;
import com.sms.globle.Constant;
import com.sms.ui.activity.GroupDetailActivity;
import com.sms.utils.ToastUtils;

public class GroupFragment extends BaseFragment {

	private Button bt_group_newgroup;
	private ListView lv_group_list;
	private GroupListAdapter adapter;
	private SimpleQueryHandler queryHandler;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_group, null);
		bt_group_newgroup = (Button) view.findViewById(R.id.bt_group_newgroup);
		lv_group_list = (ListView) view.findViewById(R.id.lv_group_list);
		return view;
	}

	@Override
	public void initListener() {
		bt_group_newgroup.setOnClickListener(this);
		//����Ŀ���ó�������
		lv_group_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor  = (Cursor) adapter.getItem(position);
				final Group group = Group.createFromCursor(cursor);
				//����֮���������һ���Ի���
				ListDialog.showDialog(getActivity(), "ѡ�����", new String[]{"�޸�","ɾ��"}, new OnListDialogLietener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						switch (position) {
						case 0://�޸�
							//��������Ի���
							InputDialog.showDialog(getActivity(), "�޸�Ⱥ��", new OnInputDialogListener() {
								
								@Override
								public void onConfirm(String text) {
									//ȷ���޸�Ⱥ������
									GroupDao.updateGroupName(getActivity().getContentResolver(), text, group.get_id());
								}
								@Override
								public void onCancel() {
								}
							});
							break;
						case 1://ɾ��
							GroupDao.deleteGroup(getActivity().getContentResolver(), group.get_id());
							break;
						}
					}
				});
				return false;
			}
		});
		lv_group_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��תʱЯ��Ⱥ�����֡�Ⱥ��id
				Cursor cursor = (Cursor) adapter.getItem(position);
				Group group = Group.createFromCursor(cursor);
				if(group.getThread_count() > 0){
					//���Ⱥ���б����Ŀ����ת��Ⱥ����ϸActivity
					Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
					intent.putExtra("name", group.getName());
					intent.putExtra("group_id", group.get_id());
					startActivity(intent);
				}
				else{
					ToastUtils.ShowToast(getActivity(), "��ǰȺ��û���κλỰ");
				}
			}
		});
	}

	@Override
	public void initData() {
		adapter = new GroupListAdapter(getActivity(), null);
		lv_group_list.setAdapter(adapter);
		
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_GROUP_QUERY, null, null, null, "create_date desc");
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_group_newgroup:
			InputDialog.showDialog(getActivity(), "����Ⱥ��", new OnInputDialogListener() {
				
				@Override//���ｫ�û������������ΪonConfirm�����Ĳ������ݽ�ȥ��
				public void onConfirm(String text) {
					if(!TextUtils.isEmpty(text)){
						GroupDao.insertGroup(getActivity().getContentResolver(), text);
					}
					else{
						ToastUtils.ShowToast(getActivity(), "Ⱥ��������Ϊ��");
					}
				}
				@Override
				public void onCancel() {
				}
			});
			break;
		}
	}

}
