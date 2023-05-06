package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.CheckInHistory;

import java.util.List;


/**
 * Created by orimbo on 03/02/17.
 */

public class CheckInHistoryAdapter extends  RecyclerView.Adapter<CheckInHistoryAdapter.NoteVH>{
    Context context;
    List<CheckInHistory> checkInHistoryList;

    public CheckInHistoryAdapter(Context context, List<CheckInHistory> checkInHistoryList) {
        this.context = context;
        this.checkInHistoryList = checkInHistoryList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_check_in_history, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        holder.textViewUserName.setText(String.valueOf(checkInHistoryList.get(position).getUser_name().toUpperCase()));

        String check_in = checkInHistoryList.get(position).getCheck_in();
        if(check_in.equalsIgnoreCase("1")){
            holder.textViewCheckIn.setText("YES");
            holder.linearLayoutCheckIn.setVisibility(View.GONE);
        }else{
            holder.textViewCheckIn.setText("NO");
            holder.linearLayoutCheckIn.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return checkInHistoryList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewUserName, textViewCheckIn;
        CheckBox checkBoxCheckIn;
        LinearLayout linearLayoutCheckIn;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewUserName = (TextView) itemView.findViewById(R.id.text_view_user_name);
            textViewCheckIn = (TextView) itemView.findViewById(R.id.text_view_check_in);
            checkBoxCheckIn = (CheckBox) itemView.findViewById(R.id.checkbox_check_in);
            linearLayoutCheckIn = (LinearLayout) itemView.findViewById(R.id.linear_layout_check_in);

            checkBoxCheckIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    CheckInHistory checkInHistory = checkInHistoryList.get(getAdapterPosition());
                    if(isChecked){
                        checkInHistory.setCheck_in("1");
                    }else {
                        checkInHistory.setCheck_in("0");
                    }

                }
            });


        }

    }

}

