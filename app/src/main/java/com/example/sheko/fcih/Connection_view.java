package com.example.sheko.fcih;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Connection_view extends AppCompatActivity {
    Button save_btn;
    public EditText cloud_ip,cloud_username,cloud_pw,cloud_db,network_ip,network_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_setting);
        init();
        save_btn = (Button)findViewById(R.id.button);
        Set_text();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                Intent intent = new Intent(Connection_view.this,home_activity.class);
                startActivity(intent);
            }
        });
    }
    public void init(){
    cloud_db = (EditText)findViewById(R.id.database_name_cloud);
    cloud_ip = (EditText)findViewById(R.id.ip_cloud);
    cloud_username = (EditText)findViewById(R.id.username_cloud);
    cloud_pw= (EditText)findViewById(R.id.database_password_cloud);

    network_db = (EditText)findViewById(R.id.network_databasename);
    network_ip = (EditText)findViewById(R.id.network_ip);
    }
    public void Set_text(){
        cloud_ip.setText(Database.CLOUD_info[0]);
        cloud_db.setText(Database.CLOUD_info[1]);
        cloud_username.setText(Database.CLOUD_info[2]);
        cloud_pw.setText(Database.CLOUD_info[3]);
        network_ip.setText(Database.LAN_info[0]);
        network_db.setText(Database.LAN_info[1]);
    }
    public void Save(){
        Database.CLOUD_info[0] = cloud_ip.getText().toString();
        Database.CLOUD_info[1] = cloud_db.getText().toString();
        Database.CLOUD_info[2] = cloud_username.getText().toString();
        Database.CLOUD_info[3] = cloud_pw.getText().toString();
        Database.LAN_info[0] = network_ip.getText().toString();
        Database.LAN_info[1] = network_db.getText().toString();
    }
}
