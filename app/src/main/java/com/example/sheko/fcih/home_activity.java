package com.example.sheko.fcih;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class home_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ImageView users = (ImageView)findViewById(R.id.users_list);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_activity.this.startActivity(new Intent(getApplicationContext(), UserActivity.class));
            }
        });


        ImageView docs = (ImageView)findViewById(R.id.docs_list);
        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_activity.this.startActivity(new Intent(getApplicationContext(), doc_list_activity.class));
            }
        });

        ImageView settings = (ImageView)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_activity.this.startActivity(new Intent(getApplicationContext(),Connection_view.class));
            }
        });
    }
}
