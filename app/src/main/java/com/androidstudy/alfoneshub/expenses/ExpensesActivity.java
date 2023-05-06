package com.androidstudy.alfoneshub.expenses;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.expenses.activations.ActivationExpensesActivity;
import com.androidstudy.alfoneshub.expenses.general.GeneralExpensesActivity;

public class ExpensesActivity extends AppCompatActivity {

    CardView cardViewGeneralExpenses, cardViewActivationExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Expenses Centre");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cardViewGeneralExpenses = (CardView)findViewById(R.id.card_view_general_expense);
        cardViewGeneralExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpensesActivity.this, GeneralExpensesActivity.class);
                startActivity(intent);
            }
        });

        cardViewActivationExpenses = (CardView)findViewById(R.id.card_view_activation_expense);
        cardViewActivationExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpensesActivity.this, ActivationExpensesActivity.class);
                startActivity(intent);
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
