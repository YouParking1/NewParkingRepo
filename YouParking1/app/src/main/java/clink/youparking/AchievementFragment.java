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
 * {@link AchievementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AchievementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView achievement1Progress, achievement2Progress, achievement3Progress, achievement1Goal,
            achievement2Goal, achievement3Goal;
    ImageButton unknownAchievement1, unknownAchievement2, unknownAchievement3, knownAchievement1,
            knownAchievement2, knownAchievement3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchievementFragment newInstance(String param1, String param2) {
        AchievementFragment fragment = new AchievementFragment();
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
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/college.ttf");
        TextView title = (TextView)getView().findViewById(R.id.achievementsTitle);
        title.setTypeface(font);

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

        achievement1Progress = (TextView)getView().findViewById(R.id.achievement1_progress);
        achievement2Progress = (TextView)getView().findViewById(R.id.achievement2_progress);
        achievement3Progress = (TextView)getView().findViewById(R.id.achievement3_progress);
        achievement1Goal = (TextView)getView().findViewById(R.id.achievement1_goal);
        achievement2Goal = (TextView)getView().findViewById(R.id.achievement2_goal);
        achievement3Goal = (TextView)getView().findViewById(R.id.achievement3_goal);
        unknownAchievement1 = (ImageButton)getView().findViewById(R.id.unknownAchievement1);
        unknownAchievement2 = (ImageButton)getView().findViewById(R.id.unknownAchievement2);
        unknownAchievement3 = (ImageButton)getView().findViewById(R.id.unknownAchievement3);
        knownAchievement1 = (ImageButton)getView().findViewById(R.id.knownAchievement1);
        knownAchievement2 = (ImageButton)getView().findViewById(R.id.knownAchievement2);
        knownAchievement3 = (ImageButton)getView().findViewById(R.id.knownAchievement3);

        JSONObject jsonObject = new JSONObject(output);
        int spotsHeld = jsonObject.getInt("SpotsHeld");
        int spotsFound = jsonObject.getInt("SpotsFound");

        System.out.println("SPOTS HELD: " + spotsHeld);
        System.out.println("SPOTS FOUND: " + spotsFound);

        achievement1Progress.setText(Integer.toString(spotsHeld));
        achievement2Progress.setText(Integer.toString(spotsFound));
        achievement3Progress.setText(Integer.toString(spotsHeld));

        if(Integer.valueOf(achievement1Progress.getText().toString()) >= 5)
        {
            unknownAchievement1.setVisibility(View.GONE);
            knownAchievement1.setVisibility(View.VISIBLE);
            achievement1Progress.setText("5");
        }
        if(Integer.valueOf(achievement2Progress.getText().toString()) >= 5)
        {
            unknownAchievement2.setVisibility(View.GONE);
            knownAchievement2.setVisibility(View.VISIBLE);
            achievement2Progress.setText("5");
        }
        if(Integer.valueOf(achievement3Progress.getText().toString()) >= 10) {
            unknownAchievement3.setVisibility(View.GONE);
            knownAchievement3.setVisibility(View.VISIBLE);
            achievement3Progress.setText("10");
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
