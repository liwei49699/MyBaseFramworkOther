package com.sanshang.li.mybaseframwork;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sanshang.li.mybaseframwork.base.BaseActivity;
import com.sanshang.li.mybaseframwork.service.NetWorkService;
import com.sanshang.li.mybaseframwork.translucent.TranslucentActivity;
import com.sanshang.li.mybaseframwork.util.DeviceUtils;
import com.sanshang.li.mybaseframwork.util.LogUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_content)
    TextView mTvContent;
    private Intent networkIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        hideState();
        hideTitle();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitleName("主页");

        //监听网络变化
        networkIntent = new Intent(this, NetWorkService.class);
        startService(networkIntent);

        int width = DeviceUtils.getWidth(this);
        int height = DeviceUtils.getHeight(this);
        int stateHeight = DeviceUtils.getStateHeight(this);

        LogUtils.d("MainActivity onCreate()" + width);
        LogUtils.d("MainActivity onCreate()" + height);
        LogUtils.d("MainActivity onCreate()" + stateHeight);

        String offsetLon = "21";
        String offsetAlt = "21";

        String accuracy = "0";
        if(!TextUtils.isEmpty(offsetLon) && !"null".equals(offsetLon)
                && !TextUtils.isEmpty(offsetAlt) && !"null".equals(offsetAlt)) {

            double lon = Math.pow(Double.parseDouble(offsetLon),2);
            double alt = Math.pow(Double.parseDouble(offsetAlt), 2);

            double sqrt = Math.sqrt(lon + alt);

            //数字格式化 用BigDecimal表示，然后在输出string

            DecimalFormat df = new DecimalFormat("0.00");
            String formatAccuracy = df.format(sqrt);
            accuracy = formatAccuracy;
        }

        Log.d("--TAG--", "MainActivity onCreate():" + accuracy);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(networkIntent);
    }

    @OnClick(R.id.tv_content)
    public void onViewClicked() {

        Message obtain = Message.obtain();

        obtain.arg1 = 10;
        obtain.arg2 = 100;
        mHandler.sendMessage(obtain);
    }

    @Override
    protected void handlerMsg(Message msg) {

        Toast toast = Toast.makeText(MainActivity.this, msg.arg1 + "==" + msg.arg2, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);

        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View inflate = getLayoutInflater().inflate(R.layout.toast_simple_show, rootView,false);
        toast.setView(inflate);
        toast.show();


        startActivity(new Intent(this, TranslucentActivity.class));
    }
}
