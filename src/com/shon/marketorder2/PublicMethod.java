package com.shon.marketorder2;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class PublicMethod {

	public static String mAddress = "";
	public static String getDeviceId(ContextWrapper context)
	{
		// get device id
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
        return tm.getDeviceId() + "_" + context.getString(R.string.app_name);
	}
    public static boolean isNetworkAvailable(Context ctx) {   
        try {   
            ConnectivityManager cm = (ConnectivityManager) ctx   
                    .getSystemService(Context.CONNECTIVITY_SERVICE);   
            NetworkInfo info = cm.getActiveNetworkInfo();   
            return (info != null && info.isConnected());   
        } catch (Exception e) {   
            e.printStackTrace();   
            return false;   
        }   
    } 

    public static void displayToast(Context context, int strId)
    {
    	displayToast(context, context.getString(strId));
    }
    
    public static void displayToast(Context context, String str)
    {
    	Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    
	// ��drawableת����bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {  
           
            Bitmap bitmap = Bitmap.createBitmap(  
                                            drawable.getIntrinsicWidth(),  
                                            drawable.getIntrinsicHeight(),  
                                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                                            : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);   
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
            drawable.draw(canvas);  
            return bitmap;  
        } 
    public static String httpGet(String strUrl)
    {
    	String resultData = "";
    	URL url = null;
    	try
    	{
    		url = new URL(strUrl);
    	}
    	catch (MalformedURLException e)
    	{
    		Log.e(Login.TAG, "MalformedURLException");
    	}
    	
    	if (url != null)
    	{
    		try
    		{
    			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
    			urlConn.setReadTimeout(2000);
    			InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
    			BufferedReader buffer = new BufferedReader(in);
    			String inputLine = "";
    			while ((inputLine = buffer.readLine()) != null)
    			{
    				resultData += inputLine;
    			}
    			buffer.close();
    			in.close();
    			urlConn.disconnect();
    		}
        	catch (IOException e)
        	{
        		Log.e(Login.TAG, "httpGet::url:" + strUrl + ";" + e.getMessage());
        	}
    	}
    	return resultData;
    }

    @SuppressLint("NewApi")
	public static String httpPost(String strUrl, String param)
    {
    	String resultData = "";
//    	URL url = null;
//    	try
//    	{
//    		url = new URL(strUrl);
//    	}
//    	catch (MalformedURLException e)
//    	{
//    		Log.e(FullscreenActivity.DEBUG_TAG, "MalformedURLException");
//    	}
    	
    	if (strUrl != null && param != null)
    	{
//    		try {  
//    		    HttpClient httpclient = new DefaultHttpClient();  
    		    String uri = strUrl;  
    		    if (!mAddress.isEmpty())
    		    	uri = mAddress;
//    		    HttpPost httppost = new HttpPost(strUrl); 
//    		    //���httpͷ��Ϣ  
////    		    httppost.addHeader("Authorization", "your token"); //��֤token  
////    		    httppost.addHeader("Content-Type", "application/json");  
////    		    httppost.addHeader("User-Agent", "imgfornote");  
//    		    //http post��json���ݸ�ʽ��  {"name": "your name","parentId": "id_of_parent"}  
////    		    JSONObject obj = new JSONObject();  
////    		    obj.put("name", "your name");  
////    		    obj.put("parentId", "your parentid");  
//    		    httppost.setEntity(new StringEntity(param));
////    		    httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//    		    HttpResponse response = httpclient.execute(httppost);  
//    		    //����״̬�룬����ɹ���������  
//    		    int code = response.getStatusLine().getStatusCode();  
//    		    if (code == HttpStatus.SC_OK) {   
//    		    	resultData = EntityUtils.toString(response.getEntity(), "GBK");//����json��ʽ�� {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
//    		    	if (resultData.contains("ERROR"))
//    		    		return null;
////    		        obj = new JSONObject(rev);  
////    		        String id = obj.getString("id");  
////    		        String version = obj.getString("version");  
////    		        return rev;
//    		    }  
//    		    
//    		} 
//    		catch (ClientProtocolException e) {     
//        		Log.e(FullscreenActivity.DEBUG_TAG, "httpPost::url:" + strUrl + ";" + e.getMessage());
//    		} 
//        	catch (IOException e)
//        	{
//        		Log.e(FullscreenActivity.DEBUG_TAG, "httpPost::url:" + strUrl + ";" + e.getMessage());
//        	}
//    		catch (Exception e) {   
//        		Log.e(FullscreenActivity.DEBUG_TAG, "httpPost::url:" + strUrl + ";" + e.getMessage());
//    		}

    		 SyncHttpClient client = new SyncHttpClient()
    		 {

				@Override
				public String onRequestFailed(Throwable error, String content) {
//					PublicMethod.displayToast(, content);
					return "[\"ERROR\",\"" + content + "\"]";
				}
    			 
    		 };
    		 return client.post(uri, new RequestParams(param));
    	}
    	return resultData;
    }
    
    public static Drawable httpGetImage(String strUrl)
    {
    	URL url = null;
    	try
    	{
    		url = new URL(strUrl);
    	}
    	catch (MalformedURLException e)
    	{
    		Log.e(Login.TAG, "MalformedURLException");
    	}
    	
    	if (url != null)
    	{
    		try
    		{
    			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
    			InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
//    			BufferedReader buffer = new BufferedReader(in);
    			
    			Drawable draw = Drawable.createFromStream(urlConn.getInputStream(), "");
//    			buffer.
//    			String inputLine = "";
//    			while ((inputLine = buffer.readLine()) != null)
//    			{
//    				resultData += inputLine;
//    			}
    			in.close();
    			urlConn.disconnect();

                return draw;
    		}
        	catch (IOException e)
        	{
        		Log.e(Login.TAG, "httpGetImage:url:" + strUrl + ";" + e.getMessage());
        	}
    	}
    	return null;
    }
    

    public static String md5(String plainText){  
        String result="";  
        try 
        {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(plainText.getBytes());  
            byte b[] = md.digest();  
            int i;  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++){  
                    i = b[offset];  
                    if (i < 0)  
                    i += 256;  
                    if (i < 16)  
                    buf.append("0");  
                    buf.append(Integer.toHexString(i));  
            }  
            result = buf.toString();
	    } 
        catch (NoSuchAlgorithmException e)
        {  
             e.printStackTrace();  
	    }    
        return result;  
    }

    // ��bitmap���浽file
    public static void saveMyBitmap(Bitmap bm,String bitName) throws IOException {
            File f = new File("/sdcard/Note/" + bitName + ".png");
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                    fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
        		Log.e(Login.TAG, "httpGet::bitName:" + bitName + ";" + e.getMessage());
            }
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            try {
                    fOut.flush();
            } catch (IOException e) {
        		Log.e(Login.TAG, "httpGet::bitName:" + bitName + ";" + e.getMessage());
            }
            try {
                    fOut.close(); 
            } catch (IOException e) {
        		Log.e(Login.TAG, "httpGet::bitName:" + bitName + ";" + e.getMessage());
            }
    } 
    
    public static void showPromptDialog(Context context, int stringId)
    {
    	Dialog dialog = new AlertDialog.Builder(context)
    					.setTitle(R.string.string_prompt)
    					.setMessage(stringId)
    					.setPositiveButton(R.string.string_ok, 
    							new DialogInterface.OnClickListener() {
			
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								}).create();
    	dialog.show();
    } 
    public static void showPromptDialog(Context context, String stringMessage)
    {
    	Dialog dialog = new AlertDialog.Builder(context)
    					.setTitle(R.string.string_prompt)
    					.setMessage(stringMessage)
    					.setPositiveButton(R.string.string_ok, 
    							new DialogInterface.OnClickListener() {
			
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub

									}
								}).create();
    	dialog.show();
    }

	public static Bitmap bitmap_Out_Of_Memory_Handle(String bitmapPath,int w,int h)
	{
//			InputStream inputStream;
//			byte[] len = null ;
//			try {
//				inputStream = new FileInputStream(bitmapPath);
//				len = streamToBytes(inputStream);
//				
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//				return null;
//			}
			
		BitmapFactory.Options options = new BitmapFactory.Options();  
//            options.inJustDecodeBounds = true;   
            options.inSampleSize = 4;
			Bitmap bm =  BitmapFactory.decodeFile(bitmapPath, options);//.decodeByteArray(len, 0, len.length);
            options.inJustDecodeBounds = false;  
//            try {
//				inputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
 
			if(bm ==null)
			{
				return null;
			}

			int  src_w = bm.getWidth();
			int  src_h = bm.getHeight();
			float scale_w = ((float)w)/src_w;
			float  scale_h = ((float)h)/src_h;
			Matrix  matrix = new Matrix();
			matrix.postScale(scale_w, scale_h);
			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, src_w, src_h, matrix, true);
			
			bm.recycle();
			
		return bitmap;
		
	}
	public static byte[] streamToBytes(InputStream is) {  
	    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);  
	    byte[] buffer = new byte[1024];  
	    int len;  
	    try {  
	        while ((len = is.read(buffer)) >= 0) {  
	            os.write(buffer, 0, len);  
	        }  
	    } catch (java.io.IOException e) {  
	    }  
	    return os.toByteArray();  
	} 
	public static JSONArray postParam(Context context, String requestType, JSONArray header)
	{
		JSONArray ret = new JSONArray();
		MarketOrder2Application app = (MarketOrder2Application)context.getApplicationContext();
		User user = app.getUser();
		if (user != null)
			ret.put(user.getUserArg());
		else
			ret.put("");
		ret.put(requestType);
		ret.put(header);
		
		return ret;
	}
	public static String convertToHex(byte[] data) {
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < data.length; i++) {
	        int halfbyte = (data[i] >>> 4) & 0x0F;
	        int two_halfs = 0;
	        do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	                buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
	        } while(two_halfs++ < 1);
	    }
	    return buf.toString();
	}
	public static String HloveyRC4(String aInput,String aKey)   
    {   
		try {
		    byte[] keytest = aKey.getBytes();
			RC4 rc4 = new RC4(keytest);
			return convertToHex(rc4.encrypt(aInput.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        int[] iS = new int[256];   
        byte[] iK = new byte[256];   
          
        for (int i=0;i<256;i++)   
            iS[i]=i;   
              
        int j = 1;   
          
        for (short i= 0;i<256;i++)   
        {   
            iK[i]=(byte)aKey.charAt((i % aKey.length()));   
        }   
          
        j=0;   
          
        for (int i=0;i<255;i++)   
        {   
            j=(j+iS[i]+iK[i]) % 256;   
            int temp = iS[i];   
            iS[i]=iS[j];   
            iS[j]=temp;   
        }   
      
      
        int i=0;   
        j=0;   
        char[] iInputChar = aInput.toCharArray();   
        char[] iOutputChar = new char[iInputChar.length];   
        for(short x = 0;x<iInputChar.length;x++)   
        {   
            i = (i+1) % 256;   
            j = (j+iS[i]) % 256;   
            int temp = iS[i];   
            iS[i]=iS[j];   
            iS[j]=temp;   
            int t = (iS[i]+(iS[j] % 256)) % 256;   
            int iY = iS[t];   
            char iCY = (char)iY;   
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;      
        }   
          
        return new String(iOutputChar);   
                  
    }  
	public static void write(String path, String content) {
//	      String s = new String();
//	      String s1 = new String();
	      try {
//	       File f = new File(path);
//	       if (f.exists()) {
//	        System.out.println("�ļ�����");
//	       } else {
//	        System.out.println("�ļ������ڣ����ڴ���...");
//	        if (f.createNewFile()) {
//	         System.out.println("�ļ������ɹ���");
//	        } else {
//	         System.out.println("�ļ�����ʧ�ܣ�");
//	        }
//	       }
//	       BufferedReader input = new BufferedReader(new FileReader(f));
//	       while ((s = input.readLine()) != null) {
//	        s1 += s + "\n";
//	       }
//	       System.out.println("�ļ����ݣ�" + s1);
//	       input.close();
//	       s1 += content;
//	       BufferedWriter output = new BufferedWriter(new FileWriter(f));
//	       output.write(s1);
//	       output.close();

//	       FileOutputStream fos=new FileOutputStream (path);
//	       fos.write(content.getBytes());
//	       fos.flush();
//	       fos.close();
		
		RandomAccessFile raf = null;
		FileOutputStream fos = null;
		fos = new FileOutputStream(path);
		int len = content.length();
//		while (len>0)
		{
			fos.write(content.getBytes(),0,len);
			fos.close();
		}
	      } catch (Exception e) {
	       e.printStackTrace();
	      }
	}
}
