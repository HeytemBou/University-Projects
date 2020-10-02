import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class ByteATime {
	
	  //generate a random global key
	  public static final byte[] random_key = EcbCbcOracle.generateAESKey(16);
	
	  //the secret block to be appended
	  public static final String to_append ="Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg\r\n" + 
	  		"aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq\r\n" + 
	  		"dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg\r\n" + 
	  		"YnkK";
	
	   //modified method from the oracle class
  	    public static byte[] modifiedOracle(byte[] plaintext) {
	
		
		byte[] to_append_bytes = Base64.decodeBase64(to_append);
		byte[] new_plaintext = new byte [plaintext.length+to_append_bytes.length];
		
		
		//append the secret block to the end of the plaintext
		System.arraycopy(plaintext, 0, new_plaintext, 0, plaintext.length);
		System.arraycopy(to_append_bytes, 0, new_plaintext,plaintext.length , to_append_bytes.length);
		
		//call the ECB encryption after padding the plaintext
		byte[] ecb_result = AES_in_ECB_mode.encryptAesInEcb(random_key, PKCS_padding.pkcs_padding(new_plaintext, random_key.length));
		
		return ecb_result;
		
		
		
	}
  	    
 
  	    //utility function that count the bytes in array (non empty slots)
  	    public static int countBytes(byte[] arr_l) {
  	    	int count = 0 ;
  	    	for(int i=0 ;i<arr_l.length;i++) {
  	    		if(arr_l[i]==(byte)0) {
  	    			return count ;
  	    			

  	    		}else count ++ ;
  	    		
  	    	}
  	    	
  	    	return count ;
  	    	
  	    }
  	    
  	    public static byte decryptSingleByte(int block_size, byte[] decrypted_message_so_far) {
  	    	
  	    	//get the size of our input
  	    	int probe_length = block_size - ((1+countBytes(decrypted_message_so_far))%block_size);
  	    	
  	    	String Str = "";
  	    	//craft the input
  	    	for(int i=0;i<probe_length;i++) {
  	    		Str=Str+"A";
  	    	}
  	    	byte[] probe = Str.getBytes();

  	    	//move one byte ahead 
  	    	int testing_length = probe_length +(countBytes(decrypted_message_so_far)+1);
  	    	  	    	
  	    	//the dictionary that's gonna contain all possible decryptions
  	    	HashMap<byte[],Byte> byte_dict = new HashMap<byte[],Byte>();
  	    	
  	    	for(int i=0;i<256;i++) {
  	    		//
  	    		byte[] test_data = cancatenateByteArrays(probe,decrypted_message_so_far);
  	    		
  	    		//add that byte to the last position
  	    		test_data[test_data.length-1]=(byte)i;
  	    		//decrypt it
  	    		byte[] test_ciphertext = modifiedOracle(test_data);
  	    	    //put the decryption result in the dictionary as key and the byte as its value
  	    		byte_dict.put (Arrays.copyOfRange(test_ciphertext, 0, testing_length),(byte)i);
 
  	    	}
  	    	    byte[] inter = modifiedOracle(probe);
	    		byte[] comparison_ciphertext=Arrays.copyOfRange(inter, 0, testing_length);
	    		//look for  the ciphertext that matches the one given by the oracle and return its value
	    		return findMatch(byte_dict,comparison_ciphertext);
	    		   		
	    		
	    		
  	    	
  	    }
  	    
  	    
  	    //takes a dictionary and an element , returns the value of the element if it exists and 0 if not
  	  public static byte findMatch(HashMap<byte[],Byte> hm,byte[] element) {
  		Iterator hmIterator = hm.entrySet().iterator();
  		while(hmIterator.hasNext()) {
  			Map.Entry hm_element = (Map.Entry)hmIterator.next();
  			if(Arrays.equals(element, (byte[])hm_element.getKey())) {

  				return (byte)hm_element.getValue() ;
  			}
  		
  		}
  		return (byte)0 ;	
  		
  	}
  	
  	  //cancatenate two arrays , this is just a utility method
  	  public static byte[] cancatenateByteArrays(byte[] arr_1 , byte[] arr_2) {
  		    
			byte[] result = new byte[arr_1.length+countBytes(arr_2)+1];
			System.arraycopy(arr_1, 0, result, 0, arr_1.length); 
			System.arraycopy(arr_2, 0, result, arr_1.length, countBytes(arr_2));
			return result ;
		}

	     public static void byteAtTimeAttack(byte[] ciphertext) {
		
		//part 1 : discovering the block size
		int unknown_message_size=0 ;
		int block_size=0;
		String s ="A";
		String previous_s = "A";
		int current_block_size ;
		int previous_block_size=modifiedOracle(s.getBytes()).length ; ;
		for(int i=0;i<20;i++) {
			current_block_size = modifiedOracle(s.getBytes()).length;
			System.out.println("previous block size :"+previous_block_size);
			System.out.println("current block size :"+current_block_size);
			previous_s = s ;
			if(current_block_size != previous_block_size) {
				//we reached the end of the block size boundary , this indicates that an extra padding block
				//has been added and therefore we can guess the  block size 
				 unknown_message_size =  previous_block_size - s.getBytes().length;
				 block_size=current_block_size-previous_block_size;
				System.out.println("ciphertext size jumped by an extra block which means ");
				System.out.println("block size  : " + (block_size ));
				System.out.println("size of our input  : "+previous_s.getBytes().length);
				break ;
				
			}
			s+="A";
		}
		
		int length_secret = unknown_message_size ;
		byte[] secret = new byte[length_secret];
		
		for(int i=0;i<length_secret;i++) {
			secret[i]=decryptSingleByte(block_size,secret);
		}
		System.out.println("the secret text :");
		System.out.println(new String(secret));
		
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
          byteAtTimeAttack(null);
	}

}
