package com.shon.marketorder2;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity {
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.changepassword); 
		User user = ((MarketOrder2Application)getApplication()).getUser();
		if (user != null)
			this.setTitle(this.getString(R.string.app_name) + "-" + Login.mLinkAddressName + "-" + user.name);
		
        
		Button buttonChangePWD = (Button)findViewById(R.id.buttonChangePWD);
		buttonChangePWD.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			public void onClick(View v)
			{
				EditText editTextUserName = (EditText)findViewById(R.id.editTextUserName);
				EditText editTextUserPassword = (EditText)findViewById(R.id.editTextUserPassword);
				EditText editTextNewPassword = (EditText)findViewById(R.id.editTextNewPassword);
				EditText editTextRepeatPassword = (EditText)findViewById(R.id.editTextRepeatPassword);
				if (!editTextNewPassword.getText().toString().contentEquals(editTextRepeatPassword.getText().toString()))
				{
					Toast.makeText(ChangePassword.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONArray paramsHeader = new JSONArray();
				paramsHeader.put(editTextUserName.getText().toString().trim());
				paramsHeader.put(PublicMethod.md5(editTextUserPassword.getText().toString()));
				paramsHeader.put(PublicMethod.md5(editTextNewPassword.getText().toString()));
//				paramsHeader.put(PublicMethod.md5(editTextNewPassword.getText().toString()+editTextUserPassword.getText().toString()));
				JSONArray params = PublicMethod.postParam(ChangePassword.this, "CHGPWD", paramsHeader);

				String result = PublicMethod.httpPost(Login.mAddress, params.toString());
				if (result == null || result.isEmpty() || result.contains("ERROR"))
				{
					Toast.makeText(ChangePassword.this, "修改失败", Toast.LENGTH_SHORT).show();
					return ;
				}

				MarketOrder2Application app = (MarketOrder2Application)ChangePassword.this.getApplicationContext();
				User user = app.getUser();
				if (user != null)
					user.logout();

				Toast.makeText(ChangePassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
				ChangePassword.this.finish();
			}
		});
		

		Button buttonCancel = (Button)findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ChangePassword.this.finish();
			}
		});
    }

}
