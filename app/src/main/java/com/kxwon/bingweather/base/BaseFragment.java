package com.kxwon.bingweather.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function：fragment 基类
 * Author：kxwon on 2017/2/1 16:27
 * Email：kxwonder@163.com
 */

public abstract class BaseFragment extends Fragment {

    protected Unbinder mBinder;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(getLayoutId(), container, false);
        mBinder = ButterKnife.bind(this, view);
        initViews(view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        mBinder.unbind();
        super.onDestroyView();
    }

    /**
     * 获取布局id
     */
    public abstract int getLayoutId();

    /**
     * 初始化布局
     */
    public abstract void initViews(View view);

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
