package xyz.stepsecret.bustrackingservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.bustrackingservice.API.FlowName_API;
import xyz.stepsecret.bustrackingservice.API.Flow_API;
import xyz.stepsecret.bustrackingservice.Model.FlowName_Model;
import xyz.stepsecret.bustrackingservice.Model.Flow_Model;
import xyz.stepsecret.bustrackingservice.TinyDB.TinyDB;
import xyz.stepsecret.bustrackingservice.Waypiont.WaypiontData;

/**
 * Created by Mylove on 26/12/2558.
 */
public class Setting extends FragmentActivity {


    private ToggleButton SwitchAUTO;
    public static TinyDB tinydb;
    private TextView TV;
    private TextView AUTO_TV;

    private Spinner Spin_flow;

    private String API = "https://stepsecret.xyz";

    private RestAdapter restAdapter;

    private List<String> arrList = new ArrayList<String>();

    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        TV = (TextView) findViewById(R.id.textView2);
        AUTO_TV = (TextView) findViewById(R.id.textView3);


        tinydb = new TinyDB(getApplicationContext());

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).build();


        Spin_flow = (Spinner) findViewById(R.id.spinner);

        GetFlowName();

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);




        Spin_flow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub

                if(i != 0)
                {

                    TV.setText("Your Selected : " + arrList.get(i));

                    tinydb.putString("flow", arrList.get(i));

                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                   // pDialog.show();

                    WaypiontData.GetWaypiont();

                }


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(Setting.this,
                        String.valueOf("Your Selected Empty"),
                        Toast.LENGTH_SHORT).show();
            }

        });


        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.tinydb.clear();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        TV.setText("Your Selected : " + tinydb.getString("flow"));

        SwitchAUTO = (ToggleButton)findViewById(R.id.SwitchAUTO);

        if(tinydb.getBoolean("AUTO", false))
        {
            SwitchAUTO.setChecked(true);
            AUTO_TV.setText("AUTO");
        }
        else
        {
            SwitchAUTO.setChecked(false);
            AUTO_TV.setText("NOT AUTO");
        }

        SwitchAUTO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {


                    AUTO_TV.setText("AUTO");

                    tinydb.putBoolean("AUTO",true);


                } else {


                    AUTO_TV.setText("NOT AUTO");

                    tinydb.putBoolean("AUTO",false);


                }
            }
        });

    }

    public void GetFlowName()
    {
        final FlowName_API flow_API = restAdapter.create(FlowName_API.class);
        flow_API.Get_FlowName_API(
                new Callback<FlowName_Model>() {
                    @Override
                    public void success(FlowName_Model flowname_Model, Response response) {

                        arrList.clear();

                        arrList.add("SELECT FLOW");
                        String[] Temp = flowname_Model.GetFlowName();
                        for (int i = 0; i < Temp.length; i++) {
                            arrList.add(Temp[i]);
                        }


                        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(Setting.this,
                                android.R.layout.simple_spinner_item,
                                arrList);

                        Spin_flow.setAdapter(arrAd);


                    }

                    @Override
                    public void failure(RetrofitError error) {


                        GetFlowName();

                    }
                });
    }


}
