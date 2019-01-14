package com.bw.com.zhangshus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class ShowActivity extends AppCompatActivity {

    @BindView(R.id.erweima)
    ImageView erweima;
    @BindView(R.id.editstring)
    EditText editstring;
    @BindView(R.id.saomiao)
    Button saomiao;
    @BindView(R.id.shengcheng)
    Button shengcheng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        saomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        shengcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQRCode();
            }
        });


    }
    private void checkPermission() {
        //第一步，判断系统版本是否为6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //第二步：checkSelfPermission判断有没有此权限
            //第一个参数：上下文
            //第二个参数：我们想要判断的权限，此处为相机权限
            //PackageManager.PERMISSION_GRANTED 条件，权限有没有被授予
            if (ContextCompat.checkSelfPermission(ShowActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //如果没授予，则申请权限
                //第一个：上下文
                //第二个：要申请的权限数组，此处为相机权限
                //第三个：请求码，startActivityForResult一样
                ActivityCompat.requestPermissions(ShowActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            startActivity(new Intent(ShowActivity.this, ScanActivity.class)) ;
        }
    }


    public void checkPrison() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ShowActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ShowActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                startActivity(new Intent(ShowActivity.this, ScanActivity.class));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果requestCode匹配，切权限申请通过
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(ShowActivity.this, ScanActivity.class));
        }
    }
    private void createQRCode() {
        QRTask qrTask = new QRTask(ShowActivity.this, erweima,editstring);
        qrTask.execute(editstring.getText().toString());
    }
    static class QRTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<Context> mContext;
        private WeakReference<ImageView> mImageView;

        public QRTask(Context context, ImageView image, EditText ed) {
            mContext = new WeakReference<>(context);
            mImageView = new WeakReference<>(image);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String str = params[0];
            if (TextUtils.isEmpty(str)) {
                return null;
            }
//            int size = mContext.get().getResources().getDimensionPixelOffset(R.dimen.qr_code_size);-
            return QRCodeEncoder.syncEncodeQRCode(str, 200);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mImageView.get().setImageBitmap(bitmap);
            } else {
                Toast.makeText(mContext.get(), "生成失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(ShowActivity.this, ScanfActivity.class));
        }

    }

    private void createQRCode() {
        QRTask task = new QRTask(ShowActivity.this, ima, smEd);
    }

    class QRTask extends AsyncTask<String, Void, Bitmap> {


        public QRTask(ShowActivity showActivity, ImageView ima, EditText smEd) {

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mImageView.get().setImageBitmap(bitmap);
            } else {
                Toast.makeText(mContext.get(), "生成失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
*/

}
