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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DynamicSpotBid.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DynamicSpotBid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicSpotBid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DynamicSpotBid() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DynamicSpotBid.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicSpotBid newInstance(String param1, String param2) {
        DynamicSpotBid fragment = new DynamicSpotBid();
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
        return inflater.inflate(R.layout.fragment_dynamic_spot_bid, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView text = (TextView) getView().findViewById(R.id.spot_on_bar);
        text.setTypeface(font);

        TextView ticketIcon = (TextView) getView().findViewById(R.id.ticket_icon_find);
        ticketIcon.setTypeface(font);
        TextView cartIcon = (TextView) getView().findViewById(R.id.cart_icon_layout);
        cartIcon.setTypeface(font);
        TextView cartIcon2 = (TextView) getView().findViewById(R.id.cart_icon_layout_2);
        cartIcon2.setTypeface(font);

        TextView pointView = (TextView) getView().findViewById(R.id.spot_points);
        int point = getArguments().getInt("POINTS");
        pointView.setText(Integer.toString(point));

        TextView commentView = (TextView)getView().findViewById(R.id.spot_comment);
        String comment = getArguments().getString("COMMENT");
        commentView.setText(comment);

        TextView spotsView = (TextView)getView().findViewById(R.id.spots_held);
        int spots_held = getArguments().getInt("SPOTS");
        spotsView.setText(Integer.toString(spots_held));

        RatingBar ratingBar = (RatingBar)getView().findViewById(R.id.rating_bar);
        int percent = getArguments().getInt("PERCENT");
        float convert_percent = (float) (percent/20.0);
        ratingBar.setRating(convert_percent);

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.dynamic_front);
        int id = getArguments().getInt("ID");
        linearLayout.setId(id);

        LinearLayout linearLayout2 = (LinearLayout) getView().findViewById(R.id.layout_bid_cinco);
        int id2 = getArguments().getInt("ID");
        linearLayout2.setId(id2);

        LinearLayout linearLayout3 = (LinearLayout) getView().findViewById(R.id.layout_bid_uno);
        int id3 = getArguments().getInt("ID");
        linearLayout3.setId(id2);

        if (!((SpotLater)User.spots.get(id)).getBuyer().equals("NONE")) {
            TextView textView = (TextView) getView().findViewById(R.id.topbidder_text);
            textView.setVisibility(View.VISIBLE);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
