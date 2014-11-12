package com.example.chris_000.endgame;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class DialogActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private android.support.v4.app.Fragment DialogMsgFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        if (findViewById(R.id.dialogLayoutFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DialogMsgFragment = new DialogMsgFragment();
            //DialogMsgFragment.setArguments(getIntent().getExtras());
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment).commit();
        }



    }



}
