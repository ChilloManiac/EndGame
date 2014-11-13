package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class JoinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final Button submitButton = (Button) findViewById(R.id.join_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(JoinActivity.this, DialogActivity.class);
                JoinActivity.this.startActivity(changeActivity);
            }
        });

        final Button backButton = (Button) findViewById(R.id.join_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(JoinActivity.this, InitActivity.class);
                JoinActivity.this.startActivity(changeActivity);
            }
        });

        final EditText codeText = (EditText) findViewById(R.id.editText2);
        codeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeText.setText("");
            }
        });
    }

}
