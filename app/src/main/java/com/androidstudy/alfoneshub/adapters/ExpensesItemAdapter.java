package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.ExpensesItem;


/**
 * Created by orimbo on 03/02/17.
 */

public class ExpensesItemAdapter extends  RecyclerView.Adapter<ExpensesItemAdapter.NoteVH>{
    Context context;
    List<ExpensesItem> expensesItemList;

    public ExpensesItemAdapter(Context context, List<ExpensesItem> expensesItemList) {
        this.context = context;
        this.expensesItemList = expensesItemList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_activation_expenses_items, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        holder.textViewItemName.setText(String.valueOf(expensesItemList.get(position).getItem_name().toUpperCase()));
        holder.textViewAmount.setText(String.valueOf(expensesItemList.get(position).getItem_amount()));

    }


    @Override
    public int getItemCount() {
        return expensesItemList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewItemName, textViewAmount;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewItemName = (TextView) itemView.findViewById(R.id.text_view_item_name);
            textViewAmount = (TextView) itemView.findViewById(R.id.text_view_amount);
        }

    }

}

