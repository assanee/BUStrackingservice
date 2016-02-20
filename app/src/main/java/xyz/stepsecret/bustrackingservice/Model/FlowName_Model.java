package xyz.stepsecret.bustrackingservice.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XEUSLAB on 27/1/2559.
 */
public class FlowName_Model {

    @SerializedName("flow")
    private String[] FlowName;

    public String[] GetFlowName() {
        return FlowName;
    }


}
