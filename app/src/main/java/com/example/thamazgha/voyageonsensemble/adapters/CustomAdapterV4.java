package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.tools.CommentItem;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;

import java.util.ArrayList;

public class CustomAdapterV4 extends RecyclerView.Adapter<CustomAdapterV4.CommentViewHolder>{


    private Context context;
    private ArrayList<CommentItem> commentsList;

    public CustomAdapterV4(Context context, ArrayList<CommentItem> commentsList, Fragment personnal_posts_frag) {

        this.context = context;
        this.commentsList = commentsList;

    }

    @NonNull
    @Override
    public CustomAdapterV4.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_item_view, parent, false);
        return new CustomAdapterV4.CommentViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return this.commentsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapterV4.CommentViewHolder commentViewHolder, final int position) {

        final CommentItem  current_comment = commentsList.get(position);
        commentViewHolder.username.setText(current_comment.getUserNameOwner());
        commentViewHolder.comment_date.setText(current_comment.getDate());
        commentViewHolder.comment_text.setText(current_comment.getContent());

    }






    public class CommentViewHolder extends RecyclerView.ViewHolder {

        public View top_bar;
        public View bottom_bar;
        public TextView user_img;
        public TextView username;
        public TextView comment_text;
        public TextView comment_date;
        public RelativeLayout parentLayaout;

        public CommentViewHolder(@NonNull View itemView) {

            super(itemView);
            user_img = itemView.findViewById(R.id.comment_user_img);
            username = itemView.findViewById(R.id.comment_username);;
            comment_text = itemView.findViewById(R.id.comment_text);;
            comment_date = itemView.findViewById(R.id.comment_date);;
            parentLayaout = itemView.findViewById(R.id.parent_layaout);
            top_bar = itemView.findViewById(R.id.comment_top_bar);
            bottom_bar = itemView.findViewById(R.id.comment_bottom_bar);

        }
    }
}
