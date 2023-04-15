package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

public class ClubListActivity extends AppCompatActivity {
    public static final String TAG = "ClubListActivity";

    private ListView lsClubs;
    private CSVClubDataAccess da;
    private ArrayList<Club> allClubs;
    Button btnAddClub;
    CSVStrokeDataAccess strokeDa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        //test clubs
//        allClubs = new ArrayList<Club>(){{
//            add(new Club(1, "My Cool Driver", new Date(123,8,22)));
//            add(new Club(2, "Shitty Wedge", new Date(123,8,22)));
//            }
//        };
        strokeDa = new CSVStrokeDataAccess(this);
        btnAddClub = findViewById(R.id.btnAddClub);
        btnAddClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClubListActivity.this, ClubDetailsActivity.class);
                startActivity(i);
            }
        });

        lsClubs = findViewById(R.id.lsClubs);
        da = new CSVClubDataAccess(this);
        allClubs = da.getAllClubs();

        //if there are no clubs, navigate to club detail activity
        if(allClubs == null || allClubs.size() == 0){
            Intent i = new Intent(this, ClubDetailsActivity.class);
            startActivity(i);
        }


        ArrayAdapter<Club> adapter = new ArrayAdapter(this, R.layout.custom_club_list_item, R.id.lblClubName, allClubs){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){
                View listItemView = super.getView(position, convertView, parentListView);
                TextView lblClubName = listItemView.findViewById(R.id.lblClubName);
                CheckBox chkActive = listItemView.findViewById(R.id.chkActive);
                TextView txtNumStrokes = listItemView.findViewById(R.id.txtNumStrokes);

                Club currentClub = allClubs.get(position);
                lblClubName.setText(currentClub.getName());
                chkActive.setChecked(currentClub.isActive());

                int numStrokes = 0;
                for(Stroke s : strokeDa.getAllStrokes()){
                    if(s.getClub().getId() == currentClub.getId()){
                        numStrokes++;
                    }
                }

                txtNumStrokes.setText("Number of Strokes: " + numStrokes);

                chkActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentClub.setActive(chkActive.isChecked());

                        try {
                            da.updateClub(currentClub);
                        } catch (Exception e) {
                            Log.d(TAG, "could not change active");
                        }
                    }
                });

                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ClubListActivity.this, ClubDetailsActivity.class);
                        i.putExtra(ClubDetailsActivity.EXTRA_CLUB_ID, currentClub.getId());
                        startActivity(i);
                    }
                });

                return listItemView;

            }
        };
        lsClubs.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ball_icon);


    }
}