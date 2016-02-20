package xyz.stepsecret.bustrackingservice.Date;

import android.text.format.Time;

import xyz.stepsecret.bustrackingservice.Map;
import xyz.stepsecret.bustrackingservice.TinyDB.TinyDB;

/**
 * Created by XEUSLAB on 30/1/2559.
 */
public class Date {


    public static void GetNowDate()
    {
        Time today;

        today = new Time(Time.getCurrentTimezone());

        today.setToNow();

        String DataNow = ""+today.monthDay+today.month+today.year;

        String DateSave = Map.tinydb.getString("Date");

        if(DataNow.equals(DateSave))
        {

        }
        else
        {
            Map.tinydb.putString("Date", ""+today.monthDay+today.month+today.year);
            Map.tinydb.putInt("round", 0);
        }



    }

}
