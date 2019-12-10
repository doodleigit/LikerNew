package com.liker.android.Group.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.adapter.AppListAdapter;
import com.liker.android.Group.adapter.DataAdapter;
import com.liker.android.Group.adapter.RecyclerViewWithNavigationArrows;
import com.liker.android.Group.model.App;
import com.liker.android.Group.model.DummyDataHelper;
import com.liker.android.Group.model.Header;
import com.liker.android.Group.model.ListItem;
import com.liker.android.Group.model.Person;
import com.liker.android.Group.view.CreateGroupActivity;
import com.liker.android.R;

import java.util.ArrayList;
import java.util.List;

public class GroupContentActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView tvCreateNewGroup;
    private ImageView imageSearchGroup;

    private ArrayList<ListItem> items =  new ArrayList<>();
    private DataAdapter adapter;
    private RecyclerView recyclerView;

    private List<App> appsList = new ArrayList<>();
    private RecyclerViewWithNavigationArrows rv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCreateNewGroup = findViewById(R.id.tvCreateNewGroup);
        tvCreateNewGroup.setOnClickListener(this);
        imageSearchGroup = findViewById(R.id.image_search_group);
        imageSearchGroup.setOnClickListener(this);

        rv1 = findViewById(R.id.recycler_view_one);
        //rv2=findViewById(R.id.recycler_view_two);

        DummyDataHelper dummyDataHelper = new DummyDataHelper();
        appsList = dummyDataHelper.getAppList();
        AppListAdapter appListAdapter = new AppListAdapter(getApplicationContext(), appsList);
        rv1.setAdapter(appListAdapter);


        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DataAdapter(items);
        recyclerView.setAdapter(adapter);

//GRID LAYOUT MANAGER
        GridLayoutManager gd = new GridLayoutManager(this, 2);

        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ListItem.TYPE_HEADER ? 2 : 1;
            }
        });

        recyclerView.setLayoutManager(gd);

        items.add(new Header("Header 1"));
        items.add(new Person("Item 1"));
        items.add(new Person("Item 2"));
        items.add(new Person("Item 3"));
        items.add(new Person("Item 4"));
        items.add(new Header("Header 2"));
        items.add(new Person("Item 5"));
        items.add(new Person("Item 6"));
        items.add(new Person("Item 7"));
        items.add(new Person("Item 8"));
        items.add(new Header("Header 3"));
        items.add(new Person("Item 9"));
        items.add(new Person("Item 10"));
        items.add(new Person("Item 11"));
        items.add(new Person("Item 12"));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvCreateNewGroup:
                startActivity(new Intent(this, CreateGroupActivity.class));
                break;

            case R.id.image_search_group:
                Toast.makeText(this, "Search Group....", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
