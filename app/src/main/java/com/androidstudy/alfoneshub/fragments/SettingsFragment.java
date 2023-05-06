package com.androidstudy.alfoneshub.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidstudy.alfoneshub.LoginActivity;

import com.androidstudy.alfoneshub.R;
//import com.androidstudy.alfoneshub.models.DaoSession;
import com.androidstudy.alfoneshub.settings.AboutActivity;
import com.androidstudy.alfoneshub.settings.ActivationActivity;
import com.androidstudy.alfoneshub.checkin.CheckInActivity;
import com.androidstudy.alfoneshub.profile.ProfileActivity;
import com.androidstudy.alfoneshub.settings.ReportConfirmationActivity;
import com.androidstudy.alfoneshub.reports.ReportHistoryActivity;
import com.androidstudy.alfoneshub.settings.TeamLeaderActivity;
import com.androidstudy.alfoneshub.utils.AlfonesCommunication;
import com.androidstudy.alfoneshub.utils.Config;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by eugene on 7/12/17.
 */

public class SettingsFragment extends Fragment {
    TextView textViewProfile, textViewActivation, textViewTeamleader, textViewReportHistory,
            textViewReportConfirmation, textViewBACheckIn,textViewPaymentHistory,
            textViewInfo, textViewLogout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_name = preferences.getString("user_name", "user_name");

        //Set up layout widgets
        textViewProfile = (TextView)view.findViewById(R.id.txt_settings_profile);
        textViewActivation = (TextView)view.findViewById(R.id.txt_settings_activation);
        textViewTeamleader = (TextView)view.findViewById(R.id.txt_settings_team_leader);
        textViewReportHistory = (TextView)view.findViewById(R.id.txt_settings_records_history);
        textViewReportConfirmation = (TextView)view.findViewById(R.id.txt_settings_records_confirmation);
        textViewBACheckIn = (TextView)view.findViewById(R.id.txt_settings_check_in);
        textViewPaymentHistory = (TextView)view.findViewById(R.id.txt_settings_payment_history);
        textViewInfo = (TextView)view.findViewById(R.id.txt_settings_info);
        textViewLogout = (TextView)view.findViewById(R.id.txt_settings_logout);

        //Set profile name
        textViewProfile.setText(user_name.toUpperCase());
        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start profile activity
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        textViewActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivationActivity.class));
            }
        });

        textViewTeamleader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TeamLeaderActivity.class));
            }
        });

        textViewReportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start report history
                startActivity(new Intent(getActivity(), ReportHistoryActivity.class));
            }
        });

        textViewReportConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start report confirmation activity
                startActivity(new Intent(getActivity(), ReportConfirmationActivity.class));
            }
        });

        textViewBACheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start ba check in
                startActivity(new Intent(getActivity(), CheckInActivity.class));
            }
        });



        textViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout!!")
                        .setContentText("Are you sure you want to log out")
                        .setConfirmText("YES")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Getting editor
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean(Config.LOGGED_IN_NEW_SHARED_PREF, false);
                                editor.putString(Config.WEATHER_ID, "");
                                editor.putString(Config.WEATHER_TYPE, "");
                                editor.putString(Config.WEATHER_DESCRIPTION, "");
                                editor.putString(Config.WEATHER_TEMPERATURE, "");
                                editor.putString(Config.WEATHER_LOCATION, "");

                                editor.commit();

                                //DaoSession daoSession = ((AlfonesCommunication) getActivity().getApplicationContext()).getDaoSession();
                                //daoSession.getMerchandiseDao().deleteAll();
                                //daoSession.getProductDao().deleteAll();

                                //Fire Login intent
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });

    }
}
