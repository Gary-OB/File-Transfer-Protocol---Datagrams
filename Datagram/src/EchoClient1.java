import java.io.*;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient1 {
   static final String endMessage = ".";
   public static void main(String[] args) {
	   
		  
      try {
    	  
         String hostName = args[0];    
         String portNum = args[1];
        
         EchoClientHelper1 helper = 
            new EchoClientHelper1(hostName, portNum);
         
         boolean done = false;
         String message, response;

            message = args[2];
            
            if ((message.trim()).equals(endMessage)){
               done = true;
               helper.done();
            }
            else {
               response = helper.login(message);
               System.out.println(response);
            }
      } 
      catch (Exception ex) {
         ex.printStackTrace();
      } 
   }
}    
