package com.fyhack.pro.databindingpro;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;

/**
 * DatabindApplication
 * <p/>
 *
 * @author elc_simayi
 * @since 2016/3/25
 */

public class DatabindApplication extends Application{
    public final String TAG = "DatabindApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        initOneSDK(getApplicationContext());
    }

    /**
     * 初始化AlibabaSDK
     * @param applicationContext
     */
    private void initOneSDK(final Context applicationContext) {
        AlibabaSDK.asyncInit(applicationContext, new InitResultCallback() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "init onesdk success");
                //alibabaSDK初始化成功后，初始化移动推送通道
                initCloudChannel(applicationContext);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e(TAG, "init onesdk failed : " + message);
            }
        });

        MANService manService = AlibabaSDK.getService(MANService.class);
        try {
            if (getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getBoolean("isDebug")){
                manService.getMANAnalytics().turnOffCrashHandler();
                manService.getMANAnalytics().turnOnDebug();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化移动推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        if(cloudPushService != null) {
            cloudPushService.register(applicationContext,  new CommonCallback() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "init cloudchannel success");
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Log.d(TAG, "init cloudchannel fail" + "err:" + errorCode + " - message:"+ errorMessage);
                }
            });

            // 绑定别名
            cloudPushService.bindAccount("deviceID", new CommonCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "bindAccount success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    Log.d(TAG, "bindAccount success");
                }
            });
        }else{
            Log.i(TAG, "CloudPushService is null");
        }
    }
}
