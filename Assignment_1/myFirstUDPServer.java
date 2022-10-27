import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

//Programming Assignment 1
//Editted by: Marco Gonzalez & William Baldwin
//09-24-2021
//COMP 4320

public class myFirstUDPServer {

  private static final int ECHOMAX = 255;  // Maximum size of echo datagram

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct argument list
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    DatagramSocket socket = new DatagramSocket(servPort);
    DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

    for (;;) {  // Run forever, receiving and echoing datagrams
      socket.receive(packet);     // Receive packet from client
      System.out.println("Handling client at " +
        packet.getAddress().getHostAddress() + " on port " + packet.getPort());
      
      //--------------------------New Code--------------------------------<
      String message = new String(packet.getData());
      System.out.println("The received message was: " + message);
      
      String temp[] = message.split(" ");
      String reverseMess = "";
      for (int i = temp.length- 1; i >= 0; i--){
        reverseMess += temp[i] + " ";
      }
      System.out.println("The reversed message is: " + reverseMess);
      byte[] bytesToSend = reverseMess.getBytes("UTF-16BE");
      byte[] byteBuffer = new byte[bytesToSend.length];
      int count = 0;
      for (int i = 0; i < bytesToSend.length; i++){
        if (bytesToSend[i] == 0){
        }
        else{
          byteBuffer[count] = bytesToSend[i];
          count++;
        }
      }
      DatagramPacket sendPacket = new DatagramPacket(byteBuffer,  // Sending packet
        byteBuffer.length, packet.getAddress(), packet.getPort());
      socket.send(sendPacket);
      //--------------------------New Code-------------------------------->

      //socket.send(packet);       // Send the same packet back to client
      packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }
}
