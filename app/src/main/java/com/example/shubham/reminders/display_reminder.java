package com.example.shubham.reminders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class display_reminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reminder);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);

       // setSupportActionBar(toolbar);
        Button dismiss;
        dismiss=(Button) findViewById(R.id.dismiss);
        Intent i=getIntent();
        String str[]=i.getStringExtra(intent_constants.DISPLAY_INTENT).split(";");
        TextView desc,date,time;
        desc= (TextView) findViewById(R.id.display_desccription);
        date=(TextView) findViewById(R.id.display_date);
        time=(TextView) findViewById(R.id.display_time);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitle(str[0]);
        desc.setText(str[1]);
        date.setText(str[2]);
        time.setText(str[3]);

    }
}
