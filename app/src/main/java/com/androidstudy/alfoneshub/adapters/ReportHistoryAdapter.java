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
import com.androidstudy.alfoneshub.models.ReportHistory;


/**
 * Created by orimbo on 03/02/17.
 */

public class ReportHistoryAdapter extends  RecyclerView.Adapter<ReportHistoryAdapter.NoteVH>{
    Context context;
    List<ReportHistory> checkBoardEntries;

    public ReportHistoryAdapter(Context context, List<ReportHistory> checkBoardEntries) {
        this.context = context;
        this.checkBoardEntries = checkBoardEntries;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_report_history, parent, false);
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

        //Extract customer first name
        String customer_name = String.valueOf(checkBoardEntries.get(position).getCustomer_name().toUpperCase());
        customer_name = String.valueOf(customer_name.charAt(0));


        holder.CustomerName.setText(String.valueOf(checkBoardEntries.get(position).getCustomer_name().toUpperCase()));
        holder.CustomerPhone.setText(String.valueOf(checkBoardEntries.get(position).getCustomer_phone()));
        holder.CampaignDate.setText(String.valueOf(checkBoardEntries.get(position).getCreated_at()));
        holder.BAName.setText(String.valueOf(checkBoardEntries.get(position).getUser_name().toUpperCase()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(customer_name);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return checkBoardEntries.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView CustomerName, CustomerPhone, CustomerComment, CampaignDate, BAName;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            CustomerName = (TextView) itemView.findViewById(R.id.txtReportInteractionEntriesCustomerName);
            CustomerPhone = (TextView) itemView.findViewById(R.id.txtReportInteractionEntriesCustomerPhone);
            CampaignDate = (TextView) itemView.findViewById(R.id.txtReportInteractionEntriesConductionDate);
            BAName = (TextView) itemView.findViewById(R.id.txtReportInteractionEntriesBAName);
            BAName = (TextView) itemView.findViewById(R.id.txtReportInteractionEntriesBAName);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

