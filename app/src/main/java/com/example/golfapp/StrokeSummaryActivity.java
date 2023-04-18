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
    TextView lblSummaryHeader;
    TextView lblAvgDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoke_summary);

        strokeDa = new CSVStrokeDataAccess(this);
        clubDa = new CSVClubDataAccess(this);
        allStrokes = strokeDa.getAllStrokes();
        lblStrokeCount = findViewById(R.id.lblCountStrokes);

        txtCountHook = findViewById(R.id.txtCountHook);
        txtCountPull = findViewById(R.id.txtCountPull);
        txtCountDraw = findViewById(R.id.txtCountDraw);
        txtCountPure = findViewById(R.id.txtCountPure);
        txtCountFade = findViewById(R.id.txtCountFade);
        txtCountPush = findViewById(R.id.txtCountPush);
        txtCountSlice = findViewById(R.id.txtCountSlice);
        lblSummaryHeader = findViewById(R.id.lblSummaryHeader);
        lblAvgDistance = findViewById(R.id.lblAverageDistance);

        Intent i = getIntent();
        long clubId = i.getLongExtra(EXTRA_CLUB_ID, -1);

        if(clubId > 0){
            //todo fill activity with club related data
            ArrayList<Stroke> clubStrokes = new ArrayList();
            for(Stroke s : allStrokes){
                if(s.getClub().getId() == clubId){
                    clubStrokes.add(s);
                }
            }
            allStrokes = clubStrokes;

            lblSummaryHeader.setText(clubDa.getClubById(clubId).getName());

        }

        totalStrokes = allStrokes.size();
        int totalDistance = 0;
        for(Stroke s : allStrokes){
            totalDistance += s.getDistance();
        }
        int averageDistance = totalDistance / totalStrokes;

        lblStrokeCount.setText(getString(R.string.lbl_num_strokes) + "\n" + totalStrokes);
        lblAvgDistance.setText(getString(R.string.lbl_average_distance) + "\n" + averageDistance + " yards");

        txtCountHook.setText(String.format("%d - %d%%", strokeCountByDirection("Hook"), (strokeCountByDirection("Hook")*100)/totalStrokes));
        txtCountPull.setText(String.format("%d - %d%%", strokeCountByDirection("Pull"), (strokeCountByDirection("Pull")*100)/totalStrokes));
        txtCountDraw.setText(String.format("%d - %d%%", strokeCountByDirection("Draw"), (strokeCountByDirection("Draw")*100)/totalStrokes));
        txtCountPure.setText(String.format("%d - %d%%", strokeCountByDirection("Pure"), (strokeCountByDirection("Pure")*100)/totalStrokes));
        txtCountFade.setText(String.format("%d - %d%%", strokeCountByDirection("Fade"), (strokeCountByDirection("Fade")*100)/totalStrokes));
        txtCountPush.setText(String.format("%d - %d%%", strokeCountByDirection("Push"), (strokeCountByDirection("Push")*100)/totalStrokes));
        txtCountSlice.setText(String.format("%d - %d%%", strokeCountByDirection("Slice"), (strokeCountByDirection("Slice")*100)/totalStrokes));




        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ball_icon);

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