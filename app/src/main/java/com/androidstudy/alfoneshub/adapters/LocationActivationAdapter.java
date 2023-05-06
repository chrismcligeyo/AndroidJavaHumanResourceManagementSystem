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
import com.androidstudy.alfoneshub.models.Location;


/**
 * Created by orimbo on 03/02/17.
 */

public class LocationActivationAdapter extends  RecyclerView.Adapter<LocationActivationAdapter.NoteVH>{
    Context context;
    List<Location> locationList;

    public LocationActivationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_activation_locations, parent, false);
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

        String location_name = String.valueOf(locationList.get(position).getName().toUpperCase());
        location_name = String.valueOf(location_name.charAt(0));

        holder.textViewLocationName.setText(String.valueOf(locationList.get(position).getName().toUpperCase()));
        holder.textViewDate.setText(String.valueOf(locationList.get(position).getCreated_at()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(location_name);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewLocationName, textViewDate;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewLocationName = (TextView) itemView.findViewById(R.id.text_view_location_name);
            textViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

