package com.bw.com.zhangshus;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bw.com.zhangshus.bean.LoginBean;
import com.bw.com.zhangshus.home.presenter.HomePresenterInter;
import com.bw.com.zhangshus.home.view.IHomeView;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IHomeView {

    @BindView(R.id.login_edname)
    EditText loginEdname;
    @BindView(R.id.login_pass)
    EditText loginPass;
    @BindView(R.id.login_login)
    Button loginLogin;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.share)
    Button share;
    private HomePresenterInter homePresenterInter;
    private Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(MainActivity.this);
        ButterKnife.bind(this);
        homePresenterInter = new HomePresenterInter(MainActivity.this);

    }

    @Override
    public void GetView(String viewData) {
        /*   Toast.makeText(MainActivity.this,viewData,Toast.LENGTH_SHORT).show();*/
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(viewData, LoginBean.class);
        String code = loginBean.getCode();
        if (code.equals("0")) {
            Junmp();
        } else {
            Toast.makeText(MainActivity.this, "密码不对", Toast.LENGTH_SHORT).show();
        }

    }

    //登录的点击事件
    @OnClick(R.id.login_login)
    public void onViewClicked() {
        String phone = loginEdname.getText().toString();
        String pass = loginPass.getText().toString();
        if (!phone.isEmpty() || !pass.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("mobile", phone);
            map.put("password", pass);
            homePresenterInter.GetPresenter(map);
        } else {
            Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    public void Junmp() {
        Intent intent = new Intent(MainActivity.this, ShowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        homePresenterInter.destory();
    }
    /**
     * 动态添加权限，模拟器Android版本小于6.0的不用写
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }
    /**
     * 动态添加权限回调，模拟器Android版本小于6.0的不用写，再有问的打死
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }
    @OnClick({R.id.login, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                //获得UMShareAPI实例
                UMShareAPI umShareAPI = UMShareAPI.get(MainActivity.this);

                //开始登录
                //第一个参数：上下文
                //第二个参数，登录哪种平台
                //第三个参数，添加回调
                umShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    /**
                     * 开始登录回调
                     * @param share_media
                     */
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Log.i("dj", "UMAuthListener onStart");
                    }

                    /**
                     * 登录完成
                     * @param share_media
                     * @param i
                     * @param map
                     */
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //头像，昵称，如果拿不到，自己debug看Key是啥，再问打死
                        Log.i("dj", "UMAuthListener onComplete");

                        //获取名字
                        String name = map.get("screen_name");
                        //获取头像
                        String img = map.get("profile_image_url");

                        Log.i("dj", "name is " + name);
                        Log.i("dj", "img is " + img);
                    }

                    /**
                     * 登录失败
                     * @param share_media
                     * @param i
                     * @param throwable
                     */
                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.i("dj", "UMAuthListener onError" + throwable.getLocalizedMessage());
                    }

                    /**
                     * 登录取消
                     * @param share_media
                     * @param i
                     */
                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Log.i("dj", "UMAuthListener onCancel");
                    }
                });
                break;
            case R.id.share:
                //分享用的图片
                UMImage image = new UMImage(MainActivity.this, R.drawable.umeng_socialize_qq);
                new ShareAction(MainActivity.this)
                        //分享的标题
                        .withText("hello")
                        //分享的图片
                        .withMedia(image)
                        //分享到哪，这里面只写了QQ，如有需要在后面添加
                        .setDisplayList(SHARE_MEDIA.QQ)
                        //设置回调
                        .setCallback(shareListener)
                        //打开分享面板
                        .open();
                break;
        }
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onStart");
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onResult");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.i("dj", "UMShareListener onError");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onCancel");
        }
    };
}
