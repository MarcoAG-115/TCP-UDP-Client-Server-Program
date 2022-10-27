import java.net.*;
import java.io.*;   
import java.util.*;
import java.nio.charset.Charset;

//Programming Assignment 2
//Editted by: Marco Gonzalez & William Baldwin
//10-30-2021
//COMP 4320

public class ServerUDP 
{

  public static void main(String args[]) throws Exception 
  {
      //Checks that only one argument is provided (Group Port Number 10010 + 3 = 10013).
      if (args.length != 1 && args.length != 2)  
      {      
	        throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");
      }

      //Port number argument is asssigned to recvPort.
      int recvPort = Integer.parseInt(args[0]);
      
      //This while loop will always run. This is what allows to server to always be ready to
      //  receive a packet from a client.
      while(true)
      {
        DatagramSocket sock = new DatagramSocket(recvPort);
        DatagramPacket oprPacket = receivedPacket(sock);
        System.out.println("Received the packet.");
        byte[] headerResult = performRequest(oprPacket).getBytes("UTF-16BE");
        System.out.println("The request was successful.");
        System.out.println("Attempting to send.");
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
  
  //Takes received packet and prepares it by ensuring it's formatted correctly.
  private static DatagramPacket receivedPacket(DatagramSocket sock) throws Exception
  {
    DatagramPacket packet = new DatagramPacket(new byte[100], 100);
    System.out.println("Waiting for number.");
    sock.receive(packet);
    return packet;
  }    
  
  //The packet containing the hexadecimal number that will be received by the client is prepared and sent.
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

  //Takes the formatted / received packet and extracts the data. This data is formatted to be usable (converts to string).
  //The data is checked to ensure that it is an integer.
  //Decimal format (the data as it was received) is printed first.
  //Next, the number is converted from decimal to hexadecimal, and this is printed.
  //Lastly, the hexadecimal number is printed one byte at a time.
  //Theis method returns the hexadecimal number so that it can be sent back to the client.
  private static String performRequest(DatagramPacket p) throws IOException 
  {
    byte[] buffer = p.getData();
    String stringResult = new String(buffer, "UTF-16BE");
    String hexResult = "";
    int intResult = 0;

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

  
}