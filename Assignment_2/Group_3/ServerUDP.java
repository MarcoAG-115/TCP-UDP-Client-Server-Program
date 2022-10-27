import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.*;
import java.nio.charset.Charset;

public class ServerUDP 
{

  public static void main(String args[]) throws Exception 
  {
      InetAddress destination = InetAddress.getByName("localhost");

      if (args.length != 1 && args.length != 2)  // Test for correct # of args  
      {      
	        throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");
      }

      int recvPort = Integer.parseInt(args[0]);
      int destPort = 10013;
      
      while(true)
      {
        DatagramSocket sock = new DatagramSocket(recvPort);
        DatagramPacket oprPacket = receivedPacket(sock);
        System.out.println("Received the packet.");
        //byte[] headerResult = encodeAnswer(performRequest(oprPacket));
        byte[] headerResult = performRequest(oprPacket).getBytes("UTF-16BE");
        System.out.println("The request was successful.");
        System.out.println("Attempting to send.");
        Thread.sleep(2000);
        if (!sendPacket(sock, oprPacket, headerResult))
        {
          System.out.println("Failed to send.");
        }
        else
        {
          System.out.println("Successfully sent");
          System.out.println("------------------------------------------------------------------------------------------");
        }
        sock.close();
      }
  }
    
  private static DatagramPacket receivedPacket(DatagramSocket sock) throws Exception
  {
    DatagramPacket packet = new DatagramPacket(new byte[100], 100);
    System.out.println("Waiting for number.");
    sock.receive(packet);
    return packet;
  }    
      
  private static boolean sendPacket(DatagramSocket sock, DatagramPacket p,  byte[] header) 
  {
      try 
      {
          DatagramPacket outPacket = new DatagramPacket(header, header.length, p.getAddress(), p.getPort());
          sock.send(outPacket);
      } catch (Exception e) 
      {
          return false;
      }
      return true;
  }

  private static String performRequest(DatagramPacket p) throws IOException 
  {
    byte[] buffer = p.getData();
    String stringResult = new String(buffer, "UTF-16");
    //System.out.println(stringResult);
    String hexResult = "";
    int intResult = 0;
    //System.out.println(Arrays.toString(buffer));
    // int recLngth = p.getLength();
    // int result = 0;
    // int operation = -1;
    // RequestDecoderBin decoder = new RequestDecoderBin();     	      
    // Request op = decoder.decode(p);

    //operation = op.code;


    // System.out.println("Attempting Request below: ");   
    // System.out.println(op);

    stringResult = stringResult.trim();
    try
		{
			intResult = Integer.parseInt(stringResult);
		}
		catch(NumberFormatException e)
		{
			intResult = 0;
		}
    System.out.println("Returning result number in decimal format: ");
    System.out.println(intResult);

    hexResult = Integer.toHexString(intResult);
    System.out.println("Returning result number in hexadecimal format: ");
    System.out.println(hexResult);

    System.out.println("Returning result number in hexadecimal format one byte at a time: ");
    
    if (hexResult.length() % 2 == 0) 
    {
      for (int i = 0; i < hexResult.length(); i = i+2)
      {
        System.out.println("0x" + hexResult.substring(i,i+2));
      }
    }
    else
    {
      String modHexResult = "0" + hexResult;
      for (int i = 0; i < modHexResult.length(); i = i+2)
      {
        System.out.println("0x" + modHexResult.substring(i,i+2));
      }
    }
    
    return hexResult;

  }

  // public static byte[] encodeAnswer(Response ans) throws IOException 
  // {
  //   ByteArrayOutputStream buf = new ByteArrayOutputStream();
  //   DataOutputStream out = new DataOutputStream(buf);
  //   out.writeByte(ans.total_length);
  //   out.writeByte(ans.ID);
  //   out.writeByte(ans.error);
  //   out.writeInt(ans.result);

  //   out.flush();
  //   return buf.toByteArray();
  // }
}