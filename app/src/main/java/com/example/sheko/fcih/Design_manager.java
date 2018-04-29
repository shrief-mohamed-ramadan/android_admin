package com.example.sheko.fcih;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheko on 4/18/2018.
 */

//Class for managing Desing (create table - frame - ...)

public class Design_manager extends AppCompatActivity {
    public static ArrayList<String> search_items = new ArrayList<String>(10);
    public static final Admin admin = new Admin();
    public  static ArrayList<String> Users_name = new ArrayList<String>(5);
    public  static ArrayList<String> IPs = new ArrayList<String>(5);
    private Context context;

    /*
     * Function: Generate_Table
     * ----------------------------
     *   Function for generating table
     *   @Paramters
     *              Resultset from database contatins data
     *              TableLayout for display data on it
     *              context
     *   @returns: void (nothing)
     */
    public static void Generate_Table (ArrayList<List<String>> data, TableLayout table , final Context app_context ) {
        try
        {
            table.removeAllViews();
            /**********************************************
             General Style for table
             **********************************************/
            int header_text_size      = 20;
            int background_header     = R.color.colorPrimary;
            int text_color_header     = R.color.white;
            int padding_header_left   = 30;
            int padding_header_top    = 60;
            int padding_header_right  = 30;
            int padding_header_bottom = 60;
            int user_text_size        = 14;
            int row_toggle1_color     = R.color.light1_gray;
            int row_toggle2_color     = R.color.light2_gray;
            final int on_color        = R.color.correct_color;
            final int off_color       = R.color.danger_color;
            int padding_user_left     = 30;
            int padding_user_top      = 30;
            int padding_user_right    = 30;
            int padding_user_bottom   = 30;
            int padding_row_left      = 0;
            int padding_row_top       = 15;
            int padding_row_right     = 0;
            int padding_row_bottom    = 15;
            final String phone_column       = "mobile";
            final String attend_column      = "attendees";
            /**********************************************
            ADD HEADER OF TABLE & ACTIONS
             **********************************************/
            TableRow header_column = new TableRow(app_context);
            for (int c = 0; c < data.get(0).size(); c++) {
                final TextView column_name = new TextView(app_context);
                column_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(search_items.indexOf(column_name.getText().toString()) == -1)
                        {
                            search_items.add(column_name.getText().toString());
                            column_name.setBackgroundResource(R.color.correct_color);
                        }
                        else
                        {
                            search_items.remove(column_name.getText().toString());
                            column_name.setBackgroundResource(R.color.danger_color);
                        }
                    }
                });
                column_name.setText(data.get(0).get(c));
                column_name.setTextSize(header_text_size);
                column_name.setBackgroundResource(background_header);
                column_name.setTextColor(Color.WHITE);
                column_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                column_name.setPadding(padding_header_left, padding_header_top, padding_header_right, padding_header_bottom);
                header_column.addView(column_name);
            }//end column names
            table.addView(header_column);
            /**********************************************
             ADD DATA OF TABLE & ACTIONS
             **********************************************/
            for(int row = 1 ; row<data.size() ;row++ )
            {
                final TableRow user_row = new TableRow(app_context);
                for(int column = 0 ; column <data.get(0).size() ; column++)
                {
                    final String column_header_name = data.get(0).get(column);
                    final String id_doc = data.get(row).get(0);
                    final TextView user_column = new TextView(app_context);
                    //CREATE long click action (edit)
                    user_column.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(column_header_name.equals(attend_column) || column_header_name.equals("id"));
                            else
                                generate_edit_frame(app_context , v , user_column.getText().toString() , column_header_name ,id_doc, user_column);
                            return true;
                        }
                    });
                    user_column.setText(data.get(row).get(column));
                    if (row % 2 == 0)
                        user_column.setBackgroundResource(row_toggle1_color);
                    else
                        user_column.setBackgroundResource(row_toggle2_color);
                    user_column.setTextSize(user_text_size);
                    user_column.setPadding(padding_user_left , padding_user_top , padding_user_right , padding_user_bottom);
                    user_column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //create phone action
                    if(data.get(0).get(column).equals(phone_column))
                    {
                        user_column.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile ,0,0,0);
                        user_column.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //function call here
                                admin.call_doctor(user_column.getText().toString(),app_context);
                            }
                        });
                    }
                    //create attend action
                    else if (data.get(0).get(column).equals(attend_column))
                    {
                        if(data.get(row).get(column).equals("0"))
                        {
                            user_column.setText("OFF");
                            user_column.setBackgroundResource(off_color);
                        }
                        else
                        {
                            user_column.setText("ON");
                            user_column.setBackgroundResource(on_color);
                        }
                        user_column.setTextColor(Color.WHITE);
                        user_column.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(user_column.getText().equals("OFF"))
                                {
                                    admin.update_doc_attendance(id_doc , 1,app_context);
                                    user_column.setText("ON");
                                    user_column.setBackgroundResource(on_color);
                                }
                                else
                                {
                                    admin.update_doc_attendance(id_doc , 0,app_context);
                                    user_column.setText("OFF");
                                    user_column.setBackgroundResource(off_color);
                                }
                            }
                        });
                    }
                    user_row.addView(user_column);
                }//end columns
                user_row.setPadding(padding_row_left,padding_row_top,padding_user_right,padding_row_bottom);
                table.addView(user_row);
            }//end rows
        }//end try
        catch (Exception e)
        {

        }//end catch
    }//end function Generate_Table
    /*************************************************************************************************************/
    public static void search(String doc_name , TableLayout table , ArrayList<List<String>> data , Context contex)
    {
        if(doc_name.equals(""))
        {
            Generate_Table(data, table, contex );
        }
        else
        {
            ArrayList<List<String>> listCopy = new ArrayList<List<String>>(data);
            for (int row = 1; row < listCopy.size(); row++)
            {
                int found=1;
                for(int i_search =0 ; i_search < search_items.size() ; i_search++)
                {
                    int search_column = listCopy.get(0).indexOf(search_items.get(i_search));
                    if (listCopy.get(row).get(search_column).toLowerCase().contains(doc_name.toLowerCase()))
                    {found=1; break;}
                    else
                    {found=0;}
                }//end columns of search
                if(found==0)
                {
                    listCopy.remove(row);
                    row--;
                }
            }//end passing on rows
            Generate_Table(listCopy, table, contex );
        }
    }//end function search
    /*************************************************************************************************************/
    public static void generate_edit_frame(final Context app_contex , View v , String value , final String column_name , final String id_doc , final TextView user_column)
    {
        final PopupWindow popup = new PopupWindow(app_contex);
        LayoutInflater inflater = (LayoutInflater) app_contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.edit_doc, null);
        popup.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        final EditText edit_Tex = (EditText)layout.findViewById(R.id.edit_text_id);
        edit_Tex.setText(value);
        edit_Tex.setHint(column_name);
        final Button edit_bt = (Button) layout.findViewById(R.id.edit_bt);
        edit_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //call edit function
                String new_value = edit_Tex.getText().toString();
                admin.edit_doc(new_value , column_name , id_doc ,app_contex);
                user_column.setText(new_value);
                layout.setVisibility(View.GONE);
            }
        });
        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(v);
    }//end generate edit frame
    /*************************************************************************************************************/
    public static void generate_add_doc_frame(final Context app_contex , View v)
    {
        PopupWindow popup = new PopupWindow(app_contex);
        LayoutInflater inflater = (LayoutInflater) app_contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_doctor, null);
        popup.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(v);
        //fetch data and pass it to add_doctor_function
        Button add_bt = (Button)layout.findViewById(R.id.add_doc_bt);
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText doc_name = (EditText)layout.findViewById(R.id.name);
                EditText doc_mobile = (EditText)layout.findViewById(R.id.mobile);
                EditText doc_sponsor = (EditText)layout.findViewById(R.id.sponsor);
                EditText doc_email = (EditText)layout.findViewById(R.id.mail);
                admin.add_doctor(doc_name.getText().toString() , doc_mobile.getText().toString() , doc_sponsor.getText().toString(),doc_email.getText().toString(),app_contex);
                layout.setVisibility(View.GONE);
            }
        });
    }//end generate add_doctor_frame
    /*************************************************************************************************************/
    //search in users
    public void Search_users(CharSequence username , ListView user_list, ResultSet rs){
        Users_name = Getting_Names("username" , rs);
        IPs = Getting_Names("IP" , rs);
        for(int count = 0;count <  Users_name.size() ; count++){

            if(Users_name.get(count).contains(username)); // do nothing
            else{//delete the usernames

                Users_name.remove(count);
                IPs.remove(count);
                count--;
            }
        }
        init_list(user_list);
    }
    void init_list(ListView user_list){
        CustomAdapter custom = new CustomAdapter();
        user_list.setAdapter(custom);
    }
    public  void generate_users_list(ResultSet result , ListView user_list ,Context cn){
        context = cn;
        Users_name = Getting_Names("username" , result);
        IPs = Getting_Names("IP",result);
        CustomAdapter custom = new CustomAdapter();
        user_list.setAdapter(custom);
    }
    public  ArrayList Getting_Names(String colm_name , ResultSet rs){
        ArrayList<String> temp = new ArrayList<String>(5);

            try {
                rs.beforeFirst();
                while(rs.next()) {

                    temp.add(rs.getString(colm_name));

                }
            } catch (Exception e) {
                Log.w("talaat",e);
                e.printStackTrace();
            }

        return temp;
    }
    //class for fill the users list
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            try {

                return Users_name.size();
            }
            catch(Exception ex){
                Log.w("getcount",String.valueOf(Users_name.size()) + " and "+ String.valueOf(Users_name.size()));
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.user_custom_list_view, null);

            ImageView image = convertView.findViewById(R.id.imageView);
            TextView username = convertView.findViewById(R.id.textView_username);
            TextView ip = convertView.findViewById(R.id.textView_ip);
            try {
                String user_name = Users_name.get(position);
                String IP = IPs.get(position);


                    if (IP.equals("none")) {
                        image.setImageResource(R.drawable.offline_icon);
                        ip.setText("Offline");
                    } else {
                        image.setImageResource(R.drawable.online_icon);
                        ip.setText(IP);
                    }
                    username.setText(user_name);

            }
            catch(Exception ex){
                Log.w("getview",ex);
            }
            return convertView;
        }
    }
}//end Class Design_manager
