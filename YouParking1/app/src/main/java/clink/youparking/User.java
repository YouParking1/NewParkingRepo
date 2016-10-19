package clink.youparking;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by acous on 8/28/2016.
 */
public class User {

    public static String email = null; // Retrieve the logged user's email
    public static String school = null; // Retrieve the logged user's school
    public static String fName = null;
    public static String lName = null;
    public static int numCars = 0;

    public static int percentage = 0;
    public static int spotsHeld = 0;

    public static LatLng myLocation = null;

    public static int points = 0;

    public static boolean failedLogin = false;
    public static ArrayList<LatLng> heldLocation = new ArrayList<>();

    public static long time = 0;

    public static ArrayList<Spot> spots = new ArrayList<>();
    public static ArrayList<Vehicles> vehicles = new ArrayList<>();

    public static int id = 0;

    public static boolean holdingSpot = false;

    public static Socket mSocket;

    public static GoogleApiClient mGoogleApiClient;

}