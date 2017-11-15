import java.io.*;

import javax.swing.JOptionPane;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient1 {
   static final String endMessage = ".";
   public static void main(String[] args) {
	   
		  
      try {
    	  
         String hostName = JOptionPane.showInputDialog("Server Hostname");    
         String portNum = JOptionPane.showInputDialog("Server PortNum"); 
        
         EchoClientHelper1 helper = 
            new EchoClientHelper1(hostName, portNum);
         
         boolean done = false;
         String message, response;

            message = JOptionPane.showInputDialog("Server Message"); 
            
            if ((message.trim()).equals(endMessage)){
               done = true;
               helper.done();
            }
            else {
               response = helper.login(message);
               JOptionPane.showMessageDialog(null, response);
               System.out.println(response);
            }
      } 
      catch (Exception ex) {
         ex.printStackTrace();
      } 
   }
}    
