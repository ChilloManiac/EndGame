package com.example.chris_000.endgame;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;


public class InitActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private android.support.v4.app.Fragment hostFragment;
    private android.support.v4.app.Fragment initFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_init);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            initFragment = new InitFragment();
            hostFragment = new HostFragment();
            initFragment.setArguments(getIntent().getExtras());
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, initFragment).commit();
        }



    }
}
