package xyz.stepsecret.bustrackingservice.API;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import xyz.stepsecret.bustrackingservice.Model.SignUP_Model;

/**
 * Created by Assanee on 8/7/2558.
 */
public interface SignUP_API {

    @FormUrlEncoded
    @POST("/task_manager/v1/register")
    public void SignUP(@Field("name") String name, @Field("email") String email, @Field("password") String password,@Field("class") String classs, Callback<SignUP_Model> response);

}
