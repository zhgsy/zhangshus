package com.bw.com.zhangshus.home.presenter;

import com.bw.com.zhangshus.MainActivity;
import com.bw.com.zhangshus.home.model.HomeModelInter;
import com.bw.com.zhangshus.home.model.IHomeModelInter;
import com.bw.com.zhangshus.utils.Api;

import java.util.Map;

public class HomePresenterInter implements IHomePresenterInter{

    MainActivity mview;
    private final HomeModelInter homeModelInter;


    public HomePresenterInter(MainActivity mview) {
        this.mview = mview;
        homeModelInter = new HomeModelInter();
    }
    @Override
    public void GetPresenter(final Map<String,String> mpars) {
        homeModelInter.getData(Api.LOGIN, mpars, new IHomeModelInter.HomeModelInter() {
            @Override
            public void Success(String data) {
                mview.GetView(data);
            }

            @Override
            public void Faild() {

            }
        });
    }
    public void destory(){
        if (mview!=null){
            mview=null;
        }
    }
}
