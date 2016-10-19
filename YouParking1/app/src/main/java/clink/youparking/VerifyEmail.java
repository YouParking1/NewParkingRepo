package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

public class VerifyEmail extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
    }

    public void registerVehicle(View view)
    {
        EditText code = (EditText)findViewById(R.id.verifyCode);
        String user_code = code.getText().toString();
        String type = "verifyEmail";

        System.out.println("User code is : " + user_code);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, user_code);
    }

    @Override
    public void processFinish(String output) throws JSONException {

        if(output.contains("Continue")) {
            Intent intent = new Intent(this, VehicleRegistrationActivity.class);
            startActivity(intent);
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "Wrong verification code!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
