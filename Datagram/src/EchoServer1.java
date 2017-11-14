import java.io.*;

/**
 * This module contains the application logic of an echo server
 * which uses a connectionless datagram socket for interprocess 
 * communication.
 * A command-line argument is required to specify the server port.
 * @author M. L. Liu
 */

public class EchoServer1 {
   public static void main(String[] args) {
      int serverPort = 7;    
      if (args.length == 1 )
         serverPort = Integer.parseInt(args[0]); 
      
      String currentUser;
      
      try {
         
    	  
   	   MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort); 
         System.out.println("Echo server ready.");  
         while (true) {
            DatagramMessage request = 
               mySocket.receiveMessageAndSender();
            System.out.println("Request received");
            String message = request.getMessage( );
            System.out.println("message received: "+ message);

            if(message.startsWith("100")){
            	message.replace("100", "");
            	//if(//Current User Exists)
            	// strcmp(message, "100");
            	currentUser = message.trim();
            	
            	mySocket.sendMessage(request.getAddress( ),
                        request.getPort( ), currentUser + " logged in Successfully");
            } else {
            	mySocket.sendMessage(request.getAddress( ),
                        request.getPort( ), "400 Not Found");
            }          
		   } 
       } 
	    catch (Exception ex) {
          ex.printStackTrace( );
	    } 
   } 
}      
