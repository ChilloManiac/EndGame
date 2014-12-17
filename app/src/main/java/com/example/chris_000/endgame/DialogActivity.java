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
    private String player;
    private TextView dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            iWon = (Boolean) extras.get("won");
            player = (String) extras.get("player");
        }

        dialogue = (TextView) findViewById(R.id.TextViewDialog);

        if (iWon != null) {
            if (findViewById(R.id.dialogLayoutFrame) != null && iWon == false) {
                if (savedInstanceState != null) {
                    return;
                }
                DialogMsgFragment = new DialogMsgFragment();
                DialogMsgFragment.setArguments(extras);
                transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment).commit();

                if (player != null) {
                    if (player.equals("player1")) {
                        dialogue.setText(this.getString(R.string.loseAllies));
                    } else if (player.equals("player2")) {
                        dialogue.setText(this.getString(R.string.loseAxis));
                    }
                }

            } else if (iWon == true) {
                if (findViewById(R.id.dialogLayoutFrame) != null && iWon == true) {
                    if (savedInstanceState != null) {
                        return;
                    }
                    DialogChoiceFragment = new DialogChoiceFragment();
                    DialogChoiceFragment.setArguments(extras);
                    transaction = getSupportFragmentManager().beginTransaction();

                    transaction.add(R.id.dialogLayoutFrame, DialogChoiceFragment).commit();

                    if (player != null) {
                        if (player.equals("player1")) {
                            dialogue.setText(this.getString(R.string.winAllies));
                        } else if (player.equals("player2")) {
                            dialogue.setText(this.getString(R.string.winAxis));
                        }
                    }
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

                if (player != null) {
                    if (player.equals("player1")) {
                        dialogue.setText(this.getString(R.string.introAllies));
                    } else if (player.equals("player2")) {
                        dialogue.setText(this.getString(R.string.introAxis));
                    }
                }
            }
        }
    }
}
