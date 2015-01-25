package com.shon.marketorder2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shon.marketorder2.ApprovalProductList.ItemClickEvent;
import com.shon.marketorder2.TableAdapter.TableCell;
import com.shon.marketorder2.TableAdapter.TableRow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

public class ApprovalProductList extends Activity {
	final String strJsonKey_header = "header";
	final String strJsonKey_title = "title";
	final String strJsonKey_width = "width";
	final String strJsonKey_align = "align";
	final String strJsonKey_value = "value";
	final String strJsonKey_body = "body";

	private ListView lvTitle; 
    private ListView lv; 
    private ArrayList<TableRow> table; 

	@Override
	public void onConfigurationChanged(Configuration newConfig) {   

	    super.onConfigurationChanged(newConfig);
	}
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.searchresultview);  
		User user = ((MarketOrder2Application)getApplication()).getUser();
		if (user != null)
			this.setTitle(this.getString(R.string.app_name)  + "-" + user.name);
		  
		lvTitle = (ListView)this.findViewById(R.id.TitleListView);
        lv = (ListView) this.findViewById(R.id.SearchResultListView); 
        
	    try {
	        table = new ArrayList<TableRow>(); 
	    	JSONArray headerJson = ApprovalQuery.mProductInfoJSONObject.getJSONArray(strJsonKey_header);
	    	JSONObject headerItem;
	    	int tGravity;
	        ArrayList<TableRow>tableTitle = new ArrayList<TableRow>(); 
	        TableCell[] titles = new TableCell[headerJson.length()];
	        
	        for (int i = 0; i<headerJson.length(); i++)
	        {
	        	headerItem =  headerJson.getJSONObject(i);
				tGravity = Gravity.CENTER;
	        	titles[i] = new TableCell(headerItem.getString(strJsonKey_title), 
	        			headerItem.getInt(strJsonKey_width),
	        			LayoutParams.FILL_PARENT,
	        			TableCell.STRING,
	        			tGravity);
	        }
	        tableTitle.add(new TableRow(titles)); 
			
	        TableAdapter tableTitleAdapter = new TableAdapter(ApprovalProductList.this, tableTitle, false, true);
	        lvTitle.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	        lvTitle.setAdapter(tableTitleAdapter); 
	        
	        //body
	    	JSONArray bodyJson = ApprovalQuery.mProductInfoJSONObject.getJSONArray(strJsonKey_body);
	    	JSONArray bodyRow;
	    	TableCell[] cells;
	    	
	        for (int i = 0; i < bodyJson.length(); i++)  
	        {
	        	bodyRow = bodyJson.getJSONArray(i);
	            cells = new TableCell[bodyRow.length()];  
	            for (int j = 0; j < cells.length; j++) {
		        	headerItem =  headerJson.getJSONObject(j);
	                tGravity = Gravity.LEFT;
	                if (headerItem.getString(strJsonKey_align).contentEquals("right"))
	                	tGravity = Gravity.RIGHT;
	                else if (headerItem.getString(strJsonKey_align).contentEquals("center"))
	                	tGravity = Gravity.CENTER;
	                
	                cells[j] = new TableCell(bodyRow.getString(j),
	                						headerItem.getInt(strJsonKey_width),   
	                                        LayoutParams.FILL_PARENT,   
	                                        TableCell.STRING,
	                                        tGravity);  
	            }
	        	table.add(new TableRow(cells)); 
	        }
			
	        TableAdapter tableAdapter = new TableAdapter(ApprovalProductList.this, table, false, false);
	        lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	        lv.setAdapter(tableAdapter);  

	        lv.setOnItemClickListener(new ItemClickEvent());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		

        findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
//				ApprovalProductList.this.setResult(FunctionList.MenuResult);
				ApprovalProductList.this.finish();
			}
        });
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {  
        @SuppressLint("NewApi")
		@Override  
        public void onItemClick(AdapterView<?> arg0, View tableRowView, int index,  
                long arg3) {  

			// 8 批示价格
	        final TableRow tableRow = ApprovalProductList.this.table.get(index);
        	String approvalPrice = tableRow.getCellValue(8).value.toString();

        	String typeStr = "";
	        try {
	        	//0 审批类型
				typeStr = ApprovalQuery.mApprovalInfoJSONArray.getJSONObject(0).getString(strJsonKey_value);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (approvalPrice == null || approvalPrice.contentEquals("") || Float.compare((float) 0.0, Float.valueOf(approvalPrice).floatValue()) == 0)
	        {
				PublicMethod.displayToast(ApprovalProductList.this.getApplicationContext(), "价格为0，不能修改");
				return;
			}
	        if (typeStr.contentEquals("200"))
	        {
				PublicMethod.displayToast(ApprovalProductList.this.getApplicationContext(), "此环节是确认环节，不允许修改价格");
				return;
			}
	        

	        // 0 行号 
        	final String billsNumber = ApprovalProductList.this.table.get(index).getCellValue(0).value.toString();
	        final ConditionText conditionText = new ConditionText(ApprovalProductList.this);
	        conditionText.setTitle("审批价格");
	        conditionText.setText(approvalPrice);
	        Dialog dialog = new AlertDialog.Builder(ApprovalProductList.this)
			.setTitle(R.string.string_prompt)
			.setView(conditionText)
			.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ApprovalQuery.mNumberPrice.put(billsNumber, conditionText.getText());
							// 8 批示价格
							tableRow.getCellValue(8).value = conditionText.getText();
							TableAdapter sAdapter = (TableAdapter)lv.getAdapter();               
							sAdapter.notifyDataSetChanged(); 
						}
					})
			.setNegativeButton(R.string.string_cancel, 
					new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create();
	        dialog.show();
        }  
    }  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String returnResult = bundle.getString("result");
			Intent resultIntent = new Intent();
			Bundle newbundle = new Bundle();
			newbundle.putString(getString(R.string.string_key_orderCode), returnResult);
			newbundle.putString(getString(R.string.string_key_searchType), "ORDERLIST");
			resultIntent.putExtras(newbundle);
			ApprovalProductList.this.setResult(RESULT_OK, resultIntent);
			ApprovalProductList.this.finish();
		}
//		else if(resultCode == FunctionList.MenuResult)
//		{
//			ApprovalProductList.this.setResult(FunctionList.MenuResult);
//			ApprovalProductList.this.finish();
//		}
	}
}
