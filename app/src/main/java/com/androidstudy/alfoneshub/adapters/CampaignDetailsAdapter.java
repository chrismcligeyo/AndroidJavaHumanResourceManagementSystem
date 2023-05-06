package com.androidstudy.alfoneshub.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.models.CampaignDetail;

import java.util.List;
import java.util.Random;


/**
 * Created by orimbo on 03/02/17.
 */

public class CampaignDetailsAdapter extends  RecyclerView.Adapter<CampaignDetailsAdapter.NoteVH>{
    Context context;
    List<CampaignDetail> campaignList;

    public CampaignDetailsAdapter(Context context, List<CampaignDetail> campaignList) {
        this.context = context;
        this.campaignList = campaignList;

    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_campaign_list, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.mdcolor_400);
        int randomColor = getRandom(colorArray);
        Log.d("Color", String.valueOf(randomColor));
        //Extract customer first name
        String campaign_name = String.valueOf(campaignList.get(position).getCampaign_name().toUpperCase());
        campaign_name = String.valueOf(campaign_name.charAt(0));

        /**
         * Settings Data from Sugar ORM to their respective TextViews % Image view in the Layout
         */
        holder.campaignName.setText(String.valueOf(campaignList.get(position).getCampaign_name().toUpperCase()));
        holder.campaignCompany.setText(String.valueOf(campaignList.get(position).getCampaign_company()));
        holder.campaignDate.setText(String.valueOf(campaignList.get(position).getCampaign_start_date()));
        holder.textViewProfile.setBackgroundColor(randomColor);
        holder.textViewProfile.setText(campaign_name);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public int getItemCount() {
        return campaignList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView campaignName, campaignCompany, campaignDate;
        TextView textViewProfile;
        public NoteVH(View itemView) {
            super(itemView);

            /**
             * Finding the views, and binding them with their ID's
             */
            campaignName = (TextView) itemView.findViewById(R.id.txtCampaignName);
            campaignCompany = (TextView) itemView.findViewById(R.id.textCampaignCompany);
            campaignDate = (TextView) itemView.findViewById(R.id.textCampaignDate);
            textViewProfile = (TextView) itemView.findViewById(R.id.text_view_profile);
        }

    }

}

