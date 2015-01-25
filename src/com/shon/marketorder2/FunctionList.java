package com.shon.marketorder2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shon.marketorder2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//{
//	  "type": "menu",
//	  "page": "mainmenu",
//	  "title": "主菜单",
//	  "submenu": [
//	    {
//	      "type": "todo",
//	      "page": "todo",
//	      "title": "待办事项"
//	    },
//	    {
//	      "type": "menu",
//	      "page": "1000",
//	      "title": "销售结构表"
//	    },
//	    {
//	      "type": "menu",
//	      "page": "2000",
//	      "title": "收款表"
//	    },
//	    {
//	      "type": "conditionsearch",
//	      "page": "3000",
//	      "title": "库存表"
//	    },
//	    {
//	      "page": "4000",
//	      "title": "生产计划"
//	    },
//	    {
//	      "page": "5000",
//	      "title": "销售价格查询"
//	    }
//	  ]
//	}
@SuppressLint("ViewHolder")
public class FunctionList extends Activity {
	static int tier = 0;

	final String strJsonKey_submenu = "submenu";
	final String strJsonKey_title = "title";
	final String strJsonKey_page = "page";
	final String strJsonKey_pagetype = "pagetype";
	final String strJsonKey_number = "number";
	
	JSONArray menulist;
	private String thisPage;
    
