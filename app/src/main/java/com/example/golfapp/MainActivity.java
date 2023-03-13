package com.example.golfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Club c = new Club(1, "test", new Date(123,8,22));
        Stroke s = new Stroke(1, new Date(), c, 100, "Hook");



        Log.d(TAG, c.toString());
        Log.d(TAG, s.toString());

    }
}