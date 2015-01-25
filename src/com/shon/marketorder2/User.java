package com.shon.marketorder2;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.shon.marketorder2.PublicMethod;

public class User {
	public static final int FALSE 		= 0;	
	public static final int TRUE 		= 1;
	
	public static final String randNumber = "32908457";
	
    public Application application = null;   
    public String name = "";  
    public String phone = "";  
    public String MD5Password = "";
    public String nineListJson = "";
    public String sessionKey = "";
    public int isLogin = User.FALSE;  
    
    public String author = null;
//    public boolean author_INVOWNER = false;
//    public boolean author_INVPLANT = false;
//    public boolean author_ORDCRT = false;//ANDR.ORDCRT	BASE  ��������
//    public boolean author_ORDISSUE = false;//ANDR.ORDISSUE	BASE  ���������ѯ
//    public boolean author_ORDLIST = false;//ANDR.ORDLIST	BASE  ������ѯ
//    public boolean author_ORDPAY = false;//ANDR.ORDPAY	BASE  ���������ѯ
//    public boolean author_PICKLIST = false;//ANDR.PICKLIST	BASE  �ᵥ��ѯ
//    public boolean author_ORDTRACE = false;//ANDR.ORDTRACE	BASE  ���۶���ִ�и��ٱ��ѯ
//    public boolean author_REBLIST = false;//ANDR.REBLIST	BASE  ��������ѯ
//    public boolean author_PLANLIST = false;//ANDR.PLANLIST	BASE  �����ƻ���ѯ
    
    public User(Application app)
    {
    	application = app;
    }
    
    public void copy(User user)
    {
        name = user.name;  
        phone = user.phone;  
        MD5Password = user.MD5Password;
        nineListJson = user.nineListJson;
        sessionKey = user.sessionKey;
        author = user.author;
//        author_INVOWNER = user.author_INVOWNER;
//        author_INVPLANT = user.author_INVPLANT;
//        author_ORDCRT = user.author_ORDCRT;
//        author_ORDISSUE = user.author_ORDISSUE;
//        author_ORDLIST = user.author_ORDLIST;
//        author_ORDPAY = user.author_ORDPAY;
//        author_PICKLIST = user.author_PICKLIST;
//        author_ORDTRACE = user.author_ORDTRACE;
//        author_REBLIST = user.author_REBLIST;
//        author_PLANLIST = user.author_PLANLIST;
        isLogin = user.isLogin;         
    }
    
    public void logout()
    {
    	author = null;
//        author_INVOWNER = false;//ANDR.INVOWNER	BASE  ���п���ѯ
//        author_INVPLANT = false;//ANDR.INVPLANT	BASE  ��������ѯ
//        author_ORDCRT = false;//ANDR.ORDCRT	BASE  ��������/�鿴���ﳵ
//        author_ORDISSUE = false;//ANDR.ORDISSUE	BASE  ���������ѯ
//        author_ORDLIST = false;//ANDR.ORDLIST	BASE  ������ѯ
//        author_ORDPAY = false;//ANDR.ORDPAY	BASE  ���������ѯ
//        author_PICKLIST = false;//ANDR.PICKLIST	BASE  �ᵥ��ѯ
//        author_ORDTRACE = false;//ANDR.ORDTRACE	BASE  ���۶���ִ�и��ٱ��ѯ
//        author_REBLIST = false;//ANDR.REBLIST	BASE  ��������ѯ
//        author_PLANLIST = false;//ANDR.PLANLIST	BASE  �����ƻ���ѯ
        
    	isLogin = User.FALSE;	
    }
    
    @SuppressLint("NewApi") 
    public boolean login(Context context) {    
        String address = Login.mAddress;
                
        logout();
        
        JSONArray paramsHeader = new JSONArray();
        paramsHeader.put(name);
        paramsHeader.put(MD5Password);
		JSONArray params = PublicMethod.postParam(application, "LOGIN", paramsHeader);
		String result = PublicMethod.httpPost(address, params.toString());//"[\,\"\",[\"\",\"95322\",\"OWNER\",\"AA\"]]");
		if (result == null || result.isEmpty())
			return false;
		
    	try {
    		JSONObject jsonCheckResult = new JSONObject(result);
	        if (jsonCheckResult.has("ERROR"))
	        {
	        	PublicMethod.displayToast(context, jsonCheckResult.getString("ERROR"));
	        	return false;
	        }

			author = result;
	    } catch (JSONException e) {  
	    	Log.e(Login.TAG, "error while login:" + e.getMessage());
        	return false;
	    }

//        if (loginReturn.contentEquals(application.getString(R.string.string_en_error)))
//        {
//        	author = USER_AUTHOR_FREE;
//        	isLogin = User.FALSE;
//        	return false;
//        }
        
        isLogin = User.TRUE;
        return true;
    }   
    
    public String getUserArg()
    {
		
		String loginStr = getTimeArg() + "," + randNumber + "," + this.name;
		String md5Str = loginStr + "," + this.MD5Password; 
		
		return PublicMethod.md5(md5Str) + "," + loginStr;
    }
    
    @SuppressLint("NewApi") 
    public String getTimeArg()
    {
    	String result = PublicMethod.httpPost(Login.mAddress, "[\"\",\"GETTIME\",[\"\"]]");

		Date curDate  = new Date(System.currentTimeMillis()); 
		SimpleDateFormat formatterDate = new SimpleDateFormat ("yyyyMMdd,HHmmss");
		String strDate = formatterDate.format(curDate);   
		if (result != null && !result.isEmpty())
		{
			try
			{
				JSONArray serverTimeRet = new JSONArray(result);
				if (serverTimeRet.length() >= 2 && serverTimeRet.getString(0).equals("GETTIME"))
				{
					JSONArray serverTime = serverTimeRet.getJSONArray(1);
					if (serverTime.length() >= 2)
						strDate = serverTime.getString(0) + "," + serverTime.getString(1);
					else
				    	Log.e(Login.TAG, "error while gettime:");
				}
				else
			    	Log.e(Login.TAG, "error while gettime:");
			}
			catch(JSONException	e)
			{
		    	Log.e(Login.TAG, "error while gettime:" + e.getMessage());
			}
		}
		return strDate;
    }

}
