package com.example.sheko.fcih;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by Sheko on 4/18/2018.
 */

//Class for managing Desing (create table - frame - ...)

public class Design_manager {

    /*
     * Function: Generate_Table
     * ----------------------------
     *   Function for generating table
     *   @Paramters
     *              Resultset from database contatins data
     *              TableLayout for display data on it
     *
     *   @returns: void (nothing)
     */
    public static void Generate_Table (ResultSet data , final TableLayout table , final Context app_context) {
        try
        {
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
            int block_color          = R.color.correct_color;
            int padding_user_left     = 30;
            int padding_user_top      = 30;
            int padding_user_right    = 30;
            int padding_user_bottom   = 30;
            int padding_row_left      = 0;
            int padding_row_top       = 15;
            int padding_row_right     = 0;
            int padding_row_bottom    = 15;
            String phone_column       = "phone";
            String block_column       = "block";
            int row_num               = 0;
            /**********************************************
            ADD HEADER OF TABLE
             **********************************************/
            TableRow header_column = new TableRow(app_context);
            for (int c=0 ; c<data.getMetaData().getColumnCount() ; c++)
            {
                TextView column_name = new TextView(app_context);
                column_name.setText(data.getMetaData().getColumnName(c));
                column_name.setTextSize(header_text_size);
                column_name.setBackgroundResource(background_header);
                column_name.setTextColor(app_context.getResources().getColor(text_color_header , null));
                column_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                column_name.setPadding(padding_header_left, padding_header_top, padding_header_right, padding_header_bottom);
                header_column.addView(column_name);
            }//end column names
            table.addView(header_column);
            /**********************************************
             ADD DATA OF TABLE
             **********************************************/
            while(data.next())
            {
                final TableRow user_row = new TableRow(app_context);
                for(int column = 0 ; column <data.getMetaData().getColumnCount() ; column++)
                {
                    final TextView user_column = new TextView(app_context);
                    user_column.setText(data.getString(column));
                    if (row_num % 2 == 0)
                        user_column.setBackgroundResource(row_toggle1_color);
                    else
                        user_column.setBackgroundResource(row_toggle2_color);
                    user_column.setTextSize(user_text_size);
                    user_column.setPadding(padding_user_left , padding_user_top , padding_user_right , padding_user_bottom);
                    user_column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if(data.getString(column).equals(phone_column))
                    {
                        user_column.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile ,0,0,0);
                        user_column.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //function call here
                            }
                        });
                    }
                    else if (data.getString(column).equals(block_column))
                    {
                        user_column.setText("Block");
                        user_column.setTextColor(app_context.getResources().getColor(text_color_header , null));
                        user_column.setBackgroundResource(block_color);
                        user_column.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //function call here
                            }
                        });
                    }
                    user_row.addView(user_column);
                }//end columns
                user_row.setPadding(padding_row_left,padding_row_top,padding_user_right,padding_row_bottom);
                table.addView(user_row);
                row_num++;
            }//end rows
        }//end try
        catch (Exception e)
        {

        }//end catch
    }//end function Generate_Table
    /*************************************************************************************************************/

}//end Class Design_manager
