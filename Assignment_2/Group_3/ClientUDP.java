import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ClientUDP {

	public static void main(String args[]) throws Exception 
  {

		if (args.length != 2 && args.length != 3)  // Test for correct # of args        
			throw new IllegalArgumentException("Parameter(s): <Destination> <Port> [encoding]");


		InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
		int destPort = Integer.parseInt(args[1]);               // Destination port
		int recPort = 10013;
		byte ID = 0;
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
			//RequestEncoderBin encoder = new RequestEncoderBin();
			//byte[] operationHeader = encoder.encode(op);
			byte[] operationHeader = content.getBytes("UTF-16BE");
			long startTime = System.nanoTime();
			if(sendPacket(sock, destAddr, destPort, operationHeader)) 
      		{	
				System.out.println("Sent number: " + content);
			}
			DatagramPacket answerPacket = receivePacket(sock);
			long endTime = System.nanoTime();
			System.out.println("Received a answer packet (hexadecimal conversion): ");
			//handleAnswer(answerPacket);
			byte[] buffer = answerPacket.getData();
			//System.out.println(Arrays.toString(buffer));
			System.out.println(new String(buffer));
			long elaspedTime = endTime - startTime; 
			long elaspedTimeNoServer = endTime - startTime - 2000000000; //two is the server wait time
			System.out.println("Total Elapsed Time: " + elaspedTime / 1000000 + " ms");
			System.out.println("Elasped Time without server wait: " + elaspedTimeNoServer / 1000000 + " ms");
			System.out.println("----------------------------------------------------------------------------");
			sock.close();

		}
	}

	private static boolean sendPacket(DatagramSocket sock, InetAddress ip, int port, byte[]header) 
  	{
		try 
    {
			DatagramPacket outPacket = new DatagramPacket(header, header.length, ip, port);
			sock.send(outPacket);
		} catch (Exception e) 
    {
			return false;
		}
		return true;

	}
	private static DatagramPacket receivePacket(DatagramSocket sock) throws Exception 
  {

		DatagramPacket packet = new DatagramPacket(new byte[100],100);
		System.out.println("Awaiting number");
		sock.receive(packet); 
		return packet;

	}

	public static String getRequest(byte ID)
  	{
		//Request retOp;
		String operation = "0";
		short operand1 = 0;
		short operand2 = 0;
		byte op_code;
		System.out.println("Please enter your number (Enter 0 to exit the program)");
		Scanner in = new Scanner(System.in);
		operation = in.nextLine();
		
		if (isANum(operation)) 
    	{

		} 
		else 
    	{
			System.out.println("No valid number was entered");
		}
	
		return operation;

	}
	// private static void handleAnswer(DatagramPacket p) throws IOException 
  	// {
	// 	byte[] buffer = p.getData();
	// 	System.out.println(Arrays.toString(buffer));
	// 	ByteArrayInputStream payload =
	// 		new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
	// 	DataInputStream src = new DataInputStream(payload);
	// 	byte length = src.readByte();
	// 	byte id = src.readByte();
	// 	byte errorCode = src.readByte();
	// 	int answer = src.readInt();
	// 	System.out.println("Answer for request ID number " + id + ": " + answer);

	// }

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
