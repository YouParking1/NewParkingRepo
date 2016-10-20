package clink.youparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UploadVehicleActivity extends AppCompatActivity implements AsyncResponse {
    public static final String UPLOAD_URL = "http://www.troyparking.com/uploads.php";
    public static final String UPLOAD_KEY = "image";

    private int REQUEST_LOAD_IMAGE = 1;

    private ImageView imageToUpload;

    private Bitmap bitmap;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
    }

    public void choosePhoto(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageToUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPhoto(View view)
    {
        uploadImage();
    }

    public void noImage(View view)
    {
        if(!User.isLoggedIn)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Thanks for Registering!")
                    .setMessage("Please login and enjoy our app!")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            String id = getIntent().getStringExtra("id");

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(id);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(UploadVehicleActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s.contains("Error"))
                {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
                else
                {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                System.out.println("&&&&&&&&&&&&&&&&&&&");
                System.out.println("ID IN DO IN BACKGROUND: " + id);

                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void processFinish(String output) throws JSONException {

    }
}
