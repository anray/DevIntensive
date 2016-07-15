package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anray on 14.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<UserListRes.UserData> mUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener customClickListener) {
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
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {

        UserListRes.UserData user = mUsers.get(position);

        String photo = user.getPublicInfo().getPhoto();
        if (!photo.isEmpty()) {
            Picasso.with(mContext)
                    .load(photo)
                    .resize(768,512)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                    .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                    .into(holder.mUserImage);
        }
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRaiting()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        String bio = user.getPublicInfo().getBio();
        if (bio == null || bio.isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
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

            mShowMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                mListener.onUserItemClickListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener{

            void onUserItemClickListener (int position);
        }
    }

}
