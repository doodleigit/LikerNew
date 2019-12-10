package com.liker.android.Group.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liker.android.Group.model.Header;
import com.liker.android.Group.model.ListItem;
import com.liker.android.Group.model.Person;
import com.liker.android.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListItem> items;

    public DataAdapter(ArrayList<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return  new VHHeader(v);
        } else if(viewType == ListItem.TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHHeader) {
            Header header = (Header) items.get(position);
            VHHeader VHheader = (VHHeader)holder;
            VHheader.tvName.setText(header.getName());
        } else if(holder instanceof VHItem) {
            Person person = (Person) items.get(position);
            VHItem VHitem = (VHItem)holder;
            VHitem.tvItem.setText(person.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    class VHHeader extends RecyclerView.ViewHolder{
        TextView tvName;
        public VHHeader(View itemView) {
            super(itemView);
            this.tvName = (TextView)itemView.findViewById(R.id.tvName);
        }
    }

    class VHItem extends RecyclerView.ViewHolder{
        TextView tvItem;
        public VHItem(View itemView) {
            super(itemView);
            this.tvItem = (TextView)itemView.findViewById(R.id.tvItem);
        }
    }
}