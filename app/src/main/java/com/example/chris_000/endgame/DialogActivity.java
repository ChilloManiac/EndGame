package com.example.chris_000.endgame;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private android.support.v4.app.Fragment DialogMsgFragment;
    private android.support.v4.app.Fragment DialogChoiceFragment;
    private ArrayList<FieldPoint> field = null;
    private Boolean iWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            iWon = (Boolean) extras.get("won");
        }

        if (iWon != null) {
            if (findViewById(R.id.dialogLayoutFrame) != null && iWon == false) {
                if (savedInstanceState != null) {
                    return;
                }
                DialogMsgFragment = new DialogMsgFragment();
                DialogMsgFragment.setArguments(extras);
                transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment).commit();

                //todo loser code here

            } else if (iWon == true) {
                if (findViewById(R.id.dialogLayoutFrame) != null && iWon == true) {
                    if (savedInstanceState != null) {
                        return;
                    }
                    DialogChoiceFragment = new DialogChoiceFragment();
                    DialogChoiceFragment.setArguments(extras);
                    transaction = getSupportFragmentManager().beginTransaction();

                    transaction.add(R.id.dialogLayoutFrame, DialogChoiceFragment).commit();

                    //todo victory code here
                }
            }
        } else {
            if (findViewById(R.id.dialogLayoutFrame) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                DialogMsgFragment = new DialogMsgFragment();
                DialogMsgFragment.setArguments(extras);
                transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment).commit();

                //initial text
            }
        }

        TextView textView = (TextView) findViewById(R.id.TextViewDialog);
        //TODO: put in dialog code here
        if (field != null) {
            textView.setText("");
            for (FieldPoint fp : field) {
                textView.append(fp.toString() + "\n");
            }
        }
        if (iWon != null) {
            textView.setText(iWon.toString());
        }

    }


}
