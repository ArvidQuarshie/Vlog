package com.amusoft.vlog.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amusoft.vlog.Activities.ViewSingleVlog;
import com.amusoft.vlog.Constants;
import com.amusoft.vlog.Objects.Vlog;
import com.amusoft.vlog.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sophiebot on 11/19/16.
 */

public class VlogItemRecyclerAdapter extends RecyclerView.Adapter<VlogItemRecyclerAdapter.CustomViewHolder> {
    private List<Vlog> feedItemList;
    private Context mContext;
    private DatabaseReference mFirebaseReference;


    public VlogItemRecyclerAdapter(Context context, List<Vlog> feedItemList, DatabaseReference mFirebaseReference) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.mFirebaseReference = mFirebaseReference;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vlog_item, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Vlog feedItem = feedItemList.get(i);


        customViewHolder.tool.setTitle(feedItem.getTitle());
        customViewHolder.tool.setTitleTextColor(mContext.getResources().getColor(R.color.pure_white));
        customViewHolder.tool.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        customViewHolder.tool.inflateMenu(R.menu.menu_post_item);
        if (customViewHolder.tvTitle != null) {
            customViewHolder.tvTitle.setText(feedItem.getTitle());
        }

        if (customViewHolder.tvUploader != null) {
            customViewHolder.tvUploader.setText(feedItem.getUploader());
        }

        if (customViewHolder.tvViews != null) {
            customViewHolder.tvViews.setText(feedItem.getViews() + "Views");
        }
        if (customViewHolder.imPhoto != null) {

//                Bitmap v=retriveVideoFrameFromVideo(feedItem.getPath());

            Picasso.with(mContext).load(feedItem.getPath()).into(customViewHolder.imPhoto);


        }


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getPosition();

                int views = Integer.parseInt(feedItemList.get(position).getViews());
                views++;
                String setviews = String.valueOf(views);

                mFirebaseReference.child(feedItemList.get(position).getFirekey()).
                        child(Constants.firebase_reference_video_views).setValue(setviews);

                Intent xbrew = new Intent(mContext, ViewSingleVlog.class);
                xbrew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                xbrew.putExtra(Constants.extras_firekeyreference,
                        feedItemList.get(position).getFirekey().toString());


                mContext.startActivity(xbrew);


            }
        };
        //Handle click event on both title and image click
        customViewHolder.tvTitle.setOnClickListener(clickListener);
        customViewHolder.tvUploader.setOnClickListener(clickListener);
        customViewHolder.tvViews.setOnClickListener(clickListener);
        customViewHolder.imPhoto.setOnClickListener(clickListener);
//        customViewHolder.t.setOnClickListener(clickListener);

        customViewHolder.tvTitle.setTag(customViewHolder);
        customViewHolder.tvUploader.setTag(customViewHolder);
        customViewHolder.tvViews.setTag(customViewHolder);
        customViewHolder.imPhoto.setTag(customViewHolder);
        setAnimation(customViewHolder, i);


    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(CustomViewHolder viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        int lastPosition = position - 1;
        if (position > lastPosition) {
            final Animation fade = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            viewToAnimate.t.setAnimation(fade);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
//        return (null != feedItemList ? feedItemList.size() : 0);
        if (feedItemList != null && feedItemList.size() != 0) ;
        return feedItemList.size();

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle;
        protected TextView tvUploader;
        protected TextView tvViews;
        protected ImageView imPhoto;
        protected Toolbar tool;
        protected CardView t;

        public CustomViewHolder(View v) {
            super(v);
            this.tvTitle = (TextView) v.findViewById(R.id.memTitle);
            this.tvUploader = (TextView) v.findViewById(R.id.memuploader);
            this.tvViews = (TextView) v.findViewById(R.id.memViews);
            this.imPhoto = (ImageView) v.findViewById(R.id.vlogvideo);
            this.tool = (Toolbar) v.findViewById(R.id.toolbarvlogItem);
            this.t = (CardView) v.findViewById(R.id.toAnimate);


        }
    }

}