package com.bw.com.zhangshus.utils;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.bw.com.zhangshus.ScanActivity;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttps {

    private  static volatile OkHttps instanc;
    private final OkHttpClient client;
    private FormBody.Builder body;
    public Handler handler=new Handler();

    private Interceptor getAppInterceptor(){
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.d("++++++++++", "intercept: "+request);
                Response response = chain.proceed(request);
                Log.e("++++++++++", "intercept: "+response );
                return response;
            }
        };
        return interceptor;
    }
    public static OkHttps getinstans(){
        if (instanc==null){
            synchronized (OkHttps.class){
                instanc=new OkHttps();
            }
        }
        return instanc;
    }

    public OkHttps(){
        File file=new File(Environment.getExternalStorageDirectory(),"fals");
        client = new OkHttpClient().newBuilder()
        .readTimeout(3000,TimeUnit.SECONDS)
                .addInterceptor(getAppInterceptor())
                .cache(new Cache(file,1024*2))
        .build();

    }

    public void doGet(String url, final NetWorkUtils netWorkUtils){
        final Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netWorkUtils.Success(result);
                    }
                });

            }
        });
    }
    public void  dopost(String url, Map<String,String> mpars, final NetWorkUtils netWorkUtils){
        body = new FormBody.Builder();
        for (String key:mpars.keySet()){
            body.add(key,mpars.get(key));
        }
        Request request=new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netWorkUtils.Success(result);
                    }
                });
            }
        });


    }
    public interface NetWorkUtils{
        public void Success(String data);
        public void Faild();
    }
}
