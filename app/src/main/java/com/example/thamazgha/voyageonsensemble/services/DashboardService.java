package com.example.thamazgha.voyageonsensemble.services;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thamazgha.voyageonsensemble.activities.DashboardActivity;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class DashboardService {
/*
    private ArrayList<PublicationItem> publicationsList;
    protected static WeakReference<DashboardActivity> DAshboardActivityWeakReference;
    static String api = Resources.getSystem().getString(R.string.api);
    public DashboardService(ArrayList<PublicationItem> publicationsList, WeakReference<DashboardActivity> DAshboardActivityWeakReference) {
        this.publicationsList = publicationsList;
        this.DAshboardActivityWeakReference = DAshboardActivityWeakReference;
    }

    public void joinHandler(int abo_id, int pub_id, final int position) {
        DashboardActivity activity = DAshboardActivityWeakReference.get();
        Toast.makeText(activity, "hello from join", Toast.LENGTH_SHORT).show();
        String url = api+"/join";
        JSONObject json = new JSONObject();

        final int pos = position;
        try {
            //TODO recuperer userID apartir de connexionactivity
            json.put("userid", 1);
            json.put("pubid",pub_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Me : server responce",response.toString());
                        publicationsList.get(pos).setNbPers(publicationsList.get(pos).getNbPers()+1);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("me : errorCustomAdapter","jsonObjectRequest");
                    }
                });

        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void quitHandler(int abo_id, int pub_id, int position) {
        DashboardActivity activity = DAshboardActivityWeakReference.get();
        String url = api+"/UnsubscribeToPublication";

        JSONObject json = new JSONObject();

        final int pos = position;
        try {
            //TODO recuperer userID apartir de connexionactivity
            json.put("userid", 1);
            json.put("pubid",pub_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Me : server responce",response.toString());
                        publicationsList.get(pos).setNbPers(publicationsList.get(pos).getNbPers()+1);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("me : errorCustomAdapter","jsonObjectRequest");
                    }
                });

        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }*/

    private ArrayList<PublicationItem> publicationsList;
    protected WeakReference<DashboardActivity> DAshboardActivityWeakReference;

    public DashboardService(ArrayList<PublicationItem> publicationsList, WeakReference<DashboardActivity> DAshboardActivityWeakReference) {
        this.publicationsList = publicationsList;
        this.DAshboardActivityWeakReference = DAshboardActivityWeakReference;
    }

    public void joinHandler(int abo_id, int pub_id, int position) {
        DashboardActivity activity = DAshboardActivityWeakReference.get();
        Toast.makeText(activity, "hello from join", Toast.LENGTH_SHORT).show();
        String url ="http://192.168.1.44:8080/DAR_PROJECT/join";
        JSONObject json = new JSONObject();

        final int pos = position;
        try {

            //TODO recuperer userID apartir de connexionactivity
            json.put("userid", 1);
            json.put("pubid",pub_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Me : server responce",response.toString());
                        publicationsList.get(pos).setNbPers(publicationsList.get(pos).getNbPers()+1);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("me : errorCustomAdapter","jsonObjectRequest");
                    }
                });

        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }
}

