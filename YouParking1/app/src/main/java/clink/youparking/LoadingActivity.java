package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity implements AsyncResponse {

    public enum Operation { LOGIN, HOLDINGSPOT, NONE }
    Operation operation = Operation.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        SharedPreferences preferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        String Username = preferences.getString("Username", "");

        System.out.println("USERNAME: " + Username);
        System.out.println(preferences.contains("Username"));

        String fName = preferences.getString("first_name", "");
        String lName = preferences.getString("last_name", "");
        String school = preferences.getString("University", "");
        String pass = preferences.getString("Password", "");
        if(Username.length() != 0)
        {
            operation = Operation.LOGIN;

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("login", Username, pass);
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void processFinish(String output) throws JSONException {

        if(output.contains("0"))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        if(operation == Operation.LOGIN)
        {
            JSONObject jsonObject = new JSONObject(output);
            String strLoginID = jsonObject.optString("Email");
            String strSchool = jsonObject.optString("University");
            String strFName = jsonObject.optString("FName");
            String strLName = jsonObject.optString("LName");
            User.points = jsonObject.optInt("Points");
            User.numCars = jsonObject.optInt("Num_of_Cars");
            User.email = strLoginID;
            User.school = strSchool;
            User.fName = strFName;
            User.lName = strLName;
            String active = jsonObject.optString("Active");

            if (active.equals("false")) {
                Toast.makeText(this, "Must verify your email. ", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, VerifyEmail.class);
                startActivity(intent);
            }
            else if(User.numCars < 1)
            {
                Toast.makeText(this, "Must register your vehicle. ", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, VehicleRegistrationActivity.class);
                startActivity(intent);
            }
            else
            {
                operation = Operation.HOLDINGSPOT;

                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.delegate = this;
                backgroundWorker.execute("holdingSpot");
            }
        }
        else if(operation == Operation.HOLDINGSPOT)
        {

            if(output.contains("nospots"))
            {
                User.isLoggedIn = true;
                User.holdingSpot = false;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                JSONObject jsonObject = new JSONObject(output);

                try
                {
                    User.myLocation = new LatLng(jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"));
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }

                User.isLoggedIn = true;
                User.holdingSpot = true;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
