import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
 import org.apache.commons.codec.binary.Hex;

public class HexToBase64 {
	
	//convert from Hex to Base64
	public static String FromHex_ToBase64(String hex_str ) throws DecoderException{
		
		byte[] decodedHex = Hex.decodeHex(hex_str);
		return Base64.encodeBase64String(decodedHex);
		
	}
	//convert from Base64 to Hex
	public static String FromBase64_ToHex(String base64_str) {
		
		byte[] decodedBase64 = Base64.decodeBase64(base64_str);
		return Hex.encodeHexString(decodedBase64);
		
	}
	

	public static void main(String[] args) {
		String hex_buffer1 = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		try {
		//Hex to Base64 conversion
		System.out.println(FromHex_ToBase64(hex_buffer1) );
		//exprected output :SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t
		
		//the encoded String content
		System.out.println(new String (Base64.decodeBase64(FromHex_ToBase64(hex_buffer1))));
		
				
		}catch(DecoderException e) {
			System.out.println(e.getMessage());
			
		}

	}

}
