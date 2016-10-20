package clink.youparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

            int carID = User.spots.get(spotID).getHolder_car();

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("getVehicleByID", Integer.toString(carID));

            //TODO: MAKE THIS BACKGROUND WORKER
        }
        else if (role.equals("Holder"))
            setContentView(R.layout.activity_holding_spot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_confirm);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Comment can go here!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public void processFinish(String output) throws JSONException {

        System.out.println("WE ARE IN HERE!!! ()()()()()()()()()()()()");
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
