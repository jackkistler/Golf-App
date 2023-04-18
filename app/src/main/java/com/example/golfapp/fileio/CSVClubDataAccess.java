package com.example.golfapp.fileio;

import android.content.Context;
import android.util.Log;

import com.example.golfapp.models.Club;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVClubDataAccess {

    public static final String TAG = "CSVClubDataAccess";
    public static final String DATA_FILE = "clubs.csv";

    private ArrayList<Club> allClubs;
    private Context context;
    CSVStrokeDataAccess strokeDa;

    public CSVClubDataAccess(Context c){
        this.context = c;
        this.allClubs = new ArrayList();
//        strokeDa = new CSVStrokeDataAccess(c);

        loadClubs();
    }

    public ArrayList<Club> getAllClubs(){
        loadClubs();
        return allClubs;
    }

    public Club getClubById(long id){
        loadClubs();
        for(Club c : allClubs){
            if(c.getId() == id){
                return c;
            }
        }

        return null;
    }

    public Club insertClub(Club c) throws Exception{
        if(c.isValid()){
            long newId = getMaxId() + 1;
            c.setId(newId);
            allClubs.add(c);
            saveClubs();
        }else{
            throw new Exception("Uh oh! That is an Invalid Club");
        }

        return c;
    }

    public Club updateClub(Club c) throws Exception {
        if(c.isValid()){
            Club clubToUpdate = getClubById(c.getId());
            clubToUpdate.setName(c.getName());
            clubToUpdate.setActive(c.isActive());
            clubToUpdate.setDate(c.getDate());
            saveClubs();

        }else{
            throw new Exception("Bummer! That is an Invalid Club");
        }

        return c;
    }

    public int deleteClub(Club c){
        Club clubToRemove = getClubById(c.getId());
        if(clubToRemove != null){

            allClubs.remove(allClubs.indexOf(clubToRemove));




            saveClubs();
            return 1;
        }else{
            return 0;
        }
    }

    private String convertClubToCSV(Club c){
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

        String retVal = c.getId() + ","
                + c.getName() + ","
                + sdf.format(c.getDate()) + ","
                + c.isActive();
        return retVal;
    }

    private Club convertCSVToClub(String csv){
        String[] data = csv.split(",");
        long id = Long.parseLong(data[0]);
        String name = data[1];
        Date date = new Date(data[2]);
        boolean active = Boolean.parseBoolean(data[3]);

        return new Club(id, name, date, active);
    }

    private void loadClubs(){
        ArrayList<Club> dataList = new ArrayList();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null || dataString.isEmpty()){
            Log.d(TAG, "NO DATA FILE");
//            //test clubs
//            dataList.add(new Club(1, "My Cool Driver", new Date(123,8,22)));
//            dataList.add(new Club(2, "Shitty Wedge", new Date(123, 4,23)));
//            this.allClubs = dataList;
//            //end test club loading

            return;
        }

        String[] lines = dataString.trim().split("\n");

        for(String line : lines){
            Club c = convertCSVToClub(line);
            if(c != null){
                dataList.add(c);
            }
        }




        this.allClubs = dataList;
    }

    private void saveClubs(){
        String dataString = "";
        StringBuilder sb = new StringBuilder();

        for(Club c : allClubs){
            String csv = convertClubToCSV(c);
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
        for(Club c : allClubs){
            long cId = c.getId();
            maxId = cId > maxId ? cId : maxId;
        }
        return maxId;
    }

    public void resetClubs(){
        for(Club c : allClubs){
            deleteClub(c);
        }
    }


}
