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
import com.androidstudy.alfoneshub.models.Expenses;


/**
 * Created by orimbo on 03/02/17.
 */

public class ExpensesAdapter extends  RecyclerView.Adapter<ExpensesAdapter.NoteVH>{
    Context context;
    List<Expenses> expensesList;

    public ExpensesAdapter(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_activation_expenses, parent, false);
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

        String expenses_name = String.valueOf(expensesList.get(position).getRequisition_title().toUpperCase());
        expenses_name = String.valueOf(expenses_name.charAt(0));

        holder.textViewRequisitionTitle.setText(String.valueOf(expensesList.get(position).getRequisition_title().toUpperCase()));
        holder.textViewAmount.setText(String.valueOf(expensesList.get(position).getAmount()));
        holder.textViewDate.setText(String.valueOf(expensesList.get(position).getCreated_at().toUpperCase()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(expenses_name);

        String check_in = expensesList.get(position).getRequisition_status();

        if(check_in.equalsIgnoreCase("1")){
            holder.textViewStatus.setText("APPROVED");
        }else if(check_in.equalsIgnoreCase("2")){
            holder.textViewStatus.setText("DECLINE");
        }else{
            holder.textViewStatus.setText("PENDING");
        }

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewRequisitionTitle, textViewAmount, textViewStatus, textViewDate;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewRequisitionTitle = (TextView) itemView.findViewById(R.id.text_view_requisition_title);
            textViewAmount = (TextView) itemView.findViewById(R.id.text_view_amount);
            textViewStatus = (TextView) itemView.findViewById(R.id.text_view_status);
            textViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

