package simpleTools;


public class stringMultiply {
	
	private static int[] convertString2IntArray( String input_num_str , int str_len ){
		int[] res_array = new int[str_len];
		for(int i=0; i < str_len; i++)
			res_array[str_len-i-1] = (input_num_str.charAt(i)-'0');
	
		return res_array;
	}
	
	public static String str_pos_multiply( String num_a, String num_b ){
		String res_string = "";
		
		int len_a = num_a.length();
		int len_b = num_b.length();
		int len_res = len_a + len_b;
		
		int[] array_a = convertString2IntArray(num_a, len_a);
		int[] array_b = convertString2IntArray(num_b, len_b);
		
		int[] array_res = new int[len_a+len_b];
		
		for(int i=0; i < len_b; i++)
			for(int j=0; j < len_a; j++)
				array_res[i+j]+=array_a[j]*array_b[i];
		
		int current_carry = 0;
		for(int i=0; i < len_res; i++){
			int val = array_res[i] + current_carry;
			if(val==0)
				continue;
			array_res[i] = val%10;
			current_carry = val/10;
		}
		
		int highest_bit = len_res;
		while(highest_bit>1 && array_res[highest_bit-1]==0)
			highest_bit--;
		
		StringBuilder myBuilder = new StringBuilder(highest_bit);
		for(int k=highest_bit-1; k>=0; k--)
			myBuilder.append((char) ('0'+array_res[k]));
			
		
		res_string = myBuilder.toString();
		
		return res_string;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		String reString = str_pos_multiply("0", "0");
		
		
		System.out.println(reString);
	}

}
