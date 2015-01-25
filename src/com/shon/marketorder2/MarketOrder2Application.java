package com.shon.marketorder2;

import com.shon.marketorder2.User;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class MarketOrder2Application extends Application {

	public MarketOrder2Application() {
		// TODO Auto-generated constructor stub
	}

	public static String USER_TAG = "user";
	
	private User mUser = null;
	private SharedPreferences settings;

	public void initUser()
	{		 
        this.mUser = new User(this);
        settings = getSharedPreferences(USER_TAG, Activity.MODE_PRIVATE);
//        mUser.name = settings.getString(getString(R.string.string_en_userName), "");
//        mUser.phone = settings.getString(getString(R.string.string_en_userPhone), "");
//        mUser.MD5Password = settings.getString(getString(R.string.string_en_userPassword), "");
//        mUser.sessionKey = settings.getString(getString(R.string.string_en_sessionKey), "");
//        mUser.nineListJson = settings.getString(getString(R.string.string_en_nineListJson), "");
//        mUser.author = settings.getInt(getString(R.string.string_en_author), User.USER_AUTHOR_FREE);
	}
	
	public User getUser(){
		return mUser;
	}
	
	public void setUser(User user){

    	SharedPreferences.Editor editor = settings.edit();
//    	editor.putString(getString(R.string.string_en_userName), user.name);
//    	editor.putString(getString(R.string.string_en_userPhone), user.phone);
//    	editor.putString(getString(R.string.string_en_userPassword), user.MD5Password);
//    	editor.putString(getString(R.string.string_en_sessionKey), user.sessionKey);
//    	editor.putInt(getString(R.string.string_en_author), user.author);
//    	editor.putString(getString(R.string.string_en_nineListJson), user.nineListJson);    	
		editor.commit();

		mUser.copy(user);
	}
	
	public void setName(String name){
    	SharedPreferences.Editor editor = settings.edit();
//    	editor.putString(getString(R.string.string_en_userName), name); 	
		editor.commit();
		mUser.name = name;
	}
	
    public void setPhone(String phone){
    	SharedPreferences.Editor editor = settings.edit();
//    	editor.putString(getString(R.string.string_en_userPhone), phone);
		editor.commit();
    	mUser.phone = phone;  
    }
    
    public void setMD5Password(String MD5Password){
    	SharedPreferences.Editor editor = settings.edit();
//    	editor.putString(getString(R.string.string_en_userPassword), MD5Password);  	
		editor.commit();
    	mUser.MD5Password = MD5Password;
    }
    
    public void setSessionKey(String sessionKey){
    	SharedPreferences.Editor editor = settings.edit();
//    	editor.putString(getString(R.string.string_en_sessionKey), sessionKey); 	
		editor.commit();
    	mUser.sessionKey = sessionKey;
    }
    
    public void setIsLogin(int isLogin){
    	mUser.isLogin = isLogin;
    }
}
