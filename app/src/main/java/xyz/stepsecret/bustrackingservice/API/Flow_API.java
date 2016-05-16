package xyz.stepsecret.bustrackingservice.API;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import xyz.stepsecret.bustrackingservice.Model.Flow_Model;

/**
 * Created by Mylove on 14/1/2559.
 */
public interface Flow_API {

    @GET("/task_manager/v1/Get_flow_data")
    public void Get_Flow_API(@Query("flow") String flow,Callback<Flow_Model> response);

}
