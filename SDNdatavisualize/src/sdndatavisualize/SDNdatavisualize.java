/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdndatavisualize;

/**
 *
 * @author HP
 */
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SDNdatavisualize {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here

        final SDNdatavisualize sdn = new SDNdatavisualize();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    sdn.get_response();
                } catch (IOException ex) {
                    Logger.getLogger(SDNdatavisualize.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
        
    }
    
    public void get_response() throws IOException  {
        //create trafic file
        String query_packets = "SELECT mac,received_packets,transmitted_packets,date_time as date FROM packets WHERE DATE(date_time)>='2018-09-29' ORDER BY DATE(date_time) ASC,mac ASC,id ASC";
        String query_packets_l = "SELECT mac FROM packets WHERE DATE(date_time)>='2018-09-29' GROUP BY mac";

        trafficData td = new trafficData();
        td.create_file(query_packets, query_packets_l);

        //create bytes file
        String query_bytes = "SELECT mac,received_bytes,transmitted_bytes FROM bytes WHERE DATE(date_time)>='2018-09-29' ORDER BY mac ASC,id ASC";
        String query_bytes_l = "SELECT mac FROM bytes WHERE DATE(date_time)>='2018-09-29' GROUP BY mac";

        bytesData bd = new bytesData();
        bd.create_file(query_bytes, query_bytes_l);

        //create error detail file
        String query_errors = "SELECT mac,received_drops,transmitted_drops,transmitted_errors,received_errors FROM error_details WHERE DATE(date_time)>='2018-09-29' ORDER BY mac ASC,id ASC";
        String query_errors_l = "SELECT mac FROM error_details WHERE DATE(date_time)>='2018-09-29' GROUP BY mac";

        errorDetails ed = new errorDetails();
        ed.create_file(query_errors, query_errors_l);

    }
    
}
