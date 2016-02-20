package xyz.stepsecret.bustrackingservice.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Assanee on 8/7/2558.
 */
public class Map_Model {

    @SerializedName("error")
    private String error ;

    @SerializedName("message")
    private String message ;

    @SerializedName("saves_id")
    private String saves_id ;

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getSaves_id() {
        return saves_id;
    }

}
