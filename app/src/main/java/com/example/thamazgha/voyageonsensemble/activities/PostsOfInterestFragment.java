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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.adapters.CustomAdapterV3;
import com.example.thamazgha.voyageonsensemble.tools.CustomJsonRequest;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsOfInterestFragment extends Fragment {

    static JWT token ;

    private View v;
    private TextView tv ;
    private RecyclerView mRecyclerView;

    private CustomAdapterV3 mCustomAdapter;
    private ArrayList<PublicationItem> mPublicationList;

    public  static void set_token(JWT token){ PostsOfInterestFragment.token = token; }

    public static PostsOfInterestFragment getInstance(){

        return  new PostsOfInterestFragment();

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_posts_of_interest, container, false);
        tv = (TextView) v.findViewById(R.id.posts_of_interst_header);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_posts_of_interest);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPublicationList = new ArrayList<>();
        mCustomAdapter = new CustomAdapterV3(getActivity(), mPublicationList, this);
        mRecyclerView.setAdapter(mCustomAdapter);

        JSONObject json = new JSONObject();

        try {

            json.put("userid", token.getClaim("id").asString());
            json.put("Posts_type", "Interest");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.api) + "/GetPosts";

        CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            tv.setText("You Have Subscribed to "+response.length()+" Posts");

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject publication = response.getJSONObject(i);
                                int pub_owner = publication.getInt("owner");
                                double roomPrice = publication.getDouble("roomPrice");
                                int nbPers = publication.getInt("nbPers");
                                String checkOutDate = publication.getString("checkOutDate");
                                String chekInDate = publication.getString("chekInDate");
                                String city = publication.getString("city");
                                String hotelName = publication.getString("hotelName");
                                JSONObject weather = publication.getJSONObject("weather");
                                String img_url = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
                                int pub_id = publication.getInt("pub_id");
                                mPublicationList.add(new PublicationItem(pub_id,img_url, ""+pub_owner, roomPrice, nbPers, checkOutDate, chekInDate, city, hotelName,null,null,0));
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
