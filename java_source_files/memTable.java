package simpleTools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;



public class memTable {
	
	public ArrayList<ArrayList<String>> table_in_mem;
	public ArrayList<String> label_row;
	public boolean has_label_row;
	
	//##########################################################################
	
	public HashMap<String, Integer> id_2_rowidx_map;
	public HashSet<String> id_set;
	public int main_key_idx;
	
	//##########################################################################
	
	
	
	public memTable(String load_table_absolute_path, String split_reg){
		int dot_idx = load_table_absolute_path.lastIndexOf('.');
		
		String extesion_name = load_table_absolute_path.substring(dot_idx, load_table_absolute_path.length());
		
		if(extesion_name.equals(".txt"))
			load_table_from_txt_file(load_table_absolute_path, split_reg);
		if(extesion_name.equals(".csv"))
			load_table_from_txt_file(load_table_absolute_path, ",");
		
	}

	public memTable(String load_table_absolute_path){
		this(load_table_absolute_path, "\t");
	}
	
	public memTable() {
		// TODO Auto-generated constructor stub
		this.table_in_mem = new ArrayList<ArrayList<String>>();
	}
	
	@SuppressWarnings("unchecked")
	private void load_table_from_txt_file( String dot_txt_path , String split_reg){
		
    	BufferedReader reader;
    	
		try {
			reader = new BufferedReader(new FileReader(dot_txt_path));
			String line = reader.readLine();//The first Line is assumed to contain column labels.
			String col_labels[] = line.split(split_reg);
			
			
			table_in_mem = new ArrayList<ArrayList<String>>();
			ArrayList<String> label_row = new ArrayList<String>(); 
			
			
			for(int i=0; i < col_labels.length; i++ )
				label_row.add(col_labels[i]);
			
			table_in_mem.add(label_row);
			this.label_row = (ArrayList<String>) label_row.clone();
			
			while((line=reader.readLine())!=null ){
				
	    		String items[] = line.split(split_reg);
	    		ArrayList<String> t_row = new ArrayList<String>(); 
	    		
	    		for(int i=0; i < items.length; i++ )
	    			t_row.add(items[i]);
	    		
	    		table_in_mem.add(t_row);
	    	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't find the file.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in reading file.");
			e.printStackTrace();
		}
		
		return;
	}
	
	public  void save_table_in_txt_file( String save_absolute_path ){
		BufferedWriter bufferedWriter=null;
		try {
			bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(save_absolute_path)));
			int row_count = table_in_mem.size();
			for(int i=1; i <= row_count; i++){
				ArrayList<String> tmp_array_string = table_in_mem.get(i-1);
				int length_of_this_row = tmp_array_string.size(); 
				for(int j=1; j <= length_of_this_row; j++){
					
					bufferedWriter.write(""+tmp_array_string.get(j-1));
					if(j!=length_of_this_row)
						bufferedWriter.write("\t");
				}
				
				if(i!=row_count)
					bufferedWriter.write("\r\n");
			}
			bufferedWriter.flush();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("Falied to open the output file.");
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Falied to save table.");
			e.printStackTrace();
		}
	}
	
	public  int get_length(){
		return table_in_mem.size();
	}
	
	public 	void print(){
		System.out.println("Print table "+"table_length: "+String.valueOf(this.get_length()));
		System.out.println("#########################################################");
		
		int row_size = get_length();
		for(int i=0; i < row_size; i++){
			String row_string = "";
			ArrayList<String> tmp_row = table_in_mem.get(i);
			for(int j = 0 ; j < tmp_row.size(); j++){
				if(j!=0)
					row_string = row_string + "\t";
				row_string = row_string + tmp_row.get(j);
			}
			System.out.println(row_string);
			System.out.println("#########################################################");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public  static memTable convertToMemTable( ArrayList<ArrayList<String>> input_array ){
		memTable res = new memTable();
		res.table_in_mem = input_array;
		res.label_row = (ArrayList<String>) input_array.get(0).clone();
		
		return res;
	}
	
	public 	memTable selectColumnsAt( int[] column_idxs ){
		ArrayList<ArrayList<String>> res_array = new ArrayList<>();
		
		int row_size = this.get_length();
		
		for(int i = 0; i < row_size; i++){
			ArrayList<String> new_row = new ArrayList<>();
			ArrayList<String> t_row = this.table_in_mem.get(i);
			
			for(int j=0; j < column_idxs.length; j++)
				new_row.add(t_row.get(column_idxs[j]));
			
			res_array.add(new_row);
		}
		
		return memTable.convertToMemTable(res_array);
	}
	
	public  memTable selectSubRows( int from_idx , int to_idx ){
		
		List<ArrayList<String>> tmp = this.table_in_mem.subList(from_idx, to_idx);
		
		
		ArrayList<ArrayList<String>> ttmp = new ArrayList<ArrayList<String>>(tmp);
		
		return convertToMemTable(ttmp);
	}
	
	public 	static memTable merge_memTable( memTable A, memTable B ){
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> my_array_A = (ArrayList<ArrayList<String>>) A.table_in_mem.clone();
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> my_array_B = (ArrayList<ArrayList<String>>) B.table_in_mem.clone();
		
		int b_length = B.get_length();
		for(int i=0; i < b_length; i++)
			my_array_A.add(my_array_B.get(i));
		
		return memTable.convertToMemTable(my_array_A);
	}
	
	public 	void removeDuplicates( int input_main_key_idx ){
		this.main_key_idx = input_main_key_idx;
		id_set = new HashSet<String>();		
		for(Iterator<ArrayList<String>> iterator = table_in_mem.iterator(); iterator.hasNext(); ){
			ArrayList<String> t_row = iterator.next();
			String item_id = t_row.get(main_key_idx);
			if(id_set.contains(item_id))
				iterator.remove();
			else
				id_set.add(item_id);
		}
				
	}
	
	public 	void removeDuplicates(){
		removeDuplicates(0);
	}
	
	public 	void generateItem2idxMap(){
		this.id_2_rowidx_map = new HashMap<String, Integer>();
		
		int idx_counter = 0;
		for(ArrayList<String> each_row : this.table_in_mem){
			String item_id = each_row.get(main_key_idx);
			id_2_rowidx_map.put(item_id, idx_counter);
			idx_counter++;
		}
	}
	
	public 	void sortWithStringKey( int key_idx , Boolean descending){
		
		Collections.sort(this.table_in_mem, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(key_idx).compareTo(o2.get(key_idx));
			}
		});
		
		if(descending)
			Collections.reverse(table_in_mem);
	}
	
	public 	void sortWithStringKey( int key_idx ){
		sortWithStringKey(key_idx, false);
	}
	
	public 	void sortWithNumericKey( int key_idx , Boolean descending ){
		Collections.sort(this.table_in_mem, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				Double number_o1 = Double.parseDouble(o1.get(key_idx));
				Double number_o2 = Double.parseDouble(o2.get(key_idx));
				
				if(number_o1>number_o2)
					return 1;
				else if(number_o1<number_o2)
					return -1;
				else
					return 0;
			}
		});
		
		if(descending)
			Collections.reverse(table_in_mem);
	}
	
	public 	void sortWithNumericKey( int key_idx ){
		sortWithNumericKey(key_idx, false);
	}
	
	public  String get_element_at( int row_idx, int col_idx ){
		return this.table_in_mem.get(row_idx).get(col_idx);
	}
	
	@SuppressWarnings("unchecked")
	public 	ArrayList<String> get_row_at( int row_idx ){
		return (ArrayList<String>)this.table_in_mem.get(row_idx).clone();
	}
	
	public 	ArrayList<String> get_column_at( int col_idx ){
		ArrayList<String> reStrings = new ArrayList<String>();
		for(ArrayList<String> each_row : this.table_in_mem){
			reStrings.add(each_row.get(col_idx));
		}
		
		return reStrings;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		memTable my_table_A = new memTable("C:\\Users\\peiqchen\\Desktop\\my_tmp_A.txt");
		memTable my_table_B = new memTable("C:\\Users\\peiqchen\\Desktop\\my_tmp_B.txt");
		memTable my_table_C = memTable.merge_memTable(my_table_A, my_table_B);
		
		System.out.println(my_table_A.get_length());
		System.out.println(my_table_B.get_length());
		System.out.println(my_table_C.get_length());
		my_table_C.removeDuplicates();
		System.out.println(my_table_C.get_length());
		
		my_table_B.sortWithStringKey(0);
		my_table_B.print();
		my_table_B.sortWithNumericKey(0, true);
		my_table_B.print();
		
	}

}