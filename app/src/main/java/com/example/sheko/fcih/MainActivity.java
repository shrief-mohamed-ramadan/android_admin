package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_table_activity);

        ResultSet rs= null;
        TableLayout table = (TableLayout) findViewById(R.id.user_table_id);
        Design_manager.Generate_Table(rs , table , getApplicationContext());
        //to use db u must use thread mytask is a thread initialized down
        new Mytask(MainActivity.this).execute();
    }

}
//class that will do the db work u need to do u must do it to use the database so u will put ur code here then call it above
class Mytask extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;

    public Mytask(Context c){
        context  = c;
    }
    @Override
    protected String doInBackground(Void... Params) {
        /* u  do whatever u want her */
        Log.w("test","hello world");
        Database db = new Database();
        return "Complete";
    }
    protected void onPreExecute() {

        mProgressDialog = ProgressDialog.show(context, "Database",

                "Please wait, getting database...");

    }
    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Complete")){
            mProgressDialog.dismiss();
        }
    }
}
