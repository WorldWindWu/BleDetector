package com.zhijiatech.bledetector.activity;

import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.view.ReportPopupWindow;

public class ReportActivity extends BaseActivity implements View.OnClickListener {

    private int mRoomArea;
    private EditText mEditTextRoomArea;
    private Button mButton1, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8, mButton9;
    private Button mButtonGenerateReport;
    private ReportPopupWindow mReportPopupWindow;

    @Override
    public void init() {
        setContentView(R.layout.activity_report);
        initCom();
        mRoomArea = 0;

    }

    private void initCom() {
        mEditTextRoomArea = (EditText) findViewById(R.id.editText);
        mButton1 = (Button) findViewById(R.id.default_value_8);
        mButton1.setOnClickListener(this);

        mButton2 = (Button) findViewById(R.id.default_value_10);
        mButton2.setOnClickListener(this);

        mButton3 = (Button) findViewById(R.id.default_value_15);
        mButton3.setOnClickListener(this);

        mButton4 = (Button) findViewById(R.id.default_value_20);
        mButton4.setOnClickListener(this);

        mButton5 = (Button) findViewById(R.id.default_value_30);
        mButton5.setOnClickListener(this);

        mButton6 = (Button) findViewById(R.id.default_value_45);
        mButton6.setOnClickListener(this);

        mButton7 = (Button) findViewById(R.id.default_value_90);
        mButton7.setOnClickListener(this);

        mButton8 = (Button) findViewById(R.id.default_value_110);
        mButton8.setOnClickListener(this);

        mButton9 = (Button) findViewById(R.id.default_value_140);
        mButton9.setOnClickListener(this);

        mButtonGenerateReport = (Button) findViewById(R.id.generate_report);
        mButtonGenerateReport.setOnClickListener(this);
    }

    @Override
    public void register() {

    }

    @Override
    public void baseHandleMessage(Message msg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_value_8:
                mRoomArea = 8;
                break;

            case R.id.default_value_10:
                mRoomArea = 10;
                break;

            case R.id.default_value_15:
                mRoomArea = 15;
                break;

            case R.id.default_value_20:
                mRoomArea = 20;
                break;

            case R.id.default_value_30:
                mRoomArea = 30;
                break;

            case R.id.default_value_45:
                mRoomArea = 45;
                break;

            case R.id.default_value_90:
                mRoomArea = 90;
                break;

            case R.id.default_value_110:
                mRoomArea = 110;
                break;

            case R.id.default_value_140:
                mRoomArea = 140;
                break;

            case R.id.generate_report:
                mReportPopupWindow = new ReportPopupWindow(ReportActivity.this);
                mReportPopupWindow.showAtLocation(ReportActivity.this.findViewById(R.id.report_root), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }

        Log.i("Click____", mRoomArea + "");
        mEditTextRoomArea.setText(mRoomArea + "");
    }
}
