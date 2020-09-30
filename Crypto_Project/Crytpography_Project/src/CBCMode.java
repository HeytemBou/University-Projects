import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class CBCMode {
	
	public static void officialCBC(byte[] textBytes, final byte[] cipherKeyBytes,final byte[] iv) {
	
			//read the file and return its content in the form of a byte array
			try {
				
				Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
				Key aes_key = new SecretKeySpec(cipherKeyBytes,"AES");
				
				try {
					cipher.init(Cipher.DECRYPT_MODE, aes_key, new IvParameterSpec(iv));
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				byte[] encrypted_message=cipher.doFinal(textBytes);
				
			        System.out.println(new String(encrypted_message));
				
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
			
			
			
		
		
	}
	

	public static byte[] AESInCBCMode(byte[] text_bytes,byte[] key_bytes,byte[] init_v,int cipher_mode) throws Exception 
	{
		//check if the cipher key and the init vector are of the same length, if not throw and exception
		if(key_bytes.length!=init_v.length) {
			throw new Exception("the cipher key and the init vector must have the same length !!");
		}
		//check if the plain_text is correctly padded
		if(text_bytes.length % key_bytes.length !=0) {
			throw new Exception("the text must be padded");
		}
		
		byte[] previous_block = init_v ;
		byte[] current_block;
		byte[] output_bytes = new byte[text_bytes.length];
		
		for(int i=0;i<text_bytes.length;i=i+init_v.length) {
			 current_block = Arrays.copyOfRange(text_bytes, i, i+init_v.length);
			
			byte[] xor_result ;
			byte[] aes_ecb_result;
			//0 for encypt mode and 1 for decrypt mode
			if(cipher_mode==0) {
				//xor the current block with the previous cipher(init vector for the first time)
				xor_result = FixedXor.fixed_xor_2(current_block, previous_block);
				//encrypt under ECB mode the xor result with key
				aes_ecb_result = AES_in_ECB_mode.encryptAesInEcb(key_bytes, xor_result);
				//the new encrypted block will be xored against the next block and so on and so forth
				previous_block = aes_ecb_result;
				//append the block to the result ciphertext
				System.arraycopy(aes_ecb_result, 0, output_bytes, i, init_v.length);
				
			}else{
				//decrypt the current block under ECB mode
				aes_ecb_result = AES_in_ECB_mode.decryptAesInEcb(key_bytes, current_block);
				//xor the result against the previously decrypted block
				xor_result = FixedXor.fixed_xor_2(aes_ecb_result, previous_block);
				//the xor result will be used to decrypt the next block
				previous_block = current_block;
				//append the block to the result plaintext
				System.arraycopy(xor_result, 0, output_bytes, i, init_v.length);
				
			}
			
		}
		
		return output_bytes;
		
	}
	public static void main(String[] args) {
		String key ="YELLOW SUBMARINE";
		
		String url="src\\crypto\\cbc-mode.txt";
		byte[] cipher_text = AES_in_ECB_mode.readEncryptedFile(url);
		
		
		//creating the init vector
		byte[] init_v = new byte[key.getBytes().length];
		for(int i =0;i<init_v.length;i++) {
			init_v[i]=(byte)0x00;
		}
		//the ciphertext needs to be padded before decryption
		cipher_text=PKCS_padding.pkcs_padding(cipher_text, key.getBytes().length);
		try {
			
			byte[] result =CBCMode.AESInCBCMode(cipher_text, key.getBytes(), init_v, 1);
			System.out.println(new String(result));
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}

}
