package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

/**
 * Created by anray on 15.07.2016.
 */
public class RepositoriesAdapter extends BaseAdapter {
    private List<String> mRepositoriesList;
    private Context mContext;
    private LayoutInflater mInflater;

    public RepositoriesAdapter(Context context, List<String> repositoriesList) {
        mRepositoriesList = repositoriesList;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mRepositoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRepositoriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;

        if (itemView == null){

            itemView = mInflater.inflate(R.layout.item_repositories_list,parent,false);
        }

        TextView repoName  = (TextView) itemView.findViewById(R.id.github_et);
        repoName.setText(mRepositoriesList.get(position));

        return itemView;
    }
}
