import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AES_in_ECB_mode {
	public static byte[] readEncryptedFile(String url) {
		{  
			try {
			File file=new File(url);    
			FileReader fr=new FileReader(file);    
			BufferedReader br=new BufferedReader(fr);  
			StringBuffer sb = new StringBuffer();
			String line ;
			String text="";
			
			while((line=br.readLine())!=null)  
			{  
			sb.append(line);
			sb.append("\n");
			text = text+line;
			
			}
			
			byte[] content_bytes = null ;
			//base64-decode the text 
	        content_bytes = Base64.decodeBase64(sb.toString());			
				fr.close();
				return content_bytes;
			}catch(FileNotFoundException e) {
				System.out.println(e.getMessage());
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} 
			
			return null;
		
		
	}
	}
	
	
	
	public static byte[] decryptAesInEcb(byte[] key,byte[] message) {
		//read the file and return its content in the form of a byte array
		try {
			
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			Key aes_key = new SecretKeySpec(key,"AES");
			
			//decrypt mode
			
			cipher.init(Cipher.DECRYPT_MODE, aes_key);
			
			return cipher.doFinal(message);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch(IllegalBlockSizeException | BadPaddingException e) {
			System.out.println(e.getMessage());
			
		}
		return null;
		
		
	}
	    public static byte[] encryptAesInEcb(byte[] key , byte[] message) {
		      
	             	try {
					Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
					//create the AES key
					Key aes_key = new SecretKeySpec(key,"AES");
					
					//decrypt mode
					cipher.init(Cipher.ENCRYPT_MODE, aes_key);
					
					//decrypt the message
					byte[] decrypted_message=cipher.doFinal(message);
					
						
				     return decrypted_message;
					
				} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
					System.out.println(e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					e.printStackTrace();
				} catch(IllegalBlockSizeException | BadPaddingException e) {
					System.out.println(e.getMessage());
					
				}
				return null;
				
		
	}

	public static void main(String[] args) {
		
		String key ="YELLOW SUBMARINE";
		byte[] encrypted_message = readEncryptedFile("src\\crypto\\aes-in-ecb.txt");
		System.out.println(new String(decryptAesInEcb(key.getBytes(),encrypted_message)));

	}

}
