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
import com.androidstudy.alfoneshub.models.Payslip;


/**
 * Created by orimbo on 03/02/17.
 */

public class PayslipAdapter extends  RecyclerView.Adapter<PayslipAdapter.NoteVH>{
    Context context;
    List<Payslip> payslipList;

    public PayslipAdapter(Context context, List<Payslip> payslipList) {
        this.context = context;
        this.payslipList = payslipList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_payslips, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.mdcolor_400);
        int randomColor = getRandom(colorArray);
        Log.d("Color", String.valueOf(randomColor));


        holder.textViewPayslipNo.setText(String.valueOf("Payslip #" + payslipList.get(position).getPayslip_id()));
        holder.textViewNetPay.setText(String.valueOf(payslipList.get(position).getNet_pay()));

        String month = payslipList.get(position).getMonth();

        if(month.equalsIgnoreCase("1")){

            month = "JANUARY";

        }else if(month.equalsIgnoreCase("2")){

            month = "FEBRUARY";

        }else if(month.equalsIgnoreCase("3")){

            month = "MARCH";

        }else if(month.equalsIgnoreCase("4")){

            month = "APRIL";

        }else if(month.equalsIgnoreCase("5")){

            month = "MAY";

        }else if(month.equalsIgnoreCase("6")){

            month = "JUNE";

        }else if(month.equalsIgnoreCase("7")){

            month = "JULY";

        }else if(month.equalsIgnoreCase("8")){

            month = "AUGUST";

        }else if(month.equalsIgnoreCase("9")){

            month = "SEPTEMBER";

        }else if(month.equalsIgnoreCase("10")){

            month = "OCTOBER";

        }else if(month.equalsIgnoreCase("11")){

            month = "NOVEMBER";

        }else if(month.equalsIgnoreCase("12")){

            month = "DECEMBER";

        }
        String year = payslipList.get(position).getYear();
        holder.textViewDate.setText(month + "/" + year);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return payslipList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView textViewPayslipNo, textViewNetPay, textViewDeductions, textViewDate;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */

            textViewPayslipNo = (TextView) itemView.findViewById(R.id.text_view_payslip_no);
            textViewNetPay = (TextView) itemView.findViewById(R.id.text_view_net_pay);
            textViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);

        }

    }

}

