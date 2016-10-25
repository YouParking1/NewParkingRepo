package clink.youparking;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout auctionend;
    enum Operation {
        ACHIEVEMENT, BIDS, NONE
    }

    private Operation operation = Operation.NONE;

    TextView findNowSpotsAvailable, findLaterSpotsAvailable, welcomeMessage, statSpotsHeld,
            statPercentage, statsSpotsFound, achievement1Progress, achievement2Progress,
            achievement3Progress, achievement1Goal, achievement2Goal, achievement3Goal;
    ImageButton unknownAchievement1, unknownAchievement2, unknownAchievement3, knownAchievement1,
            knownAchievement2, knownAchievement3;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //TODO:FIX THIS ASAP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        super.onViewCreated(view, savedInstanceState);

        //auctionend = (LinearLayout) getView().findViewById(R.id.auction_hide_layout);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Writing_Stuff.ttf");


//        welcomeMessage = (TextView) getView().findViewById(R.id.welcomeText);
//        welcomeMessage.setText("Welcome, " + User.fName + " " + User.lName);
//        welcomeMessage.setTypeface(font);

//        TextView spotsText = (TextView)getView().findViewById(R.id.spotsText);
//        spotsText.setTypeface(font);

//        TextView statsText = (TextView)getView().findViewById(R.id.statsText);
//        statsText.setTypeface(font);

//        TextView achievementsText = (TextView)getView().findViewById(R.id.achievementText);
//        achievementsText.setTypeface(font);

        operation = Operation.ACHIEVEMENT;
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("home", User.school);
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

        //TODO: UNCOMMENT ALL OF THIS TOO!!!!!!!!!!!!!!!!!!!!!!!

        if (operation == Operation.ACHIEVEMENT) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");

            findNowSpotsAvailable = (TextView) getView().findViewById(R.id.find_now_spots_available);
            findLaterSpotsAvailable = (TextView) getView().findViewById(R.id.find_later_spots_available);
            statSpotsHeld = (TextView) getView().findViewById(R.id.stat_spots_held);
            statsSpotsFound = (TextView) getView().findViewById(R.id.stat_spots_found);
            statPercentage = (TextView) getView().findViewById(R.id.stat_percentage);
            TextView gradeText = (TextView) getView().findViewById(R.id.percentSign);
            TextView openSpotsText = (TextView) getView().findViewById(R.id.open_spots_text);
            TextView openBidsText= (TextView) getView().findViewById(R.id.open_bids_text);
            TextView statText = (TextView) getView().findViewById(R.id.statsText);
            TextView spotHeldText = (TextView) getView().findViewById(R.id.spotsHeldText);
            TextView spotFoundText = (TextView) getView().findViewById(R.id.spotsFoundText);
            TextView percentText = (TextView) getView().findViewById(R.id.percentageText);

            findNowSpotsAvailable.setTypeface(font,Typeface.BOLD);
            findLaterSpotsAvailable.setTypeface(font,Typeface.BOLD);
            statSpotsHeld.setTypeface(font,Typeface.BOLD);
            statsSpotsFound.setTypeface(font,Typeface.BOLD);
            statPercentage.setTypeface(font,Typeface.BOLD);
            gradeText.setTypeface(font,Typeface.BOLD);
            openSpotsText.setTypeface(font,Typeface.BOLD);
            openBidsText.setTypeface(font,Typeface.BOLD);
            statText.setTypeface(font,Typeface.BOLD);
            spotHeldText.setTypeface(font,Typeface.BOLD);
            spotFoundText.setTypeface(font,Typeface.BOLD);
            percentText.setTypeface(font,Typeface.BOLD);


