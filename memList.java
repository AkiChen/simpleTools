package simpleTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class memList {
	public ArrayList<String> mem_list;
	public memList( String input_file_absolute_path ){
		this.mem_list = new ArrayList<String>();
		File mem_list_file = new File(input_file_absolute_path);
		if(!mem_list_file.exists())
			return;
		else{
			FileReader reader;
			try {
				reader = new FileReader(input_file_absolute_path);
				BufferedReader br = new BufferedReader(reader);
				
				String buffer_string = null;
				while((buffer_string = br.readLine()) != null){
					String tmp_str = buffer_string.trim();
					this.mem_list.add(tmp_str);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public memList(){
		this.mem_list = new ArrayList<String>();
	}
	
	@SuppressWarnings("unchecked")
	public static memList copyFromArrayList( ArrayList<String> input_array){
		memList my_list = new memList();
		my_list.mem_list = (ArrayList<String>) input_array.clone();
		
		return my_list;
	}
	
	public void print(){
		System.out.println("####################################################");
		System.out.println("This list has "+String.valueOf(mem_list.size())+" elements.");
		System.out.println("####################################################");
		
		for( String each_ele : this.mem_list ){
			System.out.println(each_ele);
			System.out.println("####################################################");
		}
	}
	
	public void saveInAbsoulutePath( String save_path ){
		
		try {
			FileWriter writer = new FileWriter( save_path );
			BufferedWriter bw = new BufferedWriter(writer);
			
			for(String each_ele : this.mem_list){
				bw.write(each_ele);
				bw.write('\n');
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getLength(){
		return this.mem_list.size();
	}
	
	public String getElementAt( int idx ){
		return this.mem_list.get(idx);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*memList my_list = new memList("C:\\Users\\PayenJoe\\Desktop\\A-small-practice.in");
		my_list.print();
		my_list.saveInAbsoulutePath("C:\\Users\\PayenJoe\\Desktop\\A2.in");*/
		
	}

}
