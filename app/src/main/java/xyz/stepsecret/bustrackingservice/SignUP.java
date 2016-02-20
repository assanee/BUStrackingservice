package xyz.stepsecret.bustrackingservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.bustrackingservice.API.SignUP_API;
import xyz.stepsecret.bustrackingservice.Model.SignUP_Model;

/**
 * Created by Assanee on 8/7/2558.
 */
public class SignUP extends FragmentActivity {

    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_pass;

    private String name;
    private String email;
    private String password;

    private String API = "https://stepsecret.xyz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        edit_name = (EditText) findViewById(R.id.editname);
        edit_email = (EditText) findViewById(R.id.editmail);
        edit_pass = (EditText) findViewById(R.id.editpass);


        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edit_name.getText().toString();
                email = edit_email.getText().toString();
                password = edit_pass.getText().toString();


                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).build();
                final SignUP_API signUP_API = restAdapter.create(SignUP_API.class);

                signUP_API.SignUP(name, email, password,"ev", new Callback<SignUP_Model>() {
                    @Override
                    public void success(SignUP_Model signUP_Model, Response response) {

                        String Check_error = signUP_Model.getError();

                        if (Check_error.equals("false")) {

                            Toast.makeText(getApplicationContext(), "Sign UP Success.",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign UP NOT Success.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Sign UP NOT Success.",
                                Toast.LENGTH_LONG).show();

                    }
                });


            }
        });



    }





}
