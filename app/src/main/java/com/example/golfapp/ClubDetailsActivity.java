package com.example.golfapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.models.Club;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kotlinx.coroutines.scheduling.Task;

public class ClubDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ClubDetailsActivity";
    public static final String EXTRA_CLUB_ID = "clubId";
    CSVClubDataAccess da;
    Club club;

    EditText txtClubName;
    EditText txtDateCreated;
    CheckBox chkActive;
    Button btnSave;
    Button btnDelete;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        txtClubName = findViewById(R.id.txtClubName);
        txtDateCreated = findViewById(R.id.txtDateCreated);
        chkActive = findViewById(R.id.chkActive);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        txtDateCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save()){
                    Intent i = new Intent(ClubDetailsActivity.this, ClubListActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(ClubDetailsActivity.this, "Unable to save club", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDelete.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        da = new CSVClubDataAccess(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_CLUB_ID, 0);

        if(id > 0){
            club = da.getClubById(id);
            Log.d(TAG, club.toString());

            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);
        }

    }

    private void putDataIntoUI(){
        if(club != null){
            txtClubName.setText(club.getName());
            txtDateCreated.setText(sdf.format(club.getDate()));
            chkActive.setChecked(club.isActive());
        }
    }

    private boolean validate(){
        boolean isValid = true;

        if(txtClubName.getText().toString().isEmpty()){
            isValid = false;
            txtClubName.setError("You must enter a club name");
        }

        Date dateCreated = null;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        if(txtDateCreated.getText().toString().isEmpty()){
            isValid = false;
        }

        //TODO: FINISH VALIDATION

        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if(club.getId() > 0){
                Log.d(TAG, "Update");
                try {
                    da.updateClub(club);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }else{
                Log.d(TAG, "Insert");
                try {
                    da.insertClub(club);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            return true;
        }
        return false;
    }

    private void getDataFromUI(){
        String name = txtClubName.getText().toString();
        boolean active = chkActive.isChecked();

        String dateStr = txtDateCreated.getText().toString();
        Date date = null;
        try{
            date = sdf.parse(dateStr);
        } catch(ParseException e){
            Log.d(TAG, "Unable to parse date from string");
        }

        if(club != null){
            club.setName(name);
            club.setActive(active);
            club.setDate(date);
        }else{
            club = new Club(name, date, active);
        }
    }

    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("WHAT ARE YOU DOING!?");
        alert.setMessage("Are you sure you want ot delete this club?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //actual deletion
                da.deleteClub(club);
                startActivity( new Intent(ClubDetailsActivity.this, ClubListActivity.class));
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



    private void showDatePicker(){

        Date today = new Date();
        int year = today.getYear() + 1900;
        int month = today.getMonth();
        int day = today.getDate();

        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                String selectedDate = (m+1) + "/" + d + "/" + y;
                txtDateCreated.setText(selectedDate);
            }
        }, year, month, day);

        dp.show();
    }
}