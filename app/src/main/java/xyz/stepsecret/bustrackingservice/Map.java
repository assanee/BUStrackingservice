package xyz.stepsecret.bustrackingservice;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.RestAdapter;
import xyz.stepsecret.bustrackingservice.Date.Date;
import xyz.stepsecret.bustrackingservice.Flow.Flow;
import xyz.stepsecret.bustrackingservice.Save.Save;
import xyz.stepsecret.bustrackingservice.Waypiont.WaypiontData;
import xyz.stepsecret.bustrackingservice.TinyDB.TinyDB;
import android.Manifest;


/**
 * Created by Assanee on 8/7/2558.
 */
public class Map extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener {

    public static TinyDB tinydb;


    private static final String TAG = "Log : ";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public static Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 1000; // 3 sec
    private static int FATEST_INTERVAL = 1000; // 3 sec
    private static int DISPLACEMENT = 5; // 10 meters

    // UI elements

    public static GoogleMap Main_Map;

    public static LatLng camera;

    public static Polyline line;


    public static Boolean check_first = false;

    public static Double DISTANCE = 0.0;

    public static Circle circle;


    public static String API = "https://stepsecret.xyz";

    public static RestAdapter restAdapter;

    public static Boolean Check_press_start = false;
    public static Boolean Check_AUTO_START = false;

    public static int State = 0;

    private ToggleButton Start_Stop;
    private Button btn_Setting1;

    public static TextView TVEV, TVflow, TVState, TVLatitude, TVLongitude, TVSpeed, TVStatus, TVMode, TVRound;

    public SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        Main_Map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Main_Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Main_Map.setOnMapLongClickListener(this);

        tinydb = new TinyDB(getApplicationContext());

        TVEV = (TextView) findViewById(R.id.EV);
        TVflow = (TextView) findViewById(R.id.flow);
        TVState = (TextView) findViewById(R.id.State);
        TVLatitude = (TextView) findViewById(R.id.Latitude);
        TVLongitude = (TextView) findViewById(R.id.Longitude);
        TVSpeed = (TextView) findViewById(R.id.Speed);
        TVStatus = (TextView) findViewById(R.id.Status);
        TVMode = (TextView) findViewById(R.id.Mode);
        TVRound = (TextView) findViewById(R.id.Round);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


//        btnStartLocationUpdates = (Button) findViewById(R.id.button3);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).build();

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        //btnStartLocationUpdates = (CustomView) findViewById(R.id.button3);


        findViewById(R.id.btn_Setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), Setting.class);
                startActivity(i);

            }
        });

        btn_Setting1 = (Button) findViewById(R.id.btn_Setting);

        Start_Stop = (ToggleButton) findViewById(R.id.toggleButton1);
        Start_Stop.setChecked(true);
        Start_Stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (!arg1) {
                    Check_press_start = true;

                    btn_Setting1.setEnabled(false);

                    State = 0;

                    TVState.setText("State : " + State);

                    int round = tinydb.getInt("round", 0) + 1;
                    tinydb.putInt("round", round);


                    togglePeriodicLocationUpdates();

                    Toast.makeText(getApplicationContext(), "Start!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Check_press_start = false;

                    btn_Setting1.setEnabled(true);
                    stopLocationUpdates();
                    Save.SaveStop();
                    Toast.makeText(getApplicationContext(), "Stop!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Date.GetNowDate();
        WaypiontData.GetWaypiont();
        pDialog.cancel();
    }


    public void AUTO() {
        TVMode.setText("Mode : AUTO");

        Toast.makeText(getApplicationContext(), "AUTO",
                Toast.LENGTH_SHORT).show();
        Start_Stop.setEnabled(false);

        CountDownTimer cdt = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Tick
            }

            public void onFinish() {

                State = 0;

                int round = tinydb.getInt("round", 0) + 1;
                tinydb.putInt("round", round);


                togglePeriodicLocationUpdates();

                Toast.makeText(getApplicationContext(), "Start!",
                        Toast.LENGTH_SHORT).show();
            }
        }.start();

    }


    @Override
    protected void onStart() {
        super.onStart();

        turnGPSOn();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        if (tinydb.getBoolean("AUTO", false)) {
            AUTO();
        } else {
            Start_Stop.setEnabled(true);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {

            if (Check_press_start) {
                startLocationUpdates();
                Toast.makeText(getApplicationContext(), "resume 1",
                        Toast.LENGTH_SHORT).show();

            } else if (tinydb.getBoolean("AUTO", false)) {
                Toast.makeText(getApplicationContext(), "resume 2",
                        Toast.LENGTH_SHORT).show();
                AUTO();
            } else {
                Toast.makeText(getApplicationContext(), "resume 3",
                        Toast.LENGTH_SHORT).show();
                Start_Stop.setEnabled(true);

            }


        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Toast.makeText(getApplicationContext(), "onStop!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        Toast.makeText(getApplicationContext(), "onPause!",
                Toast.LENGTH_SHORT).show();

    }


    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            //btnStartLocationUpdates.setText("STOP");

            mRequestingLocationUpdates = true;

            // Starting the location updates
            if (Check_press_start) {
                startLocationUpdates();
            } else if (tinydb.getBoolean("AUTO", false)) {
                startLocationUpdates();
            }

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
            // btnStartLocationUpdates.setText("START");

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        if (mRequestingLocationUpdates) {
            if(Check_press_start)
            {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(tinydb.getBoolean("AUTO", false))
        {
            TVMode.setText("Mode : AUTO");

                Flow.flow_AUTO(location);
        }
        else
        {
            TVMode.setText("Mode : Manual");

                Flow.flow_manual(location);

        }



    }







    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


    @Override
    public void onMapLongClick(LatLng point) {

        Toast.makeText(getApplicationContext(), "Long Click...",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                stopLocationUpdates();
                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

}

