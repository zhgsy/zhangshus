package com.bw.com.zhangshus.home.model;

import java.util.Map;

public interface IHomeModelInter {
    public void getData(String url,Map<String,String> mpars,HomeModelInter homeModelInter);
    public interface HomeModelInter{
        public void Success(String data);
        public void Faild();
    }
}
