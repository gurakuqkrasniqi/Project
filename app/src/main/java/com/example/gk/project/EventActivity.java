package com.example.gk.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity {
    private TextView title,description,mStartTime,mEndTime;
    private ImageView profilePic;
    String lat,lng;
    ImageView location;
    String locationName;
    ImageButton backButton;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        profilePic=findViewById(R.id.imageView);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        mStartTime=findViewById(R.id.startTime);
        mEndTime=findViewById(R.id.endTime);
        location=findViewById(R.id.locationImg);
        backButton=findViewById(R.id.imageButton);
        linearLayout=findViewById(R.id.loc);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_up, R.anim.go_down);
                finish();

            }
        });
        title.setTextColor(getResources().getColor(R.color.colorEvent));
        Intent intent=getIntent();

        int position=intent.getIntExtra("event",0);

        Events currentEvent=MainActivity.eventsFromServer.get(position);

        try {
            title.setText(currentEvent.getName());
            description.setText(currentEvent.getDescription());
            Picasso.get().load(currentEvent.getCoverPicture()).into(profilePic);

            String startTime = currentEvent.getStartTime();
            String[] dateAndTime = startTime.split("T");
            String date = dateAndTime[0];
            String[] unformattedTime = dateAndTime[1].split(":00");
            String time = unformattedTime[0];

            String endTime = currentEvent.getEndTime();
            String[] dateAndTimeEnd = endTime.split("T");
            String dateEnd = dateAndTimeEnd[0];
            String[] unformattedTimeEnd = dateAndTimeEnd[1].split(":00");
            String timeEnd = unformattedTimeEnd[0];
            locationName=currentEvent.getName();

            mStartTime.setText(time + " " + date);
            mEndTime.setText(timeEnd + " " + dateEnd);

            lat=currentEvent.getVenue().getLocation().getLatitude();
            lng=currentEvent.getVenue().getLocation().getLongitude();
        }catch (Exception e){
            e.printStackTrace();
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("name", locationName);
                startActivity(intent);
            }
        });
    }


}
