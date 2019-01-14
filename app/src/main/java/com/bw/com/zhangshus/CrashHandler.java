package com.bw.com.zhangshus;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.bw.com.zhangshus.utils.MyAppliction;

public class CrashHandler  implements Thread.UncaughtExceptionHandler{
    private static CrashHandler crashHandler = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultCaughtExceptionHandler;
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        MyAppliction.clearActivity();
        //启动错误处理页面，你也可以在这里写上传服务器什么的
        Intent intent = new Intent("com.crash.start");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message",throwable.getMessage());
        mContext.startActivity(intent);
      /*  Process.killProcess(Process.myPid());
        Process.killProcess(Process.myPid());//杀掉进程*/
    }
    //使用饿汉单例模式
    public static CrashHandler getInstance() {
        return crashHandler;
    }

    public void init(Context context) { //获取默认的系统异常捕获器
        mDefaultCaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //把当前的crash捕获器设置成默认的crash捕获器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }


}
