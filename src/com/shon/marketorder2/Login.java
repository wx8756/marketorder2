package com.shon.marketorder2;

import org.json.JSONArray;
import org.json.JSONException;

import com.shon.marketorder2.Login.HandlerMain;
import com.shon.marketorder2.AutoUpdate;
import com.shon.marketorder2.ChangePassword;
import com.shon.marketorder2.MarketOrder2Application;
import com.shon.marketorder2.PublicMethod;
import com.shon.marketorder2.R;
import com.shon.marketorder2.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Login extends Activity {

	public static final int MSG_SHOW_UPDATE_DIALOG	 = 10001;
	public static final int verson	 = 112;
	
	public static String TAG = "marketorder2";
	private EditText mEditTextUserName;
	private EditText mEditTextUserPassword;
	private Button mButtonLogin;
	private Handler handler = new HandlerMain();
	
	public static String mAddress;
	public static String mLinkAddressName;
	public String downloadFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		mAddress = getString(R.string.address_qingyuan);
		
		
		mEditTextUserName = (EditText)findViewById(R.id.editTextUserName);
		mEditTextUserPassword = (EditText)findViewById(R.id.editTextUserPassword);
		mButtonLogin = (Button)findViewById(R.id.buttonLogin);
		mButtonLogin.setOnClickListener(mButtonClickListener);
		
		Button buttonChangePWD = (Button)findViewById(R.id.buttonChangePWD);
		buttonChangePWD.setOnClickListener(new OnClickListener()
	{
		public void onClick(View v)
		{
            Intent ChangePasswordIntent = new Intent(getApplicationContext(), ChangePassword.class);
            Login.this.startActivity(ChangePasswordIntent);
		}
	});

		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		
		final RadioButton radioButtonQingyuan = (RadioButton)findViewById(R.id.radioButtonQingyuan);
		final RadioButton radioButtonJiangxi = (RadioButton)findViewById(R.id.radioButtonJiangxi);
		final RadioButton radioButtonTest = (RadioButton)findViewById(R.id.radioButtonTest);
//		final RadioButton radioButtonLocal = (RadioButton)findViewById(R.id.radioButtonLocal);
		final RadioButton radioButtonDongguan = (RadioButton)findViewById(R.id.radioButtonDongguan);
		radioButtonQingyuan.setVisibility(View.GONE);
		radioButtonJiangxi.setVisibility(View.GONE);
		radioButtonDongguan.setVisibility(View.GONE);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    
		    @Override
		    public void onCheckedChanged(RadioGroup arg0, int checkID) {
		    	SharedPreferences settings = Login.this.getSharedPreferences("login", 0);
		    	int addressId = 0;
			    if(radioButtonQingyuan.getId() == checkID) {
					mAddress = getString(R.string.address_qingyuan);
					mLinkAddressName = "清远";
					addressId = 1;
				}else if(radioButtonJiangxi.getId() == checkID) {
					mAddress = getString(R.string.address_jiangxi);
					mLinkAddressName = "江西";
					addressId = 2;
				}else if(radioButtonTest.getId() == checkID) {
					mAddress = getString(R.string.address_test);
					mLinkAddressName = "测试";
					addressId = 4;
//				}else if(radioButtonLocal.getId() == checkID) {
//					mAddress = getString(R.string.address_local);
//					mLinkAddressName = "����";
//					addressId = 0;
				}else if(radioButtonDongguan.getId() == checkID) {
					mAddress = getString(R.string.address_dongguan);
					mLinkAddressName = "东莞";
					addressId = 3;
				} 
				SharedPreferences.Editor editor = settings.edit(); 
				editor.putInt("addressId", addressId); 
				editor.commit(); 
		    }
		});

		SharedPreferences settings = this.getSharedPreferences("login", 0);
		switch (settings.getInt("addressId", 1))
		{
//		case 0:
//			radioButtonLocal.setChecked(true);
//			break;
		case 1:
			radioButtonQingyuan.setChecked(true);
			break;
		case 2:
			radioButtonJiangxi.setChecked(true);
			break;
		case 3:
			radioButtonDongguan.setChecked(true);
			break;
		case 4:
			radioButtonTest.setChecked(true);
			break;
		}
		mEditTextUserName.setText(settings.getString("username", ""));
		((EditText)findViewById(R.id.editTextAddress)).setText(settings.getString("address", ""));
