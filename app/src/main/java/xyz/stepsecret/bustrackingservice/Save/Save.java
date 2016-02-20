package xyz.stepsecret.bustrackingservice.Save;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.bustrackingservice.API.Map_API;
import xyz.stepsecret.bustrackingservice.MainActivity;
import xyz.stepsecret.bustrackingservice.Map;
import xyz.stepsecret.bustrackingservice.Model.Map_Model;

/**
 * Created by XEUSLAB on 30/1/2559.
 */
public class Save {

    public static void SaveNew()
    {

        Map.TVEV.setText("EV : "+ Map.tinydb.getString("userName"));
        Map.TVflow.setText("flow : "+Map.tinydb.getString("flow"));
        Map.TVStatus.setText("Status : STOP");
        Map.TVLatitude.setText("Latitude : "+Map.mLastLocation.getLatitude()+"");
        Map.TVLongitude.setText("Longitude : "+Map.mLastLocation.getLongitude()+"");
        Map.TVRound.setText("Round : "+Map.tinydb.getInt("round", 1));
        Map.TVSpeed.setText("Speed : "+Map.mLastLocation.getSpeed());
        Map.TVState.setText("State : "+Map.State);

        final Map_API map_API = Map.restAdapter.create(Map_API.class);


        map_API.Save(MainActivity.tinydb.getString("ApiKey"),
                Map.mLastLocation.getLatitude()+"",
                Map.mLastLocation.getLongitude()+"",
                Map.tinydb.getInt("round",1)+"",
                Map.mLastLocation.getSpeed()+"",
                Map.DISTANCE.toString(),
                Map.tinydb.getString("userName"),
                Map.tinydb.getString("flow"),
                "RUN",
                new Callback<Map_Model>() {
                    @Override
                    public void success(Map_Model map_Model, Response response) {

                        String Check_error = map_Model.getError();

                        if (Check_error.equals("false")) {


                        } else {

                            SaveNew();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        SaveNew();

                    }
                });
    }

    public static void SaveStop()
    {

        Map.TVEV.setText("EV : "+ Map.tinydb.getString("userName"));
        Map.TVflow.setText("flow : "+Map.tinydb.getString("flow"));
        Map.TVStatus.setText("Status : STOP");
        Map.TVLatitude.setText("Latitude : "+Map.mLastLocation.getLatitude()+"");
        Map.TVLongitude.setText("Longitude : "+Map.mLastLocation.getLongitude()+"");
        Map.TVRound.setText("Round : "+Map.tinydb.getInt("round", 1));
        Map.TVSpeed.setText("Speed : "+Map.mLastLocation.getSpeed());
        Map.TVState.setText("State : "+Map.State);

        final Map_API map_API = Map.restAdapter.create(Map_API.class);


        map_API.Save(MainActivity.tinydb.getString("ApiKey"),
                Map.mLastLocation.getLatitude() + "",
                Map.mLastLocation.getLongitude() + "",
                Map.tinydb.getInt("round", 1) + "",
                Map.mLastLocation.getSpeed() + "",
                Map.DISTANCE.toString(),
                Map.tinydb.getString("userName"),
                Map.tinydb.getString("flow"),
                "STOP",
                new Callback<Map_Model>() {
                    @Override
                    public void success(Map_Model map_Model, Response response) {

                        String Check_error = map_Model.getError();

                        if (Check_error.equals("false")) {

                        } else {

                            SaveStop();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {


                        SaveStop();

                    }
                });
    }
}
