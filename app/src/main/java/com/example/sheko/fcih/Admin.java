package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Sheko on 4/22/2018.
 */

public class Admin {

    /****************************************************************************/
    //Query to update value in database by specific column_name
    public void edit_doc(String new_value, String columndb_name, String id_doc ,Context cn) {

        String query = "update lists set "+columndb_name+"='"+new_value+"' where id="+id_doc+"";
        Log.w("test",query);
        new Mytask3(cn , query,"Update Doctor.....","Updating" ).execute();

    }//end edit doc

    /****************************************************************************/
    //update attendance column in database
    public void update_doc_attendance(String id, int attend_value , Context cn) {
        String query = "update lists set attendees="+attend_value+" where id ="+id+"";
        new Mytask3(cn , query,"Update Doctor.....","Updating" ).execute();
    }//end update_doc_attendance

    /****************************************************************************/
    //Add doctor with this paramters only
    public void add_doctor(String name, String mobile, String sponsor, String email,Context cn) {
        String query = "insert into lists (name,mobile,sponsor,email)  values('" + name + "','" + mobile + "','" + sponsor + "','" + email + "')";
        new Mytask3(cn , query,"Add Doctor.....","Updating" ).execute();
    }

    /****************************************************************************/
    //Call doctor
    public void call_doctor(String mobile, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }
    /****************************************************************************/
}
//=======================================================================================================
class Mytask3 extends AsyncTask<Void,Void,String> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private doc_list_activity UserView;

    String users_query , loading_title , loading_body;

    //overloading
    public Mytask3(Context cn ,String query,String title,String body){
        context = cn;

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

                Log.w("before",users_query);
                MainActivity.db.updata_query(users_query);



            Log.w("after",users_query);
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
