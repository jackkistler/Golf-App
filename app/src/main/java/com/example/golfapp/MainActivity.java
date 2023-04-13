package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    Button btnStrokeList;
    Button btnClubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Club c = new Club(1, "test", new Date(123,8,22));
//        Stroke s = new Stroke(1, new Date(), c, 100, "Hook");
//
//        Log.d(TAG, c.toString());
//        Log.d(TAG, s.toString());

        btnStrokeList = findViewById(R.id.btnStrokeList);
        btnStrokeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StrokeListActivity.class);
                startActivity(i);
            }
        });

        btnClubList = findViewById(R.id.btnClubList);
        btnClubList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ClubListActivity.class);
                startActivity(i);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);


    }


}