
import java.util.*;
import org.apache.commons.codec.binary.Hex;

public class EcbCbcOracle {


	//generate a random aes key of specific length
	public static byte[] generateAESKey(int key_length) {
		
		byte[] key_bytes = new byte[key_length];
		//fill the array with random bytes
		new Random().nextBytes(key_bytes);
		return key_bytes ;	
		
	}
	
	
	//this method appends random bytes to the plain text
	public static byte[] appendText( byte[] text_bytes) {
		
		int max =10;
		int min= 5;
		
		//generate the left padding size
		int left_bytes_count = (int)( Math.random()*(max - min + 1) + min);
		System.out.println("left pad : "+left_bytes_count);
		
		//generate the right padding size
		int right_bytes_count = (int)( Math.random()*(max - min + 1) + min);
		System.out.println("right pad :"+right_bytes_count);
		
		byte[] left_append_bytes = new byte[left_bytes_count];
		
		
		byte[] right_append_bytes = new byte[right_bytes_count];
		
		//generate random bytes for the padding
		new Random().nextBytes(left_append_bytes);
        new Random().nextBytes(right_append_bytes);		
        
		byte[] appended_text_bytes = new byte[text_bytes.length+left_bytes_count+right_bytes_count];
		
		//append the bytes to the plaintext
		System.arraycopy(left_append_bytes, 0, appended_text_bytes, 0, left_bytes_count);
		System.arraycopy(right_append_bytes, 0, appended_text_bytes, appended_text_bytes.length-right_bytes_count-1, right_bytes_count);
		System.arraycopy(text_bytes, 0, appended_text_bytes, left_append_bytes.length, text_bytes.length);
		
		
		return appended_text_bytes;
		
	}
	
	//generate a random init vector
	public static byte[] generateInit(int init_size) {
		byte[] init_bytes = new byte[init_size];
		new Random().nextBytes(init_bytes);
		return init_bytes ;	
	}
	
	//perform ECB or CBC encryption on the plaintext
	public static ArrayList<Object> secretCipher(byte[] plain_text, byte[] key_bytes){
		 int min = 0;
	     int max = 1;
	     //determine which mode to use 
		 int modeToUse =(int)( Math.random()*(max - min + 1) + min); //0 for ECB mode , 1 for CBC mode
		 ArrayList<Object> result = new ArrayList<Object>() ;
		 switch(modeToUse) {
		 case 0 :
			 result.add(AES_in_ECB_mode.encryptAesInEcb( key_bytes,plain_text));
			 result.add(modeToUse); 
			 System.out.println("mode used is ECB");
			 return result;
			 
		 case 1 :
			 try {
				result.add(CBCMode.AESInCBCMode(plain_text, key_bytes, generateInit(key_bytes.length), 0));
				System.out.println("mode used is CBC");
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			    result.add(modeToUse);
			 return result;
		 }
		 
		
		return null ;
	}
	
	public static boolean encryptionOracle(int modeUsed , byte[] ciphertext,int key_length) {
		
		//we already know how to detect ECB
		//check if the ciphertext contains duplicates
		int predicted_mode;
		//1. split the cypher texts into chunks of equal length
		ArrayList<byte[]> ciphertext_chunks = new ArrayList<byte[]>();
		for(int i=0;i<ciphertext.length;i=i+key_length) {
			ciphertext_chunks.add(Arrays.copyOfRange(ciphertext, i, i+key_length));
		
		}	
		
		//2. count the number of duplicates, if there are 2 or more duplicates then it's likely ECB
		int duplicates_count = DetectAesInEcb.countDuplicateChunks(ciphertext_chunks);
		if(duplicates_count>=2) {
			predicted_mode = 0;
			System.out.println("guess : ECB");
		}else {
			predicted_mode =1 ;
			System.out.println("guess : CBC");
		}
		
	    //3. test if the oracle's prediciton was true or not	
		return predicted_mode == modeUsed ;
	}
	
	public static void main(String[] args) {
		String input ="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		
		  
		  // random padding before and after the plaintext
		  byte[] input_bytes = appendText(input.getBytes());
		  System.out.println("input size after the random padding :"+input_bytes.length);
		  //PKCS padding
		  input_bytes = PKCS_padding.pkcs_padding(input_bytes, 16);
		  System.out.println("input size after the pkcs padding "+input_bytes.length);
		
		  
		  //feed input to the oracle and keep track of its performance
		int correct_guess=0;
		int attempts = 20 ;
		for(int i=0 ;i<attempts;i++) {
			System.out.println("Attempt number : "+i);
			ArrayList<Object> arr_l ;
			arr_l = secretCipher(input_bytes,generateAESKey(16));
			boolean prediction = encryptionOracle((int)arr_l.get(1),(byte[])arr_l.get(0),16);
			if(prediction)correct_guess++;
		}
		System.out.println("");
		System.out.println("oracle was right "+correct_guess+" times.");
		System.out.println("percentage of correct guesses "+((float)correct_guess/(float)attempts)*100);
		System.out.println("input size before the random padding :"+input.getBytes().length);
		System.out.println("input size after the full padding "+input_bytes.length);
	}

}
