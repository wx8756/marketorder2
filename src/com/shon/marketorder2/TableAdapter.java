package com.shon.marketorder2;

import java.io.Serializable;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.Space;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TableAdapter extends BaseAdapter { 
    private Context context;  
    private List<TableRow> table;  
    private boolean isCheckBox;
	public boolean mIsTitle = false;
    public TableAdapter(Context context, List<TableRow> table, boolean isCheckBox, boolean isTitle) {  
        this.context = context;  
        this.table = table;
        this.isCheckBox = isCheckBox;
        this.mIsTitle = isTitle;
    }  
    @Override  
    public int getCount() {  
        return table.size();  
    }  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
    public TableRow getItem(int position) {  
        return table.get(position);  
    }  
    public View getView(int position, View convertView, ViewGroup parent) {  
        TableRow tableRow = table.get(position);  
        TableRowView tableRowView = new TableRowView(this.context, tableRow, position); 
        tableRowView.mIsTitle = this.mIsTitle;
        return tableRowView;
    }  
    /** 
     * TableRowView ʵ�ֱ���е���ʽ 
     * @author hellogv 
     */  
    class TableRowView extends LinearLayout {  
    	private int mPosition = -1;
    	public boolean mIsTitle = false;
    	private Context context;
        public TableRowView(Context context, TableRow tableRow, int position) {  
            super(context);  
            this.setOrientation(LinearLayout.HORIZONTAL); 
            this.mPosition = position;
            this.context = context;
            	
            if (TableAdapter.this.isCheckBox && table.size() > 0)
            {
            	CheckBox cbCell = new CheckBox(context); 
            	cbCell.setFocusable(false);
            	if (this.mIsTitle)
            		cbCell.setVisibility(View.INVISIBLE);
				if (!TableRowView.this.mIsTitle)
				{
	    			TableRow selectTableRow = table.get(TableRowView.this.mPosition);
	            	cbCell.setChecked(selectTableRow.isChecked);
				}
            	cbCell.setOnClickListener(new OnClickListener()
            	{
					@Override
					public void onClick(View v) {
	        			TableRow selectTableRow = table.get(TableRowView.this.mPosition);
						if (TableRowView.this.mIsTitle)
						{
							((TableAdepterInterface)TableRowView.this.context).CheckboxOnClick(!selectTableRow.isChecked);
							return;
						}
	        			selectTableRow.isChecked = !selectTableRow.isChecked;
//	        			if (selectTableRow.isChecked)
//	        	    	{
//	        				Dialog dialog = new AlertDialog.Builder(TableRowView.this.getContext())
//	        	    					.setTitle(R.string.string_prompt)
//	        	    					.setMessage("�Ƿ�����Ʒ")
//	        	    					.setPositiveButton("��", 
//	        	    							new DialogInterface.OnClickListener() {
//	        				
//	        										@Override
//	        										public void onClick(DialogInterface dialog, int which) {
//	        						        			TableRow selectTableRow = Inventory.tableShoppingCart.get(TableRowView.this.mPosition-1);
//	        						        			selectTableRow.isGift = "1";
//	        										}
//	        									})
//	        									.setNegativeButton("��",  
//	        	    							new DialogInterface.OnClickListener() {
//	        				
//	        										@Override
//	        										public void onClick(DialogInterface dialog, int which) {
//	        						        			TableRow selectTableRow = Inventory.tableShoppingCart.get(TableRowView.this.mPosition-1);
//	        						        			selectTableRow.isGift = "0";
//	        										}
//	        									}).create();
//	        				dialog.show();
//	        	    	}
//	        			else
//	        				selectTableRow.isGift = "0";
					}
            	});
//	        	cbCell.setBackgroundColor(Color.BLACK);//������ɫ  
	            LinearLayout.LayoutParams layoutParamsCheckBox = new LinearLayout.LayoutParams(  
	            		LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//���ո�Ԫָ���Ĵ�С���ÿռ�  
	            addView(cbCell, layoutParamsCheckBox); 
            }
            
            for (int i = 0; i < tableRow.getSize(); i++) {//�����Ԫ��ӵ���
                TableCell tableCell = tableRow.getCellValue(i);  
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
                        tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�  
                layoutParams.setMargins(6, 0, 6, 1);//Ԥ����϶����߿�  
                if (tableCell.type == TableCell.STRING) {//�����Ԫ���ı�����  
                    TextView textCell = new TextView(context);  
                    textCell.setTextSize(16);  
                    textCell.setGravity(tableCell.gravity); //Gravity.LEFT
//                    if (tableRow.getSize() == 1 &&
//                    		(String.valueOf(tableCell.value).contains("���")
//                    		|| String.valueOf(tableCell.value).contains("��")
//                    		|| String.valueOf(tableCell.value).contains("��")))
//                        textCell.setGravity(Gravity.RIGHT); 
//                    textCell.setBackgroundColor(Color.BLACK);//������ɫ  
                    textCell.setText(String.valueOf(tableCell.value)); 
                    addView(textCell, layoutParams);  
                } else if (tableCell.type == TableCell.IMAGE) {//�����Ԫ��ͼ������  
                    ImageView imgCell = new ImageView(context);  
                    imgCell.setBackgroundColor(Color.BLACK);//������ɫ  
                    imgCell.setImageResource((Integer) tableCell.value);  
                    addView(imgCell, layoutParams);  
                } else if (tableCell.type == TableCell.CHECKBOX) {//�����Ԫ�Ǹ�ѡ��  
                	CheckBox cbCell = new CheckBox(context); 
                	cbCell.setBackgroundColor(Color.BLACK);//������ɫ  
                	cbCell.setSelected(((Boolean)tableCell.value).booleanValue()); 
                    addView(cbCell, layoutParams);  
                }  
            }  
            this.setBackgroundColor(Color.WHITE);//������ɫ�����ÿ�϶��ʵ�ֱ߿�  
        }  
        
      private CheckBox getCheckBox()
      {
    	  if (TableAdapter.this.isCheckBox && this.getChildAt(0).getClass().getName().contains("android.widget.CheckBox"))
    		  return (CheckBox)this.getChildAt(0);
    	  
    	  return null;
      }
      public void setCheckBoxIsCheck(boolean isCheck)
      {
    	  if (TableAdapter.this.isCheckBox && this.getChildAt(0).getClass().getName().contains("android.widget.CheckBox"))
    		  return;

    	  CheckBox checkbox = (CheckBox)this.getChildAt(0);
    	  checkbox.setChecked(isCheck);
    	  TableRow selectTableRow = table.get(TableRowView.this.mPosition);
    	  selectTableRow.isChecked = isCheck;
      }
      public boolean getChecked()
      {
      	if (getCheckBox() != null)
      		return getCheckBox().isChecked();
      	return false;

      }
      public void setChecked(boolean isChecked)
      {
      	if (getCheckBox() != null)
      		getCheckBox().setChecked(isChecked);
      }
    }  
    /** 
     * TableRow ʵ�ֱ����� 
     * @author hellogv 
     */  
    static public class TableRow  implements Serializable {  
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private TableCell[] cell;  
        public String quantity = "";
        public String purpose = "";
        public String price = "";
        public String remark = "";
        public String isGift = "0";
        public boolean isChecked = false;
        public TableRow(TableCell[] cell) {  
            this.cell = cell;  
        }  
        public int getSize() {  
            return cell.length;  
        }  
        public TableCell getCellValue(int index) {  
            if (index >= cell.length)  
                return null;  
            return cell[index];  
        }  
    }  
    /** 
     * TableCell ʵ�ֱ��ĸ�Ԫ 
     * @author hellogv 
     */  
    static public class TableCell {  
        static public final int STRING = 0;  
        static public final int IMAGE = 1;  
        static public final int CHECKBOX = 2;  
        public Object value;  
        public int width;  
        public int height;  
        private int type;  
        public int gravity;
        public TableCell(Object value, int width, int height, int type, int gravity) {  
            this.value = value;  
            this.width = width;  
            this.height = height;  
            this.type = type; 
            this.gravity = gravity;
        }  
    }  

}
