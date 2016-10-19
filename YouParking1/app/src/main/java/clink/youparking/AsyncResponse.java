package clink.youparking;

import org.json.JSONException;

/**
 * Created by acous on 8/30/2016.
 */
public interface AsyncResponse {

    void processFinish(String output) throws JSONException;

}