package com.kxwon.bingweather.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.kxwon.bingweather.utils.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function：基类
 * Author：kxwon on 2017/2/1 13:17
 * Email：kxwonder@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Unbinder mBinder;

    /**
     * 初始化布局id
     * @return 布局id
     */
    protected abstract int initLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * setContentView() 方法前的方法
     */
    public void initPreSetContentView() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPreSetContentView();

        setContentView(initLayoutId());

        mBinder = ButterKnife.bind(this);

        //ClearStatusBar();
        StatusBarUtils.transparencyBar(this);

        initView();

        initData();

    }

    /**
     * 状态栏着色
     */
    private void ClearStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    protected void onDestroy() {
        // 取消绑定
        mBinder.unbind();
        super.onDestroy();
    }

}
