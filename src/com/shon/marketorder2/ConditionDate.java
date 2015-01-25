package com.shon.marketorder2;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConditionDate extends LinearLayout implements ConditionInterface {
	TextView mTextViewConditionDateTitle;
	Button mButtonConditionDateSetting;

	public ConditionDate(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        inflate(context, R.layout.conditiondate, this);
        mTextViewConditionDateTitle = (TextView)this.findViewById(R.id.TextViewConditionDateTitle);
        mButtonConditionDateSetting = (Button)this.findViewById(R.id.buttonConditionDateSetting);
        this.setDate(Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        
        mButtonConditionDateSetting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) { 
				DatePickerDialog datePicker = new DatePickerDialog(ConditionDate.this.getContext(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
						ConditionDate.this.setDate(year, monthOfYear+1, dayOfMonth);
	//					Toast.makeText(AnalogDigitalClock.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
					}
				}, 
				Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
				datePicker.show();
			}
			
		});
	}

	private void setDate(int year, int month,int day)
	{
		mButtonConditionDateSetting.setText(String.format("%04d%02d%02d", year, month, day));
	}
	
	public String getText()
	{
		return mButtonConditionDateSetting.getText().toString();
	}
	
	public void setTitle(String str)
	{
		mTextViewConditionDateTitle.setText(str);
	}

	@Override
	public boolean checkMandatory() {
		return true;
	}
}
