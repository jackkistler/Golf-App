package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StrokeDetailsActivity extends AppCompatActivity {

    public static final String TAG = "StrokeDetailsActivity";
    public static final String EXTRA_STROKE_ID = "strokeId";
    public static final String EXTRA_CLUB_ID = "clubId";

    CSVStrokeDataAccess da;
    Stroke stroke;

    Spinner clubSpinner;
    Spinner directionSpinner;
    CSVClubDataAccess clubDa;
    Button btnSave;
    Button btnDelete;
    EditText txtDistance;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_details);

        clubSpinner = findViewById(R.id.clubSpinner);
        directionSpinner = findViewById(R.id.directionSpinner);
        txtDistance = findViewById(R.id.txtDistance);
        clubDa = new CSVClubDataAccess(this);
        ArrayList<String> clubsArray = new ArrayList();
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        da = new CSVStrokeDataAccess(this);
        directionSpinner.setSelection(3);

        for(Club c : clubDa.getAllClubs()){
            if(c.isActive()){
                clubsArray.add(c.getName());
            }
        }

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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_STROKE_ID, 0);
        long clubId = i.getLongExtra(EXTRA_CLUB_ID, -1);

        if(id > 0){
            stroke = da.getStrokeById(id);
//            Log.d(TAG, stroke.toString());

            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);
        }

        if(clubId > 0){
            Club selectedClub = clubDa.getClubById(clubId);
            for(int index = 0; index < clubSpinner.getCount(); index++){
                if(clubSpinner.getItemAtPosition(index).toString().equalsIgnoreCase(selectedClub.getName())){
                    clubSpinner.setSelection(index);
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);

    }

    private void putDataIntoUI(){
        if(stroke != null){
            clubSpinner.setSelection(getIndex(clubSpinner, stroke.getClub().getName()));
            directionSpinner.setSelection(getIndex(directionSpinner, stroke.getDirectionString()));
            txtDistance.setText(stroke.getDistance()+"");
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private boolean validate(){
        boolean isValid = true;

        int distance = Integer.parseInt(txtDistance.getText().toString());

        if(txtDistance.getText().toString().isEmpty()){
            isValid = false;
            txtDistance.setError("Please enter a distance");
        }else if(distance > 500){
            isValid = false;
            txtDistance.setError("Bullshit, you did not");
        }

        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if(stroke.getId() > 0){
                Log.d(TAG, "Update");
                try {
                    da.updateStroke(stroke);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }else{
                Log.d(TAG, "Insert");
                try {
                    da.insertStroke(stroke);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
            return true;
        }
        return false;
    }

    private void getDataFromUI(){
        Club club = clubDa.getAllClubs().get(0);
        for(Club c : clubDa.getAllClubs()){
            if(c.getName().equals(clubSpinner.getSelectedItem().toString())){
                Log.d(TAG, "got in");
                club = c;
            }
            Log.d(TAG, clubSpinner.getSelectedItem().toString() + " " + c.getName());
        }

        int distance = Integer.parseInt(txtDistance.getText().toString());

        String direction = directionSpinner.getSelectedItem().toString();

        if(stroke != null){
            stroke.setDistance(distance);
            stroke.setDirection(direction);
            stroke.setClub(club);
        }else{
            stroke = new Stroke(new Date(), club, distance, direction);
        }

    }

    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("WHAT ARE YOU DOING!?");
        alert.setMessage("Are you sure you want ot delete this record?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //actual deletion
                da.deleteStroke(stroke);
                startActivity( new Intent(StrokeDetailsActivity.this, StrokeListActivity.class));
                //
            }
        });
        alert.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }



}