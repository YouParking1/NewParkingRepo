package clink.youparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class BidClaimActivity extends AppCompatActivity implements AsyncResponse, BidClaimFragment.OnFragmentInteractionListener,
    GMapFragment.OnFragmentInteractionListener{

    private String role = "NONE";

    Vehicles otherVehicle;
    boolean foundVehicle = false;

    enum Operation {
        GETVEHICLE, GETVEHICLEID, ENDTRANS, NONE
    }

    Operation operation = Operation.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        System.out.println("HELD LATER: " + User.heldLater.getHolder_car());

        if (User.heldLater.getHolder_email().equals(User.email)) {
            role = "HOLDER";
            operation = Operation.GETVEHICLEID;
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("getvehiclefrombid");

        }
        else {
            role = "BUYER";
            operation = Operation.GETVEHICLE;
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("getVehicleByID", Integer.toString(User.heldLater.getHolder_car()));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_claim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void endTransaction(View view) {
        User.bidOpen = false;

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

        operation = Operation.ENDTRANS;
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("BidClaimComplete", Integer.toString(User.heldLater.getSpotId()));


        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("transID", Integer.toString(User.heldLater.getSpotId()));
        startActivity(intent);

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

    public String getRole() {
        return role;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void processFinish(String output) throws JSONException {
        if (operation == Operation.GETVEHICLE) {
            JSONObject jsonObject = new JSONObject(output);
            int id = jsonObject.getInt("id");
            String make = jsonObject.getString("Make");
            String model = jsonObject.getString("Model");
            int year = jsonObject.getInt("Year");
            String color = jsonObject.getString("Color");

            otherVehicle = new Vehicles(id, make, model, year, color);
            foundVehicle = true;
        }
        else if(operation == Operation.GETVEHICLEID) {
            operation = Operation.GETVEHICLE;
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("getVehicleByID", output);
        }
    }
}
