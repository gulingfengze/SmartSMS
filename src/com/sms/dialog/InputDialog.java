package com.sms.dialog;

import com.sms.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputDialog extends BaseDialog {

	private String title;
	private TextView tv_inputdialog_title;
	private EditText et_inputdialog_message;
	private Button bt_inputdialog_confirm;
	private Button bt_inputdialog_cancel;
	private OnInputDialogListener onInputDialogListener;
	
	protected InputDialog(Context context, String title, OnInputDialogListener onInputDialogListener) {
		super(context);
		this.title = title;
		this.onInputDialogListener = onInputDialogListener;
	}
	//�������ṩ��̬��������ȡ����Ķ���
	public static void showDialog(Context context, String title, OnInputDialogListener onInputDialogListener){
		InputDialog dialog = new InputDialog(context, title, onInputDialogListener);
		//�Ի���Ĭ�ϲ�֧���ı����룬�ֶ���һ�����������Ϊ�Ի�������ݣ�Android�Զ������������
		dialog.setView(new EditText(context));
		dialog.show();
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_input);
		tv_inputdialog_title = (TextView) findViewById(R.id.tv_inputdialog_title);
		et_inputdialog_message = (EditText) findViewById(R.id.et_inputdialog_message);
		bt_inputdialog_cancel = (Button) findViewById(R.id.bt_inputdialog_cancel);
		bt_inputdialog_confirm = (Button) findViewById(R.id.bt_inputdialog_confirm);
	}

	@Override
	public void initListener() {
		bt_inputdialog_cancel.setOnClickListener(this);
		bt_inputdialog_confirm.setOnClickListener(this);

	}

	@Override
	public void initData() {
		tv_inputdialog_title.setText(title);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_inputdialog_cancel:
			if(onInputDialogListener != null){
				onInputDialogListener.onCancel();
			}
			break;
		case R.id.bt_inputdialog_confirm:
			if(onInputDialogListener != null){
				//��ΪOnInputDialogListener��ʵ�������et_inputdialog_message�����㣬���԰�������������Ϊ�������������Ļص���
				onInputDialogListener.onConfirm(et_inputdialog_message.getText().toString());
			}
			break;
		}
		dismiss();
	}
	
	public interface OnInputDialogListener{
		void onCancel();
		void onConfirm(String text);
		
	}

}
