package com.example.android.weatherviewer;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private FloatingActionButton mButton;
    private RecyclerView mRecyclerView;
    private ArrayList<CityWeather> weatherList = new ArrayList<>();
    private WeatherAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.cityEditText);
        mButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherList.clear();
                String cityName = mEditText.getText().toString().trim();
                String urlStr = getResources().getString(R.string.url,cityName);
                new DownLoadWeatherInfo().execute(urlStr);
            }
        });

        mAdapter = new WeatherAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private class DownLoadWeatherInfo extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... url) {
            String result = "";
            if(url.length == 1)
            {
                try{
                    result = new JsonFetch().getUrlString(url[0]);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                parseItems(jsonObject);
                mAdapter.notifyDataSetChanged();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public class JsonFetch {
        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }
        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }
    }

    private void parseItems(JSONObject jsonBody) throws JSONException,ParseException
    {
        JSONArray weatherJsonArray = jsonBody.getJSONArray("HeWeather5");
        for(int i = 0; i< weatherJsonArray.length(); i++)
        {
            JSONObject jsonObject = weatherJsonArray.getJSONObject(i);
            JSONObject basicObject = jsonObject.getJSONObject("basic");
            JSONArray dailyObject = jsonObject.getJSONArray("daily_forecast");
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            for(int j = 0; j < dailyObject.length(); j++)
            {
                JSONObject forecastObject = dailyObject.getJSONObject(j);
                String cityName = basicObject.getString("city");
                int hum = forecastObject.getInt("hum");
                JSONObject tempObject = forecastObject.getJSONObject("tmp");
                int highTemp = tempObject.getInt("max");
                int lowTemp = tempObject.getInt("min");
                Date date = format.parse(forecastObject.getString("date"));
                JSONObject weatherObject = forecastObject.getJSONObject("cond");
                String weatherDay = weatherObject.getString("txt_d");
                String weatherNight = weatherObject.getString("txt_n");
                CityWeather temp = new CityWeather(cityName,weatherDay,weatherNight,highTemp,lowTemp,hum,date);
                weatherList.add(temp);
            }
        }
    }

    private class WeatherHolder extends RecyclerView.ViewHolder
    {
        public ImageView weatherImage;
        public TextView weatherDescriptionText;
        public TextView lowTempText;
        public TextView highTempText;
        public TextView humText;

        public WeatherHolder(View itemView) {
            super(itemView);
            weatherImage = (ImageView)itemView.findViewById(R.id.imageWeather);
            weatherDescriptionText = (TextView)itemView.findViewById(R.id.weatherDescription);
            lowTempText = (TextView)itemView.findViewById(R.id.lowTemp);
            highTempText = (TextView)itemView.findViewById(R.id.highTemp);
            humText = (TextView)itemView.findViewById(R.id.hum);
        }
    }

    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder>{

        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater
                    .inflate(R.layout.city_weather, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder holder, int position) {
            CityWeather weather = weatherList.get(position);
            holder.humText.setText(getResources().getString(R.string.hum,weather.getHum()));
            holder.highTempText.setText(getResources().getString(R.string.highTemp,weather.getTempHigh()));
            holder.lowTempText.setText(getResources().getString(R.string.lowTemp,weather.getTempLow()));
            String str = new SimpleDateFormat("yyyy-mm-dd").format(weather.getDate());
            String textShow = new StringBuilder(str).append(": ").append(weather.getWeatherDay()).toString();
            holder.weatherDescriptionText.setText(textShow);
        }

        @Override
        public int getItemCount() {
            return weatherList.size();
        }
    }

}
