package com.example.weather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    TextView cityField, detailsField, currentTemperatureField, weatherIcon, updatedField, day1, day2, day3, day4;

    Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        day1 = (TextView)findViewById(R.id.text1);
        day2 = (TextView)findViewById(R.id.text2);
        day3 = (TextView)findViewById(R.id.text3);
        day4 = (TextView)findViewById(R.id.text4);

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_updatedOn, String weather_iconText, String sun_rise, String[]forecast) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

                day1.setText(forecast[0]);
                day2.setText(forecast[1]);
                day3.setText(forecast[2]);
                day4.setText(forecast[3]);
            }
        });



        asyncTask.execute("37.3382", "-121.867905"); //  asyncTask.execute("Latitude", "Longitude")




    }


}