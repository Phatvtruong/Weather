package com.example.weather;

import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CityWeatherListView extends ListFragment {
    private ArrayList<City> mCities;
    private static final String TAG = "CityListFragment";
    private static final int REQUEST_CITY = 1;
    private static final int REQUEST_SETTING = 4;
    private static final int REQUEST_CITY_DETAIL = 2;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;

    private class CityAdapter extends ArrayAdapter<City> {
        public CityAdapter(ArrayList<City> cities) {
            super(getActivity(), 0, cities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_city, null);
            }
            // Configure the view for this Crime
            City c = getItem(position);
            TextView cityNameTextView =
                    (TextView)convertView.findViewById(R.id.city_name_list_item);
            cityNameTextView.setText(c.getCityName());

            TextView cityTempTextView =
                    (TextView)convertView.findViewById(R.id.city_list_item_temp);
            if(!CityListSingleton.get(getActivity()).isDegreeCelcius())
                cityTempTextView.setText(c.getTemp() +"" +  (char) 0x00B0);
            else
                cityTempTextView.setText(Api.getFarenheit(c.getTemp())+"" +  (char) 0x00B0);

            TextView timeTextView =
                    (TextView) convertView.findViewById(R.id.city_time_list_item);
            timeTextView.setText(c.getCurrentTime());
            return convertView;
        }
    }


}
