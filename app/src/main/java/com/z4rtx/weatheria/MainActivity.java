package com.z4rtx.weatheria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.z4rtx.weatheria.apis.OpenWeatherAPI;
import com.z4rtx.weatheria.apis.Responses.Response;
import com.z4rtx.weatheria.constants.Constants;
import com.z4rtx.weatheria.database.DB;
import com.z4rtx.weatheria.utils.TimeUtil;
import com.z4rtx.weatheria.widgets.AwesomeBottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    Location location;
    LocationManager locationManager;

    LinearLayout L_main_body, L_loading_body, L_error_body;
    TextView T_location;

    Retrofit retrofit;
    OpenWeatherAPI openWeatherAPI;
    Call<Response> weatherCall;


    Response weatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Before the app actually starts, we need to ask user for permission to get access to their location
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//        }

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OPEN_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherAPI = retrofit.create(OpenWeatherAPI.class);

        TextView T_date = findViewById(R.id.day_1_date);
        T_location = findViewById(R.id.location);

        L_main_body = findViewById(R.id.main_body);
        L_loading_body = findViewById(R.id.loading_body);
        L_error_body = findViewById(R.id.error_body);

        L_error_body.setVisibility(View.GONE);
        L_main_body.setVisibility(View.GONE);
        L_loading_body.setVisibility(View.VISIBLE);

        T_date.setText(TimeUtil.getTodayDate());

        DB db = new DB(this);
        String q = db.fetchCurrentSearch();
        if(q.isEmpty())
        {
            //do something about empty search stuff
            L_error_body.setVisibility(View.VISIBLE);
            L_main_body.setVisibility(View.GONE);
            L_loading_body.setVisibility(View.GONE);
            return;
        }
        T_location.setText(q);
        q = q.split(",")[0];
        fetchWeatherDetails(q);

        //
//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


