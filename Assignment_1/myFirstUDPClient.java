import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

//Programming Assignment 1
//Editted by: Marco Gonzalez & William Baldwin
//09-24-2021
//COMP 4320

public class myFirstUDPClient {

  private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
  private static final int MAXTRIES = 5;     // Maximum retransmissions

  public static void main(String[] args) throws IOException {

    if ((args.length < 2) || (args.length > 3))  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

    InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address
    // Convert input String to bytes using the default character encoding

    //--------------------------New Code--------------------------------<
    System.out.println("Enter a sentence: ");
    String line = "";
    DataInputStream input = new DataInputStream(System.in);
    line = input.readLine();
    byte[] bytesToSend = line.getBytes();
    //--------------------------New Code-------------------------------->

    //byte[] bytesToSend = args[1].getBytes();

    //int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

    //--------------------------New Code--------------------------------<
    int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 10010;

    if ((servPort < 10010) || (servPort > 10200)) {
      throw new IllegalArgumentException("The range of allowed port numbers on Tux Machines is 10010 - 10200");
    }
    //--------------------------New Code-------------------------------->

    DatagramSocket socket = new DatagramSocket();

    socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

    DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
        bytesToSend.length, serverAddress, servPort);

    DatagramPacket receivePacket =                              // Receiving packet
        new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

    int tries = 0;      // Packets may be lost, so we have to keep trying
    boolean receivedResponse = false;
    long startTime = 0;
    do {
      socket.send(sendPacket);          // Send the echo string
      //--------------------------New Code--------------------------------<
      startTime = System.currentTimeMillis();
      //--------------------------New Code-------------------------------->
      try {
        socket.receive(receivePacket);  // Attempt echo reply reception
        //--------------------------New Code--------------------------------<
        System.out.println("Duration: " + (System.currentTimeMillis() - startTime) + "ms");
        //--------------------------New Code-------------------------------->

        if (!receivePacket.getAddress().equals(serverAddress))  // Check source
          throw new IOException("Received packet from an unknown source");

        receivedResponse = true;
      } catch (InterruptedIOException e) {  // We did not get anything
        tries += 1;
        System.out.println("Timed out, " + (MAXTRIES-tries) + " more tries...");
      }
    } while ((!receivedResponse) && (tries < MAXTRIES));

    if (receivedResponse){
      System.out.println("Received: " + new String(receivePacket.getData()));
    }
    else{
      System.out.println("No response -- giving up.");
    }
    socket.close();
  }
}
