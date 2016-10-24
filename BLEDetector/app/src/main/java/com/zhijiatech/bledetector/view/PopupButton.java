package com.zhijiatech.bledetector.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.util.ScreenUtils;


/**
 * Created by 智家科技 on 2016/10/8.
 */
public class PopupButton extends PopupWindow {
    private Button mCloseReport;
    private View mView;

    public PopupButton(Activity context) {
        super(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.popup_button,null);

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ScreenUtils.getScreenHeight(context)/10);
        Log.i("-------",ScreenUtils.getScreenHeight(context)/9+"----height");
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.report_anim_style);
    }
}
