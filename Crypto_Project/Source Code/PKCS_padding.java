import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class PKCS_padding {
	
	public static byte[] pkcs_padding(byte[] text_bytes,int block_size) {
		int padding_size;
		//if the ciphertext is multiple of the block size ,we're gonna still add an extra block
		if(text_bytes.length%block_size==0) {
			padding_size = block_size;
		}else {
			
			 int n = (text_bytes.length/block_size)+1;
			 padding_size =n*block_size-text_bytes.length;
		}		
		byte[] new_padded_text = new byte[padding_size+text_bytes.length];
		
		//copy the content of the old message byte array into the new padded byte array
		for(int i=0;i<text_bytes.length;i++) {
			new_padded_text[i]=text_bytes[i];
		}
		//add the padding bytes
		for(int i=text_bytes.length;i<new_padded_text.length;i++) {
			new_padded_text[i]=(byte)padding_size;
		}
		
		return new_padded_text ;
	}

	public static void main(String[] args) throws DecoderException {
		// TODO Auto-generated method stub
       String s ="YELLOW SUBMARINE";
       byte[] ss = pkcs_padding(s.getBytes(),20);
       System.out.println("after padding : "+new String(Hex.encodeHex(ss)));
       System.out.println("length before padding : "+s.getBytes().length);
       System.out.println("length after padding : "+ss.length);
	}

}
