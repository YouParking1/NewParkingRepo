package clink.youparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProblemActivity extends AppCompatActivity {

    String transactionID, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        Bundle extras = getIntent().getExtras();
        transactionID = extras.getString("transID");
        role = extras.getString("role");
    }

    public void goToProblem(View view)
    {
        if(role.equals("Buyer"))
        {
            Intent intent = new Intent(this, FindNowProblemActivity.class);
            intent.putExtra("transID", transactionID);
            startActivity(intent);
        }
        else if(role.equals("Holder"))
        {
            Intent intent = new Intent(this, HoldSpotProblemActivity.class);
            intent.putExtra("transID", transactionID);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {}

    public void goToMainFromProblem(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
