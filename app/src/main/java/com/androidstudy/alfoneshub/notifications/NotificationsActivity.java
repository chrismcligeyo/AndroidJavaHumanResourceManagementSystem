package com.androidstudy.alfoneshub.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.NotificationDetailsActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.adapters.NotificationAdapter;
import com.androidstudy.alfoneshub.models.Notification;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class NotificationsActivity extends AppCompatActivity {

    SweetAlertDialog pDialog;

    RecyclerView recyclerViewNotification;
    com.androidstudy.alfoneshub.adapters.NotificationAdapter NotificationAdapter;
    private List<Notification> NotificationList = new ArrayList<>();
    LinearLayout linearLayoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcations);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);
        recyclerViewNotification= (RecyclerView)findViewById(R.id.recycler_notification);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewNotification.setLayoutManager(gridLayoutManager);

        fetchNotifications();


        if(NotificationList.size()>0){
            NotificationList.clear();
        }
    }

    public void fetchNotifications(){
        SharedPreferences preferences = NotificationsActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(NotificationsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Fetching notifications...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.NOTIFICATIONS_URL)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("Notification response", response.toString());
                        //Get JSON Response
                        JSONObject jsonObj = null;
                        JSONArray array =null;
                        try {
                            jsonObj = new JSONObject(String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Getting JSON Array node
                        try {
                            array = jsonObj.getJSONArray("result");
                            if(array.isNull(0)){
                                Log.d("ARRAY", "empty");
                                recyclerViewNotification.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                recyclerViewNotification.setVisibility(View.VISIBLE);
                                linearLayoutError.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Loop through the created array
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = null;
                            try {
                                c = array.getJSONObject(i);
                                String id = c.getString("id");
                                String date = c.getString("date");
                                String user_id = c.getString("user_id");
                                String message = c.getString("message");
                                String read = c.getString("read");
                                Notification notification = new Notification(id, date, user_id, message, read);
                                NotificationList.add(notification);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        NotificationAdapter = new NotificationAdapter(NotificationsActivity.this, NotificationList);
                        recyclerViewNotification.setAdapter(NotificationAdapter);
                        recyclerViewNotification.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(NotificationsActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Notification notification = NotificationList.get(position);
                                        Intent intent = new Intent(NotificationsActivity.this, NotificationDetailsActivity.class);
                                        intent.putExtra("id", notification.getId());
                                        intent.putExtra("message", notification.getMessage());
                                        intent.putExtra("date", notification.getDate());
                                        intent.putExtra("user_id", notification.getBa_id());
                                        intent.putExtra("read", notification.getRead());
                                        startActivity(intent);
                                    }
                                })
                        );
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Notification response", "Result " + anError);
                        new SweetAlertDialog(NotificationsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
