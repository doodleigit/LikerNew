package com.liker.android.Group.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.App;
import com.liker.android.Group.model.SuggestedGroup;
import com.liker.android.Group.model.Header;
import com.liker.android.Group.model.ListItem;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Group.view.GroupListActivity;
import com.liker.android.Group.view.GroupPageActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListItem> items;
    private Context mContext;
    private ProgressDialog progressDialog;
    private GroupWebservice groupWebservice;
    private PrefManager manager;
    private String userId,token,deviceId;
    private String groupCategoryName;

    public DataAdapter(Context mContext,ArrayList<ListItem> items) {
        this.items = items;
        this.mContext=mContext;
        groupWebservice=GroupWebservice.retrofitBase.create(GroupWebservice.class);
        manager=new PrefManager(mContext);
        userId=manager.getProfileId();
        token=manager.getToken();
        deviceId=manager.getDeviceId();
        progressDialog = new ProgressDialog(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_info_header_layout, parent, false);
            return new VHHeader(v);
        } else if (viewType == ListItem.TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_info_item_layout, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            Header header = (Header) items.get(position);
            VHHeader VHheader = (VHHeader) holder;
            groupCategoryName=header.getName();
            VHheader.tvName.setText(header.getName());
            VHheader.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, GroupListActivity.class).putExtra("group_category_name",header.getName()));
                }
            });

        } else if (holder instanceof VHItem) {

            SuggestedGroup suggestedGroup = (SuggestedGroup) items.get(position);
            VHItem VHitem = (VHItem) holder;
            VHitem.tvItem.setText(suggestedGroup.getName());

            VHitem.image.setImageBitmap(null);
            String coverImage = AppConstants.USER_UPLOADED_IMAGES + suggestedGroup.getImageName();
            Picasso.with(VHitem.image.getContext()).load(coverImage).into(VHitem.image);

            String groupMember = suggestedGroup.totalMember.equals("0") ? "" :"Members: "+ Tools.getFormattedLikerCount(suggestedGroup.totalMember);
            String groupPosts = suggestedGroup.totalPost.equals("0") ? "" :  " | Posts: " +Tools.getFormattedLikerCount(suggestedGroup.totalPost);
            String allCountInfo =  groupMember + groupPosts;
            VHitem.tvGroupItemInfoCount.setText(allCountInfo);

            boolean isMember=Boolean.parseBoolean(suggestedGroup.isMember);
            if (isMember) {
                VHitem. imageGroupJoin.setImageResource(R.drawable.ic_group_joined_24dp);
                VHitem. tvGroupJoin.setText(mContext.getString(R.string.groupJoined));
            } else {
                VHitem. imageGroupJoin.setImageResource(R.drawable.ic_add_group_24dp);
                VHitem. tvGroupJoin.setText(mContext.getString(R.string.groupJoin));
            }

            holder.itemView.setTag(suggestedGroup);

            VHitem.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppSingleton.getInstance().setGroupId(suggestedGroup.groupId);
                    mContext.startActivity(new Intent(mContext, GroupPageActivity.class));
                }
            });

            VHitem.isMemberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setGroupJoinStatus(isMember,VHitem.imageGroupJoin,VHitem.tvGroupJoin,suggestedGroup.groupId);

                }
            });

            if("Group you manage".equalsIgnoreCase(groupCategoryName)){
                VHitem.isMemberLayout.setVisibility(View.GONE);
            }else {
                VHitem.isMemberLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    private void setGroupJoinStatus(boolean isMember, ImageView imageGroupJoin, TextView tvGroupJoin, String groupId) {
        if (isMember) {
            //          setUnFollow(profileUserId);
            setLeaveMember(imageGroupJoin,tvGroupJoin,groupId);
        } else {
            //   setFollow(profileUserId);
            setJoinMember(imageGroupJoin,tvGroupJoin,groupId);
        }
    }

    private void setJoinMember(ImageView imageGroupJoin, TextView tvGroupJoin, String groupId) {

        progressDialog.setMessage(mContext.getString(R.string.updating));
        progressDialog.show();
        Call<String> call = groupWebservice.joinMembers(deviceId,userId, token, userId, groupId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        //  isFollow = false;
                    //    isMember = true;
                        imageGroupJoin.setImageResource(R.drawable.ic_group_joined_24dp);
                        tvGroupJoin.setText(mContext.getString(R.string.groupJoined));
                        //tvFollow.setText(getString(R.string.follow));
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    private void setLeaveMember(ImageView imageGroupJoin, TextView tvGroupJoin, String groupId) {

        progressDialog.setMessage(mContext.getString(R.string.updating));
        progressDialog.show();
        Call<String> call = groupWebservice.leaveMembers(deviceId,userId, token, userId, groupId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        // isFollow = false;
                     //   isMember = false;
                        imageGroupJoin.setImageResource(R.drawable.ic_add_group_24dp);
                        tvGroupJoin.setText(mContext.getString(R.string.groupJoin));
                        //tvFollow.setText(getString(R.string.follow));
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView tvName,tvSeeAll;

        public VHHeader(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvSeeAll = (TextView) itemView.findViewById(R.id.tvSeeAll);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView tvItem, tvGroupItemInfoCount,tvGroupJoin;
        ImageView image,imageGroupJoin;
        ViewGroup isMemberLayout;

        public VHItem(View itemView) {
            super(itemView);
            this.tvItem =itemView.findViewById(R.id.tvItem);
            this.tvGroupItemInfoCount = itemView.findViewById(R.id.tvGroupItemInfoCount);
            this.tvGroupJoin = itemView.findViewById(R.id.tvGroupJoin);
            this.image = itemView.findViewById(R.id.image);
            this.imageGroupJoin = itemView.findViewById(R.id.imageGroupJoin);
            this.isMemberLayout =  itemView.findViewById(R.id.is_member_layout);
        }
    }
}