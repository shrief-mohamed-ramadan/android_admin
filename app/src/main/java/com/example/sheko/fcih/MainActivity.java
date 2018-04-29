package com.example.sheko.fcih;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static Database db; //this must be initialized in the login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list_activity);
        //getApplicationContext().startActivity(new Intent(getApplicationContext(), doc_list_activity.class));

    }
}
