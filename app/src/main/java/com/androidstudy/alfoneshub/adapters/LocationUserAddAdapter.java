package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.LocationUserAdd;


/**
 * Created by orimbo on 03/02/17.
 */

public class LocationUserAddAdapter extends  RecyclerView.Adapter<LocationUserAddAdapter.NoteVH>{
    Context context;
    List<LocationUserAdd> locationUserAddList;

    public LocationUserAddAdapter(Context context, List<LocationUserAdd> locationUserAddList) {
        this.context = context;
        this.locationUserAddList = locationUserAddList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_activation_user_add_location, parent, false);
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

        String location_name = String.valueOf(locationUserAddList.get(position).getName().toUpperCase());
        location_name = String.valueOf(location_name.charAt(0));

        holder.textViewUserName.setText(String.valueOf(locationUserAddList.get(position).getName().toUpperCase()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(location_name);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return locationUserAddList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewUserName;
        TextView textViewProfile;
        CheckBox checkBoxAddUser;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewUserName = (TextView) itemView.findViewById(R.id.text_view_user_name);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

            checkBoxAddUser = (CheckBox) itemView.findViewById(R.id.checkbox_add_user);
            checkBoxAddUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    LocationUserAdd locationUserAdd = locationUserAddList.get(getAdapterPosition());
                    if(isChecked){
                        locationUserAdd.setSelected("1");
                    }else {
                        locationUserAdd.setSelected("0");
                    }

                }
            });

        }

    }

}

