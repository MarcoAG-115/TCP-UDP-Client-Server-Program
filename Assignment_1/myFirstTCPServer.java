import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

//Programming Assignment 1
//Editted by: Marco Gonzalez & William Baldwin
//09-24-2021
//COMP 4320

public class myFirstTCPServer {

  private static final int BUFSIZE = 32;   // Size of receive buffer

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    //--------------------------New Code--------------------------------<
    if ((servPort < 10010) || (servPort > 10200)) {
      throw new IllegalArgumentException("The range of allowed port numbers on Tux Machines is 10010 - 10200");
    }
    //--------------------------New Code-------------------------------->

    // Create a server socket to accept client connection requests
    ServerSocket servSock = new ServerSocket(servPort);

    int recvMsgSize;   // Size of received message
    byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer

    for (;;) { // Run forever, accepting and servicing connections
      Socket clntSock = servSock.accept();     // Get client connection

      System.out.println("Handling client at " +
        clntSock.getInetAddress().getHostAddress() + " on port " +
             clntSock.getPort());

      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

      //--------------------------New Code--------------------------------<
      DataInputStream input = new DataInputStream(new BufferedInputStream(clntSock.getInputStream()));
      String line = "";
      line = input.readUTF();

      System.out.println("The received message was: " + line);
      
      String temp[] = line.split(" ");
      String tempStr = "";
      for (int i = temp.length- 1; i >= 0; i--){
        tempStr += temp[i] + " ";
      }
      System.out.println("The reversed message is: " + tempStr);
      //--------------------------New Code-------------------------------->

      // Receive until client closes connection, indicated by -1 return
      // while ((recvMsgSize = in.read(byteBuffer)) != -1)
      //   out.write(byteBuffer, 0, recvMsgSize);

      //--------------------------New Code--------------------------------<
      while ((recvMsgSize = in.read(byteBuffer)) != -1){
        byte[] bytesToGo = new byte[byteBuffer.length];
        int count = 0;
        for (int i = 0; i < byteBuffer.length; i++){
          if (byteBuffer[i] == 0){
          }
          else{
            bytesToGo[count] = byteBuffer[i];
            count++;
          }
        }
        String newLine = new String(bytesToGo);
        String newTemp[] = newLine.split(" ");
        String newTempStr = "";
        for (int j = newTemp.length- 1; j >= 0; j--){
          newTempStr += newTemp[j] + " ";
        }
        byte[] bytesReversed = newTempStr.getBytes("UTF-16BE");
        byte[] bytesToSend = new byte[32];
        int count2 = 0;
        for (int z = 0; z < bytesReversed.length; z++){
          if (bytesReversed[z] == 0){
          }
          else{
            bytesToSend[count2] = bytesReversed[z];
            count2++;
          }
        }
        out.write(bytesToSend, 0, recvMsgSize);
      }
      //--------------------------New Code-------------------------------->

      clntSock.close();  // Close the socket.  We are done with this client!
    }
    /* NOT REACHED */
  }
}
