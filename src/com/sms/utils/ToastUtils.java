package com.sms.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void ShowToast(Context context, String msg){
		Toast.makeText(context, msg, 0).show();
	}
}
