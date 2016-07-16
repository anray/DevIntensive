package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {

    @BindView(R.id.user_info_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.user_info_user_photo_iv)
    ImageView mProfileImage;

    @BindView(R.id.user_info_aboutMyself_et)
    EditText mBio;

    @BindView(R.id.user_info_rating_tv)
    TextView mRating;

    @BindView(R.id.user_info_projects_tv)
    TextView mProjects;

    @BindView(R.id.user_info_numOfCodeLines_tv)
    TextView mCodeLines;

    @BindView(R.id.repositories_list)
    ListView mRepoListView;

    @BindView(R.id.user_info_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.user_info_main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);



        setupToolbar();
        initProfileData();
    }

    private void setupToolbar(){

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initProfileData(){

        final UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();

        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this,repositories);

        mRepoListView.setAdapter(repositoriesAdapter);
        setListViewHeightBasedOnChildren(mRepoListView);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Snackbar.make(mCoordinatorLayout,"Репозиторий " + repositories.get(position), Snackbar.LENGTH_LONG).show();
                Intent viewRepo = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + userDTO.getRepositories().get(position)));
                startActivity(viewRepo);
            }
        });

        mBio.setText(userDTO.getBio());
        mRating.setText(userDTO.getRating());
        mCodeLines.setText(userDTO.getCodeLines());
        mProjects.setText(userDTO.getProjects());

        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .resize(768,512)
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mProfileImage);


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        listView.measure(0, 0);
        params.height = listView.getMeasuredHeight() * listAdapter.getCount() + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
