package com.mediax.mediaxapp.adapter;

/**
 * Created by Mayank on 28/04/2016.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.Dashboard;
import com.mediax.mediaxapp.utils.LoginUtils;


/**
 * Created by Mayank on 06/03/2016.
 */

public class SideMenuListAdapter extends RecyclerView.Adapter<SideMenuListAdapter.ViewHolder> {

    String[] sideMenuArray;
    int[] sideMenuImages = {R.drawable.ic_drawer_home, R.drawable.icon_chat_drawer, R.drawable.icon_database_drawer, R.drawable.icon_news_drawer, R.drawable.icon_jobs_drawer, R.drawable.icon_feedback_drawer, R.drawable.icon_contact_drawer, R.drawable.icon_settings_drawer, R.drawable.icon_logout_drawer};
    Activity currentActivty;

    public SideMenuListAdapter(Activity currentActivty) {
        this.currentActivty = currentActivty;
        sideMenuArray = currentActivty.getResources().getStringArray(R.array.sideBarItems);

    }

    @Override
    public SideMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivty).inflate(R.layout.items_side_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SideMenuListAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textSideBarItem.setText(sideMenuArray[pos]);
        if (sideMenuArray[pos].equals("Logout")) {
            if (!LoginUtils.isLogin(currentActivty)) {
                holder.textSideBarItem.setText("Login");
            }
        }
        holder.imageSideBarItem.setImageDrawable(currentActivty.getResources().getDrawable(sideMenuImages[pos]));
        holder.containerSideMenuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Dashboard) currentActivty).setSelection(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sideMenuImages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSideBarItem;
        TextView textSideBarItem;
        LinearLayout containerSideMenuItems;

        public ViewHolder(View itemView) {
            super(itemView);
            imageSideBarItem = (ImageView) itemView.findViewById(R.id.imageSideBarItem);
            textSideBarItem = (TextView) itemView.findViewById(R.id.textSideBarItem);
            containerSideMenuItems = (LinearLayout) itemView.findViewById(R.id.containerSideMenuItems);
        }
    }
}