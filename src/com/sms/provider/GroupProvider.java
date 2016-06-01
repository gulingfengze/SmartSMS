package com.sms.provider;

import com.sms.dao.GroupOpenHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GroupProvider extends ContentProvider {

	GroupOpenHelper helper;
	private SQLiteDatabase db;
	private static final String TABLE_GROUPS = "groups";
	private static final String TABLE_THREAD_GROUP = "thread_group";
	
	private static final String authority = "com.itheima.momo";
	public static final Uri BASE_URI = Uri.parse("content://" + authority);
	
	static final int CODE_GROUPS_INSERT = 0;
	static final int CODE_GROUPS_QUERY = 1;
	static final int CODE_GROUPS_UPDATE = 2;
	static final int CODE_GROUPS_DELETE = 3;
	static final int CODE_THREAD_GROUP_INSERT = 4;
	static final int CODE_THREAD_GROUP_QUERY = 5;
	static final int CODE_THREAD_GROUP_UPDATE = 6;
	static final int CODE_THREAD_GROUP_DELETE = 7;
	
	UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	{
		//���ƥ�����
		matcher.addURI(authority , "groups/insert", CODE_GROUPS_INSERT);
		matcher.addURI(authority , "groups/query", CODE_GROUPS_QUERY);
		matcher.addURI(authority , "groups/update", CODE_GROUPS_UPDATE);
		matcher.addURI(authority , "groups/delete", CODE_GROUPS_DELETE);
		matcher.addURI(authority , "thread_group/insert", CODE_THREAD_GROUP_INSERT);
		matcher.addURI(authority , "thread_group/query", CODE_THREAD_GROUP_QUERY);
		matcher.addURI(authority , "thread_group/update", CODE_THREAD_GROUP_UPDATE);
		matcher.addURI(authority , "thread_group/delete", CODE_THREAD_GROUP_DELETE);
	}
	@Override
	public boolean onCreate() {
		helper = GroupOpenHelper.getInstance(getContext());
		db = helper.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		switch (matcher.match(uri)) {
		//����Ǳ�ʾ��ͬ�ı�����id�жϳ���ͬ�ı�Ȼ��ѡ����롣
		case CODE_GROUPS_QUERY:
			Cursor cursor = db.query(TABLE_GROUPS, projection, selection, selectionArgs, null, null, sortOrder);
			
			//����uri�����ݸı�һ�������ݹ۲���
			//ֻҪ��uri�ϵ����ݸı䣬���ݹ۲��߻����̷��֣����²�ѯ
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		case CODE_THREAD_GROUP_QUERY:
			cursor = db.query(TABLE_THREAD_GROUP, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_INSERT:
			long rawId = db.insert(TABLE_GROUPS, null, values);
			//����ʧ��
			if(rawId == -1){
				return null;
			}
			else{
				//������Լ����������ݿ�ֻ�ܵ���notifychange�����������æˢ�¡�
				getContext().getContentResolver().notifyChange(BASE_URI, null);
				//�ѷ��ص���id��ƴ����uri�ĺ��棬Ȼ�󷵻�
				return ContentUris.withAppendedId(uri, rawId);
			}
		case CODE_THREAD_GROUP_INSERT:
			rawId = db.insert(TABLE_THREAD_GROUP, null, values);
			//����ʧ��
			if(rawId == -1){
				return null;
			}
			else{
				getContext().getContentResolver().notifyChange(BASE_URI, null);
				//�ѷ��ص���id��ƴ����uri�ĺ��棬Ȼ�󷵻�
				return ContentUris.withAppendedId(uri, rawId);
			}
			
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_DELETE:
			int number = db.delete(TABLE_GROUPS, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		case CODE_THREAD_GROUP_DELETE:
			number = db.delete(TABLE_THREAD_GROUP, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_UPDATE:
			int number = db.update(TABLE_GROUPS, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		case CODE_THREAD_GROUP_UPDATE:
			number = db.update(TABLE_THREAD_GROUP, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
	}

}
