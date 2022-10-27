import java.net.*;  // for Socket

import java.io.*;   // for IOException and Input/OutputStream

//Programming Assignment 1
//Editted by: Marco Gonzalez & William Baldwin
//09-24-2021
//COMP 4320

public class myFirstTCPClient {

  public static void main(String[] args) throws IOException {

    if ((args.length < 2) || (args.length > 3))  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

    String server = args[0];       // Server name or IP address
    // Convert input String to bytes using the default character encoding
    //byte[] byteBuffer = args[1].getBytes("UTF-16BE");

    //int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

    //--------------------------New Code--------------------------------<
    int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 10010;
    //--------------------------New Code-------------------------------->


    //--------------------------New Code--------------------------------<
    if ((servPort < 10010) || (servPort > 10200)) {
      throw new IllegalArgumentException("The range of allowed port numbers on Tux Machines is 10010 - 10200");
    }
    //--------------------------New Code-------------------------------->

    // Create socket that is connected to server on specified port
    Socket socket = new Socket(server, servPort);
    System.out.println("Connected to server...sending echo string");

    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();

    //--------------------------New Code--------------------------------<
    System.out.println("Enter a sentence: ");
    String line = "";
    DataInputStream input = new DataInputStream(System.in);
    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
    line = input.readLine();
    output.writeUTF(line);

    byte[] byteBuffer = line.getBytes("UTF-16BE");
    //--------------------------New Code-------------------------------->

    out.write(byteBuffer);  // Send the encoded string to the server

    //--------------------------New Code--------------------------------<
    long startTime = System.currentTimeMillis();
    //--------------------------New Code-------------------------------->

    //Receive the same string back from the server
    int totalBytesRcvd = 0;  // Total bytes received so far
    int bytesRcvd;           // Bytes received in last read
    while (totalBytesRcvd < byteBuffer.length) {
      if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,  
                        byteBuffer.length - totalBytesRcvd)) == -1)
        throw new SocketException("Connection close prematurely");
      totalBytesRcvd += bytesRcvd;
    }
    //--------------------------New Code--------------------------------<
    System.out.println("Received: " + new String(byteBuffer));
    System.out.println("Duration: " + (System.currentTimeMillis() - startTime) + "ms");
    //--------------------------New Code-------------------------------->

    //System.out.println("Received: " + new String(byteBuffer));

    //--------------------------New Code--------------------------------<
    input.close();
    output.close();
    //--------------------------New Code-------------------------------->
    socket.close();  // Close the socket and its streams
  }
}