//            achievement1Progress = (TextView) getView().findViewById(R.id.achievement1_progress);
//            achievement2Progress = (TextView) getView().findViewById(R.id.achievement2_progress);
//            achievement3Progress = (TextView) getView().findViewById(R.id.achievement3_progress);
//            achievement1Goal = (TextView) getView().findViewById(R.id.achievement1_goal);
//            achievement2Goal = (TextView) getView().findViewById(R.id.achievement2_goal);
//            achievement3Goal = (TextView) getView().findViewById(R.id.achievement3_goal);
//            unknownAchievement1 = (ImageButton) getView().findViewById(R.id.unknownAchievement1);
//            unknownAchievement2 = (ImageButton) getView().findViewById(R.id.unknownAchievement2);
//            unknownAchievement3 = (ImageButton) getView().findViewById(R.id.unknownAchievement3);
//            knownAchievement1 = (ImageButton) getView().findViewById(R.id.knownAchievement1);
//            knownAchievement2 = (ImageButton) getView().findViewById(R.id.knownAchievement2);
//            knownAchievement3 = (ImageButton) getView().findViewById(R.id.knownAchievement3);

            JSONObject jsonObject = new JSONObject(output);
            int findNow = jsonObject.getInt("FindNow");
            int findLater = jsonObject.getInt("FindLater");
            int spotsHeld = jsonObject.getInt("SpotsHeld");
            int spotsFound = jsonObject.getInt("SpotsFound");
            int percent = jsonObject.getInt("Percentage");

            if (percent >= 90) {
                gradeText.setText("A+");
            }
            else if (percent >= 80) {
                gradeText.setText("B+");
            }
            else if (percent >= 70) {
                gradeText.setText("C+");
            }
            else if (percent >= 60) {
                gradeText.setText("D+");
            }
            else {
                gradeText.setText("F");
            }

            System.out.println("FindNow: " + findNow);
            System.out.println("FindLater: " + findLater);
            System.out.println("SpotsHeld: " + spotsHeld);
            System.out.println("SpotsFound: " + spotsFound);
            System.out.println("Percentage: " + percent);

            findNowSpotsAvailable.setText(Integer.toString(findNow));
            findLaterSpotsAvailable.setText(Integer.toString(findLater));
            statSpotsHeld.setText(Integer.toString(spotsHeld));
            statsSpotsFound.setText(Integer.toString(spotsFound));
            statPercentage.setText(Integer.toString(percent) + "%");


//            achievement1Progress.setText(Integer.toString(spotsHeld));
//            achievement2Progress.setText(Integer.toString(spotsFound));
//            achievement3Progress.setText(Integer.toString(spotsHeld));
//
//            if (Integer.valueOf(achievement1Progress.getText().toString()) >= 5) {
//                unknownAchievement1.setVisibility(View.GONE);
//                knownAchievement1.setVisibility(View.VISIBLE);
//                achievement1Progress.setText("5");
//            }
//            if (Integer.valueOf(achievement2Progress.getText().toString()) >= 5) {
//                unknownAchievement2.setVisibility(View.GONE);
//                knownAchievement2.setVisibility(View.VISIBLE);
//                achievement2Progress.setText("5");
//            }
//            if (Integer.valueOf(achievement3Progress.getText().toString()) >= 10) {
//                unknownAchievement3.setVisibility(View.GONE);
//                knownAchievement3.setVisibility(View.VISIBLE);
//                achievement3Progress.setText("10");
//            }
//
//            operation = Operation.BIDS;
//            BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
//            backgroundWorker.delegate = this;
//            backgroundWorker.execute("findclosingbids");
        }
        else if (operation == Operation.BIDS) {
            if (output.equals("-1")) {

            }
            else {
                JSONObject jsonObject = new JSONObject(output);
                User.heldLater = new SpotLater(jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"),
                        jsonObject.getInt("StartPoints"), jsonObject.getInt("CurrentCar"), jsonObject.getString("Email"),
                        jsonObject.getString("Comments"), jsonObject.getInt("Holder_Percentage"),
                        jsonObject.getInt("Holder_Spots"), jsonObject.getInt("DepartTime"), jsonObject.getInt("Spot_ID"),
                        0, jsonObject.getString("Buyer"));
                auctionend.setVisibility(View.VISIBLE);
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