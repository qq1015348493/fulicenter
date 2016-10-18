package cn.ucai.fulicenter.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/10/18.
 */

public class PageAdapter extends PagerAdapter{
    int[] images = new int[4];

    public PageAdapter() {
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

}
