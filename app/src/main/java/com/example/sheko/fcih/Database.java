package com.example.sheko.fcih;

/**
 * Created by Talaat on 4/16/2018.
 */

import android.util.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author Talaat
 */
public class Database {
    static boolean Connected = false;
    Connection cn;
    Statement st;
    //specify IP->0 DB Name->1
    public static String[] LAN_info = {"192.168.1.142:3306", "klydar_cm"};
    public static String LAN;
    //Cloud info IP->0 , DB Name->1 , username->2 , password->3
    public static String[] CLOUD_info = {"192.185.78.63:3306", "ab282_cm", "ab282_admin", "123qweasdzxc"};
    public static String CLOUD;
    public static String LOCAL = "jdbc:mysql://localhost:3306/klydar_cm?characterEncoding=utf8";
    public boolean isCloud = false;
    //HttpURLConnection con;
    //URL url;
    DatabaseMetaData dm;

    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectToCloud();
        } catch (Exception e) {
            Log.w("database Constructor",e);
        }
    }//end connection

    public ResultSet select_query(String query) {

        ResultSet rs = null;
        try {
            if (!cn.isValid(2)) {
                connectToCloud();
            }

            Statement ste = cn.createStatement();

            rs = ste.executeQuery(query);

            return rs;
        } catch (Exception e) {

            Log.w("Select query",e);
        }
        return rs;
    }//end select query

    public void updata_query(String query) {
        try {
            if (cn.isValid(2)) ;
            else {
                connectToCloud();
            }
            Statement ste = cn.createStatement();

            ste.executeUpdate(query);
        } catch (Exception e) {
            Log.w("Update_queyr",e);
        }
    }//end update query
    //function to connect to speific database

    public void connectToCloud() {
        try {
            Log.w("cn","jjj");
            CLOUD = "jdbc:mysql://" + CLOUD_info[0] + "/" + CLOUD_info[1] + "?characterEncoding=utf8";
            this.cn = DriverManager.getConnection(CLOUD, CLOUD_info[2], CLOUD_info[3]);
            Connected = true;
            isCloud = true; // used to know if it' success to access cloud
//            Log.w("cn","jjj");
        } catch (Exception ex) {
            Log.w("cn",ex);
            Connected = false;
        }
    }

    public void connectToLan() {
        try {
            LAN = "jdbc:mysql://" + LAN_info[0] + "/" + LAN_info[1] + "?characterEncoding=utf8";
            this.cn = DriverManager.getConnection(LAN);
            Connected = true;
        } catch (Exception ex) {
            Connected = false;
        }
    }

    public void switch_to_local(int choice) { // take the choice of the connection -> {Localhost , Cloud , LAN connection}
        try {

            if (choice == 0) {//conncet to the localhost



            } else if (choice == 1) { //connect to the cloud
                connectToCloud();

            } else if (choice == 2) {//lan connection
                connectToLan();

            }

        } catch (Exception ex) {

        }

    }

    public void close_db() {
        try {
            cn.close();
        } catch (SQLException ex) {

        }
    }
}

//    public char connection_type() {
//        try {
//
//            dm = cn.getMetaData();
//            String cloudName = "klydar_cm@156.209.186.29";
//            String networkName = "@";
//            String localName = "root@localhost";
//            String dbname = dm.getUserName();
//
//            if (dbname.equals(networkName)) return 'N';
//            else if (dbname.equals(localName)) return 'L';
//            else return 'C';
//        } catch (SQLException ex) {
//            connectToCloud();
//            connection_type();
//
//        }
//        return 'F';
//    }