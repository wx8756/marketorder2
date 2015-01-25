package com.shon.marketorder2;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
//{
//	  "approvalInfo": [
//	    {
//	      "name": "审批类型",
//	      "value": "",
//	      "visible": "false"
//	    },
//	    {
//	      "name": "审批步骤",
//	      "value": "1",
//	      "visible": "false"
//	    },
//		{
//		  "name": "退回",
//		  "value": "1"
//		},
//		{
//		  "name": "增加流程",
//		  "value": "1"
//		},
//	    {
//	      "name": "单据号",
//	      "value": "1"
//	    },
//	    {
//	      "name": "让利类型",
//	      "value": "1"
//	    },
//	    {
//	      "name": "客户名称",
//	      "value": "1"
//	    },
//	    {
//	      "name": "首单号",
//	      "value": "1"
//	    },
//	    {
//	      "name": "报备单号",
//	      "value": "1"
//	    },
//	    {
//	      "name": "工程名称",
//	      "value": "1"
//	    },
//	    {
//	      "name": "详细地址",
//	      "value": "1"
//	    },
//	    {
//	      "name": "让利原因",
//	      "value": "1"
//	    },
//	    {
//	      "name": "联系人",
//	      "value": "1"
//	    },
//	    {
//	      "name": "联系电话",
//	      "value": "1"
//	    }
//	  ],
//	  "productInfo": {
//	    "header": [
//	      {
//	        "title": "名字",
//	        "width": 200,
//	        "align": "left"
//	      },
//	      {
//	        "title": "价格",
//	        "width": 200,
//	        "align": "left"
//	      },
//	      {
//	        "title": "日期",
//	        "width": 500,
//	        "align": "right"
//	      }
//	    ],
//	    "body": [
//	      [
//	        "商品1",
//	        "3.00",
//	        "20141124"
//	      ],
//	      [
//	        "商品2",
//	        "3.00",
//	        "20141124"
//	      ],
//	      [
//	        "商品3",
//	        "3.00",
//	        "20141124"
//	      ]
//	    ]
//	  }
//	}
public class ApprovalQuery extends Activity {

	final String strJsonKey_approvalInfo = "approvalInfo";
	final String strJsonKey_productInfo = "productInfo";
	final String strJsonKey_name = "name";
	final String strJsonKey_value = "value";
	final String strJsonKey_visible = "visible";
	final String strJsonKey_header = "header";
	final String strJsonKey_body = "body";

	final static int ApprovalQuery_Result = 200601;
	
	public String strApprovalList = "{\"page\":\"mainmenu\",\"title\":\"主菜单\",\"submenu\":[{\"page\":\"todo\",\"title\":\"待办事项\"},{\"page\":\"\",\"title\":\"当前库存查询\"},{\"page\":\"\",\"title\":\"销售价格查询\"},{\"page\":\"\",\"title\":\"生产计划\"},{\"page\":\"\",\"title\":\"销售分析报表\",\"submenu\":[{\"page\":\"\",\"title\":\"销售回笼数据一览表\"},{\"page\":\"\",\"title\":\"IFS销售回笼数据一览表\"},{\"page\":\"\",\"title\":\"销售排行榜\"},{\"page\":\"\",\"title\":\"客户手工发票明细表\"},{\"page\":\"\",\"title\":\"期间手工发票明细表\"},{\"page\":\"\",\"title\":\"销售结构类报表\",\"submenu\":[{\"page\":\"\",\"title\":\"公司销售结构表-产品\"},{\"page\":\"\",\"title\":\"公司销售结构表-规格\"},{\"page\":\"\",\"title\":\"事业部销售结构表-产品\"},{\"page\":\"\",\"title\":\"事业部销售结构表-规格\"},{\"page\":\"\",\"title\":\"业务部销售结构表-产品\"},{\"page\":\"\",\"title\":\"业务部销售结构表-规格\"},{\"page\":\"\",\"title\":\"销区销售结构表-产品\"},{\"page\":\"\",\"title\":\"销区销售结构表-规格\"},{\"page\":\"\",\"title\":\"客户销售结构表-产品\"},{\"page\":\"\",\"title\":\"客户销售结构表-规格\"},{\"page\":\"\",\"title\":\"集团客户销售结构表1\"},{\"page\":\"\",\"title\":\"集团客户销售结构表2\"},{\"page\":\"\",\"title\":\"集团客户销售结构表1-产品\"},{\"page\":\"\",\"title\":\"集团客户销售结构表2-产品\"}]}]}]}";
	public String strOperationType;
	public String strConditionSearchType;
	static Map<String,String> mNumberPrice = new HashMap<String,String>();
	static JSONArray mApprovalInfoJSONArray;
	static JSONObject mProductInfoJSONObject;
	LinearLayout mApprovalqueryLinearLayout;
	Button mButtonChangePrice;
	Button mButtonApproval;
	Button mButtonApprovalHistory;
	Button mButtonBack;
	
	@Override

