package xyz.stepsecret.bustrackingservice.Waypiont;

import android.location.Location;
import android.util.Log;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.bustrackingservice.API.Flow_API;
import xyz.stepsecret.bustrackingservice.Map;
import xyz.stepsecret.bustrackingservice.Model.Flow_Model;

/**
 * Created by Mylove on 25/12/2558.
 */
public class WaypiontData {

    // Size = 533 OR 0 - 532

    public static Double[] latitude;

    public static Double[] longitude;

    public static Double[] distance;

    public static int[] State;

    public static void GetWaypiont()
    {


        final Flow_API flow_API = Map.restAdapter.create(Flow_API.class);
        flow_API.Get_Flow_API(
                Map.tinydb.getString("flow"),
                new Callback<Flow_Model>() {
                    @Override
                    public void success(Flow_Model flow_Model, Response response) {

                        latitude = flow_Model.getLatitude();
                        longitude = flow_Model.getLongitude();
                        distance = flow_Model.getDistance();
                        State = flow_Model.getState();

                    }

                    @Override
                    public void failure(RetrofitError error) {


                        GetWaypiont();

                    }
                });
    }

    public static int Find_Coordinate_Nearest_Neighbors_Flow(Location location)
    {
        Double Distance = 99999.0;
        Double Cal_Distance = 0.0;
        int Temp = 0;

        for(int i = 0 ; i < WaypiontData.distance.length ;i++)
        {
            if(Math.abs(Map.State - WaypiontData.State[i]) == 1 || Math.abs(Map.State - State[i]) == 0)
            {
                Log.e("TAG", "State : " + State + " ; " + Math.abs(Map.State - State[i]));

                Cal_Distance = calculate_Distance_Nearest_Neighbors(latitude[i], longitude[i],location.getLatitude(),location.getLongitude());

                if(Distance > Cal_Distance )
                {

                    Distance = Cal_Distance;
                    Temp = i;
                }
            }

        }



        return Temp;
    }


    public static Double calculate_Distance_Nearest_Neighbors(double userLat, double userLng,double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2.0) * Math.sin(latDistance / 2.0)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2.0) * Math.sin(lngDistance / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return 6371000.0 * c;
    }

}
