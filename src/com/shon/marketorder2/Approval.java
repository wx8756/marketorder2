package com.shon.marketorder2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Approval extends Activity {
	final String strJsonKey_name = "name";
	final String strJsonKey_value = "value";
	final String strJsonKey_body = "body";
	final String strJsonKey_code = "code";

	final static int Approval_Success = 200902;
	final static int Approval_Result = 200901;
	
	LinearLayout mApprovalLinearLayoutLeft;
//	LinearLayout mApprovalLinearLayoutRight;
	EditText mEditTextOpinion;
	Button mButtonAgree;
	Button mButtonReturn;
	Button mButtonDisagree;
	Button mButtonBack;
	View mButton;
	String mStrOperationType;

	List<String> mApprovalPersonCodeSelectList = new ArrayList<String>();;
	List<String> mApprovalPersonNameSelectList = new ArrayList<String>();;
	ConditionRadio mApprovalPersonConditionRadio = null;
	ConditionCheckBox mApprovalPersonConditionCheckBox = null;
	
	@Override

	public void onConfigurationChanged(Configuration newConfig) {   

	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.approval);
		mApprovalLinearLayoutLeft = (LinearLayout)findViewById(R.id.approvalLinearLayoutLeft);
//		mApprovalLinearLayoutRight = (LinearLayout)findViewById(R.id.approvalLinearLayoutRight);
		
		mEditTextOpinion = (EditText)findViewById(R.id.editTextOpinion);
		
		mButtonAgree = (Button)findViewById(R.id.buttonAgree);
		mButtonAgree.setOnClickListener(buttonAgreeOnClickListener);
		mButtonReturn = (Button)findViewById(R.id.buttonReturn);
		mButtonReturn.setOnClickListener(buttonAgreeOnClickListener);
		mButtonDisagree = (Button)findViewById(R.id.buttonDisagree);
		mButtonDisagree.setOnClickListener(buttonAgreeOnClickListener);
		//《同意》1，《退回》2，《不同意》0
		mButtonAgree.setTag(1);
		mButtonReturn.setTag(2);
		mButtonDisagree.setTag(0);
		
		mButtonBack = (Button)findViewById(R.id.buttonBack);
		mButtonBack.setOnClickListener(buttonBackOnClickListener);

		mStrOperationType = this.getIntent().getStringExtra(getString(R.string.string_key_Approval_OperationType));
		ConditionText conditionTextOperationType = new ConditionText(this);
		conditionTextOperationType.setTitle("业务类型");
		conditionTextOperationType.setText(mStrOperationType);
		conditionTextOperationType.setEnable(false);
		mApprovalLinearLayoutLeft.addView(conditionTextOperationType);
    	
		try {
			JSONArray approvalInfoJSONArray = ApprovalQuery.mApprovalInfoJSONArray;

			ConditionText conditionTextBillsNumber = new ConditionText(this);
			// 4 单据号
			conditionTextBillsNumber.setTitle(approvalInfoJSONArray.getJSONObject(4).getString(strJsonKey_name));
			conditionTextBillsNumber.setText(approvalInfoJSONArray.getJSONObject(4).getString(strJsonKey_value));
			conditionTextBillsNumber.setEnable(false);
        	// addview
			mApprovalLinearLayoutLeft.addView(conditionTextBillsNumber);

        	ConditionText conditionTextCustomerName = new ConditionText(this);
			// 7 客户名称
        	conditionTextCustomerName.setTitle(approvalInfoJSONArray.getJSONObject(7).getString(strJsonKey_name));
        	conditionTextCustomerName.setText(approvalInfoJSONArray.getJSONObject(7).getString(strJsonKey_value));
        	conditionTextCustomerName.setEnable(false);
        	// addview
        	mApprovalLinearLayoutLeft.addView(conditionTextCustomerName);
        	
			//当“退回”value为1时，退回按钮不显示 
			//2 退回   approvalInfoJSONArray.getString(0).contentEquals("200")
			if (//approvalInfoJSONArray.getJSONObject(2).getString(strJsonKey_name).contentEquals("退回") && 
					approvalInfoJSONArray.getJSONObject(2).getInt(strJsonKey_value) == 0)
			{
				mButtonReturn.setVisibility(View.GONE);
			}

			//当“”value为1时，显示 增加流程 选项 
			//3 增加流程
			if (//approvalInfoJSONArray.getJSONObject(3).getString(strJsonKey_name).contentEquals("增加流程") &&
					approvalInfoJSONArray.getJSONObject(3).getInt(strJsonKey_value) == 1)
        	{
				getApprovalPerson();

				mApprovalPersonConditionCheckBox = new ConditionCheckBox(this);
				mApprovalPersonConditionCheckBox.setTitle("是否增加审批人");
            	// addview
				mApprovalLinearLayoutLeft.addView(mApprovalPersonConditionCheckBox);
				
				mApprovalPersonConditionRadio = new ConditionRadio(this);
				mApprovalPersonConditionRadio.setTitle("增加审批人");
				mApprovalPersonConditionRadio.setSelectItem(mApprovalPersonNameSelectList);
            	// addview
				mApprovalLinearLayoutLeft.addView(mApprovalPersonConditionRadio);
        	}


        	
//        	ConditionDate conditionDate = new ConditionDate(this);
//        	conditionDate.setTitle("审批日期");
//        	// addview
//        	mApprovalLinearLayoutRight.addView(conditionDate);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressLint("NewApi")
	private void getApprovalPerson()
	{
		JSONArray paramsHeader = new JSONArray();

		JSONArray params = PublicMethod.postParam(Approval.this, "APPROVALPERSON", paramsHeader);

		String result = PublicMethod.httpPost(Login.mAddress, params.toString());
		if (result == null || result.isEmpty())
		{
        	PublicMethod.displayToast(Approval.this.getApplicationContext(), "error while APPROVALPERSON :" + result);
			return;
		}
		
    	try {
    		JSONObject jsonCheckResult = new JSONObject(result);
	        if (jsonCheckResult.has("ERROR"))
	        {
	        	PublicMethod.displayToast(Approval.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
	        	return;
	        }
	        //{"body":[["300001","陈云添"],["300002","魏锦桐"]]}
	        JSONArray resultJSONArray = jsonCheckResult.getJSONArray(strJsonKey_body);
    		for (int selectItemIndex=0; selectItemIndex < resultJSONArray.length(); selectItemIndex++)
    		{
    			mApprovalPersonCodeSelectList.add(resultJSONArray.getJSONArray(selectItemIndex).getString(0));
        		mApprovalPersonNameSelectList.add(resultJSONArray.getJSONArray(selectItemIndex).getString(1));
    		}
	    } catch (JSONException e) {  
	    	Log.e(Login.TAG, "error while login:" + e.getMessage());
        	return;
	    }
	}

	private OnClickListener buttonAgreeOnClickListener = new OnClickListener(){

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mButton = v;
			String result = approvalProcess("0", mButton.getTag().toString());
			if (result == null || result.isEmpty())
			{
	        	PublicMethod.displayToast(Approval.this.getApplicationContext(), "error while APPROVALORDER :" + result);
				return;
			}
			
	    	try {
		        if (result.contains("ERROR"))
		        {
		        	PublicMethod.displayToast(Approval.this.getApplicationContext(), result);
		        	return;
		        }
		        
	    		JSONArray jsonArrayCheckResult = new JSONArray(result);
	    		if (jsonArrayCheckResult.getJSONArray(1).getString(0).contentEquals("0"))
	    		{
	    			Dialog dialog = new AlertDialog.Builder(Approval.this)
	    			.setTitle(R.string.string_prompt)
	    			.setMessage(jsonArrayCheckResult.getJSONArray(1).getString(1))
	    			.setPositiveButton("确定", 
	    					new DialogInterface.OnClickListener() {

	    						@Override
	    						public void onClick(DialogInterface dialog, int which) {
	    							String result = approvalProcess("1", mButton.getTag().toString());
	    							if (result == null || result.isEmpty())
	    							{
	    					        	PublicMethod.displayToast(Approval.this.getApplicationContext(), "error while APPROVALORDER :" + result);
	    								return;
	    							}
	    							
	    					    	try {
	    						        if (result.contains("ERROR"))
	    						        {
	    						        	PublicMethod.displayToast(Approval.this.getApplicationContext(), result);
	    						        	return;
	    						        }

	    					    		JSONArray jsonArrayCheckResult = new JSONArray(result);
		    				    		if (jsonArrayCheckResult.getJSONArray(1).getString(0).contentEquals("1"))
		    				    		{
		    				    			PublicMethod.displayToast(Approval.this.getApplicationContext(), "审批成功");
			    			    			Approval.this.setResult(Approval.Approval_Success);
			    			    			Approval.this.finish();
		    				    			return;
		    				    		}
		    				        	
	
		    			    			PublicMethod.displayToast(Approval.this.getApplicationContext(), result);

		    			    			Approval.this.finish();
		    					    } catch (JSONException e) {  
		    					    	Log.e(Login.TAG, "error while login:" + e.getMessage());
		    				        	return;
		    					    }
	    						}
	    					})
	    			.setNegativeButton(R.string.string_cancel, 
	    					new DialogInterface.OnClickListener() {
	    					
	    						@Override
	    						public void onClick(DialogInterface dialog, int which) {
	    			    			Approval.this.finish();
	    						}
	    					}).create();
	    	        dialog.show();
	    	        return;
	    		}
	    		else if (jsonArrayCheckResult.getJSONArray(1).getString(0).contentEquals("1"))
	    		{
	    			PublicMethod.displayToast(Approval.this.getApplicationContext(), "审批成功");
	    			Approval.this.setResult(Approval.Approval_Success);
	    			Approval.this.finish();
	    			return;
	    		}
	    		else if (jsonArrayCheckResult.getJSONArray(1).getString(0).contentEquals("2"))
	    		{
	    			PublicMethod.displayToast(Approval.this.getApplicationContext(), jsonArrayCheckResult.getJSONArray(1).getString(2));
	    			Approval.this.finish();
	    			return;
	    		}
	        	

    			PublicMethod.displayToast(Approval.this.getApplicationContext(), result);
    			Approval.this.finish();
		    } catch (JSONException e) {  
		    	Log.e(Login.TAG, "error while login:" + e.getMessage());
	        	return;
		    }
		}
		
	};
	
	private String approvalProcess(String approvalTag, String buttonTag)
	{
		JSONArray paramsHeader = new JSONArray();

		paramsHeader.put(approvalTag);
		//业务类型
		paramsHeader.put(mStrOperationType);
		
		JSONArray approvalInfoJSONArray = ApprovalQuery.mApprovalInfoJSONArray;
		try {
			// 4 单据号
			paramsHeader.put(approvalInfoJSONArray.getJSONObject(4).getString(strJsonKey_value));
			// 1 审批步骤号
			paramsHeader.put(approvalInfoJSONArray.getJSONObject(1).getString(strJsonKey_value));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//审批意见
		paramsHeader.put(mEditTextOpinion.getText());
		paramsHeader.put(buttonTag);
		if (mApprovalPersonConditionRadio != null && 
				mApprovalPersonConditionCheckBox != null && 
				mApprovalPersonConditionCheckBox.getText().contentEquals("true"))
		{
			int location = 0;
			for (; location < mApprovalPersonNameSelectList.size(); location++)
			{
				if (mApprovalPersonConditionRadio.getText().contentEquals(mApprovalPersonNameSelectList.get(location)))
					break;
			}
			paramsHeader.put("1");
			paramsHeader.put(mApprovalPersonCodeSelectList.get(location));
		}
		else
		{
			paramsHeader.put("0");
			paramsHeader.put("0");
		}
		
		JSONArray params = PublicMethod.postParam(Approval.this, "APPROVALORDER", paramsHeader);

		JSONArray paramsRow;
		for (String key:ApprovalQuery.mNumberPrice.keySet())
		{
			paramsRow = new JSONArray();
			paramsRow.put(key);
			paramsRow.put(ApprovalQuery.mNumberPrice.get(key));

			params.put(paramsRow);
		}
		
		return PublicMethod.httpPost(Login.mAddress, params.toString());
	}
	
	private OnClickListener buttonBackOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Approval.this.finish();
		}
		
	};
}
