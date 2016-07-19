package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;

import java.util.List;

/**
 * Created by anray on 14.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UsersAdapter";
    private List<User> mUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener customClickListener) {
        mUsers = users;
        this.mCustomClickListener = customClickListener;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);


        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.UserViewHolder holder, int position) {

        final User user = mUsers.get(position);
        final String userPhoto;
        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
            Log.e(TAG, "onBindViewHolder: user with name " + user.getFullName() + " has empty name");
        } else {
            userPhoto = user.getPhoto();
        }


        DataManager.getInstance().getPicasso()
                .load(userPhoto)
                .error(holder.mDummy)
                .placeholder(holder.mDummy)
                .resize(768, 512)
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.mUserImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, " load from cache successful");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(userPhoto)
                                .error(holder.mDummy)
                                .placeholder(holder.mDummy)
                                .resize(768, 512)
                                .centerCrop()
                                .into(holder.mUserImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, " was not able to load photo from network");

                                    }
                                });

                    }
                });

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRaiting()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));

        String bio = user.getBio();
        if (bio == null || bio.isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected AspectRatioImageView mUserImage;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        protected Drawable mDummy;

        private CustomClickListener mListener;


        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mListener = customClickListener;

            mUserImage = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_rv_cv);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_rv_tv);
            mRating = (TextView) itemView.findViewById(R.id.rating_rv_tv);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_rv_tv);
            mProjects = (TextView) itemView.findViewById(R.id.projects_rv_tv);
            mBio = (TextView) itemView.findViewById(R.id.bio_rv_tv);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_rv_btn);

            mDummy = mUserImage.getContext().getResources().getDrawable(R.drawable.user_bg);
            mShowMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener {

            void onUserItemClickListener(int position);
        }
    }

}
