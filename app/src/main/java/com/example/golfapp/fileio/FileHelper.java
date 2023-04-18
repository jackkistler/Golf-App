package com.example.golfapp.fileio;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This class handles File IO for Android apps
 *
 * IO = Input/Output
 * Input = when your program reads data form a file stored on the android device
 * Output = when your program writes data from your app, to a file on the android device
 *
 * In order to connect to a file on the device's hard drive:
 * You must use a Stream object.
 * Think of a stream as a connection between your app, and the file that you are working with.
 * It allows you to transfer data, just like a stream throughout history has allowed humans to transfer goods.
 * So you need a stream object in order to connect your app to a file on the device's hard drive.
 *
 * Streams are NOT JUST for connecting to files.
 * You can use streams to connect to another computer, or a peripheral device, such as a keyboard, or printer.
 * So, streams come in many variations (there are many subclasses of the Stream class in Java).
 * Most of the sub classes are designed to be WRAPPERS around other stream objects.
 *
 * What if we wanted to save the file in a BINARY format, rather than as text
 * (after all, the computer would prefer binary!)?
 *
 * We'll just stick to text for now, but it's not much of a stretch to save your data in a binary format.
 *
 * But HOW ARE WE GOING TO FORMAT THIS TEXT DATA that we wish to save...what type of text)?
 * CSV?
 * XML?
 * JSON?
 * We have some choices (.csv files are everywhere - .xml used to be the king, - but now everyone uses .json)
 *
 * But we are getting ahead of ourselves...before we start worrying the the format of the text, lets just try to work with any text
 * Then, once that's working we can worry about which format the text will be.
 */


public class FileHelper {

    public static final String TAG = "FileHelper";

    public static boolean writeToFile(String filePath, String data, Context context){

        /**
         * Saves a string of data into a file on the hard drive.
         *
         * It is a static method, so you do not need to create an instance of this class in order to call the method.
         *
         * One of the parameters that you will see in this class is a Context object.
         * In Android, the OS (operating system) likes to keep track of which app/activity is trying to access the devices hard drive.
         * Note that the Activity class is a subclass of Context, so an activity object can be used as a parameter that calls for a Context
         * (because an activity object IS-A context object.
         *
         * @param filePath      The path to the file to be created/written (RELATIVE TO WHERE?)
         * @param data          The data to be written into the file
         * @param context       The Android OS likes to know which activity is reading/writing to the devices harddrive.
         *                      (Remember, an activity object IS-A context)
         * @return              Returns true the the file write completed successfully, false otherwise
         */

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);

            outputStreamWriter.close();
            return true;
        }catch(IOException e){
            Log.e(TAG, "File Write Failed" + e.toString() );
            Log.d(TAG, "filePath: " + filePath + "\ndata:" + data);
            return false;
        }

    }

    public static String readFromFile(String filePath, Context context){

        /**
         * Opens a file on the device and reads it as a string of text.
         *
         * @param filePath      The path to the file we wish to read
         *
         * @param context       Android needs to know which activity is reading data from a file on the device
         *                      (remember that the Activity class extends the Context class
         *
         * @return              Returns the data (as a String)
         */

        String data = null;
        try{
            InputStream inputStream = context.openFileInput(filePath);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                final String LINE_BREAK = System.getProperty("line.separator");
                while((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString + LINE_BREAK);
                }

                inputStream.close();
                data = stringBuilder.toString();
                Log.d(TAG, "Here's the data: \n" + data);
                //return data;
            }
        }catch(FileNotFoundException e){
            Log.d(TAG, "File not Found: " + e.toString());
        }catch (IOException e){
            Log.d(TAG, "Can not read file: " + e.toString());
        }
        return data;
    }


}
