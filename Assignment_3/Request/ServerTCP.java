import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

//Programming Assignment 3
//Editted by: Marco Gonzalez & William Baldwin
//12-03-2021
//COMP 4320

public class ServerTCP                                                              
{                                                                               
	private static Socket socket;
    private static ServerSocket servSocket;
    private static ObjectOutputStream servMess = null; 
    private static ObjectInputStream clientInput = null;  
	public static void main(String args[]) throws Exception                       
	{                                                                            
		//Checks that only one argument is provided (Group Port Number 10010 + 3 = 10013).
        if (args.length != 1 && args.length != 2)  
        {      
              throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");
        }
  
        //Port number argument is asssigned to recvPort.
        int recvPort = Integer.parseInt(args[0]);

        try
        {
            servSocket = new ServerSocket(recvPort);

            //This while loop will always run. This is what allows the server to always be ready to
            //  receive a message from a client.
            while(true)
            {
                //The server waits and accepts any message from a client. The client's message is read into
                //  an existing byte array.
                //The byte array containing the client's input is converted and decoded into a string.
                socket = servSocket.accept();
                servMess = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                clientInput = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                byte[] response = new byte[100];
                clientInput.read(response);
                String line = new String(response, "UTF-16BE");
                System.out.println("Received the number.\n");

                //The server performs the conversions of the given number and displays them.
                //The hexadecimal conversion is converted and encoded as a byte array in UTF-16BE.
                //This byte array is sent to the client.
                line = performRequest(line);
                System.out.println("\nAttempting to send.");
                byte[] encodedMessage = line.getBytes("UTF-16BE");
                servMess.write(encodedMessage);
                servMess.flush();
                System.out.println("Successfully sent");
                System.out.println("------------------------------------------------------------------------------------------\n");
            }
        }
        catch(IOException e)
        {
            
        }



	} 

    //The string received is checked to ensure that it is an integer.
    //Decimal format (the data as it was received) is printed first.
    //Next, the number is converted from decimal to hexadecimal, and this is printed.
    //Lastly, the hexadecimal number is printed one byte at a time.
    //Theis method returns the hexadecimal number so that it can be sent back to the client.
	private static String performRequest(String p) throws IOException 
    {
        String stringResult = p;
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
        System.out.println("\nReturning result number in hexadecimal format: ");
        System.out.println(hexResult);

        System.out.println("\nReturning result number in hexadecimal format one byte at a time: ");
        
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