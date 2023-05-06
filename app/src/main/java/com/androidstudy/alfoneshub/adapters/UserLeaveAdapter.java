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
import com.androidstudy.alfoneshub.models.UserLeave;


/**
 * Created by orimbo on 03/02/17.
 */

public class UserLeaveAdapter extends  RecyclerView.Adapter<UserLeaveAdapter.NoteVH>{
    Context context;
    List<UserLeave> userLeaveList;

    public UserLeaveAdapter(Context context, List<UserLeave> userLeaveList) {
        this.context = context;
        this.userLeaveList = userLeaveList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_user_leave, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.mdcolor_400);
        int randomColor = getRandom(colorArray);
        Log.d("Color", String.valueOf(randomColor));

        //Extract customer first name
        String reason = String.valueOf(userLeaveList.get(position).getReasons().toUpperCase());
        reason = String.valueOf(reason.charAt(0));


        holder.textViewReason.setText(String.valueOf(userLeaveList.get(position).getReasons()));
        holder.textViewDateFrom.setText(String.valueOf(userLeaveList.get(position).getDate_from()));
        holder.textViewDateTo.setText(String.valueOf(userLeaveList.get(position).getDate_to()));

        String status = userLeaveList.get(position).getStatus();
        if(status.equalsIgnoreCase("1")){
            status = "APPROVED";
        }else if(status.equalsIgnoreCase("2")){
            status = "DECLINED";
        }else {
            status = "PENDING";
        }

        holder.textViewStatus.setText(status.toUpperCase());
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(reason);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return userLeaveList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewReason, textViewDateFrom, CustomerComment, textViewDateTo, textViewStatus;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewReason = (TextView) itemView.findViewById(R.id.text_view_reason);
            textViewDateFrom = (TextView) itemView.findViewById(R.id.text_view_date_from);
            textViewDateTo = (TextView) itemView.findViewById(R.id.text_view_date_to);
            textViewStatus = (TextView) itemView.findViewById(R.id.text_view_status);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

