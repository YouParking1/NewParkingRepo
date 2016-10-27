package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNewVehicle extends AppCompatActivity implements AsyncResponse {

    Spinner smake, smodel, syear, scolor;
    Button newVehicleBtn;
    String MakeTxt, ModelTxt;
    TextView titleText, makeText, modelText, yearText, colorText;
    public String AssetJSONFile(String filename, Context context) throws IOException {
        //AssetManager manager = context.getAssets();
        InputStream file = getAssets().open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        String json = new String(formArray, "UTF-8");
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);

        Typeface titleFont = Typeface.createFromAsset(this.getAssets(), "fonts/college.ttf");
        SpannableString s = new SpannableString("YOUPARKING");
        s.setSpan(new CustomTypefaceSpan("", titleFont), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Handwriting.ttf");
        titleText = (TextView)findViewById(R.id.newVehicleTitle);
        titleText.setTypeface(font);
        makeText = (TextView)findViewById(R.id.newMakeText);
        makeText.setTypeface(font);
        modelText = (TextView)findViewById(R.id.newModelText);
        modelText.setTypeface(font);
        yearText = (TextView)findViewById(R.id.newYearText);
        yearText.setTypeface(font);
        colorText = (TextView)findViewById(R.id.newColorText);
        colorText.setTypeface(font);
        newVehicleBtn = (Button) findViewById(R.id.newVehicleBtn);
        newVehicleBtn.setTypeface(font);

        smake = (Spinner)findViewById(R.id.sMake);
        smodel = (Spinner)findViewById(R.id.sModel);
        syear = (Spinner) findViewById(R.id.sYear);
        scolor = (Spinner) findViewById(R.id.sColor);

        List<String> colors = Arrays.asList(getResources().getStringArray(R.array.vehicleColor));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, colors)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                ((TextView) v).setTypeface(font);
                ((TextView) v).setTextSize(20);
                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(20);
                return v;
            }
        };
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scolor.setAdapter(spinnerArrayAdapter);

        //ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", AddNewVehicle.this));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Make");
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                strArr.add(m);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, strArr)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View v = super.getView(position, convertView, parent);

                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                    ((TextView) v).setTypeface(font);
                    ((TextView) v).setTextSize(20);
                    return v;
                }

                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                    ((TextView) v).setTypeface(externalFont);
                    ((TextView) v).setTextSize(20);
                    return v;
                }
            };

            smake.setAdapter(adapter);

            smake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    MakeTxt = selectedItem;
                    picksMake(selectedItem);
                } // to close the onItemSelected
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            smodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    ModelTxt = selectedItem;
                    picksModel(selectedItem);
                } // to close the onItemSelected
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void picksMake(String str)
    {
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", AddNewVehicle.this));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            int index = 0;
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                if(m.equals(str)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
                //JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            }
            JSONObject jb =(JSONObject) jarray.get(index);
            JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Model");
            for(int j=0;j<jarray2.length();j++)
            {
                JSONObject jb2 =(JSONObject) jarray2.get(j);
                String model = jb2.getString("model");
                strArr.add(model);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, strArr)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View v = super.getView(position, convertView, parent);

                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                        ((TextView) v).setTypeface(font);
                        ((TextView) v).setTextSize(20);
                        return v;
                    }

                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);

                        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                        ((TextView) v).setTypeface(externalFont);
                        ((TextView) v).setTextSize(20);
                        return v;
                    }
                };
                smodel.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void picksModel(String str)
    {
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", AddNewVehicle.this));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            int index = 0;
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                if(m.equals(MakeTxt)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
            }
            JSONObject jb =(JSONObject) jarray.get(index);
            JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            for(int i=0;i<jarray2.length();i++)
            {
                JSONObject jb2 =(JSONObject) jarray2.get(i);
                String m = jb2.getString("model");
                if(m.equals(ModelTxt)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
            }
            JSONObject jb3 =(JSONObject) jarray2.get(index);
            JSONArray jarray3 = (JSONArray) jb3.getJSONArray("years");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Year");
            for(int j=0;j<jarray3.length();j++)
            {
                jb3 =(JSONObject) jarray3.get(j);
                String model = jb3.getInt("year") + "";
                strArr.add(model);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, strArr)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View v = super.getView(position, convertView, parent);

                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                        ((TextView) v).setTypeface(font);
                        ((TextView) v).setTextSize(20);
                        return v;
                    }

                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);

                        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Handwriting.ttf");
                        ((TextView) v).setTypeface(externalFont);
                        ((TextView) v).setTextSize(20);
                        return v;
                    }
                };
                syear.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveVehicle(View view)
    {
        smake = (Spinner)findViewById(R.id.sMake);
        smodel = (Spinner)findViewById(R.id.sModel);
        syear = (Spinner) findViewById(R.id.sYear);
        scolor = (Spinner)findViewById(R.id.sColor);

        if(smake != null && smake.getSelectedItem() != "Please Select a Make" && smodel != null &&
                smodel.getSelectedItem() != "Please Select a Model" && syear != null && syear.getSelectedItem() != "Please Select a Year"
                && scolor != null)
        {
            String selectedMake = smake.getSelectedItem().toString();
            String selectedModel = smodel.getSelectedItem().toString();
            String selectedYear = syear.getSelectedItem().toString();
            String selectedColor = scolor.getSelectedItem().toString();

            String type = "vehicleRegister";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute(type, selectedMake, selectedModel, selectedYear,
                    selectedColor);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Must select a choice for all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String output) {
        Intent intent = new Intent(this, UploadVehicleActivity.class);
        intent.putExtra("id", output);
        startActivity(intent);
    }
}
