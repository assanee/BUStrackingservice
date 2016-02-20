package xyz.stepsecret.bustrackingservice.API;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import xyz.stepsecret.bustrackingservice.MainActivity;
import xyz.stepsecret.bustrackingservice.Model.Map_Model;

/**
 * Created by Assanee on 8/7/2558.
 */

    public interface Map_API {

        @FormUrlEncoded
        @POST("/task_manager/v1/saves")
        public void Save(@Header("Authorization") String API,
                         @Field("latitude") String latitude,
                         @Field("longitude") String longitude,
                         @Field("rounds") String rounds,
                         @Field("speed") String speed,
                         @Field("distance") String distance,
                         @Field("EV_name") String EV_name,
                         @Field("flow") String flow,
                         @Field("status") String status,
                         Callback<Map_Model> response);
    }


