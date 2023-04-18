package com.example.golfapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
    Button btnClubSummary;



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
        btnClubSummary = findViewById(R.id.btnClubSummary);

        chkActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkActive.isChecked()){
                    CompoundButtonCompat.setButtonTintList(chkActive, ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                    btnRecordStroke.setBackground(getDrawable(R.drawable.layout_bg));
                    btnRecordStroke.setVisibility(View.VISIBLE);
                }else{
                    CompoundButtonCompat.setButtonTintList(chkActive, ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    btnRecordStroke.setBackground(getDrawable(R.drawable.locked_button));
                }
            }
        });

        btnClubSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClubDetailsActivity.this, StrokeSummaryActivity.class);
                i.putExtra(StrokeDetailsActivity.EXTRA_CLUB_ID, club.getId());
                startActivity(i);
            }
        });


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
                    Toast.makeText(ClubDetailsActivity.this, R.string.unable_to_save_club, Toast.LENGTH_LONG).show();
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
                if(chkActive.isChecked()){
                    Intent i = new Intent(ClubDetailsActivity.this, StrokeDetailsActivity.class);
                    i.putExtra(StrokeDetailsActivity.EXTRA_CLUB_ID, club.getId());

                    startActivity(i);
                }else{
                    Toast.makeText(ClubDetailsActivity.this, getString(R.string.invalid_button), Toast.LENGTH_LONG).show();
                }

            }
        });

        da = new CSVClubDataAccess(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_CLUB_ID, 0);

        if(id > 0){
            club = da.getClubById(id);

            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);

            if(chkActive.isChecked()){
                btnRecordStroke.setVisibility(View.VISIBLE);
            }

            for(Stroke s : strokeDa.getAllStrokes()){
                if(s.getClub().getId() == club.getId()){
                    clubStrokes.add(s);
                }
            }

            if(!clubStrokes.isEmpty()){
                lsClubStrokes.setVisibility(View.VISIBLE);
                btnClubSummary.setVisibility(View.VISIBLE);
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
//
                Log.d(TAG, position+"");
                if(position % 2==0){
                    listItemView.setBackgroundColor(getResources().getColor(R.color.light_green));
                }else{
                    listItemView.setBackgroundColor(getResources().getColor(R.color.alternate_green));

                }


                Stroke currentStroke = clubStrokes.get(position);
                lblStrokeId.setText("" + currentStroke.getId());
                lblDate.setText(sdf.format(currentStroke.getDate()));
                lblDistance.setText(currentStroke.getDistance() + "");
                lblClub.setText(currentStroke.getClub().getName());

                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d(TAG, "Display Details for " + currentTask.getId());
                        Intent i = new Intent(ClubDetailsActivity.this, StrokeDetailsActivity.class);
                        i.putExtra(StrokeDetailsActivity.EXTRA_STROKE_ID, currentStroke.getId());
                        startActivity(i);
                    }
                });

                return listItemView;
            }
        };

        lsClubStrokes.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ball_icon);

    }

    private void putDataIntoUI(){
        if(club != null){
            txtClubName.setText(club.getName());
            txtDateCreated.setText(sdf.format(club.getDate()));
            chkActive.setChecked(club.isActive());
            if(!chkActive.isChecked()){
                CompoundButtonCompat.setButtonTintList(chkActive, ColorStateList.valueOf(getResources().getColor(R.color.red)));
            }
        }
    }

    private boolean validate(){
        boolean isValid = true;

        if(txtClubName.getText().toString().isEmpty()){
            isValid = false;
            txtClubName.setError(getString(R.string.error_club_name_empty));
        }else if(txtClubName.getText().length() > 20){
            isValid = false;
            txtClubName.setError(getString(R.string.error_club_name_long));
        }



        Date dateCreated = null;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        String dateStr = txtDateCreated.getText().toString();

        if(dateStr.isEmpty()){
            isValid = false;
            txtDateCreated.setError(getString(R.string.error_date_empty));
        }else{
            try {
                dateCreated = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateCreated.after(new Date())){
                isValid = false;
                txtDateCreated.setError(getString(R.string.error_date_future));
            }
        }

        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if(club.getId() > 0){
//                Log.d(TAG, "Update");
                try {
                    da.updateClub(club);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }else{
//                Log.d(TAG, "Insert");
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
        alert.setTitle(getString(R.string.delete_club));
        alert.setMessage(getString(R.string.delete_club_text));
        alert.setPositiveButton(getString(R.string.delete_club_only), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //actual deletion
                strokeDa.removeClub(club.getId());
                da.deleteClub(club);
                startActivity( new Intent(ClubDetailsActivity.this, ClubListActivity.class));
                //
            }
        });
        alert.setNegativeButton(getString(R.string.delete_club_and_strokes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete all strokes for club
                strokeDa.deleteStrokesForClub(club.getId());
                da.deleteClub(club);
                startActivity( new Intent(ClubDetailsActivity.this, ClubListActivity.class));
            }
        });
        alert.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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