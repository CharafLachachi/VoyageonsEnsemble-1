package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CostumAdapterV2 extends RecyclerView.Adapter<CostumAdapterV2.PublicationViewHolder> {

    private Context context;
    private ArrayList<PublicationItem> publicationsList;
    private Fragment personnal_posts_frag;

    public CostumAdapterV2(Context context, ArrayList<PublicationItem> publicationsList, Fragment personnal_posts_frag) {

        this.context = context;
        this.publicationsList = publicationsList;
        this.personnal_posts_frag = personnal_posts_frag;
    }


    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.own_post_view, parent, false);
        return new PublicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PublicationViewHolder publicationViewHolder, final int position) {
        PublicationItem currentItem = publicationsList.get(position);

        String img_url = currentItem.getImg_url();
        final String pub_owner = currentItem.getUserNameOwner();
        double roomPrice = currentItem.getRoomPrice();
        int nbPers = currentItem.getNbPers();
        String checkOutDate = currentItem.getCheckOutDate();
        String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();

        final int pub_id = currentItem.getPub_id();
        publicationViewHolder.pub_owner.setText("owner"+pub_owner);
        publicationViewHolder.roomPrice.setText("Price : "+roomPrice);
        publicationViewHolder.nbPers.setText(nbPers+" participants");
        publicationViewHolder.checkOutDate.setText(checkOutDate);
        publicationViewHolder.chekInDate.setText(chekInDate);
        publicationViewHolder.city.setText("Destination : "+city);
        publicationViewHolder.hotelName.setText("Hotel : "+hotelName);


        Picasso.with(context).load(img_url).into(publicationViewHolder.meteo);
        publicationViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject json = new JSONObject();

                try {

                    json.put("publicationId", pub_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = "http://192.168.0.35:8080/DAR_PROJECT" + "/DeletePublication";


                CustomJsonRequest cr = new CustomJsonRequest(Request.Method.POST, url, json,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {

                                    Toast.makeText( personnal_posts_frag.getActivity(), response, Toast.LENGTH_SHORT).show();
                                    if(response.contains("1") ){

                                        publicationsList.remove(position);
                                        notifyDataSetChanged();
                                        TextView tv = personnal_posts_frag.getView().findViewById(R.id.personnal_posts_header);
                                        tv.setText("You Have "+publicationsList.size()+" Personnal Posts");
                                        Toast.makeText( personnal_posts_frag.getActivity(), "The post has been successfully removed !", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else if(response.contains("0")){

                                        Toast.makeText( personnal_posts_frag.getActivity(), "404  posts not found !", Toast.LENGTH_SHORT).show();
                                        return;
                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                    }
                } ) ;


                QueueSingleton.getInstance(personnal_posts_frag.getActivity()).addToRequestQueue(cr);

            }


        });
    }


    @Override
    public int getItemCount() {
        return this.publicationsList.size();
    }


    public class PublicationViewHolder extends RecyclerView.ViewHolder {

        public ImageView meteo;
        public TextView pub_owner;
        public TextView roomPrice;
        public TextView nbPers;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;

        public CardView cancel;

        public ConstraintLayout parentLayaout;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            meteo = itemView.findViewById(R.id.meteo);
            pub_owner = itemView.findViewById(R.id.owner);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            nbPers = itemView.findViewById(R.id.nbPrs);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            chekInDate = itemView.findViewById(R.id.chekInDate);
            city = itemView.findViewById(R.id.city);
            hotelName = itemView.findViewById(R.id.hotelName);
            cancel = itemView.findViewById(R.id.btn_cancel_travel);
            parentLayaout = itemView.findViewById(R.id.parent_layaout);

        }
    }




    public class CustomJsonRequest extends JsonRequest<String> {

        private JSONObject mRequestObject;
        private Response.Listener<String> mResponseListener;

        public CustomJsonRequest(int method, String url, JSONObject requestObject, Response.Listener<String> responseListener,  Response.ErrorListener errorListener) {
            super(method, url, (requestObject == null) ? null : requestObject.toString(), responseListener, errorListener);
            mRequestObject = requestObject;
            mResponseListener = responseListener;
        }

        @Override
        protected void deliverResponse(String response) {
            mResponseListener.onResponse(response);
        }


        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data);
                try {
                    return Response.success(json,
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
