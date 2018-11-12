package com.example.thamazgha.voyageonsensemble.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.adapters.CustomAdapter;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    String jwtUser;
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private CustomAdapter mCustomAdapter;
    private ArrayList<PublicationItem> mPublicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        /** RecyclerView start*/
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPublicationList = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(MainActivity.this, mPublicationList, MainActivity.this);
        mRecyclerView.setAdapter(mCustomAdapter);


        /**localStorage*/

        jwtUser = getLocalStorage();
        DashHandler();
    }

    private String getLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("tokenn", sharedPreferences.getString("token", ""));
        return sharedPreferences.getString("token", "");
    }

    public void DashHandler() {
        JSONObject json = new JSONObject();

        JWT jwtuser = new JWT(jwtUser);

        Log.e("jwttt", jwtuser.getClaim("id").asString());
        try {
            json.put("userid", jwtuser.getClaim("id").asString());
            Toast.makeText(MainActivity.this, json.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.api) + "/dashboard";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e("length",String.valueOf(response.length()));
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject publication = response.getJSONObject(i);
                        Log.e("publication : ", publication.toString());
                        String pub_owner = publication.getString("owner");
                        double roomPrice = publication.getDouble("roomPrice");
                        int nbPers = publication.getInt("nbPers");
                        String checkOutDate = publication.getString("checkOutDate");
                        String chekInDate = publication.getString("chekInDate");
                        String city = publication.getString("city");
                        String hotelName = publication.getString("hotelName");

                        JSONObject weather = publication.getJSONObject("weather");
                        String img_url = weather.getString("icon"); //"http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
                        String picture_url = publication.getString("picture");;
                        int pub_id = publication.getInt("pub_id");
                        String ownerName = null;
                        JSONArray abonnes = publication.getJSONArray("abonnes");
                        for (int j=0; j<abonnes.length(); j++){
                            JSONObject abonne = abonnes.getJSONObject(j);
                            if (abonne.getInt("abonne_id") == Integer.parseInt(pub_owner)){
                                ownerName = abonne.getString("firstname");
                                 break;
                            }
                        }
                        int abonne_count = abonnes.length();
                       // getAbonneName(publication);
                        mPublicationList.add(new PublicationItem(pub_id, img_url, pub_owner, roomPrice, nbPers, checkOutDate, chekInDate, city, hotelName,picture_url,ownerName,abonne_count));
                        mCustomAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }
        );

        QueueSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    private void getAbonneName(JSONObject publication) {
        try {
            String owner = publication.getString("owner");
            JSONObject abonnes = publication.getJSONObject("abonnes");
            //TODO verifier si c'est le owner mettre son nom
            //TODO verifier si on atteint pas le nombre de personnes
            Log.i("abonnes",abonnes.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        Intent intent;
        switch (item.getItemId()) {
            case R.id.ic_search:
                msg = "search";
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ic_logout:
                msg = "logout";
                logout();
                break;
            case R.id.ic_account:
                msg = "account";
                //intent = new Intent(this, AccountActivity.class);
                //startActivity(intent);
                break;

        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                startActivity(intentLogin);
                finish();

                SharedPreferences myPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                finish();
                Log.d("", "Now log out and start the activity login");

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}