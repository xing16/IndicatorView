package com.xing.indicatorview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xing.indicatorviewlib.IndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<BannerData> mDataList = new ArrayList<>();

    private IndicatorView indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicatorView = (IndicatorView) findViewById(R.id.indicator_view);
        IndicatorView numIndicatorView = (IndicatorView) findViewById(R.id.indicator_view_number);
        IndicatorView letterIndicatorView = (IndicatorView) findViewById(R.id.indicator_view_letter);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initData();
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setPageMargin(30);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, mDataList);
        mViewPager.setAdapter(adapter);
        indicatorView.setupViewPager(mViewPager);
        letterIndicatorView.setupViewPager(mViewPager);
        numIndicatorView.setupViewPager(mViewPager);

    }

    private void initData() {
        mDataList.add(new BannerData(R.drawable.ic_1, "2017高考现难题"));
        mDataList.add(new BannerData(R.drawable.ic_2, "2017高考现难题"));
        mDataList.add(new BannerData(R.drawable.ic_3, "2017高考现难题"));
        mDataList.add(new BannerData(R.drawable.ic_4, "2017高考现难题"));
    }
}
