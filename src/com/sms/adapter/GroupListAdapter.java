package com.sms.adapter;

import com.sms.R;
import com.sms.bean.Group;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GroupListAdapter extends CursorAdapter {

	public GroupListAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_group_list, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getHolder(view);
		
		//将结果集转化为一个bean对象。
		Group group = Group.createFromCursor(cursor);
		
		holder.tv_grouplist_name.setText(group.getName() + " (" + group.getThread_count() + ")");
		if(DateUtils.isToday(group.getCreate_date())){
			holder.tv_grouplist_date.setText(DateFormat.getTimeFormat(context).format(group.getCreate_date()));
		}
		else{
			holder.tv_grouplist_date.setText(DateFormat.getDateFormat(context).format(group.getCreate_date()));
		}

	}

	private ViewHolder getHolder(View view){
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder == null){
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	class ViewHolder{
		private TextView tv_grouplist_name;
		private TextView tv_grouplist_date;

		public ViewHolder(View view) {
			tv_grouplist_name = (TextView) view.findViewById(R.id.tv_grouplist_name);
			tv_grouplist_date = (TextView) view.findViewById(R.id.tv_grouplist_date);
		}
	}
}
