package clink.youparking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class HoldSpotProblemActivity extends AppCompatActivity implements AsyncResponse {

    TextView problemHoldTitle, question1HoldText, holdRadioBtn1Yes, holdRadioBtn1No,
            question2HoldText, holdRadioBtn2Yes, holdRadioBtn2No, holdProblemCommentsText;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton rb1, rb2;
    Button holdThanksBtn;
    EditText commentsField;
    String transactionID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hold_spot_problem);

        Typeface titleFont = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
        SpannableString s = new SpannableString("YOUPARKING");
        s.setSpan(new CustomTypefaceSpan("", titleFont), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Handwriting.ttf");
        problemHoldTitle = (TextView)findViewById(R.id.problemHoldTitle);
        problemHoldTitle.setTypeface(font);
        question1HoldText = (TextView)findViewById(R.id.question1HoldText);
        question1HoldText.setTypeface(font);
        question2HoldText = (TextView)findViewById(R.id.question2HoldText);
        question2HoldText.setTypeface(font);
        holdRadioBtn1Yes = (TextView)findViewById(R.id.holdRadioBtn1Yes);
        holdRadioBtn1Yes.setTypeface(font);
        holdRadioBtn1No = (TextView)findViewById(R.id.holdRadioBtn1No);
        holdRadioBtn1No.setTypeface(font);
        holdRadioBtn2Yes = (TextView)findViewById(R.id.holdRadioBtn2Yes);
        holdRadioBtn2Yes.setTypeface(font);
        holdRadioBtn2No = (TextView)findViewById(R.id.holdRadioBtn2No);
        holdRadioBtn2No.setTypeface(font);
        holdProblemCommentsText = (TextView)findViewById(R.id.holdProblemCommentsText);
        holdProblemCommentsText.setTypeface(font);
        commentsField = (EditText)findViewById(R.id.holdProblemComments);
        commentsField.setTypeface(font);
        holdThanksBtn = (Button)findViewById(R.id.holdThanksBtn);
        holdThanksBtn.setTypeface(font);

        Bundle extras = getIntent().getExtras();
        transactionID = extras.getString("transID");

        radioGroup1 = (RadioGroup)findViewById(R.id.question1Holdgroup);
        radioGroup1.clearCheck();
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb1 = (RadioButton) group.findViewById(checkedId);
                if(rb1 != null && checkedId > -1){
                    //DO SOMETHING WITH THE GRADING
                }
            }
        });

        radioGroup2 = (RadioGroup)findViewById(R.id.question2Holdgroup);
        radioGroup2.clearCheck();
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb2 = (RadioButton) group.findViewById(checkedId);
                if(rb2 != null && checkedId > -1){
                    //DO SOMETHING WITH THE GRADING
                }
            }
        });
    }

    public void goToMainFromHold(View view)
    {
        commentsField = (EditText)findViewById(R.id.holdProblemComments);
        String comments = commentsField.getText().toString();

        if(comments.equals("") || radioGroup1.getCheckedRadioButtonId() == -1 | radioGroup2.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
        }
        else
        {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("saveHolderProblems", transactionID, comments);

            Toast.makeText(this, "Thank you for your input!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinish(String output) throws JSONException {
        if (output.contains("Error")) {
            Toast.makeText(this, "Sorry there was a problem.", Toast.LENGTH_LONG).show();
        }
    }
}