//		final View controlsView = findViewById(R.id.fullscreen_content_controls);
//		final View contentView = findViewById(R.id.fullscreen_content);
//
//		// Set up an instance of SystemUiHider to control the system UI for
//		// this activity.
//		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
//				HIDER_FLAGS);
//		mSystemUiHider.setup();
//		mSystemUiHider
//				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//					// Cached values.
//					int mControlsHeight;
//					int mShortAnimTime;
//
//					@Override
//					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//					public void onVisibilityChange(boolean visible) {
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//							// If the ViewPropertyAnimator API is available
//							// (Honeycomb MR2 and later), use it to animate the
//							// in-layout UI controls at the bottom of the
//							// screen.
//							if (mControlsHeight == 0) {
//								mControlsHeight = controlsView.getHeight();
//							}
//							if (mShortAnimTime == 0) {
//								mShortAnimTime = getResources().getInteger(
//										android.R.integer.config_shortAnimTime);
//							}
//							controlsView
//									.animate()
//									.translationY(visible ? 0 : mControlsHeight)
//									.setDuration(mShortAnimTime);
//						} else {
//							// If the ViewPropertyAnimator APIs aren't
//							// available, simply show or hide the in-layout UI
//							// controls.
//							controlsView.setVisibility(visible ? View.VISIBLE
//									: View.GONE);
//						}
//
//						if (visible && AUTO_HIDE) {
//							// Schedule a hide().
//							delayedHide(AUTO_HIDE_DELAY_MILLIS);
//						}
//					}
//				});
//
//		// Set up the user interaction to manually show or hide the system UI.
//		contentView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (TOGGLE_ON_CLICK) {
//					mSystemUiHider.toggle();
//				} else {
//					mSystemUiHider.show();
//				}
//			}
//		});
//
//		// Upon interacting with UI controls, delay any scheduled hide()
//		// operations to prevent the jarring behavior of controls going away
//		// while interacting with the UI.
//		findViewById(R.id.dummy_button).setOnTouchListener(
//				mDelayHideTouchListener);
	}

//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//
//		// Trigger the initial hide() shortly after the activity has been
//		// created, to briefly hint to the user that UI controls
//		// are available.
//		delayedHide(100);
//	}
//
//	/**
//	 * Touch listener to use for in-layout UI controls to delay hiding the
//	 * system UI. This is to prevent the jarring behavior of controls going away
//	 * while interacting with activity UI.
//	 */
	private View.OnClickListener mButtonClickListener = new OnClickListener()
	{
		public void onClick(View v)
		{
//			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//			startActivityForResult(intent, 0);
			EditText addressEditText = (EditText)findViewById(R.id.editTextAddress);
			if (addressEditText.getVisibility() == View.VISIBLE)
				PublicMethod.mAddress = addressEditText.getText().toString().trim();
		
			MarketOrder2Application app = (MarketOrder2Application)Login.this.getApplicationContext();
			app.initUser();
			User user = new User(app);
			user.name = mEditTextUserName.getText().toString().trim();
			user.MD5Password = PublicMethod.md5(mEditTextUserPassword.getText().toString());
			app.setUser(user);
			if (app.getUser().login(Login.this))
			{
				SharedPreferences settings = Login.this.getSharedPreferences("login", 0);  
				SharedPreferences.Editor editor = settings.edit(); 
				editor.putString("username", user.name); 
				editor.putString("address", PublicMethod.mAddress); 
				editor.commit(); 
				
				if (!checkUpdate())
				{
		            Intent FunctionListIntent = new Intent(Login.this, FunctionList.class);
		            FunctionListIntent.putExtra(Login.this.getString(R.string.string_key_FunctionList_menu), app.getUser().author);
		            Login.this.startActivity(FunctionListIntent);
				}
			}
		}
	};

