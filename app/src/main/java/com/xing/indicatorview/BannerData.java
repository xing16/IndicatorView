package com.xing.indicatorview;

/**
 * Created by Administrator on 2017/6/10.
 */

public class BannerData {

    private Integer icon;

    private String title;

    public BannerData() {
    }

    public BannerData(Integer icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BannerData{" +
                "title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
