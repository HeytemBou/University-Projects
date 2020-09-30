import org.apache.commons.codec.*;
import org.apache.commons.codec.binary.Hex;

public class FixedXor {
	
	
	public static String fixed_xor(String buffer1,String buffer2) {
	    try{
		byte[] buffer1_bytes = Hex.decodeHex(buffer1);
		byte[] buffer2_bytes = Hex.decodeHex(buffer2);
        byte[] xor_result = new byte[buffer1_bytes.length];
		
		for(int i=0;i<buffer1_bytes.length;i++) {
			xor_result[i]= (byte)(buffer1_bytes[i] ^ buffer2_bytes[i]);
		}
		
		return Hex.encodeHexString(xor_result);
		
	    }catch(DecoderException e) {
	    	System.out.println(e.getMessage());
	    	
	    }
		
		return null;
		
		
	}
	public static byte[] fixed_xor_2(byte[] buffer1_bytes,byte[] buffer2_bytes) {
	 
		
        byte[] xor_result = new byte[buffer1_bytes.length];
		
		for(int i=0;i<buffer1_bytes.length;i++) {
			xor_result[i]= (byte)(buffer1_bytes[i] ^ buffer2_bytes[i]);
		}
		
		return xor_result;
		
		
		
		
	}

	public static void main(String[] args) {
	   String buffer1 ="1c0111001f010100061a024b53535009181c"; 
	   String buffer2 ="686974207468652062756c6c277320657965";
	   System.out.println("result: "+fixed_xor(buffer1,buffer2));
	   try {
		System.out.println(new String (Hex.decodeHex(fixed_xor(buffer1,buffer2))));
	} catch (DecoderException e) {
		
		e.printStackTrace();
	}
		

	}

}
