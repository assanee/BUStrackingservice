package xyz.stepsecret.bustrackingservice;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.bustrackingservice.API.SignIN_API;
import xyz.stepsecret.bustrackingservice.Model.SignIN_Model;
import xyz.stepsecret.bustrackingservice.TinyDB.TinyDB;

public class MainActivity extends FragmentActivity {


    private EditText edit_email;
    private EditText edit_pass;

    private String email;
    private String password;

    private String API = "https://stepsecret.xyz";

    public static TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_email = (EditText) findViewById(R.id.editText);
        edit_pass = (EditText) findViewById(R.id.editText2);

        tinydb = new TinyDB(getApplicationContext());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edit_email.getText().toString();
                password = edit_pass.getText().toString();


                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).build();
                final SignIN_API signIN_api = restAdapter.create(SignIN_API.class);

                signIN_api.SignIN(email,password, new Callback<SignIN_Model>() {
                    @Override
                    public void success(SignIN_Model signIN_Model, Response response) {

                        String Check_error = signIN_Model.getError();

                        if(Check_error.equals("false"))
                        {
                            tinydb.putBoolean("login",true);
                            tinydb.putString("userName", signIN_Model.getName());
                            tinydb.putString("email", signIN_Model.getEmail());
                            tinydb.putString("ApiKey", signIN_Model.getApiKey());
                            tinydb.putString("flow", "RED");
                            tinydb.putBoolean("AUTO", false);

                            Toast.makeText(getApplicationContext(), "Sign IN Success.",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), Map.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Sign IN NOT Success.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Sign IN NOT Success.",
                                Toast.LENGTH_LONG).show();

                    }
                });


            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SignUP.class);
                startActivity(i);
                finish();
            }
        });


        check_login();
    }





    public void check_login()
    {
        Boolean login = tinydb.getBoolean("login", false);

        if(login==true)
        {
            Intent i = new Intent(getApplicationContext(), Map.class);
            startActivity(i);
            finish();
        }
    }
}
