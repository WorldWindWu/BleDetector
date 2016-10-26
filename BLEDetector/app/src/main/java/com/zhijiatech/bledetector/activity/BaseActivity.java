package com.zhijiatech.bledetector.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.ble.BLEReceiver;

import java.lang.reflect.Field;

/**
 * Created by Jiafeng on 2016/10/13.
 */
public abstract class BaseActivity extends FragmentActivity implements SlidingPaneLayout.PanelSlideListener {

    public final static String TAG = BaseActivity.class.getCanonicalName();

    public BLEReceiver mBLEReceiver;

    public Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        register();
        initHandler();
        init();

    }

    public abstract void init();
    public abstract void register();

    public abstract void baseHandleMessage(Message msg);

    public void unRegister(){
        if (null != mBLEReceiver)
        {
            unregisterReceiver(mBLEReceiver);
        }
    }

    public void sendHandlerMessage(int what, Object object)
    {
        if (mHandler == null)
        {
            return;
        }
        Message msg = mHandler.obtainMessage(what, object);
        mHandler.sendMessage(msg);
    }

    private void initHandler()
    {
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    default:
                        baseHandleMessage(msg);
                        break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            try {
                //属性
                Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                f_overHang.setAccessible(true);
                f_overHang.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));

            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            slidingPaneLayout.addView(leftView, 0);

            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
            decor.removeView(decorChild);
            decor.addView(slidingPaneLayout);
            slidingPaneLayout.addView(decorChild, 1);
        }
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
        finish();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }
}

