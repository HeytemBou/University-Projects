import org.apache.commons.codec.*;
import org.apache.commons.codec.binary.Hex;
import java.nio.*;
import java.nio.charset.Charset;
import java.util.*;
public class SingleByteXorCypher {

	public static ArrayList<String> SingleByteXor(String encrypted_message) throws DecoderException {
		
		    
		    HashMap<Character, String> all_decryptions = new HashMap<Character, String>();
		    ArrayList<String> results = new ArrayList<String>();
		    
		    //xor the message against all 256 possible characters and store the decryptions
			for(int i =0;i<256;i++) {
			all_decryptions.put((char)i,FixedXor.fixed_xor(encrypted_message,duplicateKey((char)i,encrypted_message.length())) );
		  
			
			}
			return getTextWithBestScore(all_decryptions);
	}
	public static String duplicateKey(char key,int times) {
		StringBuilder sb = new StringBuilder();
		String newKey="";
				
		for(int i=0;i<times;i++) {
		sb.append(key);
		}
		
		return Hex.encodeHexString(sb.toString().getBytes());
		
	}
	
	public static ArrayList<String> getTextWithBestScore(HashMap<Character,String> all_decryptions) throws DecoderException  {
		 ArrayList<String> result = new ArrayList<String>();
		 double best_score_so_far =999999;
		 char key = 0;
		 String hex_encoded_decrypted_message="";
		 HashMap<Character,Double> letters_frequencies = new HashMap<Character,Double>();
		 
		//get the english letter frequencies and store them
		 getLettersFrequencies(letters_frequencies);
		 
		 //getting an iterator to traverse the hashmap
		 Iterator decryptions_iterator = all_decryptions.entrySet().iterator();
		 
		 //iterating through the decryptions and compute the score for each entry
		 while(decryptions_iterator.hasNext()) {
			 
			 Map.Entry single_decryption_element = (Map.Entry)decryptions_iterator.next();
			//compute the score of this entry
			double score = computeTextScore((String)single_decryption_element.getValue(),letters_frequencies);
			//compare it with the best score found so far
			if(score<best_score_so_far) {
				 key = (char)single_decryption_element.getKey();
				 hex_encoded_decrypted_message=(String)single_decryption_element.getValue();
				 best_score_so_far=score;
				
			}
			
		 }
		
		 //return the message
		 result.add(Character.toString(key));
		 result.add(hex_encoded_decrypted_message);
		 result.add(Double.toString(best_score_so_far));
		 
		 
		 
		  return result;
		
		 
		
	}
	
	
	public static double computeTextScore(String hex_encoded_message,HashMap<Character,Double> letters_frequencies) {
		double score=0;
		try {
		byte[] message_bytes = Hex.decodeHex(hex_encoded_message);
		String message_string = new String(message_bytes);
		
		HashMap<Character,Double> message_bytes_letters_frequencies = getMessageLettersFrequencies(message_bytes);
		for(char c='a';c<='z';c++) {
			//get the actual frequency of the letter in the english language
			double actual_frequency =(double)message_bytes_letters_frequencies.get(c);
			//get the letter frequency 
			double expected_frequency=(double)letters_frequencies.get(c);
	        //compute the score using  chi-squared formula
			score =(Math.pow((actual_frequency-expected_frequency),2)/expected_frequency) + score;
			
		}
		return score;
		}catch(DecoderException e) {
			System.out.println(e.getMessage());
		}
		
		
		return 0;
		
	}
	
	
	public static HashMap<Character,Double> getMessageLettersFrequencies(byte[] message_bytes) {
		
		HashMap<Character,Double> message_letters_frequencies = new HashMap<Character,Double>();
		int space_count = 0 ;
		for(char c='a';c<='z';c++) {
			//for each english letter look for its frequency in the message
			double letter_count=0;
			for(int i=0;i<message_bytes.length;i++) {
				//loop through the message bytes
				//test if this character is an english letter
				if(Character.isLetter((char)message_bytes[i])) {
					char y = Character.toLowerCase((char)message_bytes[i]);
					if(y==c) {
						//count the occurences 
						letter_count++;							
					}	
				}
			}
			//add the letter and its frequency to the hashmap
			message_letters_frequencies.put(c, letter_count);
			
		}
		
		
		//return a hashmap containing the letters frequencies of the text passed as parameter
		return message_letters_frequencies;
		
	}
	
	
	
	public static void main(String[] args) throws DecoderException {
		ArrayList<String> result = new ArrayList<String>();
		
		
		String encrypted_message="1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		result=SingleByteXor(encrypted_message);
		try {
			System.out.println("hex-encoded message :"+new String(result.get(1)));
			System.out.println("message : "+new String(Hex.decodeHex(result.get(1))));
			System.out.println("the key is : "+result.get(0));
			
		}catch(DecoderException e) {
			
		}
		
		

	}
	
	public static void getLettersFrequencies(HashMap letters_frequencies) {
		//english letter frequencies , source :https://www.sttmedia.com/characterfrequency-english
		letters_frequencies.put('a', 8.34);
		letters_frequencies.put('b', 1.54);
		letters_frequencies.put('c', 2.73);
		letters_frequencies.put('d', 4.14);
		letters_frequencies.put('e', 12.60);
		letters_frequencies.put('f', 2.03);
		letters_frequencies.put('g', 1.92);
		letters_frequencies.put('h', 6.11);
		letters_frequencies.put('i', 6.71);
		letters_frequencies.put('j', 0.23);
		letters_frequencies.put('k', 0.87);
		letters_frequencies.put('l',4.24);
		letters_frequencies.put('m', 2.53);
		letters_frequencies.put('n', 6.80);
		letters_frequencies.put('o', 7.70);
		letters_frequencies.put('p', 1.66);
		letters_frequencies.put('q', 0.09);
		letters_frequencies.put('r', 5.68);
		letters_frequencies.put('s', 6.11);
		letters_frequencies.put('t', 9.37);
		letters_frequencies.put('u', 2.85);
		letters_frequencies.put('v',  1.06);
		letters_frequencies.put('w',2.34);
		letters_frequencies.put('x', 0.20);
		letters_frequencies.put('y', 2.04);
		letters_frequencies.put('z', 0.06 );
		
		
	}

}
