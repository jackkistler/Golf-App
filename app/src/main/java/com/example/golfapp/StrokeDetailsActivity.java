package com.example.golfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.util.ArrayList;

public class StrokeDetailsActivity extends AppCompatActivity {

    public static final String TAG = "StrokeDetailsActivity";

    CSVStrokeDataAccess da;
    Stroke stroke;

    Spinner clubSpinner;
    CSVClubDataAccess clubDa;
    Button btnSave;
    Button btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_details);

        clubSpinner = findViewById(R.id.clubSpinner);
        clubDa = new CSVClubDataAccess(this);
        ArrayList<String> clubsArray = new ArrayList();
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        for(Club c : clubDa.getAllClubs()){
            clubsArray.add(c.getName());
        }

//        for(String name: clubsArray){
//            Log.d(TAG, name);
//        }

        ArrayAdapter<String> clubSpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, clubsArray);
        clubSpinner.setAdapter(clubSpinnerAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save()){
                    Intent i = new Intent(StrokeDetailsActivity.this, StrokeListActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(StrokeDetailsActivity.this, "Unable to save stroke", Toast.LENGTH_LONG).show();
                }
            }
        });

        //btnDelete on click listener

        Intent i = getIntent();
        //long id = i.getLongExtra(EXTRA_Stroke_ID, 0);

//        if(id > 0){
//            stroke = da.getStrokeById(id);
////            Log.d(TAG, stroke.toString());
//
//            putDataIntoUI();
//            btnDelete.setVisibility(View.VISIBLE);
//        }

    }

    private void putDataIntoUI(){
        //todo: this is where i left off
    }
}