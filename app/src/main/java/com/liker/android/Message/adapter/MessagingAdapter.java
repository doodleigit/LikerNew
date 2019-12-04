package com.liker.android.Message.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Message.model.Messages;
import com.liker.android.Message.model.User;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.Tools;
//import com.doodle.App;
//import com.doodle.Message.model.Messages;
//import com.doodle.Message.model.User;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Messages> arrayList;
    private ArrayList<User> users;
    private String userId;
    private int LEFT_CHAT = 0, RIGHT_CHAT = 1, seenPosition = -1;

    public MessagingAdapter(Context context, ArrayList<Messages> arrayList, ArrayList<User> users, String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.users = users;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == LEFT_CHAT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_left, viewGroup, false);
            return new LeftChatViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_right, viewGroup, false);
            return new RightChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        String userId, userImage = "", message, messageTime, seen;
        userId = arrayList.get(i).getFromUserId();
        message = arrayList.get(i).getContent().trim();
        messageTime = arrayList.get(i).getTimePosted();
        seen = arrayList.get(i).getSeen();

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                userImage = user.getPhoto();
                break;
            }
        }

        if (holder instanceof LeftChatViewHolder) {
            LeftChatViewHolder viewHolder = (LeftChatViewHolder) holder;

            viewHolder.tvMessage.setText(message);
            Linkify.addLinks(viewHolder.tvMessage, Linkify.ALL);
            viewHolder.tvMessageTime.setText(Tools.chatDateCompare(context, Long.valueOf(messageTime)));

            Glide.with(App.getAppContext())
                    .load(AppConstants.PROFILE_IMAGE + userImage)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.ivProfileImage);

            if (seenPosition == i) {
                viewHolder.tvSeenStatus.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvSeenStatus.setVisibility(View.GONE);
            }

            viewHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = seenPosition;
                    if (seenPosition == i) {
                        seenPosition = -1;
                    } else {
                        seenPosition = i;
                    }
                    notifyItemChanged(pos);
                    notifyItemChanged(seenPosition);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = seenPosition;
                    if (seenPosition == i) {
                        seenPosition = -1;
                    } else {
                        seenPosition = i;
                    }
                    notifyItemChanged(pos);
                    notifyItemChanged(seenPosition);
                }
            });
        } else {
            RightChatViewHolder viewHolder = (RightChatViewHolder) holder;

            viewHolder.tvMessage.setText(message);
            Linkify.addLinks(viewHolder.tvMessage, Linkify.ALL);
            viewHolder.tvMessageTime.setText(Tools.chatDateCompare(context, Long.valueOf(messageTime)));

            Glide.with(App.getAppContext())
                    .load(AppConstants.PROFILE_IMAGE + userImage)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.ivProfileImage);

            if ((arrayList.size() - 1) == i) {
                viewHolder.ivSeen.setVisibility(View.VISIBLE);
                if (seen.equals("1")) {
                    Glide.with(App.getAppContext())
                            .load(AppConstants.PROFILE_IMAGE + userImage)
                            .placeholder(R.drawable.ic_sent_24dp)
                            .error(R.drawable.profile)
                            .centerCrop()
                            .dontAnimate()
                            .into(viewHolder.ivSeen);
                } else {
                    viewHolder.ivSeen.setImageResource(R.drawable.ic_sent_24dp);
                }
            } else {
                viewHolder.ivSeen.setVisibility(View.GONE);
            }

            if (seenPosition == i) {
                viewHolder.tvSeenStatus.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvSeenStatus.setVisibility(View.GONE);
            }

            viewHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = seenPosition;
                    if (seenPosition == i) {
                        seenPosition = -1;
                    } else {
                        seenPosition = i;
                    }
                    notifyItemChanged(pos);
                    notifyItemChanged(seenPosition);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = seenPosition;
                    if (seenPosition == i) {
                        seenPosition = -1;
                    } else {
                        seenPosition = i;
                    }
                    notifyItemChanged(pos);
                    notifyItemChanged(seenPosition);
                }
            });
        }

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy hh:mm a", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getFromUserId().equals(userId)) {
            return RIGHT_CHAT;
        } else {
            return LEFT_CHAT;
        }
    }

    public class RightChatViewHolder extends RecyclerView.ViewHolder {

        FrameLayout layout;
        ImageView ivProfileImage, ivSeen;
        TextView tvMessage, tvMessageTime, tvSeenStatus;

        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            ivProfileImage = itemView.findViewById(R.id.profile_image);
            ivSeen = itemView.findViewById(R.id.seen);
            tvMessage = itemView.findViewById(R.id.message);
            tvMessageTime = itemView.findViewById(R.id.message_time);
            tvSeenStatus = itemView.findViewById(R.id.seen_status);
        }
    }

    public class LeftChatViewHolder extends RecyclerView.ViewHolder {

        FrameLayout layout;
        ImageView ivProfileImage;
        TextView tvMessage, tvMessageTime, tvSeenStatus;

        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            ivProfileImage = itemView.findViewById(R.id.profile_image);
            tvMessage = itemView.findViewById(R.id.message);
            tvMessageTime = itemView.findViewById(R.id.message_time);
            tvSeenStatus = itemView.findViewById(R.id.seen_status);
        }
    }

    public void addPagingData(ArrayList<Messages> messages) {
        arrayList.addAll(messages);
        notifyDataSetChanged();
    }

    public void seenStatusChange(ImageView ivSeen, String photo) {
        if (arrayList.size() > 0) {
            if (arrayList.get(arrayList.size() - 1).getFromUserId().equals(userId)) {
                ivSeen.setVisibility(View.VISIBLE);
                if (arrayList.get(arrayList.size() - 1).getSeen().equals("1")) {
                    Glide.with(App.getAppContext())
                            .load(photo)
                            .placeholder(R.drawable.ic_sent_24dp)
                            .error(R.drawable.profile)
                            .centerCrop()
                            .dontAnimate()
                            .into(ivSeen);
                } else {
                    ivSeen.setImageResource(R.drawable.ic_sent_24dp);
                }
            } else {
                ivSeen.setVisibility(View.GONE);
            }
        } else {
            ivSeen.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

}
