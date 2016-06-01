package com.sms.dao;

import com.sms.globle.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThreadGroupDao {

	public static boolean hasGroup(ContentResolver resolver, int thread_id){
		//ֻҪ�ܲ鵽������˵������Ự�Ѿ�������thread_group����
		Cursor cursor = resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, null, "thread_id = " + thread_id, null, null);
		return cursor.moveToNext();
	}
	
	/**
	 * ͨ���Ựidȥ��ѯȺ��id
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static int getGroupIdByThreadId(ContentResolver resolver, int thread_id){
		Cursor cursor = resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, new String[]{"group_id"}, "thread_id = " + thread_id, null, null);
		cursor.moveToFirst();
		int group_id = cursor.getInt(0);
		return group_id;
	}
	
	/**
	 * �ѻỰ�����Ⱥ��
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean insertThreadGroup(ContentResolver resolver, int thread_id, int group_id){
		ContentValues values = new ContentValues();
		values.put("thread_id", thread_id);
		values.put("group_id", group_id);
		Uri uri = resolver.insert(Constant.URI.URI_THREAD_GROUP_INSERT, values);
		
		if(uri != null){
		//����Ự�󣬸ı�Ⱥ��ĻỰ����
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count + 1);
		}
		return uri != null;
	}
	
	/**
	 * ��Ⱥ����ɾ���Ự
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean deleteThreadGroupByThreadId(ContentResolver resolver, int thread_id, int group_id){
		int number = resolver.delete(Constant.URI.URI_THREAD_GROUP_DELETE, "thread_id = "+ thread_id, null);
		
		if(number > 0){
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count - 1);
		}
		
		return number > 0;
	}
}
