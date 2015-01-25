package com.shon.marketorder2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**  
 * Android AutoUpdate.  
 *   
 * lazybone/2010.08.20  
 *   
 * 1.Set apkUrl.  
 *   
 * 2.check().  
 *   
 * 3.add delFile() method in resume()\onPause().  
 */  
public class AutoUpdate {   
    public Activity activity = null;   
    public int versionCode = 0;   
    public String versionName = "";   
    private static final String TAG = "AutoUpdate";   
    private String currentTempFilePath = "";   
    private String fileEx = "";   
    private String fileNa = "";   
    public String strURL = "";   
    public int total = 0;
    private AlertDialog dialog;   
    private int progressStatus = 0;
    private Handler mHandler = new Handler();
    private ProgressBar progressBarUpdate;   
    private TextView textViewPercent;   
    public AutoUpdate(Activity activity, String url, int size) {   
        this.activity = activity;   
        this.strURL = url;
        this.total = size;
        getCurrentVersion();   
    }   
    public void showWaitDialog() {   
        dialog = new AlertDialog.Builder(activity).create();   
        dialog.setTitle(R.string.string_downLoading);   
		LayoutInflater factory = LayoutInflater.from(activity);
		LinearLayout linearLayoutProgressBar = (LinearLayout)factory.inflate(R.layout.progressbarview, null);
		progressBarUpdate = (ProgressBar)linearLayoutProgressBar.findViewById(R.id.progressBarUpdate);
		textViewPercent = (TextView)linearLayoutProgressBar.findViewById(R.id.textViewPercent);
        dialog.setView(linearLayoutProgressBar);
        dialog.setCancelable(true);   
        dialog.show();   
    }   
    public void getCurrentVersion() {   
        try {   
            PackageInfo info = activity.getPackageManager().getPackageInfo(   
                    activity.getPackageName(), 0);   
            this.versionCode = info.versionCode;   
            this.versionName = info.versionName;   
        } catch (NameNotFoundException e) {   
            Log.e(TAG, "error getCurrentVersion()"); 
        }   
    }   
    public void downloadTheFile(/*final String strPath*/) {   
        fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())   
                .toLowerCase();   
        fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,   
                strURL.lastIndexOf("."));   
		Runnable r = new Runnable() {   
		    public void run() {   
		        try {   
		            doDownloadTheFile(strURL);   
		        } catch (Exception e) {   
		            Log.e(TAG, "error doDownloadTheFile();url:" + strURL);  
		            PublicMethod.showPromptDialog(activity, R.string.string_downLoadError);
		            dialog.cancel();   
		            dialog.dismiss();   
		        }   
		    }   
		};   
		new Thread(r).start();   
		showWaitDialog();
    }   
    private void doDownloadTheFile(String strPath) throws Exception {   
        Log.i(TAG, "getDataSource()");   
        if (!URLUtil.isNetworkUrl(strPath)) {   
            Log.i(TAG, "getDataSource() It's a wrong URL!");   
        } else {   
            URL myURL = new URL(strPath);   
            URLConnection conn = myURL.openConnection();   
            conn.connect();   
            InputStream is = conn.getInputStream();   
            if (is == null) {   
                throw new RuntimeException("stream is null");   
            }    
            File tempFile = File.createTempFile(fileNa, "." + fileEx);
            FileOutputStream fos = new FileOutputStream(tempFile);
            
            byte buf[] = new byte[128];   
            int index = 0;
            do {   
            	index++;
                int numread = is.read(buf);   
                if (numread <= 0) {   
                    break;   
                }   
                fos.write(buf, 0, numread);   
                
                progressStatus = (int)(index * 12800.0 / total);

                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                    	progressBarUpdate.setProgress(progressStatus);
                    	textViewPercent.setText(String.valueOf(progressStatus > 100 ? 100 : progressStatus) + "%");
                    }
                });
            } while (true);   
            Log.i(TAG, "getDataSource() Download  ok...");   
            dialog.cancel();   
            dialog.dismiss();   
            openFile(tempFile);   
            try {   
                is.close();   
            } catch (Exception ex) {   
	            Log.e(TAG, "error doDownloadTheFile(),is.close()");    
            }   
        }   
    }   
    private void openFile(File f) {   
        Intent intent = new Intent();   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        intent.setAction(android.content.Intent.ACTION_VIEW);   
        String type = getMIMEType(f);   
        intent.setDataAndType(Uri.fromFile(f), type);   
        activity.startActivity(intent);   
    }   
    public void delFile() {   
        Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");   
        File myFile = new File(currentTempFilePath);   
        if (myFile.exists()) {   
            myFile.delete();   
        }   
    }   
    private String getMIMEType(File f) {   
        String type = "";   
        String fName = f.getName();   
        String end = fName   
                .substring(fName.lastIndexOf(".") + 1, fName.length())   
                .toLowerCase();   
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")   
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {   
            type = "audio";   
        } else if (end.equals("3gp") || end.equals("mp4")) {   
            type = "video";   
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")   
                || end.equals("jpeg") || end.equals("bmp")) {   
            type = "image";   
        } else if (end.equals("apk")) {   
            type = "application/vnd.android.package-archive";   
        } else {   
            type = "*";   
        }   
        if (end.equals("apk")) {   
        } else {   
            type += "/*";   
        }   
        return type;   
    }   
}  