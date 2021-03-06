package com.liker.android.Group.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
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

import com.liker.android.Authentication.model.CountryInfo;
import com.liker.android.Group.adapter.GroupCategoryAdapter;
import com.liker.android.Group.adapter.DataAdapter;
import com.liker.android.Group.adapter.RecyclerViewWithNavigationArrows;
import com.liker.android.Group.model.App;
import com.liker.android.Group.model.SuggestedCategory;
import com.liker.android.Group.model.SuggestedGroup;
import com.liker.android.Group.model.GroupContent;
import com.liker.android.Group.model.GroupContentData;
import com.liker.android.Group.model.GroupManage;
import com.liker.android.Group.model.GroupYouIn;
import com.liker.android.Group.model.Header;
import com.liker.android.Group.model.ListItem;
import com.liker.android.Group.model.Message;
import com.liker.android.Group.model.Success;
import com.liker.android.Group.service.GroupContentService;
import com.liker.android.R;
import com.liker.android.Search.LikerSearch;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupContentActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "GroupContentActivity";
    private Toolbar toolbar;
    private TextView tvCreateNewGroup, tvGroupName;
    private ImageView imageSearchGroup;

    private ArrayList<ListItem> items = new ArrayList<>();
    private DataAdapter adapter;
    private RecyclerView recyclerView;

    private List<App> appsList = new ArrayList<>();
    private RecyclerViewWithNavigationArrows rv1;
    private List<CountryInfo> countryInfos;
    private GroupContent groupContent;
    private GroupContentData groupContentData;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            groupContent = intent.getParcelableExtra(GroupContentService.GROUP_CONTENT_SERVICE_PAYLOAD);
            displayData();
        }
    };

    private void displayData() {
        if (groupContent != null) {
            boolean status = groupContent.isStatus();
            Message message = groupContent.getMessage();
            Success success = message.getSuccess();
            String successStatus = success.getMessage();
         //   Toast.makeText(this, successStatus, Toast.LENGTH_SHORT).show();
            if (status) {
                items.clear();
                groupContentData = groupContent.getData();
                readyGroupCategory(groupContentData);
                List<SuggestedGroup> suggestedGroups1 = new ArrayList<SuggestedGroup>();
                List<SuggestedGroup> suggestedGroups2 = new ArrayList<SuggestedGroup>();
                List<SuggestedGroup> suggestedGroups3 = new ArrayList<SuggestedGroup>();
                List<GroupYouIn> groupYouInData = new ArrayList<GroupYouIn>();
                List<GroupManage> groupManageData = new ArrayList<GroupManage>();


                if (groupContentData.getSuggestedGroupData().size() != 0) {
                    Header followerHeader = new Header("Suggested group");
                    items.add(followerHeader);
                    suggestedGroups1.addAll(groupContentData.getSuggestedGroupData());
                    if (suggestedGroups1.size() > 3) {
                        items.addAll(suggestedGroups1.subList(0, 3));
                    } else {
                        items.addAll(suggestedGroups1);
                    }

                }

                if (groupContentData.getGroupYouInData().size() != 0) {
                    Header groupYouInDataHeader = new Header("Group you're in");
                    items.add(groupYouInDataHeader);
                    groupYouInData.addAll(groupContentData.getGroupYouInData());
                    for (GroupYouIn item : groupYouInData) {

                        SuggestedGroup suggestedGroup = new SuggestedGroup();
                        suggestedGroup.name = item.getName();
                        suggestedGroup.imageName = item.getImageName();
                        suggestedGroup.totalMember = item.getTotalMember();
                        suggestedGroup.totalPost = item.getTotalPost();
                        suggestedGroup.groupId = item.getGroupId();
                        suggestedGroup.isMember = item.getIsMember();
                        suggestedGroups2.add(suggestedGroup);

                    }
                    if (suggestedGroups2.size() > 3) {
                        items.addAll(suggestedGroups2.subList(0, 3));
                    } else {
                        items.addAll(suggestedGroups2);
                    }
                }

                if (groupContentData.getGroupManageData().size() != 0) {
                    Header groupManageHeader = new Header("Group you manage");
                    items.add(groupManageHeader);
                    groupManageData.addAll(groupContentData.getGroupManageData());

                    for (GroupManage item : groupManageData) {
                        SuggestedGroup suggestedGroup = new SuggestedGroup();
                        suggestedGroup.name = item.getName();
                        suggestedGroup.imageName = item.getImageName();
                        suggestedGroup.totalMember = item.getTotalMember();
                        suggestedGroup.totalPost = item.getTotalPost();
                        suggestedGroup.groupId = item.getGroupId();
                        suggestedGroup.isMember = item.getIsMember();
                        suggestedGroups3.add(suggestedGroup);
                    }
                    if (suggestedGroups3.size() > 3) {
                        items.addAll(suggestedGroups3.subList(0, 3));
                    } else {
                        items.addAll(suggestedGroups3);
                    }
                }


   /*     items.add(new Header("Header 1"));
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
        items.add(new Person("Item 12"));*/
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_content);
        initComponent();
        readyGroupInfo();

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            Intent intent = new Intent(this, GroupContentService.class);
            startService(intent);
        } else {
            Toast.makeText(this, "no internet!", Toast.LENGTH_SHORT).show();
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver, new IntentFilter(GroupContentService.GROUP_CONTENT_SERVICE_MESSAGE));
        IntentFilter newPostFilter = new IntentFilter();
        newPostFilter.addAction(AppConstants.GROUP_CONTENT_UPDATE_BROADCAST);
        Objects.requireNonNull(this).registerReceiver(groupContentUpdateReceiver, newPostFilter);

    }
    BroadcastReceiver groupContentUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
                Intent intentGroupService = new Intent(GroupContentActivity.this, GroupContentService.class);
                startService(intentGroupService);
            } else {
                Toast.makeText(GroupContentActivity.this, "no internet!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void readyGroupInfo() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DataAdapter(this, items);
        recyclerView.setAdapter(adapter);

//GRID LAYOUT MANAGER
        GridLayoutManager gd = new GridLayoutManager(this, 3);

        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ListItem.TYPE_HEADER ? 3 : 1;
            }
        });
        recyclerView.setLayoutManager(gd);
    }

    private void readyGroupCategory(GroupContentData groupContentData) {
        List<SuggestedCategory> categoryList = groupContentData.getSuggestedCategoryData();
        if (categoryList.size() > 0) {
            tvGroupName.setVisibility(View.VISIBLE);
        } else {
            tvGroupName.setVisibility(View.GONE);
        }
        GroupCategoryAdapter groupCategoryAdapter = new GroupCategoryAdapter(getApplicationContext(), categoryList);
        rv1.setAdapter(groupCategoryAdapter);
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvCreateNewGroup = findViewById(R.id.tvCreateNewGroup);
        tvGroupName = findViewById(R.id.tvGroupName);
        tvCreateNewGroup.setOnClickListener(this);
        imageSearchGroup = findViewById(R.id.image_search_group);
        imageSearchGroup.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        rv1 = findViewById(R.id.recycler_view_one);
        //rv2=findViewById(R.id.recycler_view_two);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvCreateNewGroup:
              Intent groupCreateIntent=new Intent(this, GroupCreateActivity.class);
                groupCreateIntent.putExtra("group_id","");
                groupCreateIntent.putExtra("group_name","");
                startActivity(groupCreateIntent);
                break;
            case R.id.image_search_group:
                startActivity(new Intent(GroupContentActivity.this, LikerSearch.class));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(this).unregisterReceiver(groupContentUpdateReceiver);
    }
}
