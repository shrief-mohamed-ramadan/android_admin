package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Edit_user extends AppCompatActivity {
    public String username;
    public EditText username_text , password_text;
    public CheckBox cme , update , add , validation ;
    public RadioGroup type;
    public String select_user;

    RadioButton admin_choice , user_choice;
    Button edit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_user);
        Intent i = getIntent();
        username = i.getStringExtra("username");

        SetupToolbar(username);

        select_user = "select password,type,addUser,update_users,allow_cme,validation from user_settings where username ='"+username+"'";
        //init component
        init_component();
        //btn action handling
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Update_data();
            }
        });

        //start thread to get the data from the db and assign it to the component
        new Edit_op(Edit_user.this,this,select_user,"Connecting","Retrieve "+username+" data....." ).execute();

    }
    void SetupToolbar(String username){
        Toolbar my = (Toolbar)findViewById(R.id.edit_toolbar);
        my.setTitle("Edit On "+username);
        my.setBackgroundColor(Color.parseColor("#4d4dff"));
        my.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(my);


    }
    void init_component(){
        username_text = (EditText)findViewById(R.id.user_name);
        password_text = (EditText)findViewById(R.id.user_pass);
        cme = (CheckBox)findViewById(R.id.cme_check);
        add = (CheckBox)findViewById(R.id.add_check);
        update = (CheckBox)findViewById(R.id.update_check);
        validation = (CheckBox)findViewById(R.id.valid_check);

        type = (RadioGroup)findViewById(R.id.type);
        admin_choice = (RadioButton)findViewById(R.id.admin_choice);
        user_choice = (RadioButton)findViewById(R.id.user_choice);
        edit_btn = (Button)findViewById(R.id.edit_btn);
    }
    public void alert(String mess  , String title){

        android.app.AlertDialog.Builder dlgAlert  = new android.app.AlertDialog.Builder(Edit_user.this);

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
    public void assign_Data(ResultSet result){
        try {

            if(result.next()){
                username_text.setText(username);

                password_text.setText(result.getString("password"));

                if(result.getString("type").equals("admin"))
                    admin_choice.setChecked(true);
                else
                    user_choice.setChecked(true);
                if(result.getInt("addUser") == 1)
                    add.setChecked(true);
                if(result.getInt("update_users") == 1)
                    update.setChecked(true);
                if(result.getInt("allow_cme") == 1)
                    cme.setChecked(true);
                if(result.getInt("validation") == 1)
                    validation.setChecked(true);
            }
        } catch (SQLException e) {
            alert("Check your connection error when assigning data->" +e , "Connection Error");
        }
    }
    public void Update_data(){
        String New_username = username_text.getText().toString();
        String Password = password_text.getText().toString();
        String type = admin_choice.isChecked() ? "admin" : "user";
        int is_cme =  cme.isChecked() ? 1 : 0;;
        int is_add =  add.isChecked() ? 1 : 0;;
        int is_update =  update.isChecked() ? 1 : 0;;
        int is_valid =  validation.isChecked() ? 1 : 0;;
        String update_query =
                "UPDATE user_settings SET username='"+New_username+"',password='"+Password+"',type='"
                +type+"',allow_cme="+is_cme+",addUser = "+is_add+", update_users="+is_update+
                        ",validation = "+is_valid+" where username ='"+username+"'";

        if(New_username.equals("")) {

            alert("You must insert username", "Invalid username");
        }
        else if(Password.equals("")) {

            alert("You must insert Password", "Invalid Password");
        }
        else {

            new Edit_op(Edit_user.this, this, 0, update_query, "Updating", "Update User " + username).execute();

        }
    }

}

class Edit_op extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private Edit_user UserView;
    private ResultSet rs;
    String users_query , loading_title , loading_body;
    int choice = 1; // select buy default  0 for update 1 for select
    public Edit_op(Context c , Edit_user UserView ,String query ,String title ,String body){
        context  = c;
        this.UserView = UserView;

        users_query = query;
        loading_body = body;
        loading_title = title;
        choice = 1;
    }
    //overloading
    public Edit_op(Context c,Edit_user userView,int update_select ,String query,String title,String body){
        context = c;
        UserView = userView;
        choice = update_select;
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
            if (choice == 0)//mean update
            {

                MainActivity.db.updata_query(users_query);

            }
            else if (choice == 1) {// mean select

                rs = MainActivity.db.select_query(users_query);
                UserView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    UserView.assign_Data(rs);
                    }
                });

            }
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

            if(choice == 0){//mean u pressed edit btn and want to update data then do and go back
                //return to the list

                context.startActivity(new Intent(context, UserActivity.class));
            }
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