package com.example.admin.weatherapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class DataLoader extends android.support.v4.content.AsyncTaskLoader<String> {
  public String city =null;
    public static final String TAG="weatherapp";
    public DataLoader(Context context,String inputdata)
    {

        super(context);
        city=inputdata;
    }
    @Override
    protected void onStartLoading() {
      //  super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "getweatherResponse: yes 2 working");
        return networkutils.getweatherResponse(city);

    }
}
