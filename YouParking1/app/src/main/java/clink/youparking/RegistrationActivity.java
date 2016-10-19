package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements AsyncResponse {

    EditText FName, LName, Email, Password, ConfirmPass;
    AutoCompleteTextView University;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        FName = (EditText) findViewById(R.id.reg_first_name);
        LName = (EditText) findViewById(R.id.reg_last_name);
        Email = (EditText) findViewById(R.id.reg_email);
        Password = (EditText) findViewById(R.id.reg_password);
        ConfirmPass = (EditText) findViewById(R.id.reg_confirm_password);
        University = (AutoCompleteTextView) findViewById(R.id.reg_university);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.universities, R.layout.dropdown_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        University.setAdapter(adapter);

    }

    public void goToEmailVerification(View view)
    {
        String firstName = FName.getText().toString();
        String lastName = LName.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmpassword = ConfirmPass.getText().toString();
        String university = University.getText().toString();

        if (!password.equals(confirmpassword))
        {
            Context context = getApplicationContext();
            CharSequence text = "Passwords do not match!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (!(email.toUpperCase().contains(".EDU") && email.contains("@"))){
            Context context = getApplicationContext();
            CharSequence text = "Invalid Email format!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute(type, firstName, lastName, university,
                    email,password);
        }
    }

    @Override
    public void processFinish(String output) {

        System.out.println("OUTPUT PROCESS FOR REGISTRATION: " + output);

        if (output.contains("success")) {
            TextView email = (TextView)findViewById(R.id.reg_email);
            TextView fname = (TextView)findViewById(R.id.reg_first_name);
            TextView lname = (TextView)findViewById(R.id.reg_last_name);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.reg_university);

            User.email = email.getText().toString();
            User.fName = fname.getText().toString();
            User.lName = lname.getText().toString();
            User.school = autoCompleteTextView.getText().toString();

            Intent intent = new Intent(this, VerifyEmail.class);
            startActivity(intent);
        }
    }
}


