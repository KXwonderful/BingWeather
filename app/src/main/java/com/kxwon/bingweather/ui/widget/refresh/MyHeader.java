package com.kxwon.bingweather.ui.widget.refresh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kxwon.bingweather.R;
import com.liaoinstan.springview.container.BaseHeader;

import java.util.Date;

/**
 * Function：SpringView 下拉刷新头部
 * Author：kxwon on 2017/2/6 10:07
 * Email：kxwonder@163.com
 */

public class MyHeader extends BaseHeader{

    private TextView mTextView;

    public long preLongTime = 0;//定义上一次刷新的时间

    /**
     * 获取Header
     */
    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.spring_view_header, viewGroup, true);
        mTextView = (TextView)view.findViewById(R.id.tv_spring_view);
        return view;
    }

    /**
     * 拖拽开始前回调
     */
    @Override
    public void onPreDrag(View rootView) {

    }

    /**
     * 手指拖拽过程中不断回调，dy为拖拽的距离，可以根据拖动的距离添加拖动过程动画
     */
    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    /**
     * 手指拖拽过程中每次经过临界点时回调，upORdown是向上经过还是向下经过
     */
    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        long curTime = (new Date()).getTime();//当前的时间
        if ((curTime - preLongTime) <= 1000*60*5){//若小于5分钟显示刚刚更新
            mTextView.setText(R.string.test_refreshed);
        }else {
            mTextView.setText(R.string.test_refresh);
            preLongTime = curTime;//当前刷新事件变为上次时间
        }
    }

    /**
     * 拉动超过临界点后松开时回调
     */
    @Override
    public void onStartAnim() {

    }

    /**
     * 头部已经全部弹回时回调
     */
    @Override
    public void onFinishAnim() {
        if (preLongTime == 0){  //第一次刷新,初始化为本次刷新的时间
            preLongTime = (new Date()).getTime();
        }
    }

    /**
     * 这个方法用于设置当前View的临界高度(limit hight)，即拉动到多少会被认定为刷新超作，而没到达该高度则不会执行刷新
     * 返回值大于0才有效，如果<=0 则设置为默认header的高度
     * 默认返回0
     */
    @Override
    public int getDragLimitHeight(View rootView) {
        return 100;
    }

    /**
     * 这个方法用于设置下拉最大高度(max height)，无论怎么拉动都不会超过这个高度
     * 返回值大于0才有效，如果<=0 则默认600px
     * 默认返回0
     */
    @Override
    public int getDragMaxHeight(View rootView) {
        return super.getDragMaxHeight(rootView);
    }

    /**
     * 这个方法用于设置下拉弹动高度(spring height)，即弹动后停止状态的高度
     * 返回值大于0才有效，如果<=0 则设置为默认header的高度
     * 默认返回0
     */
    @Override
    public int getDragSpringHeight(View rootView) {
        return 250;
    }
}
