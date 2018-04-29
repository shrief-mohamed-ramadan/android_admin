package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class doc_list_activity extends AppCompatActivity {
    public ResultSet rs;
    public TableLayout table;
    public  ArrayList<List<String>> all_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_table_activity);
        Log.i("tag" , "ahshdha");
        /**************************************************************************/
        all_data = new ArrayList<List<String>>();
        /*all_data.add(Arrays.asList("id" , "name" , "attendees" , "mobile" , "sponsor" , "type" ,"vip"));
        all_data.add(Arrays.asList("1" , "ahmed mohamed abdelsalaam" , "1" , "0111213123" , "sponsor1" , "type1" ,"vip"));
        all_data.add(Arrays.asList("2" , "ahmed aly" , "0" , "0111213123" , "sponsor2" , "type2" ,"vip"));
        all_data.add(Arrays.asList("3" , "ahmed farouk" , "1" , "0111213123" , "sponsor3" , "type3" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));*/

        //generate table
        table = (TableLayout) findViewById(R.id.doc_table_id);
        //Design_manager.Generate_Table(all_data , table , getApplicationContext());
        convert_result_set_arraylist();


        //search action
        final EditText search_text = (EditText)findViewById(R.id.search_text_id);
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Design_manager.search(search_text.getText().toString() , table , all_data , getApplicationContext());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //add user action
        TextView click_bt =  (TextView) findViewById(R.id.add_user_id);
        click_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Design_manager.generate_add_doc_frame(getApplicationContext() , v);
            }
        });
        /***********************************************************************************************/
    }//end oncreate
    //=============================================================
    public void convert_result_set_arraylist ()
    {
        Log.w("tag","here");
        String doc_table_query = "select id,name,attendees,mobile,sponsor,type,profession from lists ";
        new Mytask2(doc_list_activity.this,this ,doc_table_query,"Retrieve Doctors data.....","Connecting" ).execute();

    }//end convert resultset to arraylist
}//end class
//==========================================================================================
class Mytask2 extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private doc_list_activity UserView;

    String users_query , loading_title , loading_body;
    int choice = 1; // select buy default  0 for update 1 for select
    public Mytask2(Context c , doc_list_activity UserView,  String query ,String title ,String body){
        context  = c;
        this.UserView = UserView;


        users_query = query;
        loading_body = body;
        loading_title = title;
        choice = 1;
    }
    //overloading
    public Mytask2(Context c,doc_list_activity userView,int update_select ,String query,String title,String body){
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
                MainActivity.db.updata_query(users_query);
            else if (choice == 1) {// mean select
                UserView.rs = MainActivity.db.select_query(users_query);
                UserView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           UserView.all_data.add(Arrays.asList("id" , "name" , "attendees" , "mobile" , "sponsor" , "type" ,"profession"));
                            while (UserView.rs.next())
                            {
                                UserView.all_data.add(Arrays.asList(UserView.rs.getString("id") , UserView.rs.getString("name") , UserView.rs.getString("attendees") , UserView.rs.getString("mobile") , UserView.rs.getString("sponsor") , UserView.rs.getString("type") ,UserView.rs.getString("profession")));
                            }//end get rows data
                            Design_manager.Generate_Table(UserView.all_data , UserView.table ,context);
                        }//end try
                        catch (Exception e)
                        {
                            Log.w("heree",e);
                        }
                    }//end run
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