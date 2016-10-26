package com.zhijiatech.bledetector.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.util.DescriptionUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMainFragment extends Fragment {

    private TextView mNowPm25ValueTextView;
    private TextView mNowPm25DescriptionTextView;

    private TextView mNowCo2ValueTextView;
    private TextView mNowCo2DescriptionTextView;
    private TextView mNowStatusTextView;

    private ImageView mCenterLineImageView;

    private RelativeLayout mNowStatusLayout;

    public FrameLayout getFrameLayout() {
        return mFrameLayout;
    }

    private FrameLayout mFrameLayout;

    public LeftMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_left_main, container, false);
        initComponent(view);
        return view;
    }

    public void initComponent(View view) {
        mNowPm25ValueTextView = (TextView) view.findViewById(R.id.now_pm25_value);
        mNowPm25DescriptionTextView = (TextView) view.findViewById(R.id.now_pm25_description);

        mNowCo2ValueTextView = (TextView) view.findViewById(R.id.now_co2_value);
        mNowCo2DescriptionTextView = (TextView) view.findViewById(R.id.now_co2_description);

        mCenterLineImageView = (ImageView) view.findViewById(R.id.center_line);
        mNowStatusTextView = (TextView) view.findViewById(R.id.now_status);
        mNowStatusLayout = (RelativeLayout) view.findViewById(R.id.now_status_layout);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.fragment_left);
    }

    public void refreshNowPm25Value(int value){
        mNowPm25ValueTextView.setText(value+"");
        mNowPm25DescriptionTextView.setText(DescriptionUtil.despPm25(value));
    }

    public void refreshNowCO2Value(int value){
        mNowCo2ValueTextView.setText(value+"");
        mNowCo2DescriptionTextView.setText(DescriptionUtil.despCO2(value));
    }

    public void refreshNowStatus(String value){
        mNowStatusTextView.setText(value+"");
    }

    public void setNowStatusTextViewInVisible(){
        mNowStatusTextView.setVisibility(View.INVISIBLE);
    }
}