//    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.d("MainActivity", "Lat::: " + location.getLatitude() + "  --  Lon:::" + location.getLongitude());
//
//            fetchWeatherDetails(location.getLatitude(), location.getLongitude());
//
//            locationManager.removeUpdates(this);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

    private void fetchWeatherDetails(final String city) {
//        weatherCall = openWeatherAPI.getWeathers(latitude, longitude);
        weatherCall = openWeatherAPI.getWeatherForecase(city);
        weatherCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    Log.d("WEEE", "Success in " + response.body().getCity().getName());
                    weatherResponse = response.body();
                    updateUI();
                }else{
                    //Something also went wrong here
                    // =_=
                    Log.d("WEEE", "Success NOT : " + response.toString());
                    L_error_body.setVisibility(View.VISIBLE);
                    L_main_body.setVisibility(View.GONE);
                    L_loading_body.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                //Something went wrong
                //SHow some sort of error here
                Log.d("WEEE", "Failed because ::: " + t.getMessage());
                Log.d("WEEE", "www ::: " + t.toString());
                t.printStackTrace();
                fetchWeatherDetails(city);
            }
        });
    }

    private void updateUI() {
        int flag = 0;
        TimeUtil timeUtil = new TimeUtil();
        //Here we set Stuffs
        TextView T_date = findViewById(R.id.day_1_date);
        TextView T_status = findViewById(R.id.day_1_status);
        TextView T_temp = findViewById(R.id.day_1_temp);
        ImageView I_img = findViewById(R.id.day_1_img);

        T_status.setText(weatherResponse.getList().get(flag).getWeather().get(0).getMain());
        T_temp.setText(( Math.round(weatherResponse.getList().get(flag).getMain().getTemp()) + "°"));
        I_img.setImageResource(getIcon(weatherResponse.getList().get(flag).getWeather().get(0).getMain()));

        flag+=7;
        T_date = findViewById(R.id.day_2_date);
        T_status = findViewById(R.id.day_2_status);
        T_temp = findViewById(R.id.day_2_temp);
        I_img = findViewById(R.id.day_2_img);
        T_date.setText(timeUtil.toDate(weatherResponse.getList().get(flag).getDate()));
        T_status.setText(weatherResponse.getList().get(flag).getWeather().get(0).getMain());
        T_temp.setText(( Math.round(weatherResponse.getList().get(flag).getMain().getTemp()) + "°"));
        I_img.setImageResource(getIcon(weatherResponse.getList().get(flag).getWeather().get(0).getMain()));

        flag+=7;
        T_date = findViewById(R.id.day_3_date);
        T_status = findViewById(R.id.day_3_status);
        T_temp = findViewById(R.id.day_3_temp);
        I_img = findViewById(R.id.day_3_img);
        T_date.setText(timeUtil.toDate(weatherResponse.getList().get(flag).getDate()));
        T_status.setText(weatherResponse.getList().get(flag).getWeather().get(0).getMain());
        T_temp.setText(( Math.round(weatherResponse.getList().get(flag).getMain().getTemp()) + "°"));
        I_img.setImageResource(getIcon(weatherResponse.getList().get(flag).getWeather().get(0).getMain()));

        flag+=7;
        T_date = findViewById(R.id.day_4_date);
        T_status = findViewById(R.id.day_4_status);
        T_temp = findViewById(R.id.day_4_temp);
        I_img = findViewById(R.id.day_4_img);
        T_date.setText(timeUtil.toDate(weatherResponse.getList().get(flag).getDate()));
        T_status.setText(weatherResponse.getList().get(flag).getWeather().get(0).getMain());
        T_temp.setText(( Math.round(weatherResponse.getList().get(flag).getMain().getTemp()) + "°"));
        I_img.setImageResource(getIcon(weatherResponse.getList().get(flag).getWeather().get(0).getMain()));

        flag+=7;
        T_date = findViewById(R.id.day_5_date);
        T_status = findViewById(R.id.day_5_status);
        T_temp = findViewById(R.id.day_5_temp);
        I_img = findViewById(R.id.day_5_img);
        T_date.setText(timeUtil.toDate(weatherResponse.getList().get(flag).getDate()));
        T_status.setText(weatherResponse.getList().get(flag).getWeather().get(0).getMain());
        T_temp.setText(( Math.round(weatherResponse.getList().get(flag).getMain().getTemp()) + "°"));
        I_img.setImageResource(getIcon(weatherResponse.getList().get(flag).getWeather().get(0).getMain()));

        T_location.setText((weatherResponse.getCity().getName() + ", " + weatherResponse.getCity().getCountry()));

        L_error_body.setVisibility(View.GONE);
        L_main_body.setVisibility(View.VISIBLE);
        L_loading_body.setVisibility(View.GONE);


        //Save current stuff
        DB db = new DB(this);
        db.saveSearch(weatherResponse.getCity().getName(), weatherResponse.getCity().getCountry());
    }


    // Clear Clouds Rain Thunderstorm
    private int getIcon(String main){
        switch (main){
            case "Clouds":
                return  R.drawable.cloudy;
            case "Rain":
                return R.drawable.rainy;
            case "Thunderstorm":
                return R.drawable.storms;
            case "Clear":
            default: return R.drawable.sunny;
        }

    }

    public void searchLocation(View view) {
        //SHow  a bottom Slider to accept input from user

        final AwesomeBottomSheetDialog awesomeBottomSheetDialog = new AwesomeBottomSheetDialog(this);
        awesomeBottomSheetDialog.setOnSearchBtnClickListener(new AwesomeBottomSheetDialog.OnSearchBtnClicked() {
            @Override
            public void searchCity() {
                //Do search
                String q = awesomeBottomSheetDialog.getSearchText();
                if(q.isEmpty()){
                    awesomeBottomSheetDialog.dismiss();
                    L_error_body.setVisibility(View.VISIBLE);
                    L_main_body.setVisibility(View.GONE);
                    L_loading_body.setVisibility(View.GONE);
                }else{
                    L_error_body.setVisibility(View.GONE);
                    L_main_body.setVisibility(View.GONE);
                    L_loading_body.setVisibility(View.VISIBLE);
                    awesomeBottomSheetDialog.dismiss();
                    fetchWeatherDetails(q);
                }
            }
        });
        awesomeBottomSheetDialog.initialize();
    }

}