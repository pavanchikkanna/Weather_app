package com.example.admin.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int LOADER_ID=0;

    //String  city=null;
    TextView textview1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textview5;
    ImageView image;
 private String main_data=null;
 private String description_data=null;
 private float temp_data;
 private String temperature_data=null;
 private int humid_data;

private SharedPreferences preferences;
    public String city_data=null;




    public static final String TAG="weatherapp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview5=(TextView)findViewById(R.id.city_view);
        textview1=(TextView)findViewById(R.id.main_view);
        textView2=(TextView)findViewById(R.id.description_view);
        textView3=(TextView)findViewById(R.id.temperature_view);
        textView4=(TextView)findViewById(R.id.humidity_view);
        image=(ImageView)findViewById(R.id.imageView);
       preferences=PreferenceManager.getDefaultSharedPreferences(this);
     city_data=preferences.getString("city_key","Bengaluru");

     textview5.setText(city_data);
        Log.d(TAG, "onCreate: VALUE OF PREFERENCE "+city_data);
      preferences.registerOnSharedPreferenceChangeListener(this);
      init_loader();//initializing background task
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void init_loader()
    {
        //check if loader is running ,if it is then reconnect that by passing null as parameter for bundle
        if(getSupportLoaderManager().getLoader(LOADER_ID)!=null)
        {
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }
        else{
            Bundle querybundle= new Bundle();
            querybundle.putString("city_name",city_data);
            // this is same as initLoader except if exist, it destroys that loader and restart the loader
            getSupportLoaderManager().restartLoader(LOADER_ID,querybundle,this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id,  Bundle args) {
        return new DataLoader(this,args.getString("city_name"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.setting_id)
        {
            //launching setting activity
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onLoadFinished( Loader<String> loader, String data)
    {
      //json parsing and updating ui

        try
        {
            JSONObject object =new JSONObject(data);

           JSONArray array=object.getJSONArray("weather");
           JSONObject object1=array.getJSONObject(0);
           String whatweather=object1.getString("main");
           String about =object1.getString("description");
           JSONObject object2=object.getJSONObject("main");
          double temper=object2.getDouble("temp");
          temper=temper-273.15;
         //converting kelvin value to celsius
          int humid=object2.getInt("humidity");
            Log.d(TAG, "getweatherResponse: yes 3 working");

            //assigning  data to variables
            main_data=whatweather;
            description_data=about;
            temp_data=(float)temper;
            humid_data=humid;
            temperature_data=String.format("%s",temp_data)+"°c";
            updateUI();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset( Loader<String> loader) {

    }
    public void updateUI()
    {
        textview1.setText("Climate:\n"+main_data);
        textView2.setText(description_data);
        textView3.setText(temperature_data);
        textView4.setText("Humidity:\n"+String.format("%s",humid_data)+"%");
        return;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

//this method restarts the background task if the sharedpreference attribute ie; city name is changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        textview5.setText(sharedPreferences.getString(key,"Bengaluru"));
        Bundle querybundle= new Bundle();
        querybundle.putString("city_name",sharedPreferences.getString(key,"Bengaluru"));
        // this is same ass initLoader except if exist, it destroys that loader and restart the loader
        getSupportLoaderManager().restartLoader(LOADER_ID,querybundle,this);
    }
}
