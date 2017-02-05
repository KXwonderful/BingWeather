package com.kxwon.bingweather.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kxwon.bingweather.R;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.utils.IntentUtils;
import com.kxwon.bingweather.utils.PrefUtils;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        boolean isFirstStart = PrefUtils.getBoolean(this, Constant.Pref.FIRST_START,true);

        if (!isFirstStart){
            IntentUtils.myIntent(this,MainActivity.class);
            finish();
        }else {
            IntentUtils.myIntent(this,AddCityActivity.class);
            finish();
        }
    }
}
