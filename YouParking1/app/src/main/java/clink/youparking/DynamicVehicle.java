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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DynamicVehicle.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DynamicVehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicVehicle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView vehicleMakeText, vehicleModelText, vehicleYearText, vehicleColorText;
    ImageView vehicleImage;
    Button vehicleBtn;
    private RequestHandler requestHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DynamicVehicle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DynamicVehicle.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicVehicle newInstance(String param1, String param2) {
        DynamicVehicle fragment = new DynamicVehicle();
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
        return inflater.inflate(R.layout.fragment_dynamic_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vehicleMakeText = (TextView)getView().findViewById(R.id.vehicleMake);
        String make = getArguments().getString("MAKE");
        vehicleMakeText.setText(make);

        vehicleModelText = (TextView)getView().findViewById(R.id.vehicleModel);
        String model = getArguments().getString("MODEL");
        vehicleModelText.setText(model);

        vehicleYearText = (TextView)getView().findViewById(R.id.vehicleYear);
        int year = getArguments().getInt("YEAR");
        vehicleYearText.setText(Integer.toString(year));

        vehicleColorText = (TextView)getView().findViewById(R.id.vehicleColor);
        String color = getArguments().getString("COLOR");
        vehicleColorText.setText(color);

        vehicleImage = (ImageView)getView().findViewById(R.id.vehicleImage);
        int vehicleid = getArguments().getInt("VEHICLEID");
        getImage(vehicleid);

        vehicleBtn = (Button)getView().findViewById(R.id.deleteVehicleBtn);
        vehicleBtn.setId(vehicleid);

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.vehiclesLayout);
        int id = getArguments().getInt("ID");
        linearLayout.setId(id);

        getView().setId(vehicleid);
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
