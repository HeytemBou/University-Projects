import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class DetectAesInEcb {
	
	public static void detectECB(String url,int block_size) {
    // recieve an arraylist of the ciphertexts in byte arrays then we store them in an arrayList
	ArrayList<byte[]> ciphertexts = readEncryptedFile(url);
	int max_duplicates = 0 ;
	byte[] ecb_cipher_text=null;
	
	//split each ciphertext into chunks of equal length 
	for(byte[] element : ciphertexts) {
		ArrayList<byte[]> chunks = new ArrayList<byte[]>();
		for(int i=0;i<element.length;i=i+block_size) {
		if(element.length-1<block_size) {
			chunks.add(Arrays.copyOfRange(element, i, element.length));
			break;
		}
		chunks.add(Arrays.copyOfRange(element, i, i+block_size));
		}
		//count the duplicates by calling countDuplicateChunks and passing the chunks of the ciphertext
		int duplicates = countDuplicateChunks(chunks);
		if(duplicates>max_duplicates) {
			//store the ciphertext with the most duplicates as well as the number of duplicates
			max_duplicates= duplicates;
			ecb_cipher_text = element;
		}
	}
	    System.out.println("ciphertext :  "+ new String(Hex.encodeHex(ecb_cipher_text)));
	  
	
		
	}
	
	//read the file and store ciphertexts in arraylist
	public static ArrayList readEncryptedFile(String url) {
		{  
			try {
			
			File file=new File(url);    
			FileReader fr=new FileReader(file);    
			BufferedReader br=new BufferedReader(fr);  
			StringBuffer sb = new StringBuffer();
		    String line ;
			ArrayList<byte[]> lines_bytes = new ArrayList<byte[]>() ;
		
			while((line=br.readLine())!=null)  
			{  
			lines_bytes.add(Hex.decodeHex((line)));			
			}
			fr.close();
			return lines_bytes;
			}catch(FileNotFoundException e) {
				System.out.println(e.getMessage());
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} catch (DecoderException e) {
				System.out.println(e.getMessage());
			}
			return null;		
	}
	}
	
	//count duplicate blocks in a ciphertext passed as an arraylist of bytes
	public static int countDuplicateChunks(ArrayList<byte[]> text_byte_chunks) {
		
		//hashmap to store the number of duplicates for each block in the ciphertext
		HashMap<byte[],Integer> bytes_and_counts = new HashMap<byte[],Integer>();
		int current_duplicate_counts = 0 ;
		
		//loop through the ciphertext blocks and check for duplicates
		for(byte[] element : text_byte_chunks) {
			if(!alreadyCounted(bytes_and_counts,element)) {
				for(int i=0;i<text_byte_chunks.size();i++) {
					//if there is a duplicate increment the counter 
					if(Arrays.equals(text_byte_chunks.get(i), element)) current_duplicate_counts++;
				}
				current_duplicate_counts--;
				//put the block and its count in the hashmap
				bytes_and_counts.put(element, current_duplicate_counts);
			}
			
		}
		
		//count the total number of duplicates
		int sum = bytes_and_counts.values().stream().reduce(0, Integer::sum);
		return sum;		
	}
	
	//check if a hashmap contains an element
	public static boolean alreadyCounted(HashMap<byte[],Integer> hm,byte[] element) {
		Iterator hmIterator = hm.entrySet().iterator();
		while(hmIterator.hasNext()) {
			Map.Entry hm_element = (Map.Entry)hmIterator.next();
			if(Arrays.equals(element, (byte[])hm_element.getKey())) return true ;
		
		}
		return false ;	
		
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url ="src\\detect-aes-ecb.txt";
		detectECB(url,16);
		

	}

}
