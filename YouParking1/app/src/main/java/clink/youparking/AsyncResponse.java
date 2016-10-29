package clink.youparking;

import org.json.JSONException;

public interface AsyncResponse {

    void processFinish(String output) throws JSONException;

}