package io;

import java.io.*;
import javax.swing.*;

public class Input {

	private File file = null;
	private short [][]lawn;				// array that stores lawn
	private String filename;
	private int x_size;					// size of lawn horizontal 
	private int y_size = 1; 			// reads first line before loop
	
	public Input(String filename) {
		this.filename = filename;
	}
	
	public int readFromFile() {
		
		String temp;
		file = new File(filename);
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null, "Plik nie istnieje", "Error", JOptionPane.PLAIN_MESSAGE);
			return 1;
		}
		
		/*
		 *  Uses BufferedRedaer to read whole lines from file
		 *  First reading checks the size of lawn in order to set size of lawn array that stores lawn 
		 */
		try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
			temp = bf.readLine();
			if(temp == null || temp.length() == 0) {
				JOptionPane.showMessageDialog(null, "Pierwsza linia pliku jest pusta", "Error", JOptionPane.PLAIN_MESSAGE);
				return 2;
			}
			x_size = temp.length();
		
			while(bf.readLine() != null) {
				y_size++;
			}
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Blad w czytaniu pliku", "Error", JOptionPane.PLAIN_MESSAGE);
			return 3;
		}
		
		if(x_size > 80 || y_size > 40) {
			JOptionPane.showMessageDialog(null, "Trawnik przekracza maksymalne rozmiary (40x80): "+y_size+"x"+ x_size, "Error", JOptionPane.PLAIN_MESSAGE);
			return 4;
		}
		
		/*
		 *  Second reading turns characters of input file to fields of lawn array 
		 */
		try (BufferedReader bf = new BufferedReader(new FileReader(file))){
		
			lawn = new short[100*y_size][100*x_size];
			int i = 0;
		
			while((temp = bf.readLine()) != null) {
				char arr[] = temp.toCharArray();
				if(arr.length > x_size) {            
					JOptionPane.showMessageDialog(null, "Jeden z wierszy w pliku jest zbyt dlugi", "Error", JOptionPane.PLAIN_MESSAGE);	
					return 5;
				}
				else if(arr.length < x_size) {
					JOptionPane.showMessageDialog(null, "Jeden z wierszy w pliku jest zbyt krotki", "Error", JOptionPane.PLAIN_MESSAGE);
					return 6;
				}
				else {
					for(int j = 0; j<x_size; j++) {
						if(arr[j] != '*' && arr[j] != '-') { 
							JOptionPane.showMessageDialog(null, "Blad skladniowy elementu (" + (i+1) + ", " + (j+1) + ")", "Error", JOptionPane.PLAIN_MESSAGE);
							return 7;
						}
						else {
							if(arr[j] == '*')
								fillSquare(i,j,(short)1); 			// grass equals 1  
							if(arr[j] == '-')
								fillSquare(i,j,(short)0); 			// obstacle equals 0 
						}
					}
					i++;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Blad w czytaniu pliku", "Error", JOptionPane.PLAIN_MESSAGE);
			return 8;
		}     
		
		return 0;
	}
	
	/*
	 *   Each character in input file translates to 100x100 square of lawn array
	 */
	private void fillSquare(int y, int x, short c) {
		for (int j = 0; j < 100; j++)
	    {
	        for (int i = 0; i < 100; i++)
	        {
	            lawn[y*100+j][x*100+i] = c;
	        }
	    }
	}
	
	public short[][] getLawn(){
		return lawn;
	}
	
}
