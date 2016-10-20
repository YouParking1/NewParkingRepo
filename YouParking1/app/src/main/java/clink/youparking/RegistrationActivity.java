package clink.youparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

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
        String email = Email.getText().toString().toUpperCase();
        String password = Password.getText().toString();
        String confirmpassword = ConfirmPass.getText().toString();
        String university = University.getText().toString();
        String[] universities = getResources().getStringArray(R.array.universities);

        if(firstName.equals("") || lastName.equals("") || email.equals("")
                || password.equals("") || confirmpassword.equals(""))
        {
            Toast.makeText(this, "Please fill in all blanks.", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmpassword))
        {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
        else if (!(email.contains(".EDU") && email.contains("@"))){
            Toast.makeText(this, "Invalid Email format!", Toast.LENGTH_SHORT).show();
        }
        else if(!Arrays.asList(universities).contains(university))
        {
            Toast.makeText(this, "Please choose a university in our list.", Toast.LENGTH_SHORT).show();
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

            User.email = email.getText().toString().toUpperCase();
            User.fName = fname.getText().toString();
            User.lName = lname.getText().toString();
            User.school = autoCompleteTextView.getText().toString().toUpperCase();

            Intent intent = new Intent(this, VerifyEmail.class);
            startActivity(intent);
        }
    }
}