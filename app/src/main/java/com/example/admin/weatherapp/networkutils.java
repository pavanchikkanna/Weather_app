package com.example.admin.weatherapp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class networkutils {
    public static final String BASE_URL="http://api.openweathermap.org/data/2.5/weather?";
    public static final String QUERY_PARAM="q=";
    public static final String API="&APPID=180d734fcae7397e7b4fb179372e7f01";
      public static final String TAG="weatherapp";
   public static String getweatherResponse(String input)
   {
       String jsonresult=null;
       HttpURLConnection urlConnection=null;
       BufferedReader reader=null;
     try
     {
         String myurl=BASE_URL+QUERY_PARAM+input+API;
        URL responeUrl= new URL(myurl);
        urlConnection= (HttpURLConnection)responeUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
       InputStream inputStream=urlConnection.getInputStream();
       if(inputStream==null){
           return null;
       }
         Log.d(TAG, "getweatherResponse: ");
       reader=new BufferedReader(new InputStreamReader(inputStream));

       StringBuffer buffer=new StringBuffer ();

       String line;
       while((line=reader.readLine())!=null)
       {
           buffer.append(line+"\n"); // adding new line character helps in debugging

       }

      jsonresult=buffer.toString();
         Log.d(TAG, "getweatherResponse: yes working ");
       return jsonresult;


     }
     catch (Exception e)
     {
         e.printStackTrace();
     }
     finally {
         if(urlConnection!=null)
         {
             urlConnection.disconnect();
         }
         if(reader!=null)
         {
             try{
                 reader.close();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }

     }
     return jsonresult;

   }


}
