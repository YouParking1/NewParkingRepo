package clink.youparking;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HoldLaterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HoldLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoldLaterFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView holdLaterTitle, holdLaterDepartTime, timeText, additionalCommentsText,
            selectVehicleText, minTicketsText;
    EditText holdSpotLaterComments;
    Button holdSpotLaterBtn, timeBtn;
    ImageView vehicleImage;
    RadioGroup radioGroup;
    Spinner tickets;

    private OnFragmentInteractionListener mListener;

    public HoldLaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoldLaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoldLaterFragment newInstance(String param1, String param2) {
        HoldLaterFragment fragment = new HoldLaterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("getVehicles");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hold_later, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");
        holdLaterTitle = (TextView)getView().findViewById(R.id.holdLaterTitle);
        holdLaterTitle.setTypeface(font, Typeface.BOLD);
        selectVehicleText = (TextView)getView().findViewById(R.id.selectVehicleText);
        selectVehicleText.setTypeface(font, Typeface.BOLD);
        additionalCommentsText = (TextView)getView().findViewById(R.id.additionalCommentsText);
        additionalCommentsText.setTypeface(font, Typeface.BOLD);
        timeText = (TextView)getView().findViewById(R.id.timeText);
        timeText.setText("12:00 AM"); //TODO: Travis Clinkscales - SET DEFAULT TIME 2 HOURS EXACTLY
        timeText.setTypeface(font, Typeface.BOLD);
        holdLaterDepartTime = (TextView)getView().findViewById(R.id.holdLaterDepartTime);
        holdLaterDepartTime.setTypeface(font, Typeface.BOLD);
        minTicketsText = (TextView)getView().findViewById(R.id.minTicketsText);
        minTicketsText.setTypeface(font, Typeface.BOLD);
        holdSpotLaterComments = (EditText)getView().findViewById(R.id.holdSpotLaterComments);
        holdSpotLaterComments.setTypeface(font, Typeface.BOLD);
        timeBtn = (Button)getView().findViewById(R.id.timeBtn);
        timeBtn.setTypeface(font, Typeface.BOLD);
        holdSpotLaterBtn = (Button)getView().findViewById(R.id.holdSpotLaterBtn);
        holdSpotLaterBtn.setTypeface(font, Typeface.BOLD);
        vehicleImage = (ImageView)getView().findViewById(R.id.imageVehicleChoice);

        tickets = (Spinner)getView().findViewById(R.id.holdLaterPointsSpinner);
        List<String> points = Arrays.asList(getResources().getStringArray(R.array.points_array));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, points)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");
                ((TextView) v).setTypeface(font, Typeface.BOLD);
                ((TextView) v).setTextSize(20);
                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(20);
                return v;
            }
        };
        tickets.setAdapter(spinnerArrayAdapter);

        radioGroup = (RadioGroup)getView().findViewById(R.id.populate_vehicle_choices_hold_later);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb != null && checkedId > -1){
                    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Handwriting.ttf");
                    rb.setTypeface(font, Typeface.BOLD);
                    rb.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
                    getImage(User.vehicles.get(checkedId).getId());
                    User.holderVehicleID = User.vehicles.get(checkedId).getId();
                }
            }
        });
    }

    private void getImage(int id) {
        class GetImage extends AsyncTask<String,Void,Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                if(b == null)
                {
                    vehicleImage.setVisibility(View.GONE);
                }
                else
                {
                    vehicleImage.setImageBitmap(b);
                    vehicleImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = params[0];
                String add = "http://www.troyparking.com/getImage.php?id="+id;

                System.out.println("Link with id:" + add);

                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(Integer.toString(id));
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
        JSONArray jsonArray = new JSONArray(output);

        if (!User.vehicles.isEmpty()) {
            User.vehicles.clear();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User.vehicles.add(new Vehicles (jsonObject.getInt("id"), jsonObject.getString("Make"),
                    jsonObject.getString("Model"), jsonObject.getInt("Year"), jsonObject.getString("Color")));
        }

        RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.populate_vehicle_choices_hold_later);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        if (User.vehicles.size() > 0) {
            for (int i = 0; i < User.vehicles.size(); i++) {

                User.id = User.vehicles.get(i).getId();

                Bundle bundle = new Bundle();
                bundle.putInt("VEHICLEID", User.vehicles.get(i).getId());
                bundle.putString("MAKE", User.vehicles.get(i).getMake());
                bundle.putString("MODEL", User.vehicles.get(i).getModel());
                bundle.putInt("YEAR", User.vehicles.get(i).getYear());
                bundle.putInt("ID", i);

                System.out.println("VehicleID: " + User.vehicles.get(i).getId());
                System.out.println("Make: " + User.vehicles.get(i).getMake());
                System.out.println("Model: " + User.vehicles.get(i).getModel());
                System.out.println("Year: " + User.vehicles.get(i).getYear());
                System.out.println("ID: " + i);

                RadioButton rb = new RadioButton(getContext());
                rb.setId(i);
                rb.setText(User.vehicles.get(i).getMake() + " " + User.vehicles.get(i).getModel() + " " + User.vehicles.get(i).getYear());
                rb.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
                rb.setTypeface(font, Typeface.BOLD);
                radioGroup.addView(rb);
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
