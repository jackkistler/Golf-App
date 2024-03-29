package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    Button btnStrokeList;
    Button btnClubList;
    Button btnAbout;
    Button btnReset;
    CSVClubDataAccess clubDa;
    CSVStrokeDataAccess strokeDa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Club c = new Club(1, "test", new Date(123,8,22));
//        Stroke s = new Stroke(1, new Date(), c, 100, "Hook");
//
//        Log.d(TAG, c.toString());
//        Log.d(TAG, s.toString());

        clubDa = new CSVClubDataAccess(this);
        strokeDa = new CSVStrokeDataAccess(this);

        btnStrokeList = findViewById(R.id.btnStrokeList);
        btnStrokeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clubDa.getAllClubs().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please register a club", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, ClubDetailsActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(MainActivity.this, StrokeListActivity.class);
                    startActivity(i);
                }

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

        btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ball_icon);
    }

    public void showInfoDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.btn_about));
        alert.setMessage(getString(R.string.about_msg));
        alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.show();
    }

    public void showResetDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.total_reset));
        alert.setMessage(getString(R.string.total_reset_text));

        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //actual deletion
                strokeDa.resetStrokes();
                clubDa.resetClubs();
                //
            }
        });
        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }


}