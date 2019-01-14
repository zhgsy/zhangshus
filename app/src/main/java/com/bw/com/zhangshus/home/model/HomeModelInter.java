package com.bw.com.zhangshus.home.model;

import android.util.Log;

import com.bw.com.zhangshus.utils.OkHttps;

import java.util.Map;

public class HomeModelInter implements IHomeModelInter{

    @Override
    public void getData(String url, Map<String, String> mpars, final HomeModelInter homeModelInter) {
        OkHttps.getinstans().dopost(url, mpars, new OkHttps.NetWorkUtils() {
            @Override
            public void Success(String data) {
                homeModelInter.Success(data);
                Log.i("aaaaa", "Success: "+data);
            }

            @Override
            public void Faild() {

            }
        });
    }
}
