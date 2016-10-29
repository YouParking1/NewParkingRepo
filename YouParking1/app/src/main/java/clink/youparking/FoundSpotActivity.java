package clink.youparking;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class FoundSpotActivity extends AppCompatActivity implements HoldingMapFragment.OnFragmentInteractionListener,
        GMapFragment.OnFragmentInteractionListener, AsyncResponse{
    int spotID = -1;
    String role = "";
    String transactionID = "";

    Vehicles otherVehicle;

    boolean foundVehicle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            spotID = extras.getInt("SpotID");
            role = extras.getString("Role");
            transactionID = extras.getString("TransID");
        }

        super.onCreate(savedInstanceState);

        if (role.equals("Buyer")) {
            setContentView(R.layout.activity_found_spot);

            Typeface titleFont = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
            SpannableString s = new SpannableString("YOUPARKING");
            s.setSpan(new CustomTypefaceSpan("", titleFont), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(s);
            setSupportActionBar(toolbar);

            int carID = User.spots.get(spotID).getHolder_car();

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("getVehicleByID", Integer.toString(carID));
        }
        else if (role.equals("Holder")) {
            setContentView(R.layout.activity_holding_spot);

            Typeface titleFont = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
            SpannableString s = new SpannableString("YOUPARKING");
            s.setSpan(new CustomTypefaceSpan("", titleFont), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(s);
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onBackPressed() {
        //DISABLED BACK BUTTON PRESSED
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void endTransaction(View view) {
        User.holdingSpot = false;

        if (User.mSocket != null) {
            if (User.mGoogleApiClient != null && User.mGoogleApiClient.isConnected()) {
                User.mGoogleApiClient.disconnect();
                User.mGoogleApiClient = null;
            }

            User.mSocket.disconnect();
            User.mSocket.off();
            User.mSocket = null;
            //User.mSocket.off("new message", onNewMessage);
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("BuyNowComplete", transactionID);

        Intent intent = new Intent(this, ProblemActivity.class);
        intent.putExtra("transID", transactionID);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setBuyerVehicle(int vehicleID) {
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("getVehicleByID", Integer.toString(vehicleID));
    }

    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public void processFinish(String output) throws JSONException {

        JSONObject jsonObject = new JSONObject(output);
        int id = jsonObject.getInt("id");
        String make = jsonObject.getString("Make");
        String model = jsonObject.getString("Model");
        int year = jsonObject.getInt("Year");
        String color = jsonObject.getString("Color");

        otherVehicle = new Vehicles(id, make, model, year, color);
        foundVehicle = true;
    }

    public void getVehicleDetails(View view) {
        if (foundVehicle) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", otherVehicle.getId());
            bundle.putString("make", otherVehicle.getMake());
            bundle.putString("model", otherVehicle.getModel());
            bundle.putInt("year", otherVehicle.getYear());
            bundle.putString("color", otherVehicle.getColor());
            VehicleDetailsDialog dialog = new VehicleDetailsDialog();
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "tag");
        }
    }
}
