package com.example.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bluetoothchat.R;

import java.util.List;

/**
 * Created by mohitattri on 4/13/17.
 */

public class UserDashboardAdapter extends RecyclerView.Adapter<UserDashboardAdapter.RecyclerViewHolders> {
    private Context context;
    private List<AppItems> itemList;

    public UserDashboardAdapter(Context context, List<AppItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_app, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.appName.setText(itemList.get(position).getAppName().toString());
        holder.appIcon.setImageResource(itemList.get(position).getAppIcon());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView appName;
        public ImageView appIcon;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            appName = (TextView)itemView.findViewById(R.id.app_name);
            appIcon = (ImageView)itemView.findViewById(R.id.app_icon);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
