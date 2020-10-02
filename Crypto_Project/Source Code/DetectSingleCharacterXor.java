import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
public class DetectSingleCharacterXor {
	
	

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		HashMap<Character,String> dec_results = new HashMap <Character,String>();
		try  
		{  
		File file=new File("src\\crypto\\detecting-singlechar-xor.txt");   
		FileReader fr=new FileReader(file);   //reads the file  
		BufferedReader br=new BufferedReader(fr);  
		double best_score_so_far = 99999;
		String message="" ;
		char key=0 ;
		String line;  
		while((line=br.readLine())!=null)  
		{  
			try {
				ArrayList<String> arr_l2 = new ArrayList<String>();
				//perform single byte xor attack on each line
				arr_l2 = SingleByteXorCypher.SingleByteXor(line);
			   double score = Double.parseDouble((arr_l2.get(2)));
			   //keep track of the best score
			   if(score<best_score_so_far) {
				   //store the message , the key and score
				   message= arr_l2.get(1);
				   best_score_so_far=Double.parseDouble(arr_l2.get(2));
				   key =arr_l2.get(0).charAt(0);
			   }
			    

		       
			}
			catch(DecoderException e) {
				System.out.println(e.getMessage());
				
			}
	
		
		}
		System.out.println(message);
		try {
			Iterator decryptions_iterator = dec_results.entrySet().iterator();
			 
			
		 
		System.out.println("true message  :"+new String(Hex.decodeHex(message)));
		//System.out.println("the key  :"+arr_l.get(0));
		}
		catch(DecoderException e) {
			System.out.println(e.getMessage());
		} 
		fr.close();   
		}  
		catch(IOException e)  
		{  
		e.printStackTrace();  
		}  
		}  

	}


