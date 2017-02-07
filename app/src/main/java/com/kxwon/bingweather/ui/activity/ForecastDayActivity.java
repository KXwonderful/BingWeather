package com.kxwon.bingweather.ui.activity;

import android.widget.ImageView;

import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.gson.DayForecast;
import com.kxwon.bingweather.gson.Weather;
import com.kxwon.bingweather.gson.WeatherDayForecast;
import com.kxwon.bingweather.utils.DateUtils;
import com.kxwon.bingweather.utils.HttpUtils;
import com.kxwon.bingweather.utils.PrefUtils;
import com.kxwon.bingweather.utils.StringUtils;
import com.kxwon.bingweather.utils.ToastUtils;
import com.kxwon.bingweather.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhouzhuo.zzweatherview.AirLevel;
import me.zhouzhuo.zzweatherview.WeatherItemView;
import me.zhouzhuo.zzweatherview.WeatherModel;
import me.zhouzhuo.zzweatherview.ZzWeatherView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForecastDayActivity extends BaseActivity {

    @BindView(R.id.forecast_day_weather)
    ZzWeatherView forecastDayWeather;
    @BindView(R.id.iv_quit_forecast)
    ImageView ivQuitForecast;

    private String weatherId;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_forecast_day;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void initData() {

        weatherId = getIntent().getStringExtra(Constant.DAY_FORECAST);

        requestWeather(weatherId);

    }

    /**
     * 根据天气 id 请求城市天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = Constant.URL_WEATHER_FORECAST_DAY + weatherId + "&key=" + Constant.URL_WEATHER_KEY;
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
                final WeatherDayForecast weather = Utility.handleDayForecastResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (weather != null && "ok".equals(weather.status)) {
                            showWeatherInfo(weather);
                        } else {
                            ToastUtils.showShort("获取天气信息失败");
                        }
                    }
                });
            }
        });
    }

    /**
     * 展示预报信息
     * @param weather
     */
    private void showWeatherInfo(WeatherDayForecast weather){
        //填充天气数据
        forecastDayWeather.setList(generateData(weather));

        //画折线
        //forecastDayWeather.setLineType(ZzWeatherView.LINE_TYPE_DISCOUNT);
        //画曲线
        forecastDayWeather.setLineType(ZzWeatherView.LINE_TYPE_CURVE);

        //设置线宽
        forecastDayWeather.setLineWidth(6f);

        //点击某一列
        forecastDayWeather.setOnWeatherItemClickListener(new ZzWeatherView.OnWeatherItemClickListener() {
            @Override
            public void onItemClick(WeatherItemView itemView, int position, WeatherModel weatherModel) {
                // todo
            }
        });
    }

    /**
     * 初始化数据
     * @return
     */
    private List<WeatherModel> generateData(WeatherDayForecast weather) {
        List<WeatherModel> list = new ArrayList<WeatherModel>();
        for (DayForecast dayForecast : weather.dayForecastList){
            WeatherModel model = new WeatherModel();
            model.setDate(dayForecast.date.substring(5,10));
            if (dayForecast.equals(weather.dayForecastList.get(0))) {
                model.setWeek("今天");
            }else if (dayForecast.equals(weather.dayForecastList.get(1))){
                model.setWeek("明天");
            }else {
                model.setWeek(DateUtils.StringData(dayForecast.date));
            }
            model.setDayWeather(dayForecast.more.info_d);
            model.setDayTemp(StringUtils.stringToInt(dayForecast.temperature.max));
            model.setNightTemp(StringUtils.stringToInt(dayForecast.temperature.min));
            model.setNightWeather(dayForecast.more.info_n);
            model.setWindOrientation(dayForecast.wind.direction);
            model.setWindLevel(dayForecast.wind.windScale);
            model.setAirLevel(AirLevel.HIGH);
            list.add(model);
        }



//        WeatherModel model1 = new WeatherModel();
//        model1.setDate("12/08");
//        model1.setWeek("今天");
//        model1.setDayWeather("晴");
//        model1.setDayTemp(8);
//        model1.setNightTemp(5);
//        model1.setNightWeather("晴");
//        model1.setWindOrientation("西南风");
//        model1.setWindLevel("3级");
//        model1.setAirLevel(AirLevel.HIGH);
//        list.add(model1);
////
////
//        WeatherModel model3 = new WeatherModel();
//        model3.setDate("12/10");
//        model3.setWeek("周六");
//        model3.setDayWeather("晴");
//        model3.setDayTemp(12);
//        model3.setNightTemp(9);
//        model3.setDayPic(R.drawable.w0);
//        model3.setNightPic(R.drawable.w1);
//        model3.setNightWeather("晴");
//        model3.setWindOrientation("东北风");
//        model3.setWindLevel("3级");
//        model3.setAirLevel(AirLevel.GOOD);
//        list.add(model3);

        return list;

    }

    @OnClick(R.id.iv_quit_forecast)
    public void onClick(){
        finish();
    }
}
