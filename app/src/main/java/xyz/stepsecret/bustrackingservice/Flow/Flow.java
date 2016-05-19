package xyz.stepsecret.bustrackingservice.Flow;

import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import xyz.stepsecret.bustrackingservice.Map;
import xyz.stepsecret.bustrackingservice.Save.Save;
import xyz.stepsecret.bustrackingservice.Waypiont.WaypiontData;

/**
 * Created by XEUSLAB on 30/1/2559.
 */
public class Flow {

    public static Double first_lat;
    public static Double first_log;
    public static Double last_lat;
    public static Double last_log;

    public static void flow_AUTO(Location location)
    {

        int i = WaypiontData.Find_Coordinate_Nearest_Neighbors_Flow(location);

        if(WaypiontData.distance[i] > 50.0)
        {
            Map.State = WaypiontData.State[i];


            location.setLatitude(WaypiontData.latitude[i]);
            location.setLongitude(WaypiontData.longitude[i]);

            Map.mLastLocation = location;

            Map.DISTANCE = WaypiontData.distance[i];

            Map.TVDistance.setText(WaypiontData.distance[i].toString()+" M.");

            if(Map.check_first == false)
            {
                first_lat = location.getLatitude();
                first_log = location.getLongitude();


                // Assign the new location
                // mLastLocation = location;


                Map.camera = new LatLng(location.getLatitude(),location.getLongitude());
                Map.Main_Map.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.camera, 15));

                //draw circle
                if(Map.circle != null)
                {
                    Map.circle.remove();
                    Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(25)
                            .strokeColor(Color.parseColor("#500084d3"))
                            .fillColor(Color.parseColor("#500084d3")));
                }
                else
                {
                    Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(25)
                            .strokeColor(Color.parseColor("#500084d3"))
                            .fillColor(Color.parseColor("#500084d3")));

                }

                Map.check_first = true;

                Save.SaveNew();
            }
            else
            {

                last_lat = first_lat;
                last_log = first_log;
                first_lat = location.getLatitude();
                first_log = location.getLongitude();
                // Assign the new location
                //mLastLocation = location;


                Map.camera = new LatLng(location.getLatitude(),location.getLongitude());
                Map.Main_Map.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.camera, 15));

                //draw circle
                if(Map.circle != null)
                {
                    Map.circle.remove();
                    Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(25)
                            .strokeColor(Color.parseColor("#500084d3"))
                            .fillColor(Color.parseColor("#500084d3")));
                }
                else
                {
                    Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(25)
                            .strokeColor(Color.parseColor("#500084d3"))
                            .fillColor(Color.parseColor("#500084d3")));

                }

                Map.check_first = true;



                Save.SaveNew();
            }

            Map.Check_AUTO_START  = true;
        }
        else
        {
            if(Map.Check_AUTO_START)
            {
                Save.SaveStop();
            }
        }

    }

    public static void flow_manual(Location location)
    {
        int i = WaypiontData.Find_Coordinate_Nearest_Neighbors_Flow(location);

        Map.State = WaypiontData.State[i];


        location.setLatitude(WaypiontData.latitude[i]);
        location.setLongitude(WaypiontData.longitude[i]);

        Map.mLastLocation = location;

        Map.DISTANCE = WaypiontData.distance[i];

        Map.TVDistance.setText(WaypiontData.distance[i].toString()+" M.");

        if(Map.check_first == false)
        {
            first_lat = location.getLatitude();
            first_log = location.getLongitude();


            // Assign the new location
            // mLastLocation = location;



            Map.camera = new LatLng(location.getLatitude(),location.getLongitude());
            Map.Main_Map.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.camera, 15));

            //draw circle
            if(Map.circle != null)
            {
                Map.circle.remove();
                Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(25)
                        .strokeColor(Color.parseColor("#500084d3"))
                        .fillColor(Color.parseColor("#500084d3")));
            }
            else
            {
                Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(25)
                        .strokeColor(Color.parseColor("#500084d3"))
                        .fillColor(Color.parseColor("#500084d3")));

            }

            Map.check_first = true;

            Save.SaveNew();
        }
        else
        {

            last_lat = first_lat;
            last_log = first_log;
            first_lat = location.getLatitude();
            first_log = location.getLongitude();
            // Assign the new location
            //mLastLocation = location;


            Map.camera = new LatLng(location.getLatitude(),location.getLongitude());
            Map.Main_Map.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.camera, 15));

            //draw circle
            if(Map.circle != null)
            {
                Map.circle.remove();
                Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(25)
                        .strokeColor(Color.parseColor("#500084d3"))
                        .fillColor(Color.parseColor("#500084d3")));
            }
            else
            {
                Map.circle =  Map.Main_Map.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(25)
                        .strokeColor(Color.parseColor("#500084d3"))
                        .fillColor(Color.parseColor("#500084d3")));

            }

            Map.check_first = true;


            Save.SaveNew();
        }
    }


}
