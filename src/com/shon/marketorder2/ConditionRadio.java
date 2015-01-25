package com.shon.marketorder2;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConditionRadio extends LinearLayout implements ConditionInterface {
	TextView mTextViewConditionRadioTitle;
	Button mButtonConditionRadioContent;
    List<String> mSelectItem = null; 

	public ConditionRadio(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        inflate(context, R.layout.conditionradio, this);
        mTextViewConditionRadioTitle = (TextView)this.findViewById(R.id.TextViewConditionRadioTitle);
        mButtonConditionRadioContent = (Button)this.findViewById(R.id.buttonConditionRadioSetting);

        mButtonConditionRadioContent.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) { 
				AlertDialog.Builder builder = new AlertDialog.Builder(ConditionRadio.this.getContext());  
			   
			    //设置Dialog的标题  
			    builder.setTitle("选择"+ mTextViewConditionRadioTitle.getText()); 
			    if (mSelectItem == null)
			    	return;
			    //设置可供选择的ListView  
			    builder.setItems((String[])mSelectItem.toArray(new String[mSelectItem.size()]), new DialogInterface.OnClickListener() {  
			   
			        @Override  
			        public void onClick(DialogInterface dialog, int which) {  
			        	mButtonConditionRadioContent.setText(mSelectItem.get(which));  
			        }  
			    });  
			   
			    AlertDialog ad = builder.create();  
			   
			    ad.show();
			}
			
		});
	}

	public void setTitle(String str)
	{
		mTextViewConditionRadioTitle.setText(str);
	}
	
	public void setSelectItem(List<String> selectItem)
	{
		mSelectItem = selectItem;
    	mButtonConditionRadioContent.setText(mSelectItem.get(0));  
	}

	public String getText()
	{
		return mButtonConditionRadioContent.getText().toString();
	}
	
	public void setText(String str)
	{
    	mButtonConditionRadioContent.setText(str); 
	}
	
	@Override
	public boolean checkMandatory() {
		return true;
	}
}
