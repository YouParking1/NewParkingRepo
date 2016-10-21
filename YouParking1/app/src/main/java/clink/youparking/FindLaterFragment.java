package clink.youparking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindLaterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindLaterFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArrayList<com.daimajia.swipe.SwipeLayout> swipeLayouts = new ArrayList<>();
    public ArrayList<LinearLayout> linearLayouts = new ArrayList<>();
    public ArrayList<LinearLayout> innerLayouts = new ArrayList<>();

    // Fragment for google maps.
    private Fragment mapFrag;

    public FindLaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindLaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindLaterFragment newInstance(String param1, String param2) {
        FindLaterFragment fragment = new FindLaterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_later, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChooseVehicleDialog dialog = new ChooseVehicleDialog();
        dialog.show(getActivity().getFragmentManager(), "TAG");

        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("findLater");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(String output) throws JSONException {
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "FIND");

        if (!User.spots.isEmpty()) {
            User.spots.clear();
        }

        if (output.equals("-1")) {

        }
        else {

            JSONArray jsonArray = new JSONArray(output);

            double lats [] = new double[jsonArray.length()];
            double longs [] = new double[jsonArray.length()];

            String comments [] = new String[jsonArray.length()];

            int points [] = new int[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int bidPoints = 0;
                String buyer = "NONE";

                if (jsonObject.getString("Points").equals("null")) {
                    System.out.println("&*&(&(*&(*&(*&(*&(*&(* " + jsonObject.get("Points").getClass());
                }
                else {
                    bidPoints = jsonObject.getInt("Points");
                    buyer = jsonObject.getString("Buyer");
                    System.out.println("*(*()*)*)(*)(*)(*)(*)  THAT WORKED!!!!!");
                }

                User.spots.add(new SpotLater(jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"),
                        jsonObject.getInt("StartPoints"), jsonObject.getInt("CurrentCar"), jsonObject.getString("Email"),
                        jsonObject.getString("Comments"), jsonObject.getInt("Holder_Percentage"),
                        jsonObject.getInt("Holder_Spots"), jsonObject.getInt("DepartTime"), jsonObject.getInt("Spot_ID"),
                        bidPoints, buyer));

                lats[i] = jsonObject.getDouble("Latitude");
                longs[i] = jsonObject.getDouble("Longitude");
                comments[i] = jsonObject.getString("Comments");

                if (bidPoints == 0) {
                    points[i] = jsonObject.getInt("StartPoints");
                }
                else {
                    points[i] = jsonObject.getInt("Points");
                }
            }

            bundle.putDoubleArray("LATS", lats);
            bundle.putDoubleArray("LONGS", longs);
            bundle.putStringArray("COMMENTS", comments);
            bundle.putIntArray("POINTS", points);
        }

        mapFrag = new GMapFragment();
        mapFrag.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.find_now_map, mapFrag).commit();



        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.find_spot_populate);

        if (User.spots.size() > 0) {
            for (int i = 0; i < User.spots.size(); i++) {

                Bundle innerBundle = new Bundle();
                innerBundle.putDouble("LAT", User.spots.get(i).getLatitude());
                innerBundle.putDouble("LONG", User.spots.get(i).getLongitude());
                innerBundle.putString("EMAIL", User.spots.get(i).getHolder_email());
                innerBundle.putString("COMMENT", User.spots.get(i).getComments());

                if (((SpotLater)User.spots.get(i)).getCurrentBid() == 0) {
                    innerBundle.putInt("POINTS", User.spots.get(i).getPoints());
                }
                else {
                    innerBundle.putInt("POINTS", ((SpotLater)User.spots.get(i)).getCurrentBid());
                }
                innerBundle.putInt("CAR", User.spots.get(i).getHolder_car());
                innerBundle.putInt("PERCENT", User.spots.get(i).getHolder_percentage());
                innerBundle.putInt("SPOTS", User.spots.get(i).getHolder_spots_held());
                innerBundle.putInt("ID", i);


                Fragment fragment = new DynamicSpotBid();
                fragment.setArguments(innerBundle);
                getFragmentManager().beginTransaction().add(linearLayout.getId(), fragment).commit();

            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
