package com.sms.dao;

import java.util.List;

import com.sms.globle.Constant;
import com.sms.receiver.SendSmsReceiver;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SmsDao {

	public static void sendSms(Context context, String address, String body){
		SmsManager manager = SmsManager.getDefault();
		List<String> smss = manager.divideMessage(body);
		
		Intent intent = new Intent(SendSmsReceiver.ACTION_SEND_SMS);
		//���ŷ���ȥ��ϵͳ�ᷢ��һ���㲥����֪���Ƕ��ŷ����ǳɹ�����ʧ��
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for (String text : smss) {
			//���apiֻ���𷢶��ţ�����Ѷ���д�����ݿ�
			manager.sendTextMessage(address, null, text, sentIntent, null);
		
			//�Ѷ��Ų���������ݿ�
			insertSms(context, address, text);
		}
	}
	
	public static void insertSms(Context context, String address, String body){
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("body", body);
		values.put("type", Constant.SMS.TYPE_SEND);
		
		context.getContentResolver().insert(Constant.URI.URI_SMS, values);
	}
}
