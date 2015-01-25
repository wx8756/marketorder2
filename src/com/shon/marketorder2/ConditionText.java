package com.shon.marketorder2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConditionText extends LinearLayout implements ConditionInterface {
	TextView mTextViewConditionTextTitle;
	EditText mEditTextConditionTextContent;

	public boolean isMandatory = false;
	
	public ConditionText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        inflate(context, R.layout.conditiontext, this);
        mTextViewConditionTextTitle = (TextView)this.findViewById(R.id.TextViewConditionTextTitle);
        mEditTextConditionTextContent = (EditText)this.findViewById(R.id.EditTextConditionTextContent);
	}

	public void setTitle(String str)
	{
		mTextViewConditionTextTitle.setText(str);
	}
	
	public void setText(String str)
	{
		mEditTextConditionTextContent.setText(str);
	}

	public String getText()
	{
		return mEditTextConditionTextContent.getText().toString();
	}
	
	public void setEnable(boolean isEnable)
	{
		mEditTextConditionTextContent.setEnabled(isEnable);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean checkMandatory() {
		if (isMandatory && getText().isEmpty())
		{
			Toast.makeText(ConditionText.this.getContext(), mTextViewConditionTextTitle.getText() + "不能为空", Toast.LENGTH_LONG).show();
			return false;
		}
		else
			return true;
	}
}
