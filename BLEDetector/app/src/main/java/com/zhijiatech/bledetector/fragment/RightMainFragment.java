package com.zhijiatech.bledetector.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.activity.CaptureActivity;
import com.zhijiatech.bledetector.activity.OutdoorAirActivity;
import com.zhijiatech.bledetector.view.ReportPopupWindow;


/**
 * A simple {@link Fragment} subclass.
 */
public class RightMainFragment extends Fragment {
    private Button mDetectTypeButton_1,mDetectTypeButton_2,mDetectTypeButton_3;
    private Button mOutputReportButton;

    private ReportPopupWindow mReportPopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_right_amin, container, false);
        mDetectTypeButton_1 = (Button) view.findViewById(R.id.detect_type_1);
        mDetectTypeButton_2 = (Button) view.findViewById(R.id.detect_type_2);
        mDetectTypeButton_3 = (Button) view.findViewById(R.id.detect_type_3);

        mDetectTypeButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(getActivity(), OutdoorAirActivity.class);
                startActivity(intent_1);
            }
        });

        mDetectTypeButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_2 = new Intent(getActivity(), OutdoorAirActivity.class);
                startActivity(intent_2);
            }
        });

        mDetectTypeButton_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_3 = new Intent(getActivity(), OutdoorAirActivity.class);
                startActivity(intent_3);
            }
        });

        mOutputReportButton = (Button) view.findViewById(R.id.output_report);
        mOutputReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_4 = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent_4);
            }
        });

        return view;
    }

}