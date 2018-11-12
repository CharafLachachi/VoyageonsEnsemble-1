package com.example.thamazgha.voyageonsensemble.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.MediaStore;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.thamazgha.voyageonsensemble.activities.ProfileFragment.token;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
     static JWT token ;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // used for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText firstname;
    private EditText lastname;
    private EditText password;
    private AutoCompleteTextView email;
    private TextView goToLogin;
    private Button picture_button;
    private ImageView imageView;
    private byte[] byteArrayImage;

    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    //public  static void set_token(JWT token){ ProfileFragment.token = token; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        password = findViewById(R.id.password);
        goToLogin = findViewById(R.id.login);
        picture_button = findViewById(R.id.picture_button);
       imageView = findViewById(R.id.image);

        Button formBtn = (Button) findViewById(R.id.sign_up_button);
        formBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegister();
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLogin();
            }
        });

        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
    }

    public void sendRegister() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // For test i create cities array

        JSONArray cities = new JSONArray();
        cities.put("Paris, France");
        cities.put("Lyon, France");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("firstname", firstname.getText().toString());
        params.put("lastname", lastname.getText().toString());
        params.put("username", email.getText().toString());
        String s = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        // the java lib works but is deprecated :'( use the same lib to encode and decode the byte array otherwise it won't work (i used the android one in the backend)
        //String sss = java.util.Base64.getEncoder().encodeToString(byteArrayImage);

      //  params.put("user_id", token.getClaim("id").asString());
        params.put("image",s);

        String api = getString(R.string.api);
        final String url = api + "/SignUp";

        JSONObject paramJson = new JSONObject(params);
        try {
            paramJson.put("cities", cities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        JsonObjectRequest req = new JsonObjectRequest(url, paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Context context = getApplicationContext();

                            CharSequence text = "Account created !!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                           // if (imageViewA)

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    redirectToLogin();
                                }
                            }, duration);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(req);

    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }



    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent,
                          REQUEST_CAPTURE_IMAGE);
//        if(pictureIntent.resolveActivity(getPackageManager()) != null){
//            //Create a file to store the image
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,  getPackageName() +".provider", photoFile);
//
//                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        photoURI);
//                startActivityForResult(pictureIntent,
//                        REQUEST_CAPTURE_IMAGE);
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
              try {
                  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                  imageView.setImageBitmap(bitmap);


                  if(bitmap == null || bitmap.isRecycled()){
                      Toast.makeText(this, "Empty selection please select an image first.", Toast.LENGTH_LONG).show();
                      return;
                  }

                  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                  byteArrayImage = stream.toByteArray();

              }catch (Exception e){

              }

                //imageView.setImageURI(Uri.parse(imageFilePath));
//                InputStream iStream = null;
//                try {
//                    iStream = getContentResolver().openInputStream(Uri.parse(imageFilePath));
//                     byteArrayImage = getBytes(iStream);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void upload(byte[] byteArrayImage){
        JSONObject json = new JSONObject();

        try {
            String s = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            // the java lib works but is deprecated :'( use the same lib to encode and decode the byte array otherwise it won't work (i used the android one in the backend)
            //String sss = java.util.Base64.getEncoder().encodeToString(byteArrayImage);

            json.put("user_id", token.getClaim("id").asString());
            json.put("image",s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api) + "/UploadProfilePicture";
        CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {


                if(response.length == 0)
                    imageView.setImageResource(R.drawable.nobody_m);
                else{

                    Bitmap bmp = BitmapFactory.decodeByteArray(response,0,response.length);
                    //imageView.setImageBitmap(bmp);
                    Toast.makeText(getApplicationContext(), "Successful Upload !", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        QueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(cr);
    }
        public class CustomJsonRequest extends JsonRequest<byte[]> {

        private JSONObject mRequestObject;
        private Response.Listener<byte[]> mResponseListener;

        public CustomJsonRequest(int method, String url, JSONObject requestObject, Response.Listener<byte[]> responseListener,  Response.ErrorListener errorListener) {
            super(method, url, (requestObject == null) ? null : requestObject.toString(), responseListener, errorListener);
            mRequestObject = requestObject;
            mResponseListener = responseListener;
        }

        @Override
        protected void deliverResponse(byte[] response) {
            mResponseListener.onResponse(response);
        }


        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            try {
                byte[] bytes = response.data.clone();

                try {
                    return Response.success(bytes,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            } catch (Exception e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}



