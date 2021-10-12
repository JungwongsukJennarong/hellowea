package com.example.hellowea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView country_yt, city_yt, temp_yt, datetime_yt, latitude_yt, longitude_yt, humidity_yt, sunrise_yt, sunset_yt, pressure_yt, wind_yt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        country_yt = findViewById(R.id.country);
        city_yt = findViewById(R.id.enter_city);
        temp_yt = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);
        datetime_yt = findViewById(R.id.textView2);
        latitude_yt = findViewById(R.id.Latitude);
        longitude_yt = findViewById(R.id.Longitude);
        humidity_yt = findViewById(R.id.Humidity);
        sunrise_yt = findViewById(R.id.Sunrise);
        sunset_yt = findViewById(R.id.Sunset);
        pressure_yt = findViewById(R.id.Pressure);
        wind_yt = findViewById(R.id.Wind);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findWeather();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {

            Intent intent = new Intent(MainActivity.this,About.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void findWeather(){
            String city = editText.getText().toString();
            String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=21c2fa219c7b8f0d0cdfbdc1e11bb851&units=metric";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //calling api

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        //find country
                        JSONObject object1 = jsonObject.getJSONObject("sys");
                        String country_find = object1.getString("country");
                        country_yt.setText(country_find);

                        //find city
                        String city_find = jsonObject.getString("name");
                        city_yt.setText(city_find);

                        //find temperature
                        JSONObject object2 = jsonObject.getJSONObject("main");
                        String temp_find = object2.getString("temp");
                        temp_yt.setText(temp_find+"°C");

                        //find image icon
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String img = jsonObject1.getString("icon");
                        Picasso.get().load("http://openweathermap.org/img/wn/"+img+"@4x.png").into(imageView);

                        // find date and time
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat std = new SimpleDateFormat("yyyy/MM/dd \nHH:mm");
                        String datetime_find = std.format(calendar.getTime());
                        datetime_yt.setText(datetime_find);

                        //find latitude
                        JSONObject object = jsonObject.getJSONObject("coord");
                        double lat_find = object.getDouble("lat");
                        latitude_yt.setText(": "+lat_find+"°N");

                        //find longitude
                        JSONObject object3 = jsonObject.getJSONObject("coord");
                        double long_find = object3.getDouble("lon");
                        longitude_yt.setText(": "+long_find+"°E");

                        //find humidity
                        JSONObject object4 = jsonObject.getJSONObject("main");
                        String humidity_find = object4.getString("humidity");
                        humidity_yt.setText(": "+humidity_find+"%");

                        //find sunrise
                        JSONObject object5 = jsonObject.getJSONObject("sys");
                        Long sunrise_find = object5.getLong("sunrise");
                        Long sunrise_get = sunrise_find * 1000;
                        Date date1 = new Date(sunrise_get);
                        String sunrise_find_final = new SimpleDateFormat("HH:mm").format(date1);
                        sunrise_yt.setText(": "+sunrise_find_final);

                        //find sunset
                        JSONObject object6 = jsonObject.getJSONObject("sys");
                        Long sunset_find = object6.getLong("sunset");
                        Long sunset_get = sunset_find * 1000;
                        Date date2 = new Date(sunset_get);
                        String sunset_find_final = new SimpleDateFormat("HH:mm").format(date2);
                        sunset_yt.setText(": "+sunset_find_final);

                        //find pressure
                        JSONObject object7 = jsonObject.getJSONObject("main");
                        String pressure_find = object7.getString("pressure");
                        pressure_yt.setText(": "+pressure_find+"hPa");

                        //find wind speed
                        JSONObject object8 = jsonObject.getJSONObject("wind");
                        String wind_find = object8.getString("speed");
                        wind_yt.setText(": "+wind_find+"km/h");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT);
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);

        }

}