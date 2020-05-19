package trawniczek;

import java.io.*;
import javax.swing.*;

public class Input {

	private File file = null;
	private char lawn[][];
	private String filename;
	private int x_size;
	private int y_size = 1; // reads first line before loop
	
	public Input(String filename) {
		this.filename = filename;
	}
	
	public int readFromFile() {
		
		String temp;
		file = new File(filename);
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null, "File not exists", "Error", JOptionPane.PLAIN_MESSAGE);
			return 1;
		}
		
		try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
		temp = bf.readLine();
		x_size = temp.length();
		
		while(bf.readLine() != null)
		{
			y_size++;
		}
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Couldn't read file", "Error", JOptionPane.PLAIN_MESSAGE);
			return 6;
		}
		
		if(x_size > 80 || y_size >40) {
			JOptionPane.showMessageDialog(null, "Lawn is bigger than max size 40x80: "+y_size+"x"+ x_size, "Error", JOptionPane.PLAIN_MESSAGE);
			return 2;
		}
		
		try (BufferedReader bf = new BufferedReader(new FileReader(file))){
		lawn = new char[100*y_size][100*x_size];
		int i = 0;
		
		while((temp = bf.readLine()) != null)
		{
			char arr[] = temp.toCharArray();
			if(arr.length > x_size) {            
				JOptionPane.showMessageDialog(null, "Lawn is too wide", "Error", JOptionPane.PLAIN_MESSAGE);	
				return 3;
			}
			else if(arr.length < x_size) {
				JOptionPane.showMessageDialog(null, "Lawn not wide enough", "Error", JOptionPane.PLAIN_MESSAGE);
				return 4;
			}
			else {
					for(int j = 0; j<x_size; j++) {
						if(arr[j] != '*' && arr[j] != '-') { 
							JOptionPane.showMessageDialog(null, "Wrong syntax at (" + (i+1) + ", " + (j+1) + ")", "Error", JOptionPane.PLAIN_MESSAGE);
							return 5;
						}
						else {
							if(arr[j] == '*')
								fillSquare(i,j,'1'); //lawn[i][j] = '1'; 
							if(arr[j] == '-')
								fillSquare(i,j,'0'); //lawn[i][j] = '0'; 
						}
					}
			i++;
			}
		}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Couldn't read file", "Error", JOptionPane.PLAIN_MESSAGE);
			return 6;
		}     
		
		return 0;
	}
	
	private void fillSquare(int y, int x, char c) {
		for (int j = 0; j < 100; j++)
	    {
	        for (int i = 0; i < 100; i++)
	        {
	            lawn[y*100+j][x*100+i] = c;
	        }
	    }
	}
	
	public char[][] getLawn(){
		return lawn;
	}
	
	public int getXSize() {
		return x_size*100;
	}
	
	public int getYSize() {
		return y_size*100;
	}
}
