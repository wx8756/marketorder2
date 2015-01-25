package com.shon.marketorder2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shon.marketorder2.PublicMethod;
import com.shon.marketorder2.R;
import com.shon.marketorder2.TableAdapter;
import com.shon.marketorder2.TableAdapter.TableCell;
import com.shon.marketorder2.TableAdapter.TableRow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

//{
//	  "header": [
//	    {
//	      "title": "名字",
//	      "width": 200,
//	      "align": "left"
//	    },
//	    {
//	      "title": "价格",
//	      "width": 200,
//	      "align": "left"
//	    },
//	    {
//	      "title": "日期",
//	      "width": 500,
//	      "align": "right"
//	    }
//	  ],
//	  "body": [
//	    [
//	      "商品1",
//	      "3.00",
//	      "20141124"
//	    ],
//	    [
//	      "商品2",
//	      "3.00",
//	      "20141124"
//	    ],
//	    [
//	      "商品3",
//	      "3.00",
//	      "20141124"
//	    ]
//	  ]
//	}


public class SearchResultView extends Activity {
	final String strJsonKey_header = "header";
	final String strJsonKey_title = "title";
	final String strJsonKey_width = "width";
	final String strJsonKey_align = "align";
	final String strJsonKey_body = "body";

	private ListView lvTitle; 
    private ListView lv; 
    private ArrayList<TableRow> table; 
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.searchresultview);  
		User user = ((MarketOrder2Application)getApplication()).getUser();
		if (user != null)
			this.setTitle(this.getString(R.string.app_name)  + "-" + user.name);
		  
		lvTitle = (ListView)this.findViewById(R.id.TitleListView);
        lv = (ListView) this.findViewById(R.id.SearchResultListView); 
        
        
	    String listContent = getIntent().getStringExtra(getString(R.string.string_key_SearchCondition_result));
	    if (listContent == null)
	    	return ;
	    
	    JSONObject listJSON = null;
	    try {
	        table = new ArrayList<TableRow>(); 
	    	listJSON = new JSONObject(listContent); 
	    	JSONArray headerJson = listJSON.getJSONArray(strJsonKey_header);
	    	JSONObject headerItem;
	    	int tGravity;
	        ArrayList<TableRow>tableTitle = new ArrayList<TableRow>(); 
	        TableCell[] titles = new TableCell[headerJson.length()];
	        
	        for (int i = 0; i<headerJson.length(); i++)
	        {
	        	headerItem =  headerJson.getJSONObject(i);
                tGravity = Gravity.LEFT;
                if (headerItem.getString(strJsonKey_align).contentEquals("right"))
                	tGravity = Gravity.RIGHT;
                else if (headerItem.getString(strJsonKey_align).contentEquals("center"))
                	tGravity = Gravity.CENTER;
	        	titles[i] = new TableCell(headerItem.getString(strJsonKey_title), 
	        			headerItem.getInt(strJsonKey_width),
	        			LayoutParams.FILL_PARENT,
	        			TableCell.STRING,
	        			tGravity);
	        }
	        tableTitle.add(new TableRow(titles)); 
			
	        TableAdapter tableTitleAdapter = new TableAdapter(SearchResultView.this, tableTitle, false, true);
	        lvTitle.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	        lvTitle.setAdapter(tableTitleAdapter); 
	        
	        //body
	    	JSONArray bodyJson = listJSON.getJSONArray(strJsonKey_body);
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
			
	        TableAdapter tableAdapter = new TableAdapter(SearchResultView.this, table, false, false);
	        lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	        lv.setAdapter(tableAdapter);  

//	        if (listJSON.getString(0).contentEquals("ORDERLIST"))
//	        	lv.setOnItemClickListener(new ItemClickEvent());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		

        findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
//				SearchResultView.this.setResult(FunctionList.MenuResult);
				SearchResultView.this.finish();
			}
        });
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {  
        @SuppressLint("NewApi")
		@Override  
        public void onItemClick(AdapterView<?> arg0, View tableRowView, int index,  
                long arg3) {  

        	String listStatus = SearchResultView.this.table.get(index).getCellValue(0).value.toString();
        	if (!listStatus.contains("����") && !listStatus.contains("����") && !listStatus.contains("������"))
        	{
        		Toast.makeText(SearchResultView.this, "�����޸ĸö���", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	String listnumber = SearchResultView.this.table.get(index).getCellValue(1).value.toString();
		    

			JSONArray paramsHeader = new JSONArray();
			paramsHeader.put(listnumber);
			JSONArray params = PublicMethod.postParam(SearchResultView.this, "ORDERQUERY", paramsHeader);

			String result = PublicMethod.httpPost(Login.mAddress, params.toString());
			if (result == null || result.isEmpty())
				return ;

	    	try {
	    		JSONArray jsonCheckResult = new JSONArray(result);
		        if (jsonCheckResult.length() < 2 || jsonCheckResult.getString(0).contains("ERROR"))
		        {
		        	if (jsonCheckResult.length() >= 2 && jsonCheckResult.getJSONArray(1) != null && jsonCheckResult.getJSONArray(1).length() >=1)
		        		PublicMethod.displayToast(SearchResultView.this, jsonCheckResult.getJSONArray(1).getString(0));
		        	return ;
		        }
		        else
		        {
//		        	Intent SearchViewIntent = new Intent(SearchResultView.this, ChangeOrderCustomerInfo.class);
//		        	SearchViewIntent.putExtra(SearchResultView.this.getString(R.string.string_key_listcontent), result);
//		        	SearchViewIntent.putExtra(SearchResultView.this.getString(R.string.string_key_orderCode), listnumber);
//		        	SearchResultView.this.startActivityForResult(SearchViewIntent, 0);
		        }
		    } catch (JSONException e) {  
//		    	Log.e(SearchResultView.TAG, "error while buttonSearchBook OnClickListener:" + e.getMessage());
		    }
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
			SearchResultView.this.setResult(RESULT_OK, resultIntent);
			SearchResultView.this.finish();
		}
//		else if(resultCode == FunctionList.MenuResult)
//		{
//			SearchResultView.this.setResult(FunctionList.MenuResult);
//			SearchResultView.this.finish();
//		}
	}
}
