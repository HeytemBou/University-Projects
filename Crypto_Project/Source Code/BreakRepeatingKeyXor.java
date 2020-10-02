import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BreakRepeatingKeyXor {
	
	public static int computeHammingDistance(byte[] str1_bytes,byte[] str2_bytes) {
		//we xor the two strings against each other than we compute the number of ones in the result
		int hamming_d = 0 ;
		for(int i=0;i<str1_bytes.length;i++) {
			hamming_d+=Integer.bitCount(str1_bytes[i]^str2_bytes[i]);
		}
		
		return hamming_d ;
	}
	
	
	
	//read the file and return its content in the form of a byte array
	public static byte[] readEncryptedFile(String url) {
		{  
			try {
			File file=new File(url);    
			FileReader fr=new FileReader(file);    
			BufferedReader br=new BufferedReader(fr);  
			StringBuffer sb = new StringBuffer();
			String line ;
			
			while((line=br.readLine())!=null)  
			{  
			sb.append(line);
			sb.append("/n");
			}
			//convert the content of the file to Hex than to byte array using the method we already defined
			byte[] content_bytes = Hex.decodeHex(HexToBase64.FromBase64_ToHex(sb.toString()));
			
			fr.close();
			return content_bytes;
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
	
	public static int getTheKeySize(String fileUrl) {
		byte[] encrypted_content = readEncryptedFile(fileUrl);
	
		HashMap<Integer,Double> keys = new HashMap <Integer,Double>();
		for(int key_size=2;key_size<=40;key_size++) {
			
			
			
			//compute the hamming distance  for the first 4 keysize worth of bytes blocks
			int start =0;
			int end =start+key_size;
			byte[] first_keysize_bytes = Arrays.copyOfRange(encrypted_content, 0, key_size);
			byte[] second_keysize_bytes = Arrays.copyOfRange(encrypted_content, key_size,2*key_size);
			byte[] third_keysize_bytes = Arrays.copyOfRange(encrypted_content, key_size*2, key_size*3);
			byte[] fourth_keysize_bytes = Arrays.copyOfRange(encrypted_content, key_size*3, key_size*4);
			int h_d =computeHammingDistance(first_keysize_bytes,second_keysize_bytes);
			h_d+=computeHammingDistance(first_keysize_bytes,third_keysize_bytes);
			h_d+=computeHammingDistance(first_keysize_bytes,fourth_keysize_bytes);
			h_d+=computeHammingDistance(second_keysize_bytes,third_keysize_bytes);
			h_d+=computeHammingDistance(second_keysize_bytes,fourth_keysize_bytes);
			h_d+=computeHammingDistance(third_keysize_bytes,fourth_keysize_bytes);
			
			//normalize it
			keys.put(key_size,(double)h_d/(double)key_size );
			
		}
		
		//loop through the hashmap and get the smallest hamming distance
		double smallest_edit_distance=9999;
		int true_keysize=0 ;
		Iterator hmIterator = keys.entrySet().iterator();
		while(hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)hmIterator.next();
		    double edit_distance = (double)mapElement.getValue();
		    if(edit_distance<smallest_edit_distance) {
		    	smallest_edit_distance= edit_distance;
		    	true_keysize = (int)mapElement.getKey();
		    			
		    }
		    
		
		}
		System.out.println("the guessed key size :"+true_keysize);
		return true_keysize;
		
		
	}
	
	public static void breakRepeatingKeyXor(int keysize,byte[] cyphertext) {
		  System.out.println("number of bytes in the cypher text :"+cyphertext.length);
		  
		//divide the cyphertext into blocks of keysize length
		ArrayList<byte[]> cyphertext_blocks = new ArrayList<byte[]>();
		for(int i=0;i<cyphertext.length;i=i+keysize) {
			cyphertext_blocks.add(Arrays.copyOfRange(cyphertext, i,i+keysize ));
		}
		System.out.println("number of blocks :"+cyphertext_blocks.size());
		System.out.println("size of each block is :"+cyphertext_blocks.get(0).length);
		
		
		//transpose the blocks
		ArrayList<byte[]> trans_blocks = new ArrayList<byte[]>();
		for(int i=0;i<keysize;i++) {
			byte[] new_block = new byte[cyphertext_blocks.size()];
			int j=0;
			for(byte[] block:cyphertext_blocks) {
				new_block[j]=block[i];
				j++;
			}
			trans_blocks.add(new_block);
		}
		System.out.println("number of transpose blocks:"+trans_blocks.size());
		
		ArrayList<String> arr_l = new ArrayList<String>();
		
		//solve each block using single byte xor cipher
		for(byte[] element:trans_blocks) {
			try {
				arr_l.add(SingleByteXorCypher.SingleByteXor(Hex.encodeHexString(element)).get(0));
				
				
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		String key="";
		for(String s:arr_l) {
			
			key+=s;
		}
		System.out.println("key candidate: "+key);
		try {
			System.out.println("cyphertext after decryption :"+new String(Hex.decodeHex(RepeatingKeyXor.RepeatKeyXorCypher(Hex.encodeHexString(cyphertext), key))));
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

	public static void main(String[] args) {
		String s1 ="this is a test";
		String s2="wokka wokka!!!";
		//System.out.println("hamming distance = "+computeHammingDistance(s1.getBytes(),s2.getBytes()));
		//expected output : 37 ;
		String url="src\\crypto\\repeating-xor.txt";
		//System.out.println(getTheKeySize(url));
		breakRepeatingKeyXor(getTheKeySize(url),readEncryptedFile(url));
		//breakRepeatingKeyXor(,readEncryptedFile(url));
		

	}

}
