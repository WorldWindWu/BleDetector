package com.zhijiatech.bledetector.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijiatech.bledetector.R;

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
    }

    public void refreshNowPm25Value(String value){
        mNowPm25ValueTextView.setText(value+"");
        mNowPm25ValueTextView.invalidate();
    }

    public void refreshNowCO2Value(String value){
        mNowCo2ValueTextView.setText(value+"");
        mNowCo2ValueTextView.invalidate();
    }

    public void refreshNowStatus(String value){
        mNowStatusTextView.setText(value+"");
        mNowStatusTextView.invalidate();
    }

    public void setNowStatusTextViewInVisible(){
        mNowStatusTextView.setVisibility(View.INVISIBLE);
    }
}
