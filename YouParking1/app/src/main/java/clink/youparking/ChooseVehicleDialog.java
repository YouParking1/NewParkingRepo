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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Clink on 10/15/2016.
 */

public class ChooseVehicleDialog extends DialogFragment implements AsyncResponse {

    AlertDialog dialog;
    RadioGroup radioGroup;
    RadioButton rb;
    ImageView vehicleImage2;
    Button okBtn;


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("getVehicles");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(dialog.getContext(), rb.getText(), Toast.LENGTH_LONG).show();
            }
        });

        builder.setTitle("Choose Current Vehicle");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        radioGroup = (RadioGroup)dialog.findViewById(R.id.populate_vehicle_choices_find);
        vehicleImage2 = (ImageView)dialog.findViewById(R.id.vehicleImage2);
        okBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if(rb != null && checkedId > -1){
                    User.finderVehicleID = User.vehicles.get(checkedId).getId();
                    getImage(User.vehicles.get(checkedId).getId());
                    vehicleImage2.setVisibility(View.VISIBLE);
                    String make = User.vehicles.get(checkedId).getMake();
                    String model = User.vehicles.get(checkedId).getModel();
                    String year = Integer.toString(User.vehicles.get(checkedId).getYear());
                    User.finderVehicle = make + " " + model + " " + year;
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

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
                vehicleImage2.setImageBitmap(b);
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
    public void processFinish(String output) throws JSONException {
        JSONArray jsonArray = new JSONArray(output);

        if (!User.vehicles.isEmpty()) {
            User.vehicles.clear();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User.vehicles.add(new Vehicles(jsonObject.getInt("id"), jsonObject.getString("Make"),
                    jsonObject.getString("Model"), jsonObject.getInt("Year"), jsonObject.getString("Color")));
        }

        radioGroup = (RadioGroup)dialog.findViewById(R.id.populate_vehicle_choices_find);
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


                rb = new RadioButton(getActivity());
                rb.setId(i);
                rb.setText(User.vehicles.get(i).getMake() + " " + User.vehicles.get(i).getModel() + " " + User.vehicles.get(i).getYear());
                radioGroup.addView(rb);
            }
        }
    }
}