    private ListView listView;  
    private FunctionListViewAdapter adapter;  
    //作为数据源来使用  
    private List<FunctionListViewAdapterData> data = null; 
    //待办事项 数量
    private TextView waitNumberTextView = null;

    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.functionlist);  
        initComponent();  
        
        String strMenu = this.getIntent().getStringExtra(getString(R.string.string_key_FunctionList_menu));
        data = new ArrayList<FunctionListViewAdapterData>();
		try {
			JSONObject mainmenu = new JSONObject(strMenu);
			thisPage = mainmenu.getString(strJsonKey_page);
	        menulist = mainmenu.getJSONArray(strJsonKey_submenu);
	        String number;
	        String pagetype;
	        for (int i=0; i<menulist.length(); i++)
	        {
		        number = "0";
		        if (menulist.getJSONObject(i).has(strJsonKey_number))
		        	number = menulist.getJSONObject(i).getString(strJsonKey_number);
		        if (menulist.getJSONObject(i).has(strJsonKey_submenu))
		        	pagetype = FunctionListViewAdapterData.type_folder;
		        // 条件查询
				else if(menulist.getJSONObject(i).has(strJsonKey_page)
						&& menulist.getJSONObject(i).has(strJsonKey_pagetype)
						&& menulist.getJSONObject(i).getString(strJsonKey_pagetype).contentEquals(SearchCondition.page_tag))
		        	pagetype = FunctionListViewAdapterData.type_table;
				else
	        		pagetype = FunctionListViewAdapterData.type_approval;
		        data.add(new FunctionListViewAdapterData(menulist.getJSONObject(i).getString(strJsonKey_title), number, pagetype));
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        adapter = new FunctionListViewAdapter(FunctionList.this);  
        listView.setAdapter(adapter);  
        listView.setOnItemClickListener(new ItemClickEvent());
        
        Button buttonLogout = (Button)this.findViewById(R.id.buttonLogOut);
		if (!thisPage.contentEquals("mainmenu"))
			buttonLogout.setText("返回");
        buttonLogout.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (thisPage.contentEquals("mainmenu"))
				{
					MarketOrder2Application app = (MarketOrder2Application)FunctionList.this.getApplicationContext();
					app.getUser().logout();
				}
				FunctionList.this.finish();
			}
        	
        });
    }  
      
    private void initComponent(){  
        listView = (ListView)findViewById(R.id.listView1);  
          
    }  

    class ItemClickEvent implements AdapterView.OnItemClickListener {  
        @SuppressLint("NewApi")
		@Override  
        public void onItemClick(AdapterView<?> arg0, View tableRowView, int index,  
                long arg3) {  
        	try {
				JSONObject mainmenu = menulist.getJSONObject(index);
				
				// 子菜单
				if (mainmenu.has(strJsonKey_submenu))
				{
		            Intent FunctionListIntent = new Intent(FunctionList.this, FunctionList.class);
		            FunctionListIntent.putExtra(FunctionList.this.getString(R.string.string_key_FunctionList_menu), mainmenu.toString());
		            FunctionList.this.startActivity(FunctionListIntent);
		            return;
				}
				// 条件查询
				else if(mainmenu.has(strJsonKey_page)
						&& mainmenu.has(strJsonKey_pagetype)
						&& mainmenu.getString(strJsonKey_pagetype).contentEquals(SearchCondition.page_tag))
				{
					JSONArray paramsHeader = new JSONArray();
					JSONArray params = PublicMethod.postParam(FunctionList.this.getApplicationContext(), mainmenu.getString(strJsonKey_page), paramsHeader);
					String result = PublicMethod.httpPost(Login.mAddress, params.toString());//"[\,\"\",[\"\",\"95322\",\"OWNER\",\"AA\"]]");
					if (result == null || result.isEmpty())
					{
			        	PublicMethod.displayToast(FunctionList.this.getApplicationContext(), "error while " + mainmenu.getString(strJsonKey_page) + " :" + result);
						return;
					}
					
			    	try {
			    		JSONObject jsonCheckResult = new JSONObject(result);
				        if (jsonCheckResult.has("ERROR"))
				        {
				        	PublicMethod.displayToast(FunctionList.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
				        	return;
				        }

			            Intent SearchConditionIntent = new Intent(FunctionList.this, SearchCondition.class);
			            SearchConditionIntent.putExtra(FunctionList.this.getString(R.string.string_key_SearchCondition_condition), result);
			            FunctionList.this.startActivity(SearchConditionIntent);
			            
				    } catch (JSONException e) {  
				    	Log.e(Login.TAG, "error while login:" + e.getMessage());
			        	return;
				    }
				}
				// 待办事项
				else if(mainmenu.has(strJsonKey_page)
						&& mainmenu.getString(strJsonKey_page).contentEquals(ApprovalList.page_tag))
				{
					String result = ApprovalList.approvalListRequest(FunctionList.this.getApplicationContext(), mainmenu.getString(strJsonKey_page));
					if (result == null || result.isEmpty())
					{
			        	PublicMethod.displayToast(FunctionList.this.getApplicationContext(), "error while " + mainmenu.getString(strJsonKey_page) + " :" + result);
						return;
					}
					
			    	try {
			    		JSONObject jsonCheckResult = new JSONObject(result);
				        if (jsonCheckResult.has("ERROR"))
				        {
				        	PublicMethod.displayToast(FunctionList.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
				        	return;
				        }

			            Intent ApprovalListIntent = new Intent(FunctionList.this, ApprovalList.class);
			            ApprovalListIntent.putExtra(FunctionList.this.getString(R.string.string_key_ApprovalList_result), result);
			            ApprovalListIntent.putExtra(FunctionList.this.getString(R.string.string_key_ApprovalList_requestType), mainmenu.getString(strJsonKey_page));
			            FunctionList.this.startActivityForResult(ApprovalListIntent, ApprovalList.ApprovalList_Result);
			            
				    } catch (JSONException e) {  
				    	Log.e(Login.TAG, "error while login:" + e.getMessage());
			        	return;
				    }
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }  
    }  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == ApprovalList.ApprovalList_Result) {
			Bundle bundle = data.getExtras();
			int waitNumber = bundle.getInt(FunctionList.this.getString(R.string.string_key_ApprovalList_WaitNumber));
			FunctionList.this.updateWaitNumber(waitNumber);
		}
//		else if(resultCode == FunctionList.MenuResult)
//		{
//			SearchResultView.this.setResult(FunctionList.MenuResult);
//			SearchResultView.this.finish();
//		}
	}
	
	public void updateWaitNumber(int number)
	{
		if (waitNumberTextView == null)
			return;
		
		waitNumberTextView.setText(Integer.valueOf(number).toString());
	}
	
	class FunctionListViewAdapterData{
		final static String type_folder = "folder";
		final static String type_table = "table";
		final static String type_approval = "approval";
		public String title;
		public String number;
		public String type;
		public FunctionListViewAdapterData(String title, String number, String type) {  
            this.title = title;  
            this.number = number;
            this.type = type;
		}
	}
	public class FunctionListViewAdapter extends BaseAdapter {
        private LayoutInflater mInflater;        
        public FunctionListViewAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub                
			convertView = mInflater.inflate(R.layout.functionlistview, null);
			TextView title = (TextView)convertView.findViewById(R.id.ItemText);
			title.setText(data.get(position).title);
			if (!data.get(position).number.contentEquals("0"))
			{
				TextView number = (TextView)convertView.findViewById(R.id.textViewHint);
				number.setText(data.get(position).number);
				number.setVisibility(View.VISIBLE);
				waitNumberTextView = number;
			}
			ImageView arrowsImageView = (ImageView)convertView.findViewById(R.id.imageViewArrows);
			ImageView iconImageView = (ImageView)convertView.findViewById(R.id.imageViewIcon);
			if (data.get(position).type.contentEquals(FunctionListViewAdapterData.type_folder))
			{
				iconImageView.setImageResource(R.drawable.folder);
				arrowsImageView.setVisibility(View.VISIBLE);
			}
			else if (data.get(position).type.contentEquals(FunctionListViewAdapterData.type_table))
				iconImageView.setImageResource(R.drawable.table);
			else if (data.get(position).type.contentEquals(FunctionListViewAdapterData.type_approval))
				iconImageView.setImageResource(R.drawable.approval);
			return convertView;
		}
	
	}
}
