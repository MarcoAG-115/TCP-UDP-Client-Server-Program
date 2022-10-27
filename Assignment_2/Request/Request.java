public class Request 
{

	public byte ID;            // Item identification number
	public byte code;
	public byte numOp;
	public short opOne;
	public short opTwo;
	public byte totalLength;
	


	public Request(byte in_ID, byte op_code, byte num_Op, short op_1, short op_2)  
    {
		this.ID = in_ID;
		this.code = op_code;
		this.numOp = num_Op;
		this.opOne = op_1;
		this.opTwo = op_2;
		this.totalLength = 8;
		
	}

	public String toString() 
    {
		final String EOLN = java.lang.System.getProperty("line.separator");
		String value = "Operation # = " + ID + EOLN +
			"Number of Operands = " + numOp + EOLN +
			"First Operand  = " + opOne + EOLN +
			"Two Operand = " + opTwo + EOLN;
		switch (code)
        {
			case 0:
				value += "Operation = Sum\n";
				break;
			case 1:
				value += "Operation = Difference\n";
				break;
			case 2:
				value += "Operation = Product\n";
				break;
			case 3:
				value += "Operation = Quotient\n";
				break;
			case 4:
				value += "Operation = Shift Right\n";
				break;
			case 5:
				value += "Operation = Shift Left\n";
				break;
			case 6:
				value += "Operation = NOT/Compliment";
				break;
			default:
				value += "Operation not supported"; 
				break;

		}

		return value;
	}
}
