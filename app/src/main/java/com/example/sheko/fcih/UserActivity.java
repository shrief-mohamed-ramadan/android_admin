package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.AlertDialog.Builder;
import java.sql.ResultSet;
import android.support.design.widget.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Talaat on 4/24/2018.
 */

public class UserActivity extends AppCompatActivity {
    public String[] NAMES = {"Talaat", "Medhat", "Khaled"};
    public String[] IP = {"192.168.1.1", "192.168.1.2", "192.168.1.7"};
    public ResultSet rs;
    public Design_manager ds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_view);
        ds = new Design_manager();
        final ListView userList = findViewById(R.id.UserList); // assign the list we created here
        new Mytask(UserActivity.this,this,userList , ds).execute();
        //create handler on input search
        EditText input_search = (EditText)findViewById(R.id.search_users);
        input_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                ds.Search_users(cs,userList,rs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
            //open another activity
//        Intent intent = new Intent(Login_view.this,fragment.class);
//        startActivity(intent);
    }


}
//*********************************************************************
    class Mytask extends AsyncTask<Void,Void,String> {
        private ProgressDialog mProgressDialog;
        private Context context;
        private UserActivity UserView;
        public Design_manager ds;
        private ListView ls;
        public Mytask(Context c , UserActivity UserView ,ListView ls , Design_manager dss){
            context  = c;
            this.UserView = UserView;
             ds= dss;
             this.ls = ls;
        }
        @Override
        protected String doInBackground(Void... Params) {
        /* u  do whatever u want her */
            try {
                String select_users_query = "select * from user_settings";
                MainActivity.db = new Database();
                UserView.rs = MainActivity.db.select_query(select_users_query);
                UserView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ds.generate_users_list(UserView.rs, ls ,context);
                    }
                });

            }
            catch(Exception ex){
                Log.w("Error",ex);
            }
            if(Database.Connected)
                return "Complete";
            return "not complete";
        }
        protected void onPreExecute() {

            mProgressDialog = ProgressDialog.show(context, "Retrieve Users",

                    "Please wait, Loading users list...");

        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Complete")){
                mProgressDialog.dismiss();
            }
            else{
                mProgressDialog.dismiss();
                android.app.AlertDialog.Builder dlgAlert  = new android.app.AlertDialog.Builder(context);

                dlgAlert.setMessage("No Internet Connection");
                dlgAlert.setTitle("Check you connection...");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        }
    }


























