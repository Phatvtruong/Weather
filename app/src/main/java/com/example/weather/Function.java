package com.example.weather;

/**
 * Created by Phat on 11/1/17.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Function {

    private static final String WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API = "f0c7468004e1e74298c2ef1cf21de50b";

    private static String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric";

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }



    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output6, String output7, String output8, String[] output9);
    }





    public static class placeIdTask extends AsyncTask<String, Void, JSONArray> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            JSONArray jsonWeather = new JSONArray();
            JSONObject jsonForecast = new JSONObject();;
            JSONObject jsonCurrent = new JSONObject();
            try {
                jsonCurrent = getWeatherJSON(params[0], params[1]);
                jsonForecast = getForecastWeatherJSON(params[0], params[1]);
                jsonWeather.put(0,jsonCurrent);
                jsonWeather.put(1,jsonForecast);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArr) {
            try {
                JSONObject current = jsonArr.getJSONObject(0);
                JSONObject forecast = jsonArr.getJSONObject(1);

                int u = 0;
                if(current != null){
                    JSONObject details = current.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = current.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String[] forecastArr = new String[4];


                    String city = current.getString("name").toUpperCase(Locale.US) + ", " + current.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";
                    String updatedOn = df.format(new Date(current.getLong("dt")*1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            current.getJSONObject("sys").getLong("sunrise") * 1000,
                            current.getJSONObject("sys").getLong("sunset") * 1000);

                    String date ;
                    String date1 ;
                    String fore ;
                    if(forecast != null){

                        JSONArray jlist = forecast.getJSONArray("list");
                        for (int i=0; i < jlist.length(); i++) {
                            JSONObject jDayForecast = jlist.getJSONObject(i);
                            JSONObject wea = jDayForecast.getJSONArray("weather").getJSONObject(0);
                            JSONObject main1 = jDayForecast.getJSONObject("main");
                            DateFormat day = DateFormat.getDateTimeInstance();
                           /* String iconText1 = setWeatherIcon(wea.getInt("id"),
                                    jDayForecast.getJSONObject("sys").getLong("sunrise") * 1000,
                                    jDayForecast.getJSONObject("sys").getLong("sunset") * 1000);*/
                            date = day.format(new Date(jDayForecast.getLong("dt")*1000));
                            date = date.substring(0,6);
                            date1 = jDayForecast.getString("dt_txt");
                            date1 = date1.replaceAll("\\s+","");
                            date1 = date1.substring(10,12);
                            if(date1.equals("12"))
                            {
                                String temp = String.format("%.2f", main1.getDouble("temp"))+ "°";
                                String tempMin = main1.getString("temp_min") + "°";
                                String tempMax = main1.getString("temp_max") + "°";
                                fore = date + "    " + String.format("%6s", temp) + "   " + String.format("%6s", tempMin) + "-" + String.format("%6s", tempMax);
                                if(u < 4)
                                { forecastArr[u] = fore;
                                    u++;
                                }

                            }
                        }
                    }
                    delegate.processFinish(city, description, temperature,  updatedOn, iconText, ""+ (current.getJSONObject("sys").getLong("sunrise") * 1000), forecastArr);

                }



            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }



        }
    }






    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(WEATHER_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }


    public static JSONObject getForecastWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(FORECAST_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }


}