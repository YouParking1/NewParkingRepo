package clink.youparking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Clink on 10/18/2016.
 */

public class VehicleDetailsDialog extends DialogFragment implements AsyncResponse {

    AlertDialog dialog;
    ImageView vehicleImage3;
    int id;
    String make;
    String model;
    int year;
    String color;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        id = getArguments().getInt("id");
        make = getArguments().getString("make");
        model = getArguments().getString("model");
        year = getArguments().getInt("year");
        color = getArguments().getString("color");

        builder.setTitle("Holder Vehicle Details");
        builder.setMessage("The holder's vehicle is the \n" + "Make: " + make + "\nModel: " + model
            + "\nYear: " + year + "\nColor: " + color);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.vehicle_detail_dialog, null));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        vehicleImage3 = (ImageView)dialog.findViewById(R.id.vehicleImage3);
        getImage(id);
        vehicleImage3.setVisibility(View.VISIBLE);

        return dialog;
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
                    vehicleImage3.setVisibility(View.GONE);
                }
                else
                {
                    vehicleImage3.setImageBitmap(b);
                    vehicleImage3.setVisibility(View.VISIBLE);
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

    @Override
    public void processFinish(String output) throws JSONException {
//        JSONArray jsonArray = new JSONArray(output);
//
//        if (!User.vehicles.isEmpty()) {
//            User.vehicles.clear();
//        }
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            User.vehicles.add(new Vehicles(jsonObject.getInt("id"), jsonObject.getString("Make"),
//                    jsonObject.getString("Model"), jsonObject.getInt("Year"), jsonObject.getString("Color")));
//        }
//
//        if (User.vehicles.size() > 0) {
//            for (int i = 0; i < User.vehicles.size(); i++) {
//
//                User.id = User.vehicles.get(i).getId();
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("VEHICLEID", User.vehicles.get(i).getId());
//                bundle.putString("MAKE", User.vehicles.get(i).getMake());
//                bundle.putString("MODEL", User.vehicles.get(i).getModel());
//                bundle.putInt("YEAR", User.vehicles.get(i).getYear());
//                bundle.putInt("ID", i);
//
//                System.out.println("VehicleID: " + User.vehicles.get(i).getId());
//                System.out.println("Make: " + User.vehicles.get(i).getMake());
//                System.out.println("Model: " + User.vehicles.get(i).getModel());
//                System.out.println("Year: " + User.vehicles.get(i).getYear());
//                System.out.println("ID: " + i);
//
//            }
//        }
    }
}
