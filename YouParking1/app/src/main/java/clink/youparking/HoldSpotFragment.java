package clink.youparking;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HoldSpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HoldSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoldSpotFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView vehicleImage;
    TextView coords;
    RadioGroup radioGroup;
    double mLat, mLong;
    EditText comments;
    Spinner tickets;
    Button holdBtn;

    private OnFragmentInteractionListener mListener;

    public HoldSpotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoldSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoldSpotFragment newInstance(String param1, String param2) {
        HoldSpotFragment fragment = new HoldSpotFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hold_spot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vehicleImage = (ImageView)getView().findViewById(R.id.imageVehicleChoice);
        holdBtn = (Button)getView().findViewById(R.id.holdBtn);

        radioGroup = (RadioGroup)getView().findViewById(R.id.populate_vehicle_choices_hold);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb != null && checkedId > -1){
                    User.holderVehicleID = User.vehicles.get(checkedId).getId();
                    getImage(User.vehicles.get(checkedId).getId());
                    vehicleImage.setVisibility(View.VISIBLE);
                    String make = User.vehicles.get(checkedId).getMake();
                    String model = User.vehicles.get(checkedId).getModel();
                    String year = Integer.toString(User.vehicles.get(checkedId).getYear());
                    User.holderVehicle = make + " " + model + " " + year;
                    holdBtn.setId(User.vehicles.get(checkedId).getId());
                    System.out.println("HOLDER VEHICLE: " + User.holderVehicle);
                }
            }
        });

        coords = (TextView) getView().findViewById(R.id.showCoords);
        if (User.myLocation != null)
            coords.setText("COORDINATES ARE " + Double.toString(User.myLocation.latitude) + " " + Double.toString(User              .myLocation.longitude));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                vehicleImage.setImageBitmap(b);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void processFinish(String output) throws JSONException {

        JSONArray jsonArray = new JSONArray(output);

        if (!User.vehicles.isEmpty()) {
            User.vehicles.clear();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User.vehicles.add(new Vehicles (jsonObject.getInt("id"), jsonObject.getString("Make"),
                    jsonObject.getString("Model"), jsonObject.getInt("Year"), jsonObject.getString("Color")));
        }

        RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.populate_vehicle_choices_hold);
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
                radioGroup.addView(rb);
            }
        }
    }
}
