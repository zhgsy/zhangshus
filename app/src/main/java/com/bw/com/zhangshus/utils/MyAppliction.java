package com.bw.com.zhangshus.utils;


import android.app.Activity;
import android.app.Application;

import com.bw.com.zhangshus.CrashHandler;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;

public class MyAppliction extends Application {

    private static ArrayList<Activity> activities;
    @Override
    public void onCreate() {
        super.onCreate();
        activities = new ArrayList<>();
        CrashHandler.getInstance().init(this);//初始化全局异常捕获器
        UMConfigure.init(this,"5c089159b465f59767000066","小米",UMConfigure.DEVICE_TYPE_PHONE,"");

//        设置组件化的Log开关
//        参数:
//        boolean 默认为false，如需查看LOG设置为true
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
    /**添加activity*/
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    /**结束所有被添加的activity*/
    public static void clearActivity() {
        for (Activity activity : activities){
            activity.finish();
    }
    }

}
