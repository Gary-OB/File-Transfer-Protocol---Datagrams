import java.net.*;
/**
 * A class to use with MyServerDatagramSocket for
 * returning a message and the sender's address
 * @author M. L. Liu
 */
public class DatagramMessage{
   private String message;
   private InetAddress senderAddress;
   private int senderPort;
   private byte[] fileByteArray;
   
   public void putVal(String message, InetAddress addr, int port) {
      this.message = message;
      this.senderAddress = addr;
      this.senderPort = port;
   }
   
   public void putVal(byte[] fileByteArray, InetAddress addr, int port) {
	      this.senderAddress = addr;
	      this.senderPort = port;
	      this.fileByteArray = fileByteArray;
	   }

   public String getMessage( ) {
      return this.message;
   }

   public InetAddress getAddress( ) {
      return this.senderAddress;
   }

   public int getPort( ) {
      return this.senderPort;
   }

   public byte[] getFileByteArray() {
	  return fileByteArray;
   }

} // end class  
