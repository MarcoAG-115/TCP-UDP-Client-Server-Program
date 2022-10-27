import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket
import java.net.DatagramPacket;

public class RequestDecoderBin implements RequestDecoder, RequestBinConst 
{

  public Request decode(InputStream wire) throws IOException {
    DataInputStream src = new DataInputStream(wire);
    byte ID = src.readByte();
    byte totalLength = src.readByte();
    byte op_code = src.readByte();
    byte num_Op = src.readByte();
    short opOne = src.readShort();
    short opTwo = src.readShort(); 

    return new Request(ID, op_code, num_Op, opOne, opTwo);
  }

  public Request decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload);
  }
}
