package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.sql.ResultSet;

public class add_user extends AppCompatActivity {
    public EditText username_text , password_text;
    public CheckBox cme , update , add , validation ;
    public RadioGroup type;
    public String select_user;

    RadioButton admin_choice , user_choice;
    Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        SetupToolbar();
        init_component();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

    }
    void SetupToolbar(){
        Toolbar my = (Toolbar)findViewById(R.id.add_toolbar);
        my.setTitle("Add New");
        my.setBackgroundColor(Color.parseColor("#4d4dff"));
        my.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(my);


    }
    void init_component(){
        username_text = (EditText)findViewById(R.id.user_name);
        password_text = (EditText)findViewById(R.id.user_pass);
        cme = (CheckBox)findViewById(R.id.Allow_printcme_check);
        add = (CheckBox)findViewById(R.id.Allow_addusers_check);
        update = (CheckBox)findViewById(R.id.allow_update);
        validation = (CheckBox)findViewById(R.id.add_validation_check);


        admin_choice = (RadioButton)findViewById(R.id.admin_choice);
        user_choice = (RadioButton)findViewById(R.id.user_choice);
        add_btn = (Button)findViewById(R.id.add_bt);
    }
    void add(){
        String New_username = username_text.getText().toString();
        String Password = password_text.getText().toString();
        String type = admin_choice.isChecked() ? "admin" : "user";
        int is_cme =  cme.isChecked() ? 1 : 0;;
        int is_add =  add.isChecked() ? 1 : 0;;
        int is_update =  update.isChecked() ? 1 : 0;;
        int is_valid =  validation.isChecked() ? 1 : 0;;
        String update_query =
                "insert into  user_settings (`username`, `password`,`type`," +
                        "   `addUser`, `update_users`," +
                        " `allow_cme`, `validation`) " +
                        "VALUES('"+New_username+"' ,'"+Password+"','"+type+"',"+is_add+","+is_update+"" +
                        ", "+is_cme+" , "+is_valid+")";

        if(New_username.equals("")) {

            alert("You must insert username", "Invalid username");
        }
        else if(Password.equals("")) {

            alert("You must insert Password", "Invalid Password");
        }
        else {
            Log.w("query",update_query);
            new Add_op(add_user.this, this, update_query, "Adding", "Adding User " + New_username).execute();

        }
    }
    public void alert(String mess  , String title){

        android.app.AlertDialog.Builder dlgAlert  = new android.app.AlertDialog.Builder(add_user.this);

        dlgAlert.setMessage(mess);
        dlgAlert.setTitle(title);
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
//******************************************************
class Add_op extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private add_user UserView;
    private ResultSet rs;
    String users_query , loading_title , loading_body;


    public Add_op(Context c,add_user userView,String query,String title,String body){
        context = c;
        UserView = userView;

        users_query = query;
        loading_body = body;
        loading_title = title;
    }
    @Override
    protected String doInBackground(Void... Params) {
        /* u  do whatever u want her */
        try {
            if (MainActivity.db == null)
                MainActivity.db = new Database();

                MainActivity.db.updata_query(users_query);

        }
        catch(Exception ex){
            Log.w("Error",ex);
        }

        if(Database.Connected)
            return "Complete";
        return "not complete";
    }
    protected void onPreExecute() {

        mProgressDialog = ProgressDialog.show(context, loading_title, loading_body);

    }
    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Complete")){
            mProgressDialog.dismiss();
                //return to the list

                context.startActivity(new Intent(context, UserActivity.class));

        }
        else{

            mProgressDialog.dismiss();
            android.app.AlertDialog.Builder dlgAlert  = new android.app.AlertDialog.Builder(context);

            dlgAlert.setMessage("No Internet Connection");
            dlgAlert.setTitle("Check your connection...");
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