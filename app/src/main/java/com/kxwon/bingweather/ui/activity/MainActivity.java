package com.kxwon.bingweather.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.AppBarStateChangeListener;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.gson.DayForecast;
import com.kxwon.bingweather.gson.HourForecast;
import com.kxwon.bingweather.gson.Weather;
import com.kxwon.bingweather.service.AutoUpdateService;
import com.kxwon.bingweather.ui.widget.ColorArcProgressBar;
import com.kxwon.bingweather.ui.widget.refresh.MyHeader;
import com.kxwon.bingweather.utils.HttpUtils;
import com.kxwon.bingweather.utils.IntentUtils;
import com.kxwon.bingweather.utils.PrefUtils;
import com.kxwon.bingweather.utils.StatusBarUtils;
import com.kxwon.bingweather.utils.StringUtils;
import com.kxwon.bingweather.utils.ToastUtils;
import com.kxwon.bingweather.utils.Utility;
import com.liaoinstan.springview.widget.SpringView;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements OnMenuItemClickListener {

    @BindView(R.id.hour_line_chart)
    LineChartView hourLineChart;
    @BindView(R.id.iv_speak_weather)
    ImageView ivSpeakView;
    @BindView(R.id.spring_view)
    SpringView mSpringView;
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

    private String speakContent;   // 播报内容
    private String maxT;           // 今天最高温
    private String minT;           // 今天最低温

    private SpeechSynthesizer mTts;// 讯飞语音

    // 24 小时预报
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<String> hour = new ArrayList<>();         // 横坐标数据
    private List<Integer> temperature = new ArrayList<>(); // 温度

    private int SystemStatusType = 0;


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
                    //StatusBarUtils.StatusBarDarkMode(MainActivity.this,SystemStatusType);
                    mSpringView.setEnable(true); // 支持下拉刷新

                }else if (state == State.COLLAPSED){
                    // 折叠状态
                    toolbar.setVisibility(View.VISIBLE);// 显示toolbar
                    SystemStatusType = StatusBarUtils.StatusBarLightMode(MainActivity.this);//修改系统状态栏字体颜色
                    mSpringView.setEnable(false); // 不支持下拉刷新
                }else {
                    // 中间状态
                    toolbar.setVisibility(View.INVISIBLE);// 隐藏toolbar
                    //修改系统状态栏字体颜色
                    //StatusBarUtils.StatusBarDarkMode(MainActivity.this,SystemStatusType);
                    mSpringView.setEnable(false); // 不支持下拉刷新
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

        initSpringView();

        // 配置讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "5897ecd9");
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
    }

    /**
     * 初始化下拉刷新
     */
    private void initSpringView(){

        final MyHeader myHeader = new MyHeader();

        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                long curTime = (new Date()).getTime();//当前的时间
                if ((curTime - (myHeader.preLongTime) <= 1000*60*5)){//若小于5分钟不刷新
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSpringView.onFinishFreshAndLoad();
                        }
                    }, 2000);
                }else {
                    requestWeather(mWeatherId); //刷新
                }
            }

            @Override
            public void onLoadmore() {

            }
        });

        mSpringView.setHeader(myHeader);
        mSpringView.setEnable(false);
        //mSpringView.setFooter(new DefaultFooter(this));
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

        MenuObject send = new MenuObject("切换城市");
        send.setResource(R.mipmap.icn_switch);

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
                Intent share_localIntent = new Intent("android.intent.action.SEND");
                share_localIntent.setType("text/plain");
                share_localIntent.putExtra("android.intent.extra.SUBJECT", "分享");
                share_localIntent.putExtra("android.intent.extra.TEXT","推荐一个天气app：必应天气" +"链接:"+"xxx");
                this.startActivity(Intent.createChooser(share_localIntent, "分享"));
                break;
            case 3:
                ToastUtils.showShort(getMenuObjects().get(position).getTitle());
                break;
        }
    }

    @OnClick({R.id.btn_setting, R.id.iv_speak_weather,R.id.ll_forecast_day_layout,
            R.id.ll_forecast_hour_layout, R.id.ll_air_quality_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;

            case R.id.iv_speak_weather:
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }else {
                    // 播报语音天气
                    speakWeather(speakContent);
                }
                break;

            case R.id.ll_forecast_day_layout:
                // TODO
                Intent intent = new Intent(MainActivity.this,ForecastDayActivity.class);
                intent.putExtra(Constant.DAY_FORECAST,mWeatherId);
                startActivity(intent);
                break;
            case R.id.ll_forecast_hour_layout:
                //ToastUtils.showShort("待完善");
                break;
            case R.id.ll_air_quality_layout:
                //ToastUtils.showShort("待完善");
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
                        if (mSpringView != null){
                            mSpringView.onFinishFreshAndLoad();
                        }
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
                        if (mSpringView != null){
                            mSpringView.onFinishFreshAndLoad();
                        }
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
                maxT = dayForecast.temperature.max + "摄氏度";
                minT = dayForecast.temperature.min + "摄氏度";
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
        for (HourForecast hourForecast : weather.hourForecastList){
            hour.add(hourForecast.date.substring(11,16));
            temperature.add(StringUtils.stringToInt(hourForecast.temperature));
        }
        initLineChart();


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

            speakContent = "必应天气为您播报最新天气:" + cityName +
                    ",今天白天到晚间" + weatherRegime +
                    ",最高温:"+ maxT +
                    ",最低温:"+ minT +
                    "," + wind + windScale +
                    ",空气质量:"+ weather.aqi.city.qlty;
        }else {
            llAirQualityLayout.setVisibility(View.GONE);// 隐藏天气质量
            speakContent = "必应天气为您播报最新天气:" + cityName +
                    ",今天白天到晚间" + weatherRegime +
                    ",最高温:"+ maxT +
                    ",最低温:"+ minT +
                    "," + wind + windScale;
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
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

    }


    /**
     * 讯飞语音合成监听器
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        // 缓冲进度回调，arg0为缓冲进度，arg1为缓冲音频在文本中开始的位置，arg2为缓冲音频在文本中结束的位置，arg3为附加信息
        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

        }

        // 会话结束回调接口，没有错误时error为空
        @Override
        public void onCompleted(SpeechError error) {

        }

        // 开始播放
        @Override
        public void onSpeakBegin() {

        }

        // 停止播放
        @Override
        public void onSpeakPaused() {

        }

        // 播放进度回调,arg0为播放进度0-100；arg1为播放音频在文本中开始的位置，arg2为播放音频在文本中结束的位置。
        @Override
        public void onSpeakProgress(int arg0, int arg1, int arg2) {

        }

        // 恢复播放回调接口
        @Override
        public void onSpeakResumed() {

        }
    };

    /**
     * 播报语音
     * @param speakContent
     */
    private void speakWeather(String speakContent) {
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");

        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");

        // 设置音量0-100
        mTts.setParameter(SpeechConstant.VOLUME, "80");

        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        // 开始合成
        mTts.startSpeaking(speakContent, mTtsListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    speakWeather(speakContent);
                }else {
                    ToastUtils.showShort("你拒绝了权限请求");
                }
                break;

            default:
                break;
        }
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点

        Line line = new Line(mPointValues).setColor(Color.parseColor("#2894FF"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状
        line.setCubic(true);//曲线是否平滑
	    line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(R.color.gray_deep);//设置字体颜色

        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //X轴坐标显示个数
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(false); //x 轴分割线


        //设置行为属性，支持缩放、滑动以及平移
        hourLineChart.setInteractive(true);
        hourLineChart.setZoomType(ZoomType.HORIZONTAL); //缩放类型，水平
        hourLineChart.setMaxZoom((float) 3);//缩放比例
        hourLineChart.setLineChartData(data);
        hourLineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(hourLineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        hourLineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < hour.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(hour.get(i) ));
        }
    }
    /**
     * 图的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < temperature.size(); i++) {
            mPointValues.add(new PointValue(i, temperature.get(i)));
        }
    }

    @Override
    protected void onDestroy() {
        mTts.stopSpeaking();
        mTts.destroy();// 退出时释放连接
        super.onDestroy();
    }

    @Override
    protected void initData() {
    }
}
