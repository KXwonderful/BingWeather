package com.kxwon.bingweather.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.AppBarStateChangeListener;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.gson.DayForecast;
import com.kxwon.bingweather.gson.Weather;
import com.kxwon.bingweather.ui.widget.ColorArcProgressBar;
import com.kxwon.bingweather.utils.HttpUtils;
import com.kxwon.bingweather.utils.IntentUtils;
import com.kxwon.bingweather.utils.PrefUtils;
import com.kxwon.bingweather.utils.StatusBarUtils;
import com.kxwon.bingweather.utils.StringUtils;
import com.kxwon.bingweather.utils.ToastUtils;
import com.kxwon.bingweather.utils.Utility;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements OnMenuItemClickListener {

    @BindView(R.id.iv_title_image)
    ImageView ivTitleImage;
    @BindView(R.id.btn_setting)
    Button btnSetting;                  // 设置
    @BindView(R.id.tv_title_city)
    TextView tvTitleCity;               // 城市
    @BindView(R.id.toolbar)
    Toolbar toolbar;                    //toolbar
    @BindView(R.id.bing_image_view)
    ImageView bingImageView;            // 必应图片
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;             // 温度
    @BindView(R.id.tv_address)
    TextView tvAddress;                 // 城市
    @BindView(R.id.tv_weather)
    TextView tvWeather;                 // 天气
    @BindView(R.id.tv_now_wind)
    TextView tvNowWind;                 // 风
    @BindView(R.id.tv_now_wind_scale)
    TextView tvNowWindScale;            // 风等级
    @BindView(R.id.tv_now_humidity)
    TextView tvNowHumidity;             // 相对湿度
    @BindView(R.id.tv_now_air)
    TextView tvNowAir;                  // 空气质量
    @BindView(R.id.tv_now_air_code)
    TextView tvNowAirCode;              // 空气质量数值
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.ll_forecast_day_layout)
    LinearLayout llForecastDayLayout;           // day 趋势预报
    @BindView(R.id.ll_forecast_hour_layout)
    LinearLayout llForecastHourLayout;          // hour 趋势预报
    @BindView(R.id.tv_air_quality)
    TextView tvAirQuality;                      // 空气质量
    @BindView(R.id.tv_release_time)
    TextView tvReleaseTime;                     // 发布时间
    @BindView(R.id.cap_bar_aqi)
    ColorArcProgressBar capBarAqi;              // aqi空气质量指数
    @BindView(R.id.cap_bar_pm25)
    ColorArcProgressBar capBarPm25;             // pm2.5污染物指数
    @BindView(R.id.ll_air_quality_layout)
    LinearLayout llAirQualityLayout;            // 空气质量布局
    @BindView(R.id.tv_suggestion_travel)
    TextView tvSuggestionTravel;                // 建议 旅游
    @BindView(R.id.tv_suggestion_travel_info)
    TextView tvSuggestionTravelInfo;            // 建议 旅游详情
    @BindView(R.id.tv_suggestion_dress)
    TextView tvSuggestionDress;                 // 建议 穿着
    @BindView(R.id.tv_suggestion_dress_info)
    TextView tvSuggestionDressInfo;             // 建议 穿着详情
    @BindView(R.id.tv_suggestion_uv)
    TextView tvSuggestionUv;                    // 建议 紫外线
    @BindView(R.id.tv_suggestion_uv_info)
    TextView tvSuggestionUvInfo;                // 建议 紫外线详情
    @BindView(R.id.tv_suggestion_sport)
    TextView tvSuggestionSport;                 // 建议 运动
    @BindView(R.id.tv_suggestion_sport_info)
    TextView tvSuggestionSportInfo;             // 建议 运动详情
    @BindView(R.id.tv_suggestion_car)
    TextView tvSuggestionCar;                   // 建议 洗车
    @BindView(R.id.tv_suggestion_car_info)
    TextView tvSuggestionCarInfo;               // 建议 洗车详情
    @BindView(R.id.nsv_layout)
    NestedScrollView nsvLayout;

    private String mWeatherId;

    private FragmentManager fragmentManager;

    private ContextMenuDialogFragment mMenuDialogFragment;//菜单栏

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        fragmentManager = getSupportFragmentManager();

        initToolbar();
        toolbar.setVisibility(View.INVISIBLE);

        // 监听CollapsingToolbarLayout的展开与折叠
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED){
                    // 展开状态
                    toolbar.setVisibility(View.INVISIBLE);// 隐藏toolbar
                    //修改系统状态栏字体颜色
                    StatusBarUtils.StatusBarDarkMode(MainActivity.this);
                }else if (state == State.COLLAPSED){
                    // 折叠状态
                    toolbar.setVisibility(View.VISIBLE);// 显示toolbar
                    StatusBarUtils.StatusBarLightMode(MainActivity.this);//修改系统状态栏字体颜色
                }else {
                    // 中间状态
                    toolbar.setVisibility(View.INVISIBLE);// 隐藏toolbar
                    //修改系统状态栏字体颜色
                    StatusBarUtils.StatusBarDarkMode(MainActivity.this);
                }
            }
        });

        // 初始化菜单项
        initMenuFragment();

        // 天气信息
        String weatherString = PrefUtils.getString(this, Constant.Pref.WEATHER, null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            try{
                mWeatherId = weather.basic.weatherId;
            }catch (NullPointerException e){
                mWeatherId = Constant.DEFAULT_WEATHER_ID;
            }

            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra(Constant.WEATHER_ID);
            nsvLayout.setVisibility(View.INVISIBLE);   // 隐藏
            requestWeather(mWeatherId);
        }

        // 必应图片
        String bingPic = PrefUtils.getString(this, Constant.Pref.BRING_PIC,null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingImageView);
        }else {
            loadBingPic();
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 设置菜单项
     *
     * @return
     */
    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.icn_close);

        MenuObject send = new MenuObject("添加/删除城市");
        send.setResource(R.mipmap.icn_edit);

        MenuObject like = new MenuObject("分享天气");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.icn_share);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("设置");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_setting));
        addFr.setDrawable(bd);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        return menuObjects;
    }

    /**
     * 初始化菜单栏
     */
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                IntentUtils.myIntent(this, AddCityActivity.class);
                break;
            case 2:
                ToastUtils.showShort(getMenuObjects().get(position).getTitle());
                break;
            case 3:
                ToastUtils.showShort(getMenuObjects().get(position).getTitle());
                break;
        }
    }

    @OnClick({R.id.btn_setting, R.id.ll_forecast_day_layout,
            R.id.ll_forecast_hour_layout, R.id.ll_air_quality_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
            case R.id.ll_forecast_day_layout:
                ToastUtils.showShort("待完善");
                break;
            case R.id.ll_forecast_hour_layout:
                ToastUtils.showShort("待完善");
                break;
            case R.id.ll_air_quality_layout:
                ToastUtils.showShort("待完善");
                break;
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        HttpUtils.sendOKHttpRequest(Constant.URL_BEING_PIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                PrefUtils.setString(MainActivity.this,Constant.Pref.BRING_PIC,bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPic).into(bingImageView);
                    }
                });
            }
        });
    }

    /**
     * 根据天气 id 请求城市天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = Constant.URL_WEATHER + weatherId + "&key=" + Constant.URL_WEATHER_KEY;
        HttpUtils.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("获取天气信息失败");
                        //swipeRefresh.setRefreshing(false);// 刷新结束，隐藏刷新进度条
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            PrefUtils.setString(MainActivity.this, Constant.Pref.WEATHER, responseText);
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            ToastUtils.showShort("获取天气信息失败");
                        }
                        //swipeRefresh.setRefreshing(false);// 刷新结束，隐藏刷新进度条
                    }
                });
            }
        });
    }

    /**
     * 处理并展示 Weather 实体类中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {

        // 头部信息展示 now
        String degree = weather.now.temperature + "℃";
        String cityName = weather.basic.cityName;
        String textTitle = cityName + " " + degree;
        String weatherRegime = weather.now.more.info;
        tvTemperature.setText(degree);
        tvAddress.setText(cityName);
        // TODO
        String ivTitleCode = weather.now.more.code + ".png";
        Glide.with(this).load(Constant.URL_COND_ICON + ivTitleCode).into(ivTitleImage);
        tvTitleCity.setText(textTitle);
        tvWeather.setText(weatherRegime);

        String wind = weather.now.wind.direction;
        String windScale = weather.now.wind.windScale + "级";
        String humidity = weather.now.humidity + "%";
        String airAqi;
        String airAqiCode;
        if (weather.aqi != null){
            airAqi = "空气" + weather.aqi.city.qlty;
            airAqiCode = weather.aqi.city.aqi;
        } else {
            airAqi = "空气优";
            airAqiCode = "45";
        }
        tvNowWind.setText(wind);
        tvNowWindScale.setText(windScale);
        tvNowHumidity.setText(humidity);
        tvNowAir.setText(airAqi);
        tvNowAirCode.setText(airAqiCode);

        // 天气预报
        llForecastDayLayout.removeAllViews();
        // 前面三天预报
        for (DayForecast dayForecast : weather.dayForecastList.subList(0,3)) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, llForecastDayLayout, false);
            ImageView ivCloud = ButterKnife.findById(view,R.id.iv_forecast_cloud);
            TextView tvDate = ButterKnife.findById(view, R.id.tv_forecast_date);
            TextView tvInfo = ButterKnife.findById(view, R.id.tv_forecast_info);
            TextView tvAqi = ButterKnife.findById(view, R.id.tv_forecast_wind);
            TextView tvMax = ButterKnife.findById(view, R.id.tv_forecast_max);
            TextView tvMin = ButterKnife.findById(view, R.id.tv_forecast_min);
            TextView tvHorizontalLine = ButterKnife.findById(view, R.id.tv_forecast_horizontal_line);
            String ivCode = dayForecast.more.code_d + ".png";
            Glide.with(this).load(Constant.URL_COND_ICON + ivCode).into(ivCloud);
            if (dayForecast.equals(weather.dayForecastList.get(0))) {
                tvDate.setText("今天");
            }else if (dayForecast.equals(weather.dayForecastList.get(1))){
                tvDate.setText("明天");
            }else if (dayForecast.equals(weather.dayForecastList.get(2))){
                tvDate.setText("后天");
                tvHorizontalLine.setVisibility(View.INVISIBLE);
            }else {
                tvDate.setText(dayForecast.date);
            }
            tvInfo.setText(dayForecast.more.info_d);
            tvAqi.setText(dayForecast.wind.direction);
            String forecastMax = dayForecast.temperature.max + "°";
            String forecastMin = dayForecast.temperature.min + "°";
            tvMax.setText(forecastMax);
            tvMin.setText(forecastMin);
            llForecastDayLayout.addView(view);
        }

        // 24小时预报
        // ...

        // 空气质量
        if (weather.aqi != null){
            tvAirQuality.setText(weather.aqi.city.qlty);
            String updateTime = weather.basic.update.locateTime.substring(11,16) + "发布";
            tvReleaseTime.setText(updateTime);

            // 获取屏幕宽度
            WindowManager manager = this.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int mWidth = outMetrics.widthPixels;

            capBarAqi.setTextSize(45);         // 设置速度文字大小
            capBarAqi.setCurrentValues(StringUtils.stringToInt(weather.aqi.city.aqi)); // 设置当前进度

            capBarPm25.setTextSize(45);         // 设置速度文字大小
            capBarPm25.setCurrentValues(StringUtils.stringToInt(weather.aqi.city.pm25)); // 设置当前进度

            llAirQualityLayout.setVisibility(View.VISIBLE);// 显示天气质量
        }else {
            llAirQualityLayout.setVisibility(View.GONE);// 隐藏天气质量
        }

        // 出行建议
        String travel = "旅行：" + weather.suggestion.travel.brief;
        String travelInfo = weather.suggestion.travel.info;
        String dress = "穿着：" + weather.suggestion.dress.brief;
        String dressInfo = weather.suggestion.dress.info;
        String uv = "紫外线：" + weather.suggestion.uv.brief;
        String uvInfo = weather.suggestion.uv.info;
        tvSuggestionTravel.setText(travel);
        tvSuggestionTravelInfo.setText(travelInfo);
        tvSuggestionDress.setText(dress);
        tvSuggestionDressInfo.setText(dressInfo);
        tvSuggestionUv.setText(uv);
        tvSuggestionUvInfo.setText(uvInfo);

        // 运动建议
        String sport = "运动：" + weather.suggestion.sport.brief;
        String sportInfo = weather.suggestion.sport.info;
        tvSuggestionSport.setText(sport);
        tvSuggestionSportInfo.setText(sportInfo);

        // 驾车建议
        String carWash = "洗车：" + weather.suggestion.carWash.brief;
        String carWashInfo = weather.suggestion.carWash.info;
        tvSuggestionCar.setText(carWash);
        tvSuggestionCarInfo.setText(carWashInfo);

        nsvLayout.setVisibility(View.VISIBLE);

        // 开启服务
        //Intent intent = new Intent(this, AutoUpdateService.class);
        //startService(intent);

    }



    @Override
    protected void initData() {
    }
}
