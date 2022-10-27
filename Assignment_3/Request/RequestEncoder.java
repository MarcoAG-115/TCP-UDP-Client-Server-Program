public interface RequestEncoder {
  byte[] encode(Request op) throws Exception;
}