	public void onConfigurationChanged(Configuration newConfig) {   

	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.approvalquery);
		mApprovalqueryLinearLayout = (LinearLayout)findViewById(R.id.approvalqueryLinearLayout);
		mButtonChangePrice = (Button)findViewById(R.id.buttonChangePrice);
		mButtonChangePrice.setOnClickListener(buttonChangePriceOnClickListener);
		mButtonApproval = (Button)findViewById(R.id.buttonApproval);
		mButtonApproval.setOnClickListener(buttonApprovalOnClickListener);
		mButtonApprovalHistory = (Button)findViewById(R.id.buttonApprovalHistory);
		mButtonApprovalHistory.setOnClickListener(buttonApprovalHistoryOnClickListener);
		mButtonBack = (Button)findViewById(R.id.buttonBack);
		mButtonBack.setOnClickListener(buttonBackOnClickListener);

        strApprovalList = this.getIntent().getStringExtra(getString(R.string.string_key_ApprovalList_result));
        strOperationType = this.getIntent().getStringExtra(getString(R.string.string_key_ApprovalList_OperationType));
        try {
			JSONObject conditionSearchObject = new JSONObject(strApprovalList);
			mApprovalInfoJSONArray = conditionSearchObject.getJSONArray(strJsonKey_approvalInfo);
			ConditionText conditionText;
			JSONObject approvalInfoItem;
			for (int index = 0; index<mApprovalInfoJSONArray.length(); index++)
			{
				approvalInfoItem = mApprovalInfoJSONArray.getJSONObject(index);
				if (approvalInfoItem.has(strJsonKey_visible) && !approvalInfoItem.getBoolean(strJsonKey_visible))
					continue;
				
				conditionText = new ConditionText(this);
				conditionText.setTitle(mApprovalInfoJSONArray.getJSONObject(index).getString(strJsonKey_name));
				conditionText.setText(mApprovalInfoJSONArray.getJSONObject(index).getString(strJsonKey_value));
				conditionText.setEnable(false);
	        	// addview
	        	mApprovalqueryLinearLayout.addView(conditionText);
			}
			
			mProductInfoJSONObject = conditionSearchObject.getJSONObject(strJsonKey_productInfo);
			JSONArray productlistJSONArray = mProductInfoJSONObject.getJSONArray(strJsonKey_body);
			JSONArray productlistItemJSONArray;
			mNumberPrice.clear();
			for (int index = 0; index<productlistJSONArray.length(); index++)
			{
				productlistItemJSONArray = productlistJSONArray.getJSONArray(index);
				// 0 行号 8 批示价格
				mNumberPrice.put(productlistItemJSONArray.getString(0), productlistItemJSONArray.getString(8));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private OnClickListener buttonChangePriceOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
            Intent ApprovalProductListIntent = new Intent(ApprovalQuery.this, ApprovalProductList.class);
            ApprovalQuery.this.startActivity(ApprovalProductListIntent);
		}
		
	};

	private OnClickListener buttonApprovalOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

            Intent ApprovalQueryIntent = new Intent(ApprovalQuery.this, Approval.class);
            ApprovalQueryIntent.putExtra(ApprovalQuery.this.getString(R.string.string_key_Approval_OperationType), strOperationType);
            ApprovalQuery.this.startActivityForResult(ApprovalQueryIntent, Approval.Approval_Result);
		}
		
	};
	
	private OnClickListener buttonApprovalHistoryOnClickListener = new OnClickListener(){

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			JSONArray paramsHeader = new JSONArray();

			//业务类型
			paramsHeader.put(strOperationType);
			
			JSONArray approvalInfoJSONArray = ApprovalQuery.mApprovalInfoJSONArray;
			try {
				// 4 单据号
				paramsHeader.put(approvalInfoJSONArray.getJSONObject(4).getString(strJsonKey_value));
				// 4 审批步骤号
				paramsHeader.put(approvalInfoJSONArray.getJSONObject(1).getString(strJsonKey_value));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JSONArray params = PublicMethod.postParam(ApprovalQuery.this, "APPROVALHISTORY", paramsHeader);

			String result = PublicMethod.httpPost(Login.mAddress, params.toString());
			if (result == null || result.isEmpty())
			{
	        	PublicMethod.displayToast(ApprovalQuery.this.getApplicationContext(), "error while " + strConditionSearchType + " :" + result);
				return;
			}
			
	    	try {
	    		JSONObject jsonCheckResult = new JSONObject(result);
		        if (jsonCheckResult.has("ERROR"))
		        {
		        	PublicMethod.displayToast(ApprovalQuery.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
		        	return;
		        }

	            Intent SearchConditionIntent = new Intent(ApprovalQuery.this, SearchResultView.class);
	            SearchConditionIntent.putExtra(ApprovalQuery.this.getString(R.string.string_key_SearchCondition_result), result);
	            ApprovalQuery.this.startActivity(SearchConditionIntent);
	            
		    } catch (JSONException e) {  
		    	Log.e(Login.TAG, "error while login:" + e.getMessage());
	        	return;
		    }
		}
		
	};
	
	private OnClickListener buttonBackOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ApprovalQuery.this.finish();
		}
		
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Approval.Approval_Success) {
			ApprovalQuery.this.setResult(Approval.Approval_Success);
			ApprovalQuery.this.finish();
		}
	}
}
