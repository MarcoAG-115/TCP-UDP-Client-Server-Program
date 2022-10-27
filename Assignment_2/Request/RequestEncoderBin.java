import java.io.*;

public class RequestEncoderBin implements RequestEncoder, RequestBinConst {
	
	public byte[] encode(Request op) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeByte(op.totalLength);
		out.writeByte(op.ID);
		out.writeByte(op.code);
		out.writeByte(op.numOp);
		out.writeShort(op.opOne);
		out.writeShort(op.opTwo);
		
		out.flush();
		return buf.toByteArray();
	}
}