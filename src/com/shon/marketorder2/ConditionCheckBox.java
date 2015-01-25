package com.shon.marketorder2;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class ConditionCheckBox extends LinearLayout implements ConditionInterface {
	CheckBox mCheckBoxConditionSetting;

	public ConditionCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        inflate(context, R.layout.conditioncheckbox, this);
        mCheckBoxConditionSetting = (CheckBox)this.findViewById(R.id.checkBoxConditionSetting);
	}

	@Override
	public void setTitle(String str) {
		// TODO Auto-generated method stub
		mCheckBoxConditionSetting.setText(str);
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		if (mCheckBoxConditionSetting.isChecked())
			return "true";
		return "false";
	}

	@Override
	public boolean checkMandatory() {
		// TODO Auto-generated method stub
		return false;
	}

}
