package com.sms.dao;

import java.io.InputStream;

import com.sms.utils.CursorUtils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class ContactDao {
	/**
	 * 通过号码获取联系人名字
	 * @param resolver
	 * @param address
	 * @return
	 */
	public static String getNameByAddress(ContentResolver resolver, String address){
		String name = null;
		//把号码拼接在uri后面
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		//根据号码查询联系人名字
		Cursor cursor = resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
//		CursorUtils.printCursor(cursor);
		if(cursor.moveToFirst()){
			name = cursor.getString(0);
			cursor.close();
		}
		return name;
	}
	
	/**
	 * 通过号码获取联系人头像
	 * @param resolver
	 * @param address
	 * @return
	 */
	public static Bitmap getAvatarByAddress(ContentResolver resolver, String address){
		Bitmap avatar = null;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = resolver.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
		if(cursor.moveToFirst()){
			String _id = cursor.getString(0);
			
			//获取联系人照片
			InputStream is = Contacts.openContactPhotoInputStream(resolver, Uri.withAppendedPath(Contacts.CONTENT_URI, _id));
			avatar = BitmapFactory.decodeStream(is);
		}
		return avatar;
		
	}
}
