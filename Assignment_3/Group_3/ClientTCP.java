import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

//Programming Assignment 3
//Editted by: Marco Gonzalez & William Baldwin
//12-03-2021
//COMP 4320

public class ClientTCP
{                                                                               

    private static Socket socket = null;
    private static ObjectOutputStream clientMess = null;
    private static ObjectInputStream servOutput = null;
	public static void main(String args[])throws Exception                  
	{                                                                       
		String line = "";                                           
        try
        {
            //Checks that only two arguments are provided. (IP address of machine running server & Group Port Number 10010 + 3 = 10013)
            if (args.length != 2 && args.length != 3)        
                throw new IllegalArgumentException("Parameter(s): <Destination> <Port> [encoding]");

            InetAddress destAddr = InetAddress.getByName(args[0]);
            int destPort = Integer.parseInt(args[1]);

            //This while loop will always repeat until the user provides "0" as an input.
            //This allows the program to always prompt the user for the next number they would like to enter.
            while(true) 
            {
                socket = new Socket(destAddr, destPort);
                clientMess = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                line = getRequest();
                int checkNum = Integer.parseInt(line);
                if(checkNum == 0)
                {
                    break;
                }
                else
                {
                    //The string containing the number is converted to a byte array that is encoded using UTF-16BE
                    //This byte array is sent to the server to be decoded and to perform conversions.
                    byte[] encodedMessage = line.getBytes("UTF-16BE");
                    clientMess.write(encodedMessage);
                    clientMess.flush();

                    //The response from the server is read by populating a byte array with the encoded bytes from the server.
                    //The byte array containing the response is converted and decoded to a string.
                    System.out.println("Waiting for server response.\n");
                    servOutput = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    byte[] response = new byte[100];
                    servOutput.read(response);
                    String resultDecoded = new String(response, "UTF-16BE");
                    System.out.println("Received an answer (hexadecimal conversion): ");
                    System.out.println(resultDecoded);
                    System.out.println("------------------------------------------------------------------------------------------\n");
                }
            }
        }
        catch(IOException e)
        {
            
        }
        finally
        {
            if(socket != null)
            {
                try
                {
                socket.close();
                }
                catch(IOException e)
                {

                }
            }
        }

	}

    //Prompts the user for a number (Integer).
	//Checks if the input provided is a valid integer.
	//Returns the number if valid.
	public static String getRequest()
    {
		String number = "0";
		System.out.println("Please enter your number (Enter 0 to exit the program)");
		Scanner in = new Scanner(System.in);
		number = in.nextLine();
		
		if (isANum(number)) 
    	{

		} 
		else 
    	{
			System.out.println("No valid number was entered");
		}
	
		return number;

	}

    //Checks if the input provided is a valid integer.
	//Throws NumberFormatException if input is not valid.
	private static boolean isANum(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
}  