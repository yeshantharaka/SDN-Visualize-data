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
public class UploadBytes {
    public void upload(){
        try{
            Runtime.getRuntime().exec("cmd /c start powershell.exe /K \"cd \"C:\\Users\\HP\\Desktop\\kibana\\bytes\" && Invoke-RestMethod \"http://localhost:9200/bytedata/doc/_bulk?pretty\" -Method Post -ContentType 'application/x-ndjson' -InFile \"bytedata.json\"");
            System.out.println("Byte data successfully uploaded");
            //Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"Invoke-RestMethod \"http://localhost:9200/traficdata/doc/_bulk?pretty\" -Method Post -ContentType 'application/x-ndjson' -InFile \"traficdata.json\" && cd \"C:\\Users\\HP\\Desktop\\kibana\\traffic\"");
        }
        catch (Exception e) 
        { 
            System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
            e.printStackTrace(); 
        }
    }
    
}
