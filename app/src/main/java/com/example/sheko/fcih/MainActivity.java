package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    public static Database db; //this must be initialized in the login
    public EditText username,password ;
    public Button btn;
    public ResultSet rs;
    private MainActivity main;
    String user_name,entered_password;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set_username_password();
               String select_username_password  = "select username,password from user_settings where username ='"+user_name+"' && password='"+entered_password+"'";
                new Login_op(MainActivity.this , main , select_username_password ,"Check For User.....","Connecting" ).execute();

            }
        });
        //getApplicationContext().startActivity(new Intent(getApplicationContext(), doc_list_activity.class));

    }
    //intialize the Edittexts
    public void init(){
        username = (EditText)findViewById(R.id.login_name);
        password = (EditText)findViewById(R.id.login_pass);
        btn =  (Button)findViewById(R.id.login_bt);
        main = this;
    }
    //getusername
    public void Set_username_password(){
        user_name = username.getText().toString();
        entered_password = password.getText().toString();
    }
    public void check_username_IsExist(ResultSet rs){
        try {
            if(rs.next()){
                //open the homepage
                Intent intent = new Intent(MainActivity.this,home_activity.class);
                startActivity(intent);
            }
            else{
                alert("Invalid Username or password","Login");
            }
        } catch (Exception e) {
            alert("Invalid Username or password","Login");
        }
    }
    public void alert(String mess  , String title){

        android.app.AlertDialog.Builder dlgAlert  = new android.app.AlertDialog.Builder(MainActivity.this);

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
//class make the validation and select people from db
class Login_op extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private MainActivity UserView;
    String users_query , loading_title , loading_body;

    public Login_op(Context c , MainActivity main , String query ,String title ,String body){
        context  = c;
        UserView = main;

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

                UserView.rs = MainActivity.db.select_query(users_query);
                UserView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                UserView.check_username_IsExist(UserView.rs);
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

        mProgressDialog = ProgressDialog.show(context, loading_title, loading_body);

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

