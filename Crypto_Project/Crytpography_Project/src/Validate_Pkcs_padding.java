import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

public class Validate_Pkcs_padding {
	
	
	public static byte[] StripPkcsPadding(byte[] text_bytes) throws Exception {
		
		//get the padding size
		byte the_pad_byte = text_bytes[text_bytes.length-1];
		
		//check if the padding is valid
		for(int i=0; i<the_pad_byte;i++) {
			if(text_bytes[text_bytes.length-the_pad_byte+i]!=the_pad_byte) {
				throw new Exception("invalid padding :"+Hex.encodeHexString(text_bytes));
			}
		}
		
		
		return Arrays.copyOfRange(text_bytes, 0, text_bytes.length-the_pad_byte);
	}

	public static void main(String[] args) {
		 String s ="YELLOW SUBMARINE";
		 byte[] bs = PKCS_padding.pkcs_padding(s.getBytes(),20);
		System.out.println("after padding : "+Hex.encodeHexString(bs));
		try {
			System.out.println("after stripping the padding : "+Hex.encodeHexString(StripPkcsPadding(bs)));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
