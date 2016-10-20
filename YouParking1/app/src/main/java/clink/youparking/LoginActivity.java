package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    EditText emailEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.pass);

//        SharedPreferences preferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
//        String Username = preferences.getString("Username", "");
//        String fName = preferences.getString("first_name", "");
//        String lName = preferences.getString("last_name", "");
//        String school = preferences.getString("University", "");
//        String pass = preferences.getString("Password", "");
//
//
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
        TextView text = (TextView) findViewById(R.id.tvLogo);
        text.setTypeface(font);
//
//
//        if(Username.length() != 0)
//        {
//            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//            backgroundWorker.delegate = this;
//            backgroundWorker.execute("login", Username, pass);
//        }
    }

    public void goToRegistration(View view)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void OnLogin(View view) {
        String email = emailEt.getText().toString().toUpperCase();
        String password = passwordEt.getText().toString();
        String type = "login";

        if (email.toUpperCase().contains(".EDU") && email.contains("@")) {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute(type, email, password);
        }
    }

    @Override
    public void processFinish(String output) throws JSONException {

        System.out.println("OUTPUT FROM PROCESS LOGIN: " + output);

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

        SharedPreferences sharedPref = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Username", User.email);
        editor.putString("first_name", User.fName);
        editor.putString("last_name", User.lName);
        editor.putString("University", User.school);

        if (sharedPref.getString("Password", "").length() < 1) {
            editor.putString("Password", passwordEt.getText().toString());
        }

        editor.commit();

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
            User.isLoggedIn = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}