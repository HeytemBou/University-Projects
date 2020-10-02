import org.apache.commons.codec.binary.Hex;

public class RepeatingKeyXor {
	
	public static String RepeatKeyXorCypher(String message,String key) {
		byte[] message_bytes = message.getBytes();
		byte[] key_bytes = key.getBytes();
		int j=0;
		byte[] encrypted_message_bytes=new byte[message_bytes.length];
		//loop through the message bytes
		for(int i=0;i<message_bytes.length;i++) {
		 encrypted_message_bytes[i]=(byte)(message_bytes[i]^key_bytes[j]);
		 j++;
		 
		 //reset the index bytes if it reaches the key length
		 if(j==key_bytes.length) {
			 j=0;
		 }
			
		}
		
		return Hex.encodeHexString(encrypted_message_bytes);
		
		
	}

	public static void main(String[] args) {
		String message ="Burning ’em, if you ain’t quick and nimble\r\n" + 
				"I go crazy when I hear a cymbal";
		String key ="ICE";
		System.out.println(RepeatKeyXorCypher(message,key));
		
		 
	}

}
