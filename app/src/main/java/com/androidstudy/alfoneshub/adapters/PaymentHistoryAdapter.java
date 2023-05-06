package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.PaymentHistory;

import java.util.List;


/**
 * Created by orimbo on 03/02/17.
 */

public class PaymentHistoryAdapter extends  RecyclerView.Adapter<PaymentHistoryAdapter.NoteVH>{
    Context context;
    List<PaymentHistory> paymentHistoryList;

    public PaymentHistoryAdapter(Context context, List<PaymentHistory> paymentHistoryList) {
        this.context = context;
        this.paymentHistoryList = paymentHistoryList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_payment_history, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        holder.textViewDate.setText(String.valueOf(paymentHistoryList.get(position).getDate().toUpperCase()));

        String amount = String.valueOf(paymentHistoryList.get(position).getAmount());
        amount = "KSH "+amount;

        holder.textViewAmount.setText(amount);

        String checked_in = String.valueOf(paymentHistoryList.get(position).getChecked_in());
        if (checked_in.equals("1")){
            checked_in = "YES";
        }else{
            checked_in = "NO";
        }

        holder.textViewCheckedIn.setText(checked_in);

        String confirmed = String.valueOf(paymentHistoryList.get(position).getConfirmed());
        if (confirmed.equals("1")){
            confirmed = "YES";
        }else{
            confirmed = "NO";
        }

        holder.textViewConfirmed.setText(confirmed);

    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewAmount, textViewCheckedIn, textViewConfirmed;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewDate = (TextView) itemView.findViewById(R.id.txt_date);
            textViewAmount = (TextView) itemView.findViewById(R.id.txt_amount);
            textViewCheckedIn = (TextView) itemView.findViewById(R.id.txt_checked_in);
            textViewConfirmed = (TextView) itemView.findViewById(R.id.txt_confirmed);

        }

    }

}

