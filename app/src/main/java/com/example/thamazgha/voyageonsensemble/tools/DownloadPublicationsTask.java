package com.example.thamazgha.voyageonsensemble.tools;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.thamazgha.voyageonsensemble.activities.DashboardActivity;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DownloadPublicationsTask extends AsyncTask<Integer,Void,ArrayList<PublicationItem>> {
    protected static String url = "http://134.157.255.38:8080/DAR_PROJECT/dashboard";

    //TO DO add textView that i will modify in asyncTask and ic_search for a way to launch an asyncTask
    public DownloadPublicationsTask(DashboardActivity dashboardActivity) {
        this.DAshboardActivityWeakReference = new WeakReference<DashboardActivity>(dashboardActivity);
    }

    protected WeakReference<DashboardActivity> DAshboardActivityWeakReference;


    @Override
    protected ArrayList<PublicationItem> doInBackground(Integer... integers) {

        final ArrayList<PublicationItem> publications_local_list = new ArrayList<PublicationItem>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject publication = response.getJSONObject(i);
                        String pub_owner = publication.getString("userNameOwner");
                        double roomPrice = publication.getDouble("roomPrice");
                        int nbPers = publication.getInt("nbPers");
                        String checkOutDate = publication.getString("checkOutDate");
                        String chekInDate = publication.getString("chekInDate");
                        String city = publication.getString("city");
                        String hotelName = publication.getString("hotelName");
                        int pub_id = publication.getInt("pub_id");
                        JSONObject weather = publication.getJSONObject("weather");
                        String img_url = weather.getString("icon");


                       // publications_local_list.add(new PublicationItem(pub_id, img_url, pub_owner, roomPrice, nbPers, checkOutDate, chekInDate, city, hotelName));
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

        DashboardActivity activity = DAshboardActivityWeakReference.get();

        if(activity==null || activity.isFinishing() ){
            Log.e("DownloadPublicationTask", "activity Dashboard is null or is finished");
        }else
        {
            QueueSingleton.getInstance(activity).addToRequestQueue(jsonArrayRequest);
            //Log.e("add request----",jsonArrayRequest)
        }


        return publications_local_list;
    }

    @Override
    protected void onPreExecute() {
        Log.i("debut"," asyncTask");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<PublicationItem> publicationItems) {
        super.onPostExecute(publicationItems);
        Log.i("fin"," asyncTask");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.i("Executing..."," asyncTask");
    }



}
