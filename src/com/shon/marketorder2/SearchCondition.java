package com.shon.marketorder2;

import java.util.ArrayList;
import java.util.List;

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

public class SearchCondition extends Activity {
	public final static String page_tag = "conditionsearch";
	
	final String strJsonKey_type = "type";
	final String strJsonKey_title = "title";
	final String strJsonKey_mandatory = "mandatory";
	final String strJsonKey_defaultvalue = "defaultvalue";
	final String strJsonKey_selectItem = "selectItem";
	final String strJsonKey_condition = "condition";

	final String strType_text = "text";
	final String strType_date = "date";
	final String strType_radio = "radio";

//	{
//		  "condition": [
//		    {
//		      "type": "text",
//		      "title": "姓名",
//		      "mandatory": true
//		    },
//		    {
//		      "type": "date",
//		      "title": "创建日期"
//		    },
//		    {
//		      "type": "radio",
//		      "title": "星期",
//		      "selectItem": [
//		        "星期一",
//		        "星期二"
//		      ]
//		    }
//		  ],
//		  "type": "order"
//	}
	public String strConditionSearch = "{\"page\":\"mainmenu\",\"title\":\"主菜单\",\"submenu\":[{\"page\":\"todo\",\"title\":\"待办事项\"},{\"page\":\"\",\"title\":\"当前库存查询\"},{\"page\":\"\",\"title\":\"销售价格查询\"},{\"page\":\"\",\"title\":\"生产计划\"},{\"page\":\"\",\"title\":\"销售分析报表\",\"submenu\":[{\"page\":\"\",\"title\":\"销售回笼数据一览表\"},{\"page\":\"\",\"title\":\"IFS销售回笼数据一览表\"},{\"page\":\"\",\"title\":\"销售排行榜\"},{\"page\":\"\",\"title\":\"客户手工发票明细表\"},{\"page\":\"\",\"title\":\"期间手工发票明细表\"},{\"page\":\"\",\"title\":\"销售结构类报表\",\"submenu\":[{\"page\":\"\",\"title\":\"公司销售结构表-产品\"},{\"page\":\"\",\"title\":\"公司销售结构表-规格\"},{\"page\":\"\",\"title\":\"事业部销售结构表-产品\"},{\"page\":\"\",\"title\":\"事业部销售结构表-规格\"},{\"page\":\"\",\"title\":\"业务部销售结构表-产品\"},{\"page\":\"\",\"title\":\"业务部销售结构表-规格\"},{\"page\":\"\",\"title\":\"销区销售结构表-产品\"},{\"page\":\"\",\"title\":\"销区销售结构表-规格\"},{\"page\":\"\",\"title\":\"客户销售结构表-产品\"},{\"page\":\"\",\"title\":\"客户销售结构表-规格\"},{\"page\":\"\",\"title\":\"集团客户销售结构表1\"},{\"page\":\"\",\"title\":\"集团客户销售结构表2\"},{\"page\":\"\",\"title\":\"集团客户销售结构表1-产品\"},{\"page\":\"\",\"title\":\"集团客户销售结构表2-产品\"}]}]}]}";
	public String strConditionSearchType;
	LinearLayout mSearchconditionLinearLayoutLeft;
	LinearLayout mSearchconditionLinearLayoutRight;
	Button mButtonSearch;
	Button mButtonSearchConditionBack;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {   

	    super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.searchcondition);
		mSearchconditionLinearLayoutLeft = (LinearLayout)findViewById(R.id.searchconditionLinearLayoutLeft);
		mSearchconditionLinearLayoutRight = (LinearLayout)findViewById(R.id.searchconditionLinearLayoutRight);
		mButtonSearch = (Button)findViewById(R.id.buttonSearch);
		mButtonSearch.setOnClickListener(buttonSearchOnClickListener);
		mButtonSearchConditionBack = (Button)findViewById(R.id.buttonSearchConditionBack);
		mButtonSearchConditionBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchCondition.this.finish();
			}
			
		});
		
		layoutSearchCondition();
	}
	
	// 布局查询条件
	private void layoutSearchCondition()
	{
        strConditionSearch = this.getIntent().getStringExtra(getString(R.string.string_key_SearchCondition_condition));
		JSONArray conditionList;
		String type;
		String title;
		LinearLayout conditionLinearLayout = null;
		ConditionInterface conditionInterface = null;
		ConditionText conditionText;
		ConditionDate conditionDate;
		ConditionRadio conditionRadio;
		
		try {
			JSONObject conditionSearchObject = new JSONObject(strConditionSearch);
			strConditionSearchType = conditionSearchObject.getString(strJsonKey_type);
			conditionList = conditionSearchObject.getJSONArray(strJsonKey_condition);
			
			JSONObject itemObject;
			JSONArray selectItem;
	        for (int conditionListIndex=0; conditionListIndex<conditionList.length(); conditionListIndex++)
	        {
	        	itemObject = conditionList.getJSONObject(conditionListIndex);
	        	
	        	// type
	        	type = itemObject.getString(strJsonKey_type);
	        	//text
	        	if (strType_text.contentEquals(type))
	        	{
	        		conditionText = new ConditionText(this);
	        		conditionLinearLayout = conditionText;
	        		conditionInterface = conditionText;
	        		
		        	// defaultvalue
		        	if (itemObject.has(strJsonKey_defaultvalue))
		        		conditionText.setText(itemObject.getString(strJsonKey_defaultvalue));

		        	// isMandatory
		        	if (itemObject.has(strJsonKey_mandatory))
		        		conditionText.isMandatory = itemObject.getBoolean(strJsonKey_mandatory);
	        	}
	        	//date
	        	else if (strType_date.contentEquals(type))
	        	{
	        		conditionDate = new ConditionDate(this);
	        		conditionLinearLayout = conditionDate;
	        		conditionInterface = conditionDate;
	        	}
	        	//radio
	        	else if (strType_radio.contentEquals(type))
	        	{
	        		conditionRadio = new ConditionRadio(this);
	        		conditionLinearLayout = conditionRadio;
	        		conditionInterface = conditionRadio;
	        		selectItem = itemObject.getJSONArray(strJsonKey_selectItem);
	        		List<String> selectList = new ArrayList<String>();
	        		for (int selectItemIndex=0; selectItemIndex < selectItem.length(); selectItemIndex++)
	        			selectList.add(selectItem.getString(selectItemIndex));
	        		conditionRadio.setSelectItem(selectList);

		        	// defaultvalue
		        	if (itemObject.has(strJsonKey_defaultvalue))
		        		conditionRadio.setText(itemObject.getString(strJsonKey_defaultvalue));
	        	}

	        	// title
	        	title = itemObject.getString(strJsonKey_title);
	        	conditionInterface.setTitle(title);
	        	
	        	// addview
	        	if (conditionListIndex%2 == 0)
	        		mSearchconditionLinearLayoutLeft.addView(conditionLinearLayout);
	        	else
	        		mSearchconditionLinearLayoutRight.addView(conditionLinearLayout);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// buttonSearch OnClickListener
	private OnClickListener buttonSearchOnClickListener = new OnClickListener()
	{
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			JSONArray paramsHeader = new JSONArray();
			ConditionInterface conditionInterface;
			for (int linearLayoutLeftIndex = 0; linearLayoutLeftIndex < mSearchconditionLinearLayoutLeft.getChildCount(); linearLayoutLeftIndex++)
			{
				conditionInterface = (ConditionInterface) mSearchconditionLinearLayoutLeft.getChildAt(linearLayoutLeftIndex);
				if (!conditionInterface.checkMandatory())
					return;
				paramsHeader.put(conditionInterface.getText());
				
				if (linearLayoutLeftIndex < mSearchconditionLinearLayoutRight.getChildCount())
				{
					conditionInterface = (ConditionInterface) mSearchconditionLinearLayoutRight.getChildAt(linearLayoutLeftIndex);
					if (!conditionInterface.checkMandatory())
						return;
					paramsHeader.put(conditionInterface.getText());
				}
			}
			JSONArray params = PublicMethod.postParam(SearchCondition.this, strConditionSearchType, paramsHeader);
			String result = PublicMethod.httpPost(Login.mAddress, params.toString());
			if (result == null || result.isEmpty())
			{
	        	PublicMethod.displayToast(SearchCondition.this.getApplicationContext(), "error while " + strConditionSearchType + " :" + result);
				return;
			}
			
	    	try {
	    		JSONObject jsonCheckResult = new JSONObject(result);
		        if (jsonCheckResult.has("ERROR"))
		        {
		        	PublicMethod.displayToast(SearchCondition.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
		        	return;
		        }

	            Intent SearchConditionIntent = new Intent(SearchCondition.this, SearchResultView.class);
	            SearchConditionIntent.putExtra(SearchCondition.this.getString(R.string.string_key_SearchCondition_result), result);
	            SearchCondition.this.startActivity(SearchConditionIntent);
	            
		    } catch (JSONException e) {  
		    	Log.e(Login.TAG, "error while login:" + e.getMessage());
	        	return;
		    }
		}
	};
}
