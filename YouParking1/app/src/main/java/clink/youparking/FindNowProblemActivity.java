package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class FindNowProblemActivity extends AppCompatActivity implements AsyncResponse {

    TextView problemFindTitle, question1FindText, findRadioBtn1Yes, findRadioBtn1No,
            question2FindText, findRadioBtn2Yes, findRadioBtn2No, findProblemCommentsText;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton rb1, rb2;
    Button findThanksBtn;
    String transactionID;
    EditText commentsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_now_problem);

        Typeface titleFont = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
        SpannableString s = new SpannableString("YOUPARKING");
        s.setSpan(new CustomTypefaceSpan("", titleFont), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Handwriting.ttf");
        problemFindTitle = (TextView)findViewById(R.id.problemFindTitle);
        problemFindTitle.setTypeface(font);
        question1FindText = (TextView)findViewById(R.id.question1FindText);
        question1FindText.setTypeface(font);
        question2FindText = (TextView)findViewById(R.id.question2FindText);
        question2FindText.setTypeface(font);
        findRadioBtn1Yes = (TextView)findViewById(R.id.findRadioBtn1Yes);
        findRadioBtn1Yes.setTypeface(font);
        findRadioBtn1No = (TextView)findViewById(R.id.findRadioBtn1No);
        findRadioBtn1No.setTypeface(font);
        findRadioBtn2Yes = (TextView)findViewById(R.id.findRadioBtn2Yes);
        findRadioBtn2Yes.setTypeface(font);
        findRadioBtn2No = (TextView)findViewById(R.id.findRadioBtn2No);
        findRadioBtn2No.setTypeface(font);
        findProblemCommentsText = (TextView)findViewById(R.id.findProblemCommentsText);
        findProblemCommentsText.setTypeface(font);
        commentsField = (EditText)findViewById(R.id.findProblemComments);
        commentsField.setTypeface(font);
        findThanksBtn = (Button) findViewById(R.id.findThanksBtn);
        findThanksBtn.setTypeface(font);

        Bundle extras = getIntent().getExtras();
        transactionID = extras.getString("transID");

        radioGroup1 = (RadioGroup)findViewById(R.id.question1Findgroup);
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

        radioGroup2 = (RadioGroup)findViewById(R.id.question2Findgroup);
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

    public void goToMainFromFind(View view)
    {
        commentsField = (EditText)findViewById(R.id.findProblemComments);
        String comments = commentsField.getText().toString();

        if(comments.equals("") || radioGroup1.getCheckedRadioButtonId() == -1 | radioGroup2.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
        }
        else
        {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("saveFinderProblems", transactionID, comments);

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
            Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        }
    }
}
