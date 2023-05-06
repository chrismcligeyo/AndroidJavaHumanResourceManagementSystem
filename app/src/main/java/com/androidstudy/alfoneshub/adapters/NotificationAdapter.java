package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.Notification;


/**
 * Created by orimbo on 03/02/17.
 */

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.NoteVH>{
    Context context;
    List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_notification, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        String read_status = notificationList.get(position).getRead();
        if(read_status.equals("0")){
            holder.textViewRead.setText("UNREAD");
            holder.textViewRead.setTextColor(Color.parseColor("#d9450b"));
        }else{
            holder.textViewRead.setText("READ");
        }

        holder.textViewDate.setText(String.valueOf(notificationList.get(position).getDate().toUpperCase()));
        holder.textViewMessage.setText(String.valueOf(notificationList.get(position).getMessage()));

    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewMessage, textViewRead;;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewDate = (TextView) itemView.findViewById(R.id.txt_date);
            textViewMessage = (TextView) itemView.findViewById(R.id.txt_message);
            textViewRead = (TextView) itemView.findViewById(R.id.txt_read);
        }

    }

}

