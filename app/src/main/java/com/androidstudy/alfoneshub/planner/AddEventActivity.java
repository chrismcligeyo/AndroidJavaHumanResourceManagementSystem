package com.androidstudy.alfoneshub.planner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.alfoneshub.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddEventActivity extends AppCompatActivity {

    LinearLayout linearLayoutEvent1, linearLayoutEvent2, linearLayoutEvent3,
            linearLayoutEvent4, linearLayoutEvent5, linearLayoutEvent6, linearLayoutEvent7,
            linearLayoutEvent8, linearLayoutEvent9, linearLayoutEvent10;

    TextView textViewEvent1, textViewEvent2, textViewEvent3, textViewEvent4, textViewEvent5,
            textViewEvent6, textViewEvent7, textViewEvent8, textViewEvent9, textViewEvent10;

    String selectedDate = "", selectedFromTime = "", selectedToTime = "";
    Spinner spinnerTimeFrom, spinnerTimeTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Days event");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        if (null != intent) {
            selectedDate = intent.getStringExtra("selected_date");
        }

        linearLayoutEvent1 = (LinearLayout) findViewById(R.id.linear_layout_event_1);
        linearLayoutEvent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(1);
            }
        });

        linearLayoutEvent2 = (LinearLayout) findViewById(R.id.linear_layout_event_2);
        linearLayoutEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(2);
            }
        });

        linearLayoutEvent3 = (LinearLayout) findViewById(R.id.linear_layout_event_3);
        linearLayoutEvent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(3);
            }
        });

        linearLayoutEvent4 = (LinearLayout) findViewById(R.id.linear_layout_event_4);
        linearLayoutEvent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(4);
            }
        });

        linearLayoutEvent5 = (LinearLayout) findViewById(R.id.linear_layout_event_5);
        linearLayoutEvent5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(5);
            }
        });

        linearLayoutEvent6 = (LinearLayout) findViewById(R.id.linear_layout_event_6);
        linearLayoutEvent6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(6);
            }
        });

        linearLayoutEvent7 = (LinearLayout) findViewById(R.id.linear_layout_event_7);
        linearLayoutEvent7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(7);
            }
        });

        linearLayoutEvent8 = (LinearLayout) findViewById(R.id.linear_layout_event_8);
        linearLayoutEvent8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(8);
            }
        });

        linearLayoutEvent9 = (LinearLayout) findViewById(R.id.linear_layout_event_9);
        linearLayoutEvent9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(9);
            }
        });

        linearLayoutEvent10 = (LinearLayout) findViewById(R.id.linear_layout_event_10);
        linearLayoutEvent10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDependantDialog(10);
            }
        });

        textViewEvent1 = (TextView) findViewById(R.id.text_view_event_1);
        textViewEvent2 = (TextView) findViewById(R.id.text_view_event_2);
        textViewEvent3 = (TextView) findViewById(R.id.text_view_event_3);
        textViewEvent4 = (TextView) findViewById(R.id.text_view_event_4);
        textViewEvent5 = (TextView) findViewById(R.id.text_view_event_5);
        textViewEvent6 = (TextView) findViewById(R.id.text_view_event_6);
        textViewEvent7 = (TextView) findViewById(R.id.text_view_event_7);
        textViewEvent8 = (TextView) findViewById(R.id.text_view_event_8);
        textViewEvent9 = (TextView) findViewById(R.id.text_view_event_9);
        textViewEvent10 = (TextView) findViewById(R.id.text_view_event_10);

        getDayEvents();

    }

    private void getDayEvents() {

        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.USER_GET_DAY_PLANNER_EVENTS)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("date", selectedDate)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Day event response", response.toString());
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Loop through the created array
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = null;
                            try {
                                c = array.getJSONObject(i);
                                String id = c.getString("id");
                                String user_id = c.getString("user_id");
                                String date = c.getString("date");
                                String time = c.getString("time");
                                String event = c.getString("event");
                                String notes = c.getString("notes");
                                String status = c.getString("status");

                                int time_block = Integer.parseInt(time);
                                String color = randomColor();

                                if(time_block == 1){
                                    textViewEvent1.setText(event);
                                    linearLayoutEvent1.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 2){
                                    textViewEvent2.setText(event);

                                    linearLayoutEvent2.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 3){
                                    textViewEvent3.setText(event);

                                    linearLayoutEvent3.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 4){
                                    textViewEvent4.setText(event);

                                    linearLayoutEvent4.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 5){
                                    textViewEvent5.setText(event);

                                    linearLayoutEvent5.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 6){
                                    textViewEvent6.setText(event);

                                    linearLayoutEvent6.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 7){
                                    textViewEvent7.setText(event);

                                    linearLayoutEvent7.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 8){
                                    textViewEvent8.setText(event);

                                    linearLayoutEvent8.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 9){
                                    textViewEvent9.setText(event);

                                    linearLayoutEvent9.setBackgroundColor(Color.parseColor(color));
                                }else if(time_block == 10){
                                    textViewEvent10.setText(event);

                                    linearLayoutEvent10.setBackgroundColor(Color.parseColor(color));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle erro
                        Log.d("Day event response", "Result " + anError);
                    }
                });

    }

    private void showCreateDependantDialog(final int i) {

        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_create_event, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(AddEventActivity.this);
        dialog.setContentView(bottomSheetView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
            }
        });

        List<String> timeList = new ArrayList<String>();
        timeList.add("8:00 AM");
        timeList.add("9:00 AM");
        timeList.add("10:00 AM");
        timeList.add("11:00 AM");
        timeList.add("12:00 PM");
        timeList.add("1:00 PM");
        timeList.add("2:00 PM");
        timeList.add("3:00 PM");
        timeList.add("4:00 PM");
        timeList.add("5:00 PM");

        spinnerTimeFrom = (Spinner) dialog.findViewById(R.id.spinner_leave_time_from);
        ArrayAdapter<String> timeFromDataAdapter = new ArrayAdapter<String>(AddEventActivity.this, R.layout.spinner_item_filter_custom, timeList);
        timeFromDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeFrom.setAdapter(timeFromDataAdapter);
        spinnerTimeFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if(position == 0){

                    selectedFromTime = String.valueOf(position + 1);
                    selectedToTime = String.valueOf(position + 1);
                    spinnerTimeTo.setSelection(position + 1);

                }else{

                    selectedFromTime = String.valueOf(position);
                    selectedToTime = String.valueOf(position + 1);
                    spinnerTimeTo.setSelection(position + 1);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTimeFrom.setSelection(i-1);

        List<String> timeToList = new ArrayList<String>();
        timeToList.add("8:00 AM");
        timeToList.add("9:00 AM");
        timeToList.add("10:00 AM");
        timeToList.add("11:00 AM");
        timeToList.add("12:00 PM");
        timeToList.add("1:00 PM");
        timeToList.add("2:00 PM");
        timeToList.add("3:00 PM");
        timeToList.add("4:00 PM");
        timeToList.add("5:00 PM");

        spinnerTimeTo = (Spinner) dialog.findViewById(R.id.spinner_leave_time_to);
        ArrayAdapter<String> toFromDataAdapter = new ArrayAdapter<String>(AddEventActivity.this, R.layout.spinner_item_filter_custom, timeToList);
        toFromDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeTo.setAdapter(toFromDataAdapter);
        spinnerTimeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if(position == 0){
                    selectedToTime = String.valueOf(position + 1);
                }else {
                    selectedToTime = String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTimeTo.setSelection(i);

        final CardView cardViewProgress = (CardView) dialog.findViewById(R.id.card_view_progress);
        cardViewProgress.setVisibility(View.GONE);

        final EditText editTextDate = (EditText) dialog.findViewById(R.id.edit_text_date);
        editTextDate.setText(selectedDate);
        final EditText editTextDescription = (EditText) dialog.findViewById(R.id.edit_text_description);

        if(i == 1){
            String event = textViewEvent1.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 2){
            String event = textViewEvent2.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 3){
            String event = textViewEvent3.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 4){
            String event = textViewEvent4.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 5){
            String event = textViewEvent5.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 6){
            String event = textViewEvent6.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 7){
            String event = textViewEvent7.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 8){
            String event = textViewEvent8.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 9){
            String event = textViewEvent9.getText().toString();
            editTextDescription.setText(event);
        }else if(i == 10){
            String event = textViewEvent10.getText().toString();
            editTextDescription.setText(event);
        }


        TextView textViewUpdateEvent = (TextView) bottomSheetView.findViewById(R.id.text_view_update_event);
        textViewUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_date = editTextDate.getText().toString();
                if (TextUtils.isEmpty(input_date)) {
                    editTextDate.setError("");
                    return;
                }

                String input_description = editTextDescription.getText().toString();
                if (TextUtils.isEmpty(input_description)) {
                    editTextDescription.setError("");
                    return;
                }


                try{

                    int from_position = Integer.parseInt(selectedFromTime);
                    int to_position = Integer.parseInt(selectedToTime);
                    String color = randomColor();


                    for(int pos = from_position; pos <= to_position; pos++){

                        if(pos == 1){
                            textViewEvent1.setText(input_description);
                            linearLayoutEvent1.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 2){
                            textViewEvent2.setText(input_description);

                            linearLayoutEvent2.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 3){
                            textViewEvent3.setText(input_description);

                            linearLayoutEvent3.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 4){
                            textViewEvent4.setText(input_description);

                            linearLayoutEvent4.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 5){
                            textViewEvent5.setText(input_description);

                            linearLayoutEvent5.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 6){
                            textViewEvent6.setText(input_description);

                            linearLayoutEvent6.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 7){
                            textViewEvent7.setText(input_description);

                            linearLayoutEvent7.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 8){
                            textViewEvent8.setText(input_description);

                            linearLayoutEvent8.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 9){
                            textViewEvent9.setText(input_description);

                            linearLayoutEvent9.setBackgroundColor(Color.parseColor(color));
                        }else if(pos == 10){
                            textViewEvent10.setText(input_description);

                            linearLayoutEvent10.setBackgroundColor(Color.parseColor(color));
                        }

                        cardViewProgress.setVisibility(View.VISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        String user_id = sharedPreferences.getString("user_id", "user_id");
                        AndroidNetworking.post(URLs.USER_UPDATE_PLANNER_EVENT)
                                .addBodyParameter("user_id", user_id)
                                .addBodyParameter("date", selectedDate)
                                .addBodyParameter("time", String.valueOf(pos))
                                .addBodyParameter("event", input_description)
                                .addBodyParameter("notes", "Sample")
                                .setTag("event-create")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        cardViewProgress.setVisibility(View.GONE);
                                        Log.d("EVENT UPDATE", response.toString());

                                    }
                                    @Override
                                    public void onError(ANError error) {

                                        cardViewProgress.setVisibility(View.GONE);
                                        Log.d("EVENT UPDATE ERROR",error.toString());
                                    }
                                });

                    }


                }catch (Exception e){

                    Log.d("Error", e.toString());

                }

                dialog.dismiss();

            }
        });


        ImageView imageViewCancel = (ImageView) bottomSheetView.findViewById(R.id.image_view_cancel);
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public String randomColor(){
        String[] colors = {"#FFB6C1", "#F5F5DC", "#87CEFA", "#AFEEEE", "#BC8F8F", "#9370DB", "#FF6347", "#D3D3D3", "#5F9EA0", "#E6E6FA", "#FFA500"};
        int idx = new Random().nextInt(colors.length);
        String random = (colors[idx]);
        return random;
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
