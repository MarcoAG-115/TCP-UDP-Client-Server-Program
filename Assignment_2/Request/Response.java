public class Response {

    public byte ID;
	public byte error;
	public final byte total_length = 7;
	public int result;

	public Response(byte in_ID, byte in_error, int in_result) {
		this.ID = in_ID;
		this.error = in_error;
		this.result = in_result;
	}
}
