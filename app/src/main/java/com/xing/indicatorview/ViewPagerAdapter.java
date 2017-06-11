package com.xing.indicatorview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<BannerData> mDataList;

    public ViewPagerAdapter(Context context, List<BannerData> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }


    @Override

    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BannerData data = mDataList.get(position);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(data.getIcon()).placeholder(R.mipmap.ic_launcher).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
