package com.example.sheko.fcih;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_table_activity);

        /**************************************************************************/
        final ArrayList<List<String>> all_data = new ArrayList<List<String>>();
        all_data.add(Arrays.asList("id" , "name" , "attendees" , "mobile" , "sponsor" , "type" ,"vip"));
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
        all_data.add(Arrays.asList("4" , "ahmed mahdy" , "1" , "0111213123" , "sponsor4" , "type4" ,"vip"));

        //generate table
        final TableLayout table = (TableLayout) findViewById(R.id.doc_table_id);
        Design_manager.Generate_Table(all_data , table , getApplicationContext());
        Database obj = new Database();

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
    }
}
