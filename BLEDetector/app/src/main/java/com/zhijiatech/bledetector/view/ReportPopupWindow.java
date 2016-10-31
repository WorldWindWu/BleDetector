package com.zhijiatech.bledetector.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhijiatech.bledetector.R;


/**
 * Created by 智家科技 on 2016/10/8.
 */
public class ReportPopupWindow extends PopupWindow {
    private Button mCloseReport;
    private View mView;

    public ReportPopupWindow(final Activity context) {
        super(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.report_layout,null);

        mCloseReport = (Button) mView.findViewById(R.id.close_report);

        mCloseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.onBackPressed();
            }
        });

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.report_anim_style);
    }
}
