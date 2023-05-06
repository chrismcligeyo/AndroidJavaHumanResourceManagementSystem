package com.androidstudy.alfoneshub.expenses.general;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.expenses.general.fragments.PendingExpensesFragment;
import com.androidstudy.alfoneshub.expenses.general.fragments.ProcessedExpensesFragment;

public class GeneralExpensesActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_expenses);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("General Expenses");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("PENDING EXPENSES"));
        tabLayout.addTab(tabLayout.newTab().setText("PROCESSED EXPENSES"));

        //replace default fragment
        replaceFragment(new PendingExpensesFragment());

        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new PendingExpensesFragment());
                } else {
                    replaceFragment(new ProcessedExpensesFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
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

