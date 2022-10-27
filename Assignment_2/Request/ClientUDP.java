import java.net.*; 
import java.io.*; 
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//Programming Assignment 2
//Editted by: Marco Gonzalez & William Baldwin
//10-30-2021
//COMP 4320

public class ClientUDP {

	public static void main(String args[]) throws Exception 
  {
		//Checks that only two arguments are provided. (IP address of machine running server & Group Port Number 10010 + 3 = 10013)
		if (args.length != 2 && args.length != 3)        
			throw new IllegalArgumentException("Parameter(s): <Destination> <Port> [encoding]");


		InetAddress destAddr = InetAddress.getByName(args[0]);  
		int destPort = Integer.parseInt(args[1]);               
		byte ID = 0;

		//This while loop will always repeat until the user provides "0" as an input.
		//This allows the program to always prompt the user for the next number they would like to enter.
		while(true) 
    	{ 
			
			DatagramSocket sock = new DatagramSocket();
			String content = getRequest(ID);
			content = content.trim();
			int checkNum = Integer.parseInt(content);
			if(checkNum == 0)
			{
				break;
			}
			ID++;
			byte[] operationHeader = content.getBytes("UTF-16BE");
			long startTime = System.nanoTime();
			if(sendPacket(sock, destAddr, destPort, operationHeader)) 
      		{	
				System.out.println("Sent number: " + content);
			}
			DatagramPacket answerPacket = receivePacket(sock);
			long endTime = System.nanoTime();
			System.out.println("Received a answer packet (hexadecimal conversion): ");
			byte[] buffer = answerPacket.getData();
			System.out.println(new String(buffer));
			long elaspedTime = endTime - startTime; 
			long elaspedTimeNoServer = endTime - startTime - 2000000000;
			System.out.println("Total Elapsed Time: " + elaspedTime / 1000000 + " ms");
			System.out.println("Elasped Time without server wait: " + elaspedTimeNoServer / 1000000 + " ms");
			System.out.println("----------------------------------------------------------------------------");
			sock.close();

		}
	}

	//The packet containing the decimal integer that will be received by the server is prepared and sent.
	private static boolean sendPacket(DatagramSocket sock, InetAddress ip, int port, byte[]header) 
  	{
		try 
		{
			DatagramPacket outPacket = new DatagramPacket(header, header.length, ip, port);
			sock.send(outPacket);
		} 
		catch (Exception e) 
		{
			return false;
		}
		return true;

	}

	//Takes received packet and prepares it by ensuring it's formatted correctly (this is the response packet from the server).
	private static DatagramPacket receivePacket(DatagramSocket sock) throws Exception 
	{

		DatagramPacket packet = new DatagramPacket(new byte[100],100);
		System.out.println("Awaiting number");
		sock.receive(packet); 
		return packet;

	}

	//Prompts the user for a number (Integer).
	//Checks if the input provided is a valid integer.
	//Returns the number if valid.
	public static String getRequest(byte ID)
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
