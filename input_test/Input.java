
import java.io.*;

public class Input {

	private BufferedReader bf = null;
	private File file = null;
	private char lawn[][];
	private String filename;
	private int x_size;
	private int y_size = 1; // reads first line before loop
	
	public Input(String filename) {
		this.filename = filename;
	}

	public int checkIfExists() {
		
		file = new File(filename);			//System.err.println("Couldn't open file");
		if(!file.exists())
			return 1;
		else
			return 0;
	}
	
	public int readFromFile() {
		
		try {
		bf = new BufferedReader(new FileReader(filename));
		String temp = bf.readLine();
		x_size = temp.length();
		
		while(bf.readLine() != null)
		{
			y_size++;
		}
		bf.close();
		
		bf = new BufferedReader(new FileReader(filename));
		lawn = new char[y_size][x_size];
		int i = 0;
		
		while((temp = bf.readLine()) != null)
		{
			char arr[] = temp.toCharArray();
			if(arr.length > x_size)             //System.err.println("Lawn is too wide");
					return 2;
			else if(arr.length < x_size)             //System.err.println("Lawn not wide enough");
					return 3;
			else {
					for(int j = 0; j<x_size; j++) {
						if(arr[j] != '*' && arr[j] != '-')  //System.err.println("Wrong syntax at (" + (i+1) + ", " + (j+1) + ")");
							return 4;
						else {
							lawn[i][j] = arr[j]; 
						}
					}
					i++;
			}
		}
		
		bf.close();
		}
		catch (IOException e) {return 5;}     //System.err.println("Couldn't write to file");
		
		return 0;
	}
	
	
	public char[][] getLawn(){
		return lawn;
	}
	
	public int getXSize() {
		return x_size;
	}
	
	public int getYSize() {
		return y_size;
	}
}
