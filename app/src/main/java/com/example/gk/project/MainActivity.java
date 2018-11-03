package com.example.gk.project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "http://45.62.254.243:3000/";
    private String accessToken ="EAACEdEose0cBAGEj2GZBr5OglqZA1d0sLDwIlmQiVGBNKUZBVh6PfRuK1iU95UQSWZBtmCZCiFDXNvC0kDsQZA4CZCWkPWau4L04T42f99baCNzTqM4EgqCaLiIYCJomS1iaxCrKJ021qFAqoEPLxz6yxVmn7DzCR7OQnJOZASNA55Wm1MoGMgOorjlMqPagO5Hsu0o0VZCLeqQZDZD";
    private SeekBar seekBar;
    private ListView list1;
    private double latitude;
    private double longitude;
    private int distance;

    LocationManager locationManager;
    LocationListener locationListener;
    Button button;
    TextView distanceText;
    ArrayList<String> eventlist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    static ArrayList<Events> eventsFromServer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list1 = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventlist);
        list1.setAdapter(adapter);
        seekBar=findViewById(R.id.seekBar);
        button=findViewById(R.id.button);
        distanceText=findViewById(R.id.distanceText);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("lokacioni",location.toString());
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                Log.i("latlng",location.getLatitude()+" , "+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //ASK FOR PERMISSION
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent =new Intent(MainActivity.this,EventActivity.class);
                intent.putExtra("event",position);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up, R.anim.go_down);


            }
        });

        seekBar.setMax(2500);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                distance=position;
                double distanceInKm = (double) position/1000;
                distanceText.setText("Distance: "+distanceInKm + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getEventsData(42.6613303,21.1593072,2500);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventlist.clear();
                eventsFromServer.clear();
                getEventsData(latitude,longitude,distance);

            }
        });



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
    }

    public ApiInterface getInterfaceService(){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface mInterface=retrofit.create(ApiInterface.class);
        return mInterface;
    }

    private void getEventsData(double latitude,double longitude,int distance){
        ApiInterface mApiInterface=this.getInterfaceService();
        Call<EventResponse> mService=mApiInterface.getEvents(latitude,longitude,distance, accessToken);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();

        mService.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                EventResponse eventResponse=response.body();
                progressDoalog.dismiss();

                if(eventResponse!=null){
                    Events[] events=eventResponse.getEvents();
                    for(int i=0;i<events.length;i++){
                        Events e=events[i];
                        eventlist.add(e.getName());
                        eventsFromServer.add(events[i]);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                call.cancel();
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this,"Failure "+ t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
