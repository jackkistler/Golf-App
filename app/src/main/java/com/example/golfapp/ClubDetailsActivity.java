package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.fileio.CSVClubDataAccess;
import com.example.golfapp.fileio.CSVStrokeDataAccess;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ClubDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ClubDetailsActivity";
    public static final String EXTRA_CLUB_ID = "clubId";
    CSVClubDataAccess da;
    Club club;
    CSVStrokeDataAccess strokeDa;
    ArrayList<Stroke> clubStrokes;

    EditText txtClubName;
    EditText txtDateCreated;
    CheckBox chkActive;
    Button btnSave;
    Button btnDelete;
    Button btnRecordStroke;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
    ListView lsClubStrokes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        txtClubName = findViewById(R.id.txtClubName);
        txtDateCreated = findViewById(R.id.txtDateCreated);
        chkActive = findViewById(R.id.chkActive);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        strokeDa = new CSVStrokeDataAccess(this);
        lsClubStrokes = findViewById(R.id.lsClubStrokes);
        clubStrokes = new ArrayList();
        btnRecordStroke = findViewById(R.id.btnRecordStroke);


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

        btnRecordStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to stroke details, pushing id of club
                Intent i = new Intent(ClubDetailsActivity.this, StrokeDetailsActivity.class);
                i.putExtra(StrokeDetailsActivity.EXTRA_CLUB_ID, club.getId());
                startActivity(i);
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
            lsClubStrokes.setVisibility(View.VISIBLE);
            btnRecordStroke.setVisibility(View.VISIBLE);

            for(Stroke s : strokeDa.getAllStrokes()){
                if(s.getClub().getId() == club.getId()){
                    clubStrokes.add(s);
                }
            }
        }



        ArrayAdapter<Stroke> adapter = new ArrayAdapter(this, R.layout.custom_stroke_list_item, R.id.lblStrokeId, clubStrokes){

            boolean recolor = false;
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){
                View listItemView = super.getView(position, convertView, parentListView);
                TextView lblStrokeId = listItemView.findViewById(R.id.lblStrokeId);
                TextView lblDate = listItemView.findViewById(R.id.lblDate);
                TextView lblClub = listItemView.findViewById(R.id.lblClub);
                TextView lblDistance = listItemView.findViewById(R.id.lblDistance);
//                if(recolor){
//                    listItemView.setBackgroundColor(getResources().getColor(R.color.dark_green));
//                    recolor = false;
//                }else{
//                    recolor = true;
//                }

                if(position % 2==0){
                    listItemView.setBackgroundColor(getResources().getColor(R.color.light_green));
                }

                Stroke currentStroke = clubStrokes.get(position);
                lblStrokeId.setText("" + currentStroke.getId());
                lblDate.setText(sdf.format(currentStroke.getDate()));
                lblDistance.setText(currentStroke.getDistance() + "");
                lblClub.setText(currentStroke.getClub().getName());

                //add onclick listeners if needed
//                listItemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Log.d(TAG, "Display Details for " + currentTask.getId());
//                        Intent i = new Intent(StrokeListActivity.this, StrokeDetailsActivity.class);
//                        i.putExtra(StrokeDetailsActivity.EXTRA_STROKE_ID, currentStroke.getId());
//                        startActivity(i);
//                    }
//                });

                return listItemView;
            }
        };

        lsClubStrokes.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);

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
        }else if(txtClubName.getText().length() > 20){
            isValid = false;
            txtClubName.setError("Club name too long");
        }



        Date dateCreated = null;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        String dateStr = txtDateCreated.getText().toString();

        if(dateStr.isEmpty()){
            isValid = false;
            txtDateCreated.setError("Please enter a Date");
        }else{
            try {
                dateCreated = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateCreated.after(new Date())){
                isValid = false;
                txtDateCreated.setError("Date cannot be in the future");
            }
        }






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
                strokeDa.removeClub(club.getId());
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

        dp.getDatePicker().setMaxDate(System.currentTimeMillis());

        dp.show();
    }




}