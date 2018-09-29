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
public class errorDetails {
    public void create_file(String query, String query1) throws IOException{
        try{
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/sdn";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
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
            int temp_trans_err = 0;
            int temp_rec_err = 0;
            String[] mac_arr = new String[count_row];
            int[] transmit_arr = new int[count_row];
            int[] receive_arr = new int[count_row];
            int[] receive_err_arr = new int[count_row];
            int[] transmit_err_arr = new int[count_row];
            
            while (rs.next())
            {
              String mac_add = rs.getString("mac");
              int transmit = rs.getInt("transmitted_drops");
              int receive = rs.getInt("received_drops");
              int receive_err = rs.getInt("received_errors");
              int transmit_err = rs.getInt("transmitted_errors");
              
              if(count == 0){
                  temp = mac_add;
                  temp_rec = receive;
                  temp_trans = transmit;
                  temp_rec_err = receive_err;
                  temp_trans_err = transmit_err;
                  mac_arr[count1] = temp;
                  transmit_arr[count1] = temp_trans;
                  receive_arr[count1] = temp_rec;
                  receive_err_arr[count1] = temp_rec_err;
                  transmit_err_arr[count1] = temp_trans_err;
              } else {
                if(temp.equals(mac_add)){
                    transmit_arr[count1] = (transmit - temp_trans);
                    receive_arr[count1] = (receive - temp_rec);
                    receive_err_arr[count1] = (receive_err - temp_rec_err);
                    transmit_err_arr[count1] = (transmit_err - temp_trans_err);
                } else {
                    count1++;
                    temp = mac_add;
                    temp_rec = receive;
                    temp_trans = transmit;
                    temp_rec_err = receive_err;
                    temp_trans_err = transmit_err;
                    mac_arr[count1] = temp;
                    transmit_arr[count1] = temp_trans;
                    receive_arr[count1] = temp_rec;
                    receive_err_arr[count1] = temp_rec_err;
                    transmit_err_arr[count1] = temp_trans_err;
                }
              }
              count++;
            }
            
            new errorDetails().create_json(mac_arr,receive_arr,transmit_arr,receive_err_arr,transmit_err_arr,count_row);
            st1.close();
            st.close();
            
        } catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public void create_json(String[] mac, int[] receive, int[] transmit, int[] receive_err, int[] transmit_err, int rows) throws IOException{
        
        try(FileWriter file = new FileWriter("/Users/HP/Desktop/kibana/error_detail/errordata.json")){
            for(int i=0; i<rows; i++){
                String number = Integer.toString(i+1);

                JSONObject obj = new JSONObject();
                obj.put("_index", "errordata");
                obj.put("_id", number);

                JSONObject obj_2 = new JSONObject();
                obj_2.put("index", obj);
                file.write(obj_2.toJSONString()+ "\n");

                JSONObject obj_1 = new JSONObject();
                obj_1.put("mac", mac[i]);
                obj_1.put("number_of_receive_drops", receive[i]);
                obj_1.put("number_of_transmit_drops", transmit[i]);
                obj_1.put("number_of_receive_errors", receive_err[i]);
                obj_1.put("number_of_transmit_errors", transmit_err[i]);
                file.write(obj_1.toJSONString()+ "\n");
                //System.out.println("Successfully Copied JSON Object to File...");
                //System.out.println("\nJSON Object: " + obj_1);
            }

        }
    }
}
