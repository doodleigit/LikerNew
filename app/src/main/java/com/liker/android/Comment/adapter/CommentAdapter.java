package com.liker.android.Comment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.doodle.Comment.model.Comment_;
//import com.doodle.Comment.holder.CommentImageHolder;
//import com.doodle.Comment.holder.CommentLinkScriptHolder;
//import com.doodle.Comment.holder.CommentTextHolder;
//import com.doodle.Comment.holder.CommentYoutubeHolder;
//import com.doodle.Home.model.PostItem;
import com.liker.android.Comment.holder.CommentImageHolder;
import com.liker.android.Comment.holder.CommentLinkScriptHolder;
import com.liker.android.Comment.holder.CommentTextHolder;
import com.liker.android.Comment.holder.CommentYoutubeHolder;
import com.liker.android.Comment.model.Comment_;
import com.liker.android.Home.model.PostItem;
import com.liker.android.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;



    private List<Comment_> comment_list;
    private Context mContext;
    PostItem postItem;
//    private int size = 0;


    private CommentLinkScriptHolder.CommentListener commentLinkListener;
    private CommentTextHolder.CommentListener commentListener;
    private CommentYoutubeHolder.CommentListener commentYoutubeListener;
    private CommentImageHolder.CommentListener commentImageListener;
    private boolean isCommentMode;

    public CommentAdapter(Context context, List<Comment_> comment_list, PostItem postItem,
                          CommentTextHolder.CommentListener commentListener,
                          CommentLinkScriptHolder.CommentListener commentLinkListener,
                          CommentYoutubeHolder.CommentListener commentYoutubeListener,
                          CommentImageHolder.CommentListener commentImageListener,
                          boolean isCommentMode

    ) {
        this.mContext = context;
        this.comment_list = comment_list;
        this.postItem = postItem;
        this.commentListener = commentListener;
        this.commentLinkListener = commentLinkListener;
        this.commentYoutubeListener = commentYoutubeListener;
        this.commentImageListener = commentImageListener;
        this.isCommentMode = isCommentMode;
//        if (comment_list != null && !comment_list.isEmpty()) {
//            size = comment_list.size();
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_text, parent, false);
            return new CommentTextHolder(view, mContext, commentListener, isCommentMode);
        }


        if (viewType == VIEW_TYPE_TEXT_IMAGE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_image, parent, false);
            return new CommentImageHolder(view, mContext,commentImageListener, isCommentMode);
        }
        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_linkscript, parent, false);
            return new CommentLinkScriptHolder(view, mContext, commentLinkListener, isCommentMode);
        }

        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment_youtube, parent, false);
            return new CommentYoutubeHolder(view, mContext, commentYoutubeListener, isCommentMode);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommentTextHolder) {
            CommentTextHolder vh = (CommentTextHolder) viewHolder;
            vh.setItem(comment_list.get(position), postItem, position);
        }


        if (viewHolder instanceof CommentLinkScriptHolder) {
            CommentLinkScriptHolder vh = (CommentLinkScriptHolder) viewHolder;
            vh.setItem(comment_list.get(position), postItem, position);
        }
        if (viewHolder instanceof CommentYoutubeHolder) {
            CommentYoutubeHolder vh = (CommentYoutubeHolder) viewHolder;
            vh.setItem(comment_list.get(position), postItem, position);
        }
        if (viewHolder instanceof CommentImageHolder) {
            CommentImageHolder vh = (CommentImageHolder) viewHolder;
//            vh.setItem(postItems.get(position), comments.get(position));
            vh.setItem(comment_list.get(position), postItem,position);
        }

    }

    @Override
    public int getItemCount() {
        // return comment_list.size();
        return comment_list.size();
    }


    @Override
    public int getItemViewType(int position) {

        Log.d("size",comment_list.size()+"");
        Log.d("index",comment_list.indexOf(getItemCount())+"");

        String commentType = comment_list.get(position).getCommentType();

        int viewType = Integer.parseInt(commentType);
        switch (viewType) {
            case 1:
                return VIEW_TYPE_TEXT;
            case 2:
                return VIEW_TYPE_TEXT_IMAGE;
            case 3:
            return VIEW_TYPE_TEXT_IMAGE;
            case 4:
//                return VIEW_TYPE_TEXT_LINK_SCRIPT;
            return VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE;
            default:
                return -1;
        }
    }

/*    public void addPagingData(List<PostItem> postItemList,List<Comment> commentList) {

        for (PostItem temp : postItemList
        ) {
            comments.add(temp);
        }
        for (Comment temp : commentList
        ) {
            comments.add(temp);
        }
        notifyDataSetChanged();
    }*/

    public void addPagingData(List<Comment_> commentList) {

        for (Comment_ temp : commentList
        ) {
            comment_list.add(temp);
        }

        notifyDataSetChanged();
    }

    public void refreshData(Comment_ commentItem) {

        if (comment_list.size() > 0) {
            comment_list.add(0, commentItem);
//            size = comment_list.size();
            notifyDataSetChanged();
        } else {
            comment_list.add(commentItem);
//            size = comment_list.size();
            notifyDataSetChanged();
        }
    }

    public void updateData(Comment_ commentItem, int position) {

        if (comment_list.size() > 0) {
            comment_list.set(position, commentItem);
//            size = comment_list.size();
            notifyItemChanged(position);
        } else {
            comment_list.set(position, commentItem);
//            size = comment_list.size();
            notifyItemChanged(position);
        }
    }

    public interface CallBack {
        void myCommentCallBack();

    }

    public void deleteItem(int position) {


       // comment_list.remove(position);
       // comment_list.remove(position);

       // comment_list.remove(position);
     //   notifyItemRemoved(position);
//        size = comment_list.size();
        notifyItemRemoved(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
