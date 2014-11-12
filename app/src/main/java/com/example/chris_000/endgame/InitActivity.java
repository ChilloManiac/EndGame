package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);

        final Button hostButton = (Button) findViewById(R.id.host_button);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(InitActivity.this,HostActivity.class);
                InitActivity.this.startActivity(changeActivity);
            }
        });

        final Button joinButton = (Button) findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(InitActivity.this,JoinActivity.class);
                InitActivity.this.startActivity(changeActivity);
            }
        });
    }
}