//	Handler mHideHandler = new Handler();
//	Runnable mHideRunnable = new Runnable() {
//		@Override
//		public void run() {
//			mSystemUiHider.hide();
//		}
//	};
//
//	/**
//	 * Schedules a call to hide() in [delay] milliseconds, canceling any
//	 * previously scheduled calls.
//	 */
//	private void delayedHide(int delayMillis) {
//		mHideHandler.removeCallbacks(mHideRunnable);
//		mHideHandler.postDelayed(mHideRunnable, delayMillis);
//	}
	

	@SuppressLint("NewApi")
	public boolean checkUpdate()
	{
////		String updateUrl = mAddress + getString(R.string.url_softwareupdate) + "?p=android&v=";
//		String updateUrl = "http://down.ldceramics.com/marketorder.apk";
//		try {
//			PackageInfo info;
//			info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//			updateUrl += info.versionCode;
//		} catch (NameNotFoundException e) {
//	    	Log.e(DEBUG_TAG, "error getPackageManager");
//			e.printStackTrace();
//		}
//		String updateReturn = PublicMethod.httpGet(updateUrl);
//		try {
//    		JSONObject jsonObjectUpdateInfo = new JSONObject(updateReturn);
//    		String ret_key = getString(R.string.string_key_ret);
//    		if (!jsonObjectUpdateInfo.isNull (ret_key) && jsonObjectUpdateInfo.getString(ret_key).contentEquals(getString(R.string.string_en_ok)))
//    		{
//    			Message msg = Message.obtain(handler, MSG_SHOW_UPDATE_DIALOG);
//	            Bundle data = new Bundle(); 
////    			String file_key = getString(R.string.string_key_file);
////    			if (!jsonObjectUpdateInfo.isNull (file_key))
////    			{
////		            data.putString(file_key, URLDecoder.decode(jsonObjectUpdateInfo.getString(file_key))); 
////        			msg.setData(data);
////    			}
////    			String size_key = getString(R.string.string_key_size);
////    			if (!jsonObjectUpdateInfo.isNull (size_key))
////    			{
////		            data.putInt(size_key, jsonObjectUpdateInfo.getInt(size_key)); 
////        			msg.setData(data);
////    			}
//    			handler.sendMessage(msg);
//    			return true;
//			}
//	    } 
//    	catch (JSONException e) {  
//	    	Log.e(DEBUG_TAG, "error update url:" + updateUrl + ", return:" + updateReturn);
//	    }	
		JSONArray paramsHeader = new JSONArray();
//		paramsHeader.put(storage);
//		EditText editTextLevel = (EditText)findViewById(R.id.editTextLevel);
//		paramsHeader.put(editTextLevel.getText().toString().trim());
//		params.put(paramsHeader);
		JSONArray params = PublicMethod.postParam(Login.this, "UPGRADE", paramsHeader);
		String result = PublicMethod.httpPost(Login.mAddress, params.toString());//"[\,\"\",[\"\",\"95322\",\"OWNER\",\"AA\"]]");
		if (result == null || result.isEmpty())
			return false;

    	try {
    		JSONArray jsonCheckResult = new JSONArray(result);
	        if (jsonCheckResult.length() < 2 || jsonCheckResult.getString(0).contains("ERROR"))
	        {
	        	if (jsonCheckResult.length() >= 2 && jsonCheckResult.getJSONArray(1) != null && jsonCheckResult.getJSONArray(1).length() >=1)
	        		PublicMethod.displayToast(Login.this, jsonCheckResult.getJSONArray(1).getString(0));
	        	return false;
	        }
	        else if (jsonCheckResult.length() >= 2 || jsonCheckResult.getJSONArray(1).length() >= 2)
	        {
	        	if (jsonCheckResult.getJSONArray(1).getInt(0) <= verson)
	        		return false;
	        	
	        	Message msg = Message.obtain(handler, MSG_SHOW_UPDATE_DIALOG);
	            Bundle data = new Bundle(); 
    			String file_key = getString(R.string.string_key_file);
		        data.putString(file_key, jsonCheckResult.getJSONArray(1).getString(1)); 
        		msg.setData(data);
    			String size_key = getString(R.string.string_key_size);
//    			if (!jsonObjectUpdateInfo.isNull (size_key))
//    			{
		            data.putInt(size_key, 500*1024); 
        			msg.setData(data);
//    			}
    			handler.sendMessage(msg);
	        	return true;
	        }
	    } catch (JSONException e) {  
	    	Log.e(Login.TAG, "error while buttonSearch OnClickListener:" + e.getMessage());
	    	return false;
	    }
    	return true;
	}

	private AutoUpdate autoUpdateObject;
	public class HandlerMain extends Handler
	{					
    	public void handleMessage(Message msg)
	    {//����һ��Handler�����ڴ��������߳���UI��ͨѶ
	      if (!Thread.currentThread().isInterrupted())
	      {
	        switch (msg.what)
	        {
//    	        case MSG_RELOAD:
//    	        	reloadMainPage();
//    	        	break;
//    	        case MSG_SHOW_NO_CONNECT:
//        			PublicMethod.showPromptDialog(czzn.this, R.string.string_no_connect);
//    	        	break;
//    	        case MSG_GO_CERTIFY:
//    	        {
//					Intent registerIntent = new Intent(czzn.this, Register.class);
//					czzn.this.startActivity(registerIntent);
//    	        	break;
//    	        }
    	        case MSG_SHOW_UPDATE_DIALOG:
    	        {
    	        	downloadFile = msg.getData().getString(getString(R.string.string_key_file));
//        			autoUpdateObject = new AutoUpdate(Login.this, msg.getData().getString(getString(R.string.string_key_file))
//        														, msg.getData().getInt(getString(R.string.string_key_size)));
			    	Dialog dialog = new AlertDialog.Builder(Login.this)
			    					.setTitle(R.string.string_prompt)
			    					.setMessage(R.string.string_newViverseion)
			    					.setPositiveButton("����", 
			    							new DialogInterface.OnClickListener() {
						
												@Override
												public void onClick(DialogInterface dialog, int which) {
//								        			autoUpdateObject.downloadTheFile();
													Uri uri = Uri.parse(downloadFile);  
												    startActivity(new Intent(Intent.ACTION_VIEW,uri));
												}
											})
									.setNegativeButton(R.string.string_cancel, 
											new DialogInterface.OnClickListener() {
											
												@Override
												public void onClick(DialogInterface dialog, int which) {
										            Intent FunctionListIntent = new Intent(Login.this, FunctionList.class);
										            Login.this.startActivity(FunctionListIntent);
												}
											}).create();
			    	dialog.show();
			    	break;
    	        }
	        }
	      }
	      super.handleMessage(msg);
	    }
    }

}
