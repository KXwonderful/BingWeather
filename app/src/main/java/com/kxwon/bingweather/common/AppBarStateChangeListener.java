package com.kxwon.bingweather.common;

import android.support.design.widget.AppBarLayout;

/**
 * Function：监听CollapsingToolbarLayout的展开与折叠
 * 使用官方提供的 AppBarLayout.OnOffsetChangedListener就能实现了
 * Author：kxwon on 2017/2/5 21:04
 * Email：kxwonder@163.com
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener{

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0){
            if (mCurrentState != State.EXPANDED){
                onStateChanged(appBarLayout,State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        }else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
            if (mCurrentState != State.COLLAPSED){
                onStateChanged(appBarLayout,State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        }else {
            if (mCurrentState != State.IDLE){
                onStateChanged(appBarLayout,State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
