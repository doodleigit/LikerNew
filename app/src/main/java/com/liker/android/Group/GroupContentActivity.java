package com.liker.android.Group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.adapter.AppListAdapter;
import com.liker.android.Group.adapter.RecyclerViewWithNavigationArrows;
import com.liker.android.Group.model.App;
import com.liker.android.Group.model.DummyDataHelper;
import com.liker.android.R;

import java.util.ArrayList;
import java.util.List;

public class GroupContentActivity extends AppCompatActivity implements View.OnClickListener {


   private Toolbar toolbar;
   private TextView tvCreateNewGroup;
   private ImageView imageSearchGroup;
    private List<App> appsList = new ArrayList<>();
    private RecyclerViewWithNavigationArrows rv1,rv2;
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
        tvCreateNewGroup=findViewById(R.id.tvCreateNewGroup);
        tvCreateNewGroup.setOnClickListener(this);
        imageSearchGroup=findViewById(R.id.image_search_group);
        imageSearchGroup.setOnClickListener(this);

        rv1=findViewById(R.id.recycler_view_one);
        rv2=findViewById(R.id.recycler_view_two);

        DummyDataHelper dummyDataHelper = new DummyDataHelper();
        appsList = dummyDataHelper.getAppList();
        AppListAdapter appListAdapter=new AppListAdapter(getApplicationContext(),appsList);
        rv1.setAdapter(appListAdapter);

        AppListAdapter appListAdapterTwo=new AppListAdapter(getApplicationContext(),appsList);
        rv2.setAdapter(appListAdapterTwo);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.tvCreateNewGroup:
                startActivity(new Intent(this,CreateGroupActivity.class));
                break;

            case R.id.image_search_group:
                Toast.makeText(this, "Search Group....", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
