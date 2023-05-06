package com.androidstudy.alfoneshub.planner;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONObject;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class PlannerActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

    MaterialCalendarView widget;
    LinearLayout linearLayoutEvent;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String selectedDate = "";
    TextView textViewCurrentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Event Planner");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        widget.setOnDateLongClickListener(this);
        widget.setSelectedDate(CalendarDay.today());

        textViewCurrentEvent = (TextView) findViewById(R.id.text_view_current_event);
        linearLayoutEvent = (LinearLayout) findViewById(R.id.linear_layout_event);
        linearLayoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PlannerActivity.this, AddEventActivity.class);
                intent.putExtra("selected_date", selectedDate);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

        selectedDate = String.valueOf(FORMATTER.format(calendarDay.getDate()));

        // Get current time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH");
        String current_time = mdformat.format(calendar.getTime());
        String time = "";
        if(current_time.equalsIgnoreCase("8")){

            time = "1";

        }else if(current_time.equalsIgnoreCase("9")){

            time = "2";

        }else if(current_time.equalsIgnoreCase("10")){

            time = "3";

        }else if(current_time.equalsIgnoreCase("11")){

            time = "4";

        }else if(current_time.equalsIgnoreCase("12")){

            time = "5";

        }else if(current_time.equalsIgnoreCase("13")){

            time = "6";

        }else if(current_time.equalsIgnoreCase("14")){

            time = "7";

        }else if(current_time.equalsIgnoreCase("15")){

            time = "8";

        }else if(current_time.equalsIgnoreCase("16")){

            time = "9";

        }else if(current_time.equalsIgnoreCase("17")){

            time = "10";

        }

        textViewCurrentEvent.setText("No event currently");

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.USER_GET_CURRENT_EVENT)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("date", selectedDate)
                .addBodyParameter("time", time)
                .setTag("event-create")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        int success=0;
                        Log.d("Success Here", "Result " + response.toString());
                        try {
                            success = response.getInt(SUCCESS);

                            if (success == 1) {

                                String event = response.getString("event");
                                textViewCurrentEvent.setText(event);

                            }else{

                                textViewCurrentEvent.setText("No event currently");

                            }

                        }catch (Exception e){
                            Log.d("Exception", e.toString());

                        }



                    }
                    @Override
                    public void onError(ANError error) {

                        Log.d("EVENT UPDATE ERROR",error.toString());
                    }
                });


    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

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
