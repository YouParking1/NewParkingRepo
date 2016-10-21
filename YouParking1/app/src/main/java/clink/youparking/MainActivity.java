package clink.youparking;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, VehiclesFragment.OnFragmentInteractionListener,
        MyBidsFragment.OnFragmentInteractionListener, FindNowFragment.OnFragmentInteractionListener,
        HoldSpotFragment.OnFragmentInteractionListener, FindLaterFragment.OnFragmentInteractionListener, HoldLaterFragment.OnFragmentInteractionListener,
        SignOutFragment.OnFragmentInteractionListener, GMapFragment.OnFragmentInteractionListener, HoldSpotMapFragment.OnFragmentInteractionListener,
        MapInteraction, HoldLaterMapFragment.OnFragmentInteractionListener, DynamicSpot.OnFragmentInteractionListener, AchievementFragment.OnFragmentInteractionListener,
        DynamicVehicle.OnFragmentInteractionListener, DynamicSpotBid.OnFragmentInteractionListener, AsyncResponse {

    String outputFromProcess = null;

    int bought_spot_id = -1;

    public enum Operation { DELETE, HOLDSPOT, BUY, NUMVEHICLES, BID, NONE }

    Operation operation = Operation.NONE;

    private boolean validHoldLaterTime = false;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //This bit of code is for setting ticket icon in nav header.
        View hView = navigationView.getHeaderView(0);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView text = (TextView) hView.findViewById(R.id.inMenu);
        text.setTypeface(font);

        TextView numTickets = (TextView)hView.findViewById(R.id.numTickets);
        numTickets.setText(Integer.toString(User.points));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if(User.holdingSpot)
        {
            isHolding();
        }
        else if(User.bidOpen) {
            //TODO: PERFORM SOME ACTION FOR USERS WHO HAVE BIDS OPEN

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed(); Disabled so the user cannot return to the login activity.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (User.bidOpen || User.holdingSpot) {
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, "You're currently holding a spot open. Cancel" +
                    " the spot to reopen full access!", duration);
            toast.show();
        }
        else {
            if (id == R.id.nav_home) {
                fragmentClass = HomeFragment.class;
            } else if (id == R.id.nav_vehicles) {
                fragmentClass = VehiclesFragment.class;
            } else if (id == R.id.nav_my_bids) {
                fragmentClass = MyBidsFragment.class;
            } else if (id == R.id.nav_find_now) {
                fragmentClass = FindNowFragment.class;
            } else if (id == R.id.nav_hold_spot) {
                fragmentClass = HoldSpotMapFragment.class;
            } else if (id == R.id.nav_find_later) {
                fragmentClass = FindLaterFragment.class;
            } else if (id == R.id.nav_hold_later) {
                fragmentClass = HoldLaterMapFragment.class;
            } else if (id == R.id.nav_sign_out) {
                fragmentClass = SignOutFragment.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * finishHold & recheckSpot are functions for HoldSpotMapFragment
     * finishHold will take your coordinates and send them HoldSpotFragment.
     *
     * recheckSpot will reload the HoldSpotMapFragment in order obtain your current
     * coordinates after error.
     * @param view
     */
    @Override
    public void finishHold(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldSpotFragment()).commit();
    }

    @Override
    public void recheckSpot(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldSpotMapFragment()).commit();
    }

    public void finishHoldLater(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldLaterFragment()).commit();
    }

    public void isHolding()
    {
        Intent intent = new  Intent(this, FoundSpotActivity.class);
        intent.putExtra("SpotID", -1);
        intent.putExtra("Role", "Holder");
        intent.putExtra("TransID", "-1");
        startActivity(intent);
    }

    public void deleteVehicle(View view)
    {
        operation = Operation.DELETE;
        String type = "deleteVehicle";
        int id = view.getId();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, Integer.toString(id));
    }

    public void recheckSpotLater(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldLaterMapFragment()).commit();
    }

    public void goToAchievements(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new AchievementFragment()).commit();
    }

    /**
     * Takes content in HoldSpotFragment and sends it to database.
     * @param view
     */
    public void onHold(View view) {

        operation = Operation.HOLDSPOT;

        Spinner spinner = (Spinner) findViewById(R.id.holdPointsSpinner);
        String choice = spinner.getSelectedItem().toString();

        EditText editText = (EditText) findViewById(R.id.holdSpotComments);
        String holdComments = editText.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;

        backgroundWorker.execute("hold", choice, Integer.toString(view.getId()), Double.toString(User.myLocation.latitude),
                Double.toString(User.myLocation.longitude), holdComments);
    }

    public void onHoldLater(View view)
    {
        if (User.time != 0 && validHoldLaterTime) {

            Spinner spinner = (Spinner) findViewById(R.id.holdPointsSpinner);
            String points = spinner.getSelectedItem().toString();

            EditText editText = (EditText) findViewById(R.id.holdSpotLaterComments);
            String holdLaterComments = editText.getText().toString();

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;

            //TODO: Travis Clinkscales - Change the second argument to something else
            backgroundWorker.execute("hold_later", points, Long.toString(User.time), "1", Double.toString(User.myLocation.latitude),
                    Double.toString(User.myLocation.longitude), holdLaterComments);

            User.time = 0;
        }
        else if (User.time == 0){
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, "Please choose a departure time.", duration);
            toast.show();
        }
    }

    public void onSetTimeButtonClicked(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    public void goToVehicleRegister(View view)
    {
        operation = Operation.NUMVEHICLES;

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("numVehicles");
    }

    @Override
    public void processFinish(String output) throws JSONException {
        //outputFromProcess = output;

        System.out.println("OUTPUT FOR PROCESS: " + output);


        if(operation == Operation.DELETE)
        {
            if(output.contains("No"))
            {
                Toast.makeText(this, "You must have at least one vehicle.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Deleted Vehicle!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, new VehiclesFragment()).commit();
            }
        }
        else if(operation == Operation.HOLDSPOT)
        {
            User.holdingSpot = true;
            Intent intent = new  Intent(this, FoundSpotActivity.class);
            intent.putExtra("SpotID", -1);
            intent.putExtra("Role", "Holder");
            intent.putExtra("TransID", "-1");
            startActivity(intent);
            //finish();
        }
        else if(operation == Operation.BUY) {
            if (output.contains("-1")) { // if spot was taken while the buyer was browsing
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Sorry")
                        .setMessage("This spot was taken. Try refreshing the find now menu.")
                        .setPositiveButton("Refresh Spots",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, new FindNowFragment()).commit();
                            }
                        })
                        .show();
            }
            else {
                if (bought_spot_id != -1) {
                    int id = bought_spot_id;
                    bought_spot_id = -1;
                    Intent intent = new Intent(this, FoundSpotActivity.class);
                    intent.putExtra("SpotID", id);
                    intent.putExtra("Role", "Buyer");
                    intent.putExtra("TransID", output);
                    startActivity(intent);
                    //finish();
                }
            }
        }

        else if(operation == Operation.NUMVEHICLES)
        {
            if(Integer.parseInt(output) < 3)
            {
                Intent intent = new Intent(this, AddNewVehicle.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You have too many vehicles. Please delete one vehicle first.", Toast.LENGTH_LONG).show();
            }
        }
        else if (operation == Operation.BID) {
            if (output.equals("200")) {
                System.out.println("EVERYTHING IS OKAY!");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
            }
            else if (output.equals("-1")) {
                System.out.println("SOMETHING WENT WRONG");
            }
        }
    }

    /**
     *
     * LAUNCHES FoundSpotActivity
     * @param view
     */
    public void buySpot(View view) {
        operation = Operation.BUY;

        if (User.points >= User.spots.get(view.getId()).getPoints()) {
            bought_spot_id = view.getId();

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute("exchange", User.spots.get(view.getId()).getHolder_email(),
                    Integer.toString(User.spots.get(view.getId()).getPoints()),
                    Integer.toString(User.spots.get(view.getId()).getTime()));

        }
        else {
            Toast toast = Toast.makeText(this, "Not Enough Points", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    /**
     * Adjusts map to clicked HeldSpot in the FindNowMapFragment
     * @param view
     */
    public void moveMap(View view) {
        int i = 0;
        boolean notFound = true;
        while ((i < getSupportFragmentManager().getFragments().size()) && notFound) {
            String fragmentClass = getSupportFragmentManager().getFragments().get(i).getClass().toString();
            if (fragmentClass.contains("FindNowFragment") || fragmentClass.contains("FindLaterFragment")) {
                notFound = false;
            }
            else
                i++;
        }
        int j = 0;
        while ((j < getSupportFragmentManager().getFragments().get(i).getChildFragmentManager().getFragments().size()) && !notFound) {
            if (getSupportFragmentManager().getFragments().get(i).getChildFragmentManager().getFragments().get(j).getClass().toString()
                    .contains("GMapFragment")) {
                notFound = true;
            }
            else {
                j++;
            }
        }

        GMapFragment gmap = (GMapFragment) getSupportFragmentManager().getFragments().get(i).getChildFragmentManager()
                .getFragments().get(j);
        gmap.setToSpotClicked(view.getId());
    }

    public void bidOne(View view) {
        operation = Operation.BID;

        int currentBid = ((SpotLater) User.spots.get(view.getId())).getCurrentBid();
        int startPoints = ((SpotLater) User.spots.get(view.getId())).getPoints() + 1;
        String spotId= Integer.toString(((SpotLater)User.spots.get(view.getId())).getSpotId());
        String sPoints = "";

        if (currentBid != 0) {
            currentBid++;
            sPoints = Integer.toString(currentBid);
        }
        else {
            sPoints = Integer.toString(startPoints);
        }

        System.out.println("YOU WILL BE BIDDING " + sPoints + " *(*(*(*()*(*)*()*)(*)(*)(*)&*^*&^*&");

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("setBid", spotId, sPoints, Integer.toString(User.finderVehicleID));

    }

    public void bidFive(View view) {
        operation = Operation.BID;

        int currentBid = ((SpotLater) User.spots.get(view.getId())).getCurrentBid();
        int startPoints = ((SpotLater) User.spots.get(view.getId())).getPoints() + 1;
        String spotId= Integer.toString(((SpotLater)User.spots.get(view.getId())).getSpotId());
        String sPoints = "";

        if (currentBid != 0) {
            currentBid += 5;
            sPoints = Integer.toString(currentBid);
        }
        else {
            sPoints = Integer.toString(startPoints);
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("setBid", spotId, sPoints, Integer.toString(User.finderVehicleID));
    }

    public void setValidTime(Boolean bool) {
        validHoldLaterTime = bool;
    }


}