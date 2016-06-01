package com.sms.dao;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

public class SimpleQueryHandler extends AsyncQueryHandler {

	public SimpleQueryHandler(ContentResolver cr) {
		super(cr);
	}

	//��ѯ���ʱ����
	//arg0��arg1����ѯ��ʼʱЯ��������
	//arg2:��ѯ���
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);
//		CursorUtils.printCursor(cursor);
		if(cookie != null && cookie instanceof CursorAdapter){
			//��ѯ�õ���cursor������CursorAdapter��������cursor��������ʾ��listView
			((CursorAdapter)cookie).changeCursor(cursor);
		}
	}
}
