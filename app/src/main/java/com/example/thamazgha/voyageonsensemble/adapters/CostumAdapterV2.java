package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.activities.PersonnalPostsFragment;
import com.example.thamazgha.voyageonsensemble.tools.CommentItem;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CostumAdapterV2 extends RecyclerView.Adapter<CostumAdapterV2.PublicationViewHolder> {

    private Context context;
    private ArrayList<PublicationItem> publicationsList;
    private static Fragment personnal_posts_frag;

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
        final PublicationItem currentItem = publicationsList.get(position);

        String img_url = currentItem.getImg_url();
        final String pub_owner = currentItem.getUserNameOwner();
        double roomPrice = currentItem.getRoomPrice();
        int nbPers = currentItem.getNbPers();
        String checkOutDate = currentItem.getCheckOutDate();
        String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();
        String picture_url = currentItem.getPicture();

        final int pub_id = currentItem.getPub_id();
        publicationViewHolder.pub_owner.setText(pub_owner);
        publicationViewHolder.roomPrice.setText(String.valueOf(roomPrice));
        publicationViewHolder.nbPers.setText(String.valueOf(nbPers));
        publicationViewHolder.checkOutDate.setText(checkOutDate);
        publicationViewHolder.chekInDate.setText(chekInDate);
        publicationViewHolder.city.setText(city);
        publicationViewHolder.hotelName.setText(hotelName);

        publicationViewHolder.meteo.setImageResource(getResourceID("we_"+img_url,"drawable",context));

        Picasso.with(context).load(picture_url).into(publicationViewHolder.picture);

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

        publicationViewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               F1 f1 =  F1.newInstance(currentItem);
                f1.show(personnal_posts_frag.getFragmentManager(),null);
               //  f1.getDialog().getWindow().setLayout(200,200);
            }});
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
        public ImageView picture;
        public Button cancel;
        public Button comment;

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
            picture = itemView.findViewById(R.id.picture);
            comment = itemView.findViewById(R.id.btn_add_comment);
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
    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }



    public static class F1 extends DialogFragment {

        static PublicationItem pub ;
        public static F1 newInstance(PublicationItem pub) {
            F1.pub= pub;
            F1 f1 = new F1();
            f1.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
            return f1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Remove the default background
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DisplayMetrics metrics = personnal_posts_frag.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().setLayout(200,200);
            // Inflate the new view with margins and background
            View v = inflater.inflate(R.layout.popup_layout, container, false);
            final TextView tv = v.findViewById(R.id.comments_title);
            final TextView tv_comment_text = v.findViewById(R.id.tv_comment_text);
            final ImageView add_comment = v.findViewById(R.id.new_comment);

            RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_comments);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(personnal_posts_frag.getActivity()));

            final ArrayList<CommentItem> mCommentsList = new ArrayList<>();
            final CustomAdapterV4 mCustomAdapter = new CustomAdapterV4(personnal_posts_frag.getActivity(), mCommentsList, null);
            mRecyclerView.setAdapter(mCustomAdapter);

            add_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tv_comment_text.getText().length()==0) return ;
                    String url =  getString(R.string.api)+"/CommentServlet";
                    final JSONObject json = new JSONObject();
                    final String msg =  tv_comment_text.getText().toString(); ;
                    try {

                        json.put("comment_text",tv_comment_text.getText().toString());
                        json.put("comment_user_id",PersonnalPostsFragment.token.getClaim("id").asString());
                        json.put("comment_id_pub",pub.getPub_id());
                        tv_comment_text.setText("");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if(response.get("message").toString().equals("sucess")){
                                        CommentItem ci = new CommentItem(
                                                PersonnalPostsFragment.token.getClaim("id").asInt(),
                                                PersonnalPostsFragment.token.getClaim("firstname").asString()+
                                                        " "+PersonnalPostsFragment.token.getClaim("lastname").asString(),
                                                "Added on : now",
                                                msg);
                                        mCommentsList.add(ci);
                                        mCustomAdapter.notifyDataSetChanged();
                                        tv.setText("Total number of comments : "+mCommentsList.size());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Toast.makeText(personnal_posts_frag.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                    QueueSingleton.getInstance(personnal_posts_frag.getActivity()).addToRequestQueue(jsonObjectRequest);
                }
            });


            String url = getString(R.string.api)+"/GetComments";
            final JSONObject json = new JSONObject();

            try {

                json.put("pub_id",pub.getPub_id());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray ja = new JSONArray();
            ja.put(json);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, ja, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {


                    Toast.makeText( personnal_posts_frag.getActivity(), "comments :"+response.length(), Toast.LENGTH_SHORT).show();
                    for(int i =0;i< response.length();i++){
                        try {
                            JSONObject comment = response.getJSONObject(i);
                            CommentItem c = new CommentItem(
                                    comment.getInt("comment_id_pub"),
                                    comment.get("comment_user_name").toString(),
                                    comment.get("comment_created_date").toString(),
                                    comment.get("comment_text").toString());

                            if( ! mCommentsList.contains(c) )
                                mCommentsList.add(c);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    mCustomAdapter.notifyDataSetChanged();
                    tv.setText("Total number of comments : "+mCommentsList.size());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }
            );

            QueueSingleton.getInstance(personnal_posts_frag.getActivity()).addToRequestQueue(jsonArrayRequest);

            /* mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );
            mCommentsList.add(new CommentItem(1,"Salim","15/11/2018", "hey there") );


            mCustomAdapter.notifyDataSetChanged();  */


            // Set up a click listener to dismiss the popup if they click outside
            // of the background view
            v.findViewById(R.id.popup_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return v;
        }
    }




}
