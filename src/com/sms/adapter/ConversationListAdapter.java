package com.sms.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sms.R;
import com.sms.bean.Conversation;
import com.sms.dao.ContactDao;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationListAdapter extends CursorAdapter {

	private boolean isSelectMode = false;
	//��¼ѡ��ģʽ��ѡ����Щ��Ŀ
	private List<Integer> selectedConversationIds = new ArrayList<Integer>();
	@SuppressWarnings("deprecation")
	public ConversationListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	//���ص�View�������listView����Ŀ
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_conversation_list, null);
	}

	//����listViewÿ����Ŀ��ʾ������
	@SuppressWarnings("deprecation")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getHolder(view);
		//����cursor���ݴ����Ự���󣬴�ʱcursor��ָ���Ѿ��ƶ�����ȷ��λ��
		Conversation conversation = Conversation.createFromCursor(cursor);
		
		//�жϵ�ǰ�Ƿ����ѡ��ģʽ
		if(isSelectMode){
			holder.iv_check.setVisibility(View.VISIBLE);
			//�жϼ������Ƿ�����Ựid���Ӷ�ȷ������Ŀ�Ƿ�ѡ��
			if(selectedConversationIds.contains(conversation.getThread_id())){
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_checked);
			}
			else{
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_normal);
			}
		}
		else{
			holder.iv_check.setVisibility(View.GONE);
		}
		
		//���ú���
		//�������ѯ�Ƿ������ϵ��
		String name = ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
		if(TextUtils.isEmpty(name)){
			holder.tv_conversation_address.setText(conversation.getAddress() + "(" + conversation.getMsg_count() + ")");
		}
		else{
			holder.tv_conversation_address.setText(name + "(" + conversation.getMsg_count() + ")");
		}
		holder.tv_conversation_body.setText(conversation.getSnippet());
		
		//����ʱ��
		//�ж��Ƿ����
		if(DateUtils.isToday(conversation.getDate())){
			//����ǣ���ʾʱ��
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
		}
		else{
			//������ǣ���ʾ������
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
		}
		
		//��ȡ��ϵ��ͷ��
		Bitmap avatar = ContactDao.getAvatarByAddress(context.getContentResolver(), conversation.getAddress());
		//�ж��Ƿ�ɹ��õ�ͷ��
		if(avatar == null){
			holder.iv_conversation_avatar.setBackgroundResource(R.drawable.img_default_avatar);
		}
		else{
			holder.iv_conversation_avatar.setBackgroundDrawable(new BitmapDrawable(avatar));
		}

	}

	//����������Ŀ��View����
	private ViewHolder getHolder(View view) {
		//���ж���Ŀview�������Ƿ���holder
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder == null){
			//���û�У��ʹ���һ����������view����
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	
	public boolean getIsSelectMode() {
		return isSelectMode;
	}

	public void setIsSelectMode(boolean isSelectMode) {
		this.isSelectMode = isSelectMode;
	}

	class ViewHolder{
		
		private ImageView iv_conversation_avatar;
		private TextView tv_conversation_address;
		private TextView tv_conversation_body;
		private TextView tv_conversation_date;
		private ImageView iv_check;

		//����������Ŀ��View����
		public ViewHolder(View view) {
			//�ڹ��췽������ɷ�װ��Ŀ���������
			iv_conversation_avatar = (ImageView) view.findViewById(R.id.iv_conversation_avatar);
			tv_conversation_address = (TextView) view.findViewById(R.id.tv_conversation_address);
			tv_conversation_body = (TextView) view.findViewById(R.id.tv_conversation_body);
			tv_conversation_date = (TextView) view.findViewById(R.id.tv_conversation_date);
			iv_check = (ImageView) view.findViewById(R.id.iv_check);
		}
	}
	
	/**
	 * ��ѡ�е���Ŀ���뼯��
	 */
	public void selectSingle(int position){
		//��cursor��ȡ��position��Ӧ�ĻỰ
		Cursor cursor = (Cursor) getItem(position);
		Conversation conversation = Conversation.createFromCursor(cursor);
		if(selectedConversationIds.contains(conversation.getThread_id())){
			//ǿתΪinteger,�����ǰѲ�����Ϊ����������Ҫɾ����Ԫ��
			selectedConversationIds.remove((Integer)conversation.getThread_id());
		}
		else{
			selectedConversationIds.add(conversation.getThread_id());
		}
		
		notifyDataSetChanged();
	}
	
	public void selectAll(){
		Cursor cursor = getCursor();
		cursor.moveToPosition(-1);
		//����cursorȡ�����лỰid
		//�����лỰid,ȫ����ӵ�������
		selectedConversationIds.clear();
		while(cursor.moveToNext()){
			Conversation conversation = Conversation.createFromCursor(cursor);
			selectedConversationIds.add(conversation.getThread_id());
		}
		
		notifyDataSetChanged();
	}
	
	public void cancelSelect(){
		//��ռ���
		selectedConversationIds.clear();
		notifyDataSetChanged();
	}

	public List<Integer> getSelectedConversationIds() {
		return selectedConversationIds;
	}

	
}
