package xyz.stepsecret.bustrackingservice.API;

import retrofit.Callback;
import retrofit.http.GET;
import xyz.stepsecret.bustrackingservice.Model.FlowName_Model;

/**
 * Created by XEUSLAB on 27/1/2559.
 */
public interface FlowName_API {
    @GET("/task_manager/v1/Get_flow")
    public void Get_FlowName_API(Callback<FlowName_Model> response);
}
