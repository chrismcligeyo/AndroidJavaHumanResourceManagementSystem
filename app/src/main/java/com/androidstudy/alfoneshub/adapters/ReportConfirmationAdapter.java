package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.ReportConfirmation;

import java.util.List;


/**
 * Created by orimbo on 03/02/17.
 */

public class ReportConfirmationAdapter extends  RecyclerView.Adapter<ReportConfirmationAdapter.NoteVH>{
    Context context;
    List<ReportConfirmation> reportConfirmationList;

    public ReportConfirmationAdapter(Context context, List<ReportConfirmation> reportConfirmationList) {
        this.context = context;
        this.reportConfirmationList = reportConfirmationList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_report_confirmation, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        holder.textViewDate.setText(String.valueOf(reportConfirmationList.get(position).getDate().toUpperCase()));
        holder.textViewTeamLeader.setText(String.valueOf(reportConfirmationList.get(position).getTeam_leader()));


    }


    @Override
    public int getItemCount() {
        return reportConfirmationList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewTeamLeader;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewDate = (TextView) itemView.findViewById(R.id.txt_date);
            textViewTeamLeader = (TextView) itemView.findViewById(R.id.txt_team_leader);


        }

    }

}

