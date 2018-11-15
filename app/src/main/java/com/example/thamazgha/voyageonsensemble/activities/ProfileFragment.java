package com.example.thamazgha.voyageonsensemble.activities;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.thamazgha.voyageonsensemble.activities.MainActivity.SHARED_PREFS;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    public Activity activity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;

        if(placeAutocompleteFragment != null) {
            FragmentTransaction add = instance.getActivity().getFragmentManager().beginTransaction();
            add.remove(placeAutocompleteFragment);
            add.add(placeAutocompleteFragment, "");
            add.commit();
        }

    }
    static JWT token ;

    static ProfileFragment instance ;

    private static EditText tv_usr_name ;
    private static EditText tv_first_name;
    private static EditText tv_last_name;
    private static EditText tv_email;
    private static EditText tv_pass;
    private static EditText tv_confirm_pass;

    private static View current_view; ;
    private static TagView Tview;
    private static Button buttonChoose ;
    private static Button buttonUpload ;
    private static Button buttonSnap;
    private static CardView buttonSaveChanges;

    private static PlaceAutocompleteFragment placeAutocompleteFragment;

    public  static void set_token(JWT token){ ProfileFragment.token = token; }


    public static ProfileFragment getInstance(){

        if(instance == null)
            instance = new ProfileFragment();

        return instance;
    };


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (current_view == null){

            ProfileFragment.current_view = inflater.inflate(R.layout.fragment_profile, container, false);
            View v = current_view;
            TextView tv = (TextView) v.findViewById(R.id.edit_usr_name);

            buttonChoose = (Button) v.findViewById(R.id.buttonChoose);
            buttonUpload = (Button) v.findViewById(R.id.buttonUpload);
            buttonSnap = (Button) v.findViewById(R.id.buttonSnap);
            buttonSaveChanges = (CardView) v.findViewById(R.id.btn_save_changes);

            buttonChoose.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            buttonSnap.setOnClickListener(this);
            buttonSaveChanges.setOnClickListener(this);

            tv_usr_name = (EditText) current_view.findViewById(R.id.edit_usr_name);
            tv_first_name = (EditText) current_view.findViewById(R.id.edit_first_name);
            tv_last_name = (EditText) current_view.findViewById(R.id.edit_last_name);
            tv_email = (EditText) current_view.findViewById(R.id.edit_email);
            tv_pass = (EditText) current_view.findViewById(R.id.edit_new_pass);
            tv_confirm_pass = (EditText) current_view.findViewById(R.id.confirm_new_pass);


            Tview = (TagView) v.findViewById(R.id.tagview);

            placeAutocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            placeAutocompleteFragment.setFilter(typeFilter);

            placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    for (Tag t : Tview.getTags()) {

                        if (t.text.equals(place.getAddress()))
                            return;
                    }

                    Tag tag = new Tag(place.getAddress().toString());
                    tag.tagTextColor = Color.parseColor("#FFFFFF");
                    tag.layoutColor = Color.parseColor("#DDDDDD");
                    tag.layoutColorPress = Color.parseColor("#555555");
                    //or tag.background = this.getResources().getDrawable(R.drawable.custom_bg);
                    tag.radius = 30f;
                    tag.tagTextSize = 20f;
                    tag.layoutBorderSize = 1f;
                    tag.layoutBorderColor = Color.parseColor("#FFFFFF");
                    tag.isDeletable = true;
                    Tview.addTag(tag);
                }

                @Override
                public void onError(Status status) {
                    Toast.makeText(getActivity(), "" + status.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }   ;

        this.getProfileImage();
        this.refreshProfileInfos();
        return current_view;

    }


    public void refreshProfileInfos(){

        tv_first_name.setText(token.getClaim("firstname").asString());
        tv_last_name.setText(token.getClaim("lastname").asString());
        tv_usr_name.setText(token.getClaim("username").asString());
        tv_email.setText(token.getClaim("email").asString());
        tv_pass.setText("");
        tv_confirm_pass.setText("");

        Tview.removeAllTags();

        if( ! token.getClaim("cities").asString().equals("empty")){

            String[] cities = token.getClaim("cities").asString().split("/");

            for(String city : cities){

                Tag tag = new Tag(city);
                tag.tagTextColor = Color.parseColor("#FFFFFF");
                tag.layoutColor =  Color.parseColor("#DDDDDD");
                tag.layoutColorPress = Color.parseColor("#555555");
                tag.radius = 30f;
                tag.tagTextSize = 20f;
                tag.layoutBorderSize = 1f;
                tag.layoutBorderColor = Color.parseColor("#FFFFFF");
                tag.isDeletable = true;
                Tview.addTag(tag);
            }
        }
    }

    public void getProfileImage(){

        JSONObject json = new JSONObject();

        try {

            json.put("user_id", token.getClaim("id").asString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api) + "/GetProfilePicture";

        CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {

                CircleImageView civ = (CircleImageView) current_view.findViewById(R.id.profile_image);

                if(response.length == 0)
                    civ.setImageResource(R.drawable.nobody_m);
                else{

                    Bitmap bmp = BitmapFactory.decodeByteArray(response,0,response.length);
                    civ.setImageBitmap(bmp);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        QueueSingleton.getInstance(getActivity()).addToRequestQueue(cr);

    }

    @Override
    public void onClick(View v) {

        if (v == buttonChoose) {
            //Requesting storage permission
            requestStoragePermission();
            showFileChooser();
        }

        if (v == buttonUpload) {
            uploadImage();
        }

        if(v == buttonSnap){

            if(isAdded()){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        }

        if(v == buttonSaveChanges){

            this.onSaveChanges();
        }
    }



    public void uploadImage() {

        if(bitmap == null || bitmap.isRecycled()){
            Toast.makeText(getActivity(), "Empty selection please select an image first.", Toast.LENGTH_LONG).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        JSONObject json = new JSONObject();

        try {
            String s = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // the java lib works but is deprecated :'( use the same lib to encode and decode the byte array otherwise it won't work (i used the android one in the backend)
            //  String sss = java.util.Base64.getEncoder().encodeToString(byteArray);
            json.put("user_id", token.getClaim("id").asString());
            json.put("image",s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api) + "/UploadProfilePicture";

        CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {

                CircleImageView civ = (CircleImageView) current_view.findViewById(R.id.profile_image);

                if(response.length == 0)
                    civ.setImageResource(R.drawable.nobody_m);
                else{

                    Bitmap bmp = BitmapFactory.decodeByteArray(response,0,response.length);
                    civ.setImageBitmap(bmp);
                    Toast.makeText(getActivity(), "Successful Upload !", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        QueueSingleton.getInstance(getActivity()).addToRequestQueue(cr);
    }



    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  && data != null && data.getData() != null) {

            filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // camera result
        if(requestCode == 0 && data != null &&  data.getExtras() != null && data.getExtras().get("data") != null ){

            bitmap = (Bitmap) data.getExtras().get("data");
            Toast.makeText(getActivity(), "Bingo", Toast.LENGTH_LONG).show();
        }

    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void onSaveChanges(){

        // TODO
        // check the data correctness if something is wrong show an error toast otherwise contact the servlet and update the token in the local storage and show a success toast
        // changes regarding cities of interest : keep the current cities of interest in a static array which changes each time the token changes.
        // if cities of interest are updated => update the token and the static array which reflects it.
        // each time the user navigates to his profile copy the static array and allow him to edit that copy (same thing goes with other basic infos).

        if(tv_usr_name.getText().length() == 0){

            Toast.makeText(activity, "Oops, Invalid Username !", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tv_first_name.getText().length() == 0){

            Toast.makeText(activity, "Oops, Invalid First name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tv_last_name.getText().length() == 0){

            Toast.makeText(activity, "Oops, Invalid Last name !", Toast.LENGTH_SHORT).show();
            return;
        }

        if(! isValidEmail(tv_email.getText())){

            Toast.makeText(activity, "Oops, Invalid Email !", Toast.LENGTH_SHORT).show();
            return;
        }

        if( ( tv_pass.getText().length() > 0  && tv_pass.getText().length() < 5  ) || ( tv_confirm_pass.getText().length() > 0 && tv_confirm_pass.getText().length() < 5) ){

            Toast.makeText(activity, "Oops, Password too short !", Toast.LENGTH_SHORT).show();
            return;
        }


        if(! tv_pass.getText().toString().equals(tv_confirm_pass.getText().toString())){

            Toast.makeText(getActivity(), "Ooops, Passwords don't match !", Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject json = new JSONObject();

        try {

            json.put("user_id",token.getClaim("id").asString());
            json.put("username",tv_usr_name.getText().toString());
            json.put("firstname",tv_first_name.getText().toString());
            json.put("lastname",tv_last_name.getText().toString());
            json.put("email",tv_email.getText().toString());
            json.put("password",tv_pass.getText().toString());

            String tags = "";
            List<Tag> Tags = Tview.getTags();

            for(int i=0;i<Tags.size();i++){

                tags+= Tags.get(i).text;
                if(i<tags.length()-1) tags+="/";
            }

            json.put("cities",tags.length()>0 ? tags : "empty");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api)+"/UpdateProfileInfos";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JWT jwtInfoUser = null;

                        try {

                            // also update it in the local storage
                            jwtInfoUser = new JWT(response.get("token").toString());

                            ProfileFragment.set_token(jwtInfoUser);
                            PostsOfInterestFragment.set_token(jwtInfoUser);
                            PersonnalPostsFragment.set_token(jwtInfoUser);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",jwtInfoUser.toString());
                            editor.apply();

                            refreshProfileInfos();
                            // show what was updated and what wasn't
                            Toast.makeText(getActivity(), "Saved ! ", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        QueueSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

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


}
