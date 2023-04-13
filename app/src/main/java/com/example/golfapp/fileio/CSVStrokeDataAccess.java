package com.example.golfapp.fileio;

import android.content.Context;
import android.util.Log;

import com.example.golfapp.ClubListActivity;
import com.example.golfapp.models.Club;
import com.example.golfapp.models.Stroke;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVStrokeDataAccess {

    public static final String TAG = "CSVStrokeDataAccess";
    public static final String DATA_FILE = "strokes.csv";

    private ArrayList<Stroke> allStrokes;
    private Context context;
    CSVClubDataAccess clubDa;


    public CSVStrokeDataAccess(Context c){
        this.context = c;
        this.allStrokes = new ArrayList();
        clubDa = new CSVClubDataAccess(c);


        //convertCSVToStroke("1,4/4/2023,1,100,Hook");

        loadStrokes();
    }

    public ArrayList<Stroke> getAllStrokes(){
        loadStrokes();
        return allStrokes;
    }

    public Stroke getStrokeById(long id){
        loadStrokes();

        for(Stroke s : allStrokes){
            if(s.getId() == id){
                return s;
            }
        }
        return null;
    }

    public Stroke insertStroke(Stroke s) throws Exception{
        if(s.isValid()){
            long newId = getMaxId() + 1;
            s.setId(newId);
            allStrokes.add(s);
            saveStrokes();
        }else{
            throw new Exception("Invalid Stroke");
        }

        return s;
    }

    public Stroke updateStroke(Stroke s) throws Exception {
        if(s.isValid()){
            Stroke strokeToUpdate = getStrokeById(s.getId());
            //set everything for the stroke
            strokeToUpdate.setClub(s.getClub());
            strokeToUpdate.setDate(s.getDate());
            strokeToUpdate.setDirection(s.getDirection());
            strokeToUpdate.setDistance(s.getDistance());

            saveStrokes();
        }else{
            throw new Exception("Bummer! That is an Invalid stroke");
        }

        return s;
    }


    public int deleteStroke(Stroke s) {
        Stroke strokeToRemove = getStrokeById(s.getId());
        if(strokeToRemove != null){
            allStrokes.remove(allStrokes.indexOf(strokeToRemove));
            saveStrokes();
            return 1;
        }else{
            return 0;
        }
    }

    private String convertStrokeToCSV(Stroke s){
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        String retVal = s.getId() + ","
                + sdf.format(s.getDate()) + ","
                + s.getClub().getId() + ","
                + s.getDistance() + ","
                + s.getDirection();

        return retVal;

    }

    private Stroke convertCSVToStroke(String csv){
        String[] data = csv.split(",");
        long id = Long.parseLong(data[0]);

        Date date = new Date(data[1]);
        ArrayList<String> allClubIds = new ArrayList();
        for(Club c : clubDa.getAllClubs()){
            allClubIds.add(c.getId()+"");
        }

        Club club;
        if(allClubIds.contains(data[2])){
            club = clubDa.getClubById(Long.parseLong(data[2]));
        }else{
            club = new Club(999, "Unknown", new Date(), false);
        }

        int distance = Integer.parseInt(data[3]);
        String direction = data[4];

        Log.d(TAG, new Stroke(id, date, club, distance, direction).toString());
        return new Stroke(id, date, club, distance, direction);

    }

    private void loadStrokes(){
        ArrayList<Stroke> dataList = new ArrayList();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null || dataString.isEmpty()){
            Log.d(TAG, "NO DATA FILE");
            //TEST STROKES
//            dataList.add(new Stroke(1, new Date(), new Club(1, "testclub", new Date()), 100, "Hook"));
//            dataList.add(new Stroke(2, new Date(), new Club(1, "testclub", new Date()), 125, "Hook"));
//            dataList.add(new Stroke(3, new Date(), new Club(2, "betterclub", new Date()), 100, "Pure"));
//            this.allStrokes = dataList;
            //END TEST STROKES
            return;
        }
        String[] lines = dataString.trim().split("\n");

        for(String line : lines){
            Stroke s = convertCSVToStroke(line);
            if(s != null){
                dataList.add(s);
            }
        }

        this.allStrokes = dataList;

    }

    private void saveStrokes(){
        String dataString = "";
        StringBuilder sb = new StringBuilder();

        for(Stroke s : allStrokes){
            String csv = convertStrokeToCSV(s);
            sb.append(csv);
            sb.append("\n");
        }

        dataString = sb.toString();
        if(FileHelper.writeToFile(DATA_FILE, dataString, context)){
            Log.d(TAG, "Successfully saved data!");
        }else{
            Log.d(TAG, "oh no! you failed to save data!");
        }
    }

    private long getMaxId(){
        long maxId = 0;
        for(Stroke s : allStrokes){
            long sId = s.getId();
            maxId = sId > maxId ? sId : maxId;
        }
        return maxId;
    }

    public void removeClub(long id){
        loadStrokes();
        for(Stroke s : getAllStrokes()){
            if(s.getId() == id){
                s.setClub(new Club(999, "Unknown", new Date(), false));
            }
        }
        saveStrokes();
    }

}
