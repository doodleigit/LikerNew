package com.liker.android.Search.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.liker.android.R;
import com.liker.android.Search.adapter.ViewPagerAdapter;
import com.liker.android.Search.model.AdvanceSearches;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.Tools;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewpager;

    private AdvanceSearches advanceSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        advanceSearches = getIntent().getExtras().getParcelable("ADVANCE-SEARCH");

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.advance_search));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("search_result", advanceSearches);
        Fragment fragment = new SearchAllFragment();
        fragment.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(fragment, getString(R.string.all));
        adapter.addFrag(new PostSearchFragment(), getString(R.string.posts));
        adapter.addFrag(new PeopleSearchFragment(), getString(R.string.people));

        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Tools.setPageTraffic(this, AppConstants.ADVANCE_SEARCH_PAGE_NUMBER); //For page traffic analytics
    }
}
