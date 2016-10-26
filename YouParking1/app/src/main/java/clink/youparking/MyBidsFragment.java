package clink.youparking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBidsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBidsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBidsFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    LinearLayout linearLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyBidsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBidsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBidsFragment newInstance(String param1, String param2) {
        MyBidsFragment fragment = new MyBidsFragment();
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
        return inflater.inflate(R.layout.fragment_my_bids, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) getView().findViewById(R.id.add_bids_layout);
        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("findbids");

        TextView title = (TextView)getView().findViewById(R.id.activeBidsTitle);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");
        title.setTypeface(font);
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
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");

        if (output.equals("-1")) {
            TextView textView = new TextView(getContext());
            textView.setText("You have no active bids!");
            textView.setTextColor(Color.parseColor("#3F51B5"));
            textView.setTypeface(font, Typeface.BOLD);
            textView.setTextSize(25);
            linearLayout.addView(textView);
        }
        else {
            JSONArray jsonArray = new JSONArray(output);
            int winningBids = jsonArray.length();
            int[] pointArr = new int[winningBids];
            long[] departs = new long[winningBids];


            for (int i = 0; i < winningBids; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TextView textView = new TextView(getContext());
                textView.setTextColor(Color.BLACK);

                long now = System.currentTimeMillis() / 1000;
                long departtime = jsonObject.getLong("DepartTime");
                long diff = departtime - now;
                long hours = diff / 3600;
                long minutes = (diff - (hours * 3600)) / 60;



                String desc = ("Departs in " + hours + "Hours and " + minutes + " Minutes");
                textView.setText(desc);
                textView.setTypeface(font, Typeface.BOLD);
                textView.setTextColor(Color.parseColor("#3F51B5"));
                textView.setTextSize(25);

                TextView points = new TextView(getContext());
                points.setText("Your Bid: " + jsonObject.getInt("Points"));
                points.setTextColor(Color.parseColor("#3F51B5"));
                points.setTypeface(font, Typeface.BOLD);
                points.setTextSize(25);

                linearLayout.addView(textView);
                linearLayout.addView(points);
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
