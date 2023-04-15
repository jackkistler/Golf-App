package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Stroke;

import java.util.ArrayList;

public class StrokeSummaryActivity extends AppCompatActivity {
    public static final String TAG = "StrokeSummaryActivity";
    public static final String EXTRA_CLUB_ID = "clubId";

    CSVStrokeDataAccess strokeDa;
    CSVClubDataAccess clubDa;
    ArrayList<Stroke> allStrokes = new ArrayList();
    ArrayList<Stroke> calculatedStrokes = new ArrayList();
    TextView lblStrokeCount;
    int totalStrokes;

    TextView txtCountHook;
    TextView txtCountPull;
    TextView txtCountDraw;
    TextView txtCountPure;
    TextView txtCountFade;
    TextView txtCountPush;
    TextView txtCountSlice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoke_summary);

        strokeDa = new CSVStrokeDataAccess(this);
        clubDa = new CSVClubDataAccess(this);
        allStrokes = strokeDa.getAllStrokes();
        lblStrokeCount = findViewById(R.id.lblCountStrokes);
        totalStrokes = allStrokes.size();
        txtCountHook = findViewById(R.id.txtCountHook);
        txtCountPull = findViewById(R.id.txtCountPull);
        txtCountDraw = findViewById(R.id.txtCountDraw);
        txtCountPure = findViewById(R.id.txtCountPure);
        txtCountFade = findViewById(R.id.txtCountFade);
        txtCountPush = findViewById(R.id.txtCountPush);
        txtCountSlice =findViewById(R.id.txtCountSlice);


        lblStrokeCount.setText(totalStrokes + "");

        txtCountHook.setText(String.format("%d - %d%%", strokeCountByDirection("Hook"), (strokeCountByDirection("Hook")*100)/totalStrokes));
        txtCountPull.setText(String.format("%d - %d%%", strokeCountByDirection("Pull"), (strokeCountByDirection("Pull")*100)/totalStrokes));
        txtCountDraw.setText(String.format("%d - %d%%", strokeCountByDirection("Draw"), (strokeCountByDirection("Draw")*100)/totalStrokes));
        txtCountPure.setText(String.format("%d - %d%%", strokeCountByDirection("Pure"), (strokeCountByDirection("Pure")*100)/totalStrokes));
        txtCountFade.setText(String.format("%d - %d%%", strokeCountByDirection("Fade"), (strokeCountByDirection("Fade")*100)/totalStrokes));
        txtCountPush.setText(String.format("%d - %d%%", strokeCountByDirection("Push"), (strokeCountByDirection("Push")*100)/totalStrokes));
        txtCountSlice.setText(String.format("%d - %d%%", strokeCountByDirection("Slice"), (strokeCountByDirection("Slice")*100)/totalStrokes));








        Intent i = getIntent();
        long clubId = i.getLongExtra(EXTRA_CLUB_ID, -1);

        if(clubId > 0){
            //todo fill activity with club related data
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ball_icon);

    }

    public ArrayList<Stroke> getStrokesByClub(long id){
        ArrayList<Stroke> returnArray = new ArrayList();

        for(Stroke s : allStrokes){
            //todo: populate return array
        }


        return returnArray;
    }

    public int strokeCountByDirection(String direction){
        int count = 0;

        for(Stroke s : allStrokes){
            if(s.getDirectionString().equals(direction)){
                count++;
            }
        }
        return count;
    }
}