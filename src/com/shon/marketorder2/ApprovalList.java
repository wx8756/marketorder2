package com.shon.marketorder2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shon.marketorder2.TableAdapter.TableCell;
import com.shon.marketorder2.TableAdapter.TableRow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ApprovalList extends Activity {
	public final static String page_tag = "APPROVALLIST";
	final static int ApprovalList_Result = 200601;
	
	final String strJsonKey_header = "header";
	final String strJsonKey_title = "title";
	final String strJsonKey_width = "width";
	final String strJsonKey_align = "align";
	final String strJsonKey_body = "body";

	private ListView lvTitle; 
    private ListView lv; 
    private ArrayList<TableRow> table = new ArrayList<TableRow>(); 
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.searchresultview);  
		User user = ((MarketOrder2Application)getApplication()).getUser();
		if (user != null)
			this.setTitle(this.getString(R.string.app_name)  + "-" + user.name);
		  
		lvTitle = (ListView)this.findViewById(R.id.TitleListView);
        lv = (ListView) this.findViewById(R.id.SearchResultListView); 
        
        
	    String listContent = getIntent().getStringExtra(getString(R.string.string_key_ApprovalList_result));
	    if (listContent == null)
	    	return ;
	    
	    JSONObject listJSON = null;
	    try {
	    	listJSON = new JSONObject(listContent); 
	    	JSONArray headerJson = listJSON.getJSONArray(strJsonKey_header);
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
			
	        TableAdapter tableTitleAdapter = new TableAdapter(ApprovalList.this, tableTitle, false, true);
	        lvTitle.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	        lvTitle.setAdapter(tableTitleAdapter); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   

        
        //body
        setTable(listContent);
		
        TableAdapter tableAdapter = new TableAdapter(ApprovalList.this, table, false, false);
        lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        lv.setAdapter(tableAdapter);  

        lv.setOnItemClickListener(new ItemClickEvent());
		

        findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				Bundle returnBundle = new Bundle();
				returnBundle.putInt(ApprovalList.this.getString(R.string.string_key_ApprovalList_WaitNumber), table.size());
				returnIntent.putExtras(returnBundle);
				ApprovalList.this.setResult(ApprovalList.ApprovalList_Result, returnIntent);
				ApprovalList.this.finish();
			}
        });
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {  
        @SuppressLint("NewApi")
		@Override  
        public void onItemClick(AdapterView<?> arg0, View tableRowView, int index,  
                long arg3) {  

        	// 业务类型
        	String operationType = ApprovalList.this.table.get(index).getCellValue(0).value.toString();
        	// 单据号
        	String billsNumber = ApprovalList.this.table.get(index).getCellValue(1).value.toString();

			JSONArray paramsHeader = new JSONArray();
			paramsHeader.put(operationType);
			paramsHeader.put(billsNumber);
			JSONArray params = PublicMethod.postParam(ApprovalList.this, "APPROVALQUERY", paramsHeader);

			String result = PublicMethod.httpPost(Login.mAddress, params.toString());
			if (result == null || result.isEmpty())
				return ;

	    	try {
	    		JSONObject jsonCheckResult = new JSONObject(result);
		        if (jsonCheckResult.has("ERROR"))
		        {
		        	PublicMethod.displayToast(ApprovalList.this.getApplicationContext(), jsonCheckResult.getString("ERROR"));
		        	return;
		        }

	            Intent ApprovalQueryIntent = new Intent(ApprovalList.this, ApprovalQuery.class);
	            ApprovalQueryIntent.putExtra(ApprovalList.this.getString(R.string.string_key_ApprovalList_result), result);
	            ApprovalQueryIntent.putExtra(ApprovalList.this.getString(R.string.string_key_ApprovalList_OperationType), operationType);
	            ApprovalList.this.startActivityForResult(ApprovalQueryIntent, ApprovalQuery.ApprovalQuery_Result);
		    } catch (JSONException e) {  
//		    	Log.e(ApprovalList.TAG, "error while buttonSearchBook OnClickListener:" + e.getMessage());
		    }
        }  
    }  
    
    public static String approvalListRequest(Context context, String requestType)
    {
    	JSONArray paramsHeader = new JSONArray();
		JSONArray params = PublicMethod.postParam(context, requestType, paramsHeader);
		String result = PublicMethod.httpPost(Login.mAddress, params.toString());
		return result;
    }
    
    protected void setTable(String listContent)
    {
    	JSONObject listJSON = null;
	    try {
	        table.clear(); 
	    	listJSON = new JSONObject(listContent); 
	    	JSONArray headerJson = listJSON.getJSONArray(strJsonKey_header);
	    	JSONObject headerItem;
	    	int tGravity;
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Approval.Approval_Success) {
			ApprovalList.this.setResult(Approval.Approval_Success);
			String listContent = approvalListRequest(ApprovalList.this.getApplicationContext(), 
					ApprovalList.this.getIntent().getStringExtra(getString(R.string.string_key_ApprovalList_requestType)));
	        setTable(listContent);
			((TableAdapter)lv.getAdapter()).notifyDataSetChanged();
		}
//		else if(resultCode == FunctionList.MenuResult)
//		{
//			ApprovalList.this.setResult(FunctionList.MenuResult);
//			ApprovalList.this.finish();
//		}
	}
}
