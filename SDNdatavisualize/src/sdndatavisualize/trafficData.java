/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdndatavisualize;

import java.io.*;
//import java.util.*;
import org.json.simple.*;
import java.sql.*;
/**
 *
 * @author HP
 */
public class trafficData {
    public void create_file(String query, String query1) throws IOException{
        try{
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/testbed";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "root");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSet rs1 = st1.executeQuery(query1);
            
            
            int count_row = 0;
            while(rs1.next()){
                count_row++;
            }
            
            int count = 0;
            int count1 = 0;
            String temp = "";
            int temp_rec = 0;
            int temp_trans = 0;
            String[] mac_arr = new String[count_row];
            int[] transmit_arr = new int[count_row];
            int[] receive_arr = new int[count_row];
            
            while (rs.next())
            {
              String mac_add = rs.getString("mac");
              int transmit = rs.getInt("transmitted_packets");
              int receive = rs.getInt("received_packets");
              
              if(count == 0){
                  temp = mac_add;
                  temp_rec = receive;
                  temp_trans = transmit;
                  mac_arr[count1] = temp;
                  transmit_arr[count1] = temp_trans;
                  receive_arr[count1] = temp_rec;
              } else {
                if(temp.equals(mac_add)){
                    transmit_arr[count1] = (transmit - temp_trans);
                    receive_arr[count1] = (receive - temp_rec);
                } else {
                    count1++;
                    temp = mac_add;
                    temp_rec = receive;
                    temp_trans = transmit;
                    mac_arr[count1] = temp;
                    transmit_arr[count1] = temp_trans;
                    receive_arr[count1] = temp_rec;
                }
              }
              count++;
            }
            /**/
            new trafficData().create_json(mac_arr,receive_arr,transmit_arr,count_row);
            st1.close();
            st.close();
            
        } catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public void create_json(String[] mac, int[] receive, int[] transmit, int rows) throws IOException{
        
        try(FileWriter file = new FileWriter("/Users/HP/Desktop/kibana/traffic/traficdata.json")){
            for(int i=0; i<rows; i++){
                String number = Integer.toString(i+1);

                JSONObject obj = new JSONObject();
                obj.put("_index", "traficdata");
                obj.put("_id", number);

                JSONObject obj_2 = new JSONObject();
                obj_2.put("index", obj);
                file.write(obj_2.toJSONString()+ "\n");

                JSONObject obj_1 = new JSONObject();
                obj_1.put("mac", mac[i]);
                obj_1.put("number_of_receive_packets", receive[i]);
                obj_1.put("number_of_transmit_packets", transmit[i]);
                file.write(obj_1.toJSONString()+ "\n");
                //System.out.println("Successfully Copied JSON Object to File...");
                //System.out.println("\nJSON Object: " + obj_1);
            }
            UploadTraffic ut = new UploadTraffic();
            ut.upload();
        }
    }
      
}