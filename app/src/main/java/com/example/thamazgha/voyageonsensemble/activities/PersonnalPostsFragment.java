package com.example.thamazgha.voyageonsensemble.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.adapters.CostumAdapterV2;
import com.example.thamazgha.voyageonsensemble.tools.CustomJsonRequest;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PersonnalPostsFragment extends Fragment {


    static JWT token ;

    private CostumAdapterV2 mCustomAdapter;
    private ArrayList<PublicationItem> mPublicationList;

    public  static void set_token(JWT token){ PersonnalPostsFragment.token = token; }

    private PersonnalPostsFragment instance;
    private View v;
    private TextView tv ;
    private RecyclerView mRecyclerView;

    public static PersonnalPostsFragment getInstance(){

        return  new PersonnalPostsFragment();

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(),"WOW",Toast.LENGTH_LONG);

        v = inflater.inflate(R.layout.fragment_personnal_posts, container, false);
        tv = (TextView) v.findViewById(R.id.personnal_posts_header);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_personnal_posts);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mPublicationList = new ArrayList<>();
        mCustomAdapter = new CostumAdapterV2(getActivity(), mPublicationList, this);
        mRecyclerView.setAdapter(mCustomAdapter);

        JSONObject json = new JSONObject();

        try {

            json.put("userid", token.getClaim("id").asString());
            json.put("Posts_type", "Own");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api) + "/GetPosts";

        CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            tv.setText("You Have "+response.length()+" Personnal Posts");
                            Log.e("tailllll : ",Integer.toString(response.length()));
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject publication = response.getJSONObject(i);
                                String pub_owner = publication.getString("owner");
                                double roomPrice = publication.getDouble("roomPrice");
                                int nbPers = publication.getInt("nbPers");
                                String checkOutDate = publication.getString("checkOutDate");
                                String chekInDate = publication.getString("chekInDate");
                                String city = publication.getString("city");
                                String hotelName = publication.getString("hotelName");
                                String picture_url = publication.getString("picture");

                                JSONObject weather = publication.getJSONObject("weather");
                                String img_url = weather.getString("icon");

                                int pub_id = publication.getInt("pub_id");
                                Log.d("puuuuuuuuuuuuuuuuub",Integer.toString(pub_id));

                                // update here
                                mPublicationList.add(
                                        new PublicationItem(pub_id,img_url, pub_owner, roomPrice, nbPers, checkOutDate, chekInDate, city, hotelName,picture_url,null,0));
                                mCustomAdapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        } ) ;

        QueueSingleton.getInstance(getActivity()).addToRequestQueue(cr);

        return v;
    }




}
