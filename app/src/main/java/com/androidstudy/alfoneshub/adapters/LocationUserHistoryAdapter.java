package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.LocationUserHistory;


/**
 * Created by orimbo on 03/02/17.
 */

public class LocationUserHistoryAdapter extends  RecyclerView.Adapter<LocationUserHistoryAdapter.NoteVH>{
    Context context;
    List<LocationUserHistory> locationUserHistoryList;

    public LocationUserHistoryAdapter(Context context, List<LocationUserHistory> locationUserHistoryList) {
        this.context = context;
        this.locationUserHistoryList = locationUserHistoryList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_location_user_report_history, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.mdcolor_400);
        int randomColor = getRandom(colorArray);
        Log.d("Color", String.valueOf(randomColor));

        /**
         * Settings Data from Sugar ORM to their respective TextViews % Image view in the Layout
         */

        String location_name = String.valueOf(locationUserHistoryList.get(position).getUser_name().toUpperCase());
        location_name = String.valueOf(location_name.charAt(0));

        holder.textViewUserName.setText(String.valueOf(locationUserHistoryList.get(position).getUser_name().toUpperCase()));
        holder.textViewInteractions.setText(String.valueOf(locationUserHistoryList.get(position).getInteraction()));
        holder.textViewSales.setText(String.valueOf(locationUserHistoryList.get(position).getSales().toUpperCase()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(location_name);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return locationUserHistoryList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewUserName, textViewInteractions, textViewSales;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewUserName = (TextView) itemView.findViewById(R.id.text_view_user_name);
            textViewInteractions = (TextView) itemView.findViewById(R.id.text_view_interactions);
            textViewSales = (TextView) itemView.findViewById(R.id.text_view_sales);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

