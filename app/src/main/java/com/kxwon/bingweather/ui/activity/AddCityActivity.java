package com.kxwon.bingweather.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.gson.SearchCity;
import com.kxwon.bingweather.ui.adapter.CommonAdapter;
import com.kxwon.bingweather.ui.adapter.ViewHolder;
import com.kxwon.bingweather.utils.HttpUtils;
import com.kxwon.bingweather.utils.IntentUtils;
import com.kxwon.bingweather.utils.PrefUtils;
import com.kxwon.bingweather.utils.StatusBarUtils;
import com.kxwon.bingweather.utils.StringUtils;
import com.kxwon.bingweather.utils.ToastUtils;
import com.kxwon.bingweather.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddCityActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear_content)
    ImageView ivClearContent;
    @BindView(R.id.lv_search_word)
    ListView lvSearchWord;
    @BindView(R.id.tv_located_city)
    TextView tvLocatedCity;
    @BindView(R.id.layout_locate)
    LinearLayout layoutLocate;          // 定位
    @BindView(R.id.ll_locate_layout)
    LinearLayout llLocateLayout;        // 定位+城市列表

    //用来存储城市名称列表的list
    private List<SearchCity.City> mCityList = new ArrayList<SearchCity.City>();

    // 判断是否第一次启动
    boolean isFirstStart;

    private int SystemStatusType = 0;

    private LocationClient mLocationClient;

    private String mDistrict;
    private String mCity;
    private boolean isExist = false;

    /**
     * 开始地理位置定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(10000);  //10秒更新下当前位置
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    // 监听器
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            // 当前定位
            if (bdLocation.getDistrict()!= null){
                mDistrict = bdLocation.getDistrict();
                tvLocatedCity.setText(mDistrict);
            } else {
                mCity = bdLocation.getCity();
                tvLocatedCity.setText(mCity);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 ){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            ToastUtils.showShort("必须同意所有权限才能使用本程序");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    ToastUtils.showShort("发生未知错误");
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//停止定位
        StatusBarUtils.StatusBarDarkMode(this,SystemStatusType);// 还原系统状态栏
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_add_city;
    }

    @Override
    protected void initView() {

        SystemStatusType = StatusBarUtils.StatusBarLightMode(AddCityActivity.this);

        isFirstStart = PrefUtils.getBoolean(this, Constant.Pref.FIRST_START,true);

        // 声明权限，将权限添加到list集合中再一次性申请
        List<String> permissionList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(AddCityActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(AddCityActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(AddCityActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(AddCityActivity.this,permissions,1);
        }else {
            requestLocation();
        }

        //为输入内容进行监听
        etSearch.addTextChangedListener(mTextWatcher);
        // 默认软键盘不弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    @Override
    public void initPreSetContentView() {
        // 构建 LocationClient 实例
        mLocationClient = new LocationClient(getApplicationContext());
        // 注册一个定位监听器
        mLocationClient.registerLocationListener(new MyLocationListener());
    }

    /**
     * 为输入内容进行监听
     */
    TextWatcher mTextWatcher = new TextWatcher() {

        /**
         * 改变前
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         * @param s 改变之后的内容
         * @param start 开始位置
         * @param before 改变前的内容数量
         * @param count 新增数
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.equals("")){
                ivClearContent.setVisibility(View.VISIBLE);
                //发送请求:根据城市名称模糊查询城市列表
                requestCity(s.toString());
            } else {
                ivClearContent.setVisibility(View.GONE);
            }
        }

        /**
         * 最终内容
         */
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("")){
                ivClearContent.setVisibility(View.GONE);
                lvSearchWord.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 根据字符串请求城市信息
     * @param city
     */
    public void requestCity(String city) {
        String cityUrl = Constant.URL_CITY + city + "&key=" + Constant.URL_WEATHER_KEY;
        HttpUtils.sendOKHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SearchCity city = Utility.handleCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (city != null ) {
                            //先清除之前的数据
                            mCityList.clear();
                            for (int i = 0; i < city.cities.size(); i++){
                                if (city.cities.get(i).status.equals("ok")){
                                    // 将解析好的对象添加到list中
                                    mCityList.add(city.cities.get(i));
                                }
                            }

                            // 更新界面
                            initSearchWordList();
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
     * 根据字符串请求城市信息
     * @param city
     */
    public void confirmCity(String city) {
        String cityUrl = Constant.URL_CITY + city + "&key=" + Constant.URL_WEATHER_KEY;
        HttpUtils.sendOKHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{
                    MainActivity.instance.finish();
                }catch (Exception e){
                    Log.e("WONDERFUL", "nullMainActivity");
                }

                final String responseText = response.body().string();
                final SearchCity city = Utility.handleCityResponse(responseText);
                if (city != null ) {
                    mDistrict =  city.cities.get(0).basic.city;
                    //跳转到开始MainActivity
                    IntentUtils.myIntentString(AddCityActivity.this, MainActivity.class,
                            Constant.WEATHER_ID,mDistrict);
                } else {
                    //跳转到开始MainActivity
                    IntentUtils.myIntentString(AddCityActivity.this, MainActivity.class,
                            Constant.WEATHER_ID,mCity);
                }
                // 更新sp
                PrefUtils.setString(AddCityActivity.this, Constant.Pref.WEATHER, null);
                PrefUtils.setBoolean(AddCityActivity.this, Constant.Pref.FIRST_START, false);
                finish();
            }
        });
    }

    /**
     * 初始化搜索城市列表
     */
    public void initSearchWordList() {
        if ( mCityList!= null){
            lvSearchWord.setVisibility(View.VISIBLE);
            lvSearchWord.setAdapter(new CommonAdapter<SearchCity.City>(
                    AddCityActivity.this, mCityList, R.layout.layout_search_word_list_item) {
                @Override
                public void convert(final ViewHolder holder, SearchCity.City city) {

                    //设置listView数据
                    holder.setText(R.id.tv_search_word_item,city.basic.city);
                    //设置监听事件
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                MainActivity.instance.finish();
                            }catch (Exception e){
                                Log.e("WONDERFUL", "nullMainActivity");
                            }
                            //获取所在城市的id
                            String weatherId = getItem(holder.getPosition()).basic.id;
                            //ToastUtils.showShort(weatherId);
                            PrefUtils.setString(AddCityActivity.this, Constant.Pref.WEATHER, null);
                            //跳转到 MainActivity
                            IntentUtils.myIntentString(AddCityActivity.this, MainActivity.class,
                                    Constant.WEATHER_ID,weatherId);
                            finish();
                        }
                    });
                }
            });
        }
    }

    /**
     * 点击返回键事件
     */
    @OnClick(R.id.iv_back)
    public void setIvBack(){
        if (!isFirstStart) {
            // 返回
            finish();
        } else {
            //跳转到开始MainActivity
            IntentUtils.myIntentString(AddCityActivity.this, MainActivity.class,
                    Constant.WEATHER_ID,tvLocatedCity.getText().toString());
            // 更新sp
            PrefUtils.setString(AddCityActivity.this, Constant.Pref.WEATHER, null);
            PrefUtils.setBoolean(AddCityActivity.this, Constant.Pref.FIRST_START, false);
            finish();
        }
    }

    /**
     * 点击清空内容事件
     */
    @OnClick(R.id.iv_clear_content)
    public void setIvClearContent(){
        etSearch.setText("");
        ivClearContent.setVisibility(View.GONE);
    }

    /**
     * 点击当前定位内容事件
     */
    @OnClick(R.id.layout_locate)
    public void setLayoutLocate(){
        if (!StringUtils.removeAllSpace(tvLocatedCity.getText().toString()).equals("")) {
            confirmCity(tvLocatedCity.getText().toString());
        }else {
            requestLocation();
        }

    }


    @Override
    protected void initData() {

    }
}
