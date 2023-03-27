package com.example.golfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StrokeListActivity extends AppCompatActivity {

    public static final String TAG = "StrokeListActivity";

    private ListView lsStrokes;
    private CSVStrokeDataAccess da;
    private ArrayList<Stroke> allStrokes;

    Button btnAddStroke;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_list);

        da = new CSVStrokeDataAccess(this);

        Stroke s = new Stroke(1, new Date(), new Club(1, "testclub", new Date()), 100, "Hook");

//        da.convertStrokeToCSV(s); //needs to change to public to test

        btnAddStroke = findViewById(R.id.btnAddStroke);
        btnAddStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StrokeListActivity.this, StrokeDetailsActivity.class);
                startActivity(i);
            }
        });

        lsStrokes = findViewById(R.id.lsStrokes);
        allStrokes = da.getAllStrokes();

        if(allStrokes == null || allStrokes.size() == 0){
            Intent i = new Intent(this, StrokeDetailsActivity.class);
            startActivity(i);
        }

        ArrayAdapter<Stroke> adapter = new ArrayAdapter(this, R.layout.custom_stroke_list_item, R.id.lblStrokeId, allStrokes){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){
                View listItemView = super.getView(position, convertView, parentListView);
                TextView lblStrokeId = listItemView.findViewById(R.id.lblStrokeId);
                TextView lblDate = listItemView.findViewById(R.id.lblDate);
                TextView lblClub = listItemView.findViewById(R.id.lblClub);
                TextView lblDistance = listItemView.findViewById(R.id.lblDistance);

                Stroke currentStroke = allStrokes.get(position);
                lblStrokeId.setText("" + currentStroke.getId());
                lblDate.setText(sdf.format(currentStroke.getDate()));
                lblDistance.setText(currentStroke.getDistance() + "");
                lblClub.setText(currentStroke.getClub().getName());

                //add onclick listeners if needed

                return listItemView;
            }
        };

        lsStrokes.setAdapter(adapter);

        for(Stroke stroke : allStrokes){
            Log.d(TAG, stroke.toString());
        }


    }
}