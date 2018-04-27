package com.example.sheko.fcih;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuInflater;
import java.sql.ResultSet;
import java.sql.SQLException;
import android.view.ViewGroup;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
/**
 * Created by Talaat on 4/24/2018.
 */

public class UserActivity extends AppCompatActivity implements OnMenuItemClickListener{
    public String[] NAMES = {"Talaat", "Medhat", "Khaled"};
    public String[] IP = {"192.168.1.1", "192.168.1.2", "192.168.1.7"};
    public ResultSet rs;
    public Design_manager ds;
    public String select_users = "select username,IP from user_settings";
    public int pos_username;
    public ListView userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_view);

        SetupToolbar();

        ds = new Design_manager();
        userList = findViewById(R.id.UserList); // assign the list we created here

        //initialize menues

        //calling my thread
        new Mytask(UserActivity.this,this,userList , ds ,select_users,"Retrieve Users data.....","Connecting" ).execute();
        //create handler on input search
        EditText input_search = (EditText)findViewById(R.id.search_users);
        input_search.setSelected(false);//don't gain focus at the start
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

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //create the menu here and pass position to get the username for operation
                pos_username = position;
                ShowMenu(view , R.menu.alter_menu);


            }
        });


    }


    void SetupToolbar(){
        Toolbar my = (Toolbar)findViewById(R.id.my_toolbar);
        my.setTitle("Users");
        my.setBackgroundColor(Color.parseColor("#4d4dff"));
        my.setTitleTextColor(Color.parseColor("#FFFFFF"));

        my.setNavigationIcon(R.drawable.list);
        //my.addView(R.drawable.list,new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT));
        setSupportActionBar(my);
        //add the action listener
        my.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//on clicking the icon
                ShowMenu(v , R.menu.user_menu);
            }
        });

    }
    //show menu
    void ShowMenu(View v, int menu){
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menu, popup.getMenu());//assign menu xml to popupmenue
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //open another activity
                  Intent intent = new Intent(UserActivity.this,add_user.class);
                  startActivity(intent);
                return true;
            case R.id.menu_blockall:
                BlockAll();
                return true;
            case R.id.menu_unblock:
                try {
                    UnblockAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            case R.id.delete_item:
                Delete_user(Design_manager.Users_name.get(pos_username));
                return true;
            case R.id.edit_item:
                edit_user(Design_manager.Users_name.get(pos_username));
                return true;
            case R.id.block_item:
                Block_user(Design_manager.Users_name.get(pos_username));
                return true;

            default:
                return false;
        }
    }
    public void BlockAll(){
        String block_query = "UPDATE user_settings Set block=1 where type !='admin'";
        new Mytask(UserActivity.this,this,0,block_query,"Connecting","Blocking Users....." ).execute();

    }
    public void UnblockAll() throws SQLException {
        String unblock_query = "UPDATE user_settings Set block=0 where type !='admin'";
        new Mytask(UserActivity.this,this,0,unblock_query,"Connecting","Unblocking Users .....").execute();
    }
    public void Delete_user(String username){
        String delete_query = "Delete from user_settings where username='"+username+"' and type !='admin'";
        new Mytask(UserActivity.this,this,0,delete_query,"Connecting","Delete "+username+"  .....").execute();
        Design_manager.Users_name.remove(pos_username);
        Design_manager.IPs.remove(pos_username);
        ds.init_list(userList);
    }
    public void Block_user(String username){
        String delete_query = "Update  user_settings SET block = 1 where username='"+username+"' and type !='admin'";
        new Mytask(UserActivity.this,this,0,delete_query,"Connecting","Blocking  "+username+"  .....").execute();

    }
    void edit_user (String username){
        Intent intent = new Intent(UserActivity.this,Edit_user.class);
        intent.putExtra("username" , username);
        startActivity(intent);
    }
}
//*********************************************************************
    class Mytask extends AsyncTask<Void,Void,String> {
        private ProgressDialog mProgressDialog;
        private Context context;
        private UserActivity UserView;
        public Design_manager ds;
        private ListView ls;
        String users_query , loading_title , loading_body;
        int choice = 1; // select buy default  0 for update 1 for select
        public Mytask(Context c , UserActivity UserView ,ListView ls , Design_manager dss , String query ,String title ,String body){
            context  = c;
            this.UserView = UserView;
             ds= dss;
             this.ls = ls;
             users_query = query;
             loading_body = body;
             loading_title = title;
            choice = 1;
        }
        //overloading
    public Mytask(Context c,UserActivity userView,int update_select ,String query,String title,String body){
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
                            ds.generate_users_list(UserView.rs, ls, context);
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


























