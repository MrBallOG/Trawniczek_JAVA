package trawniczek;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Output {

	private String filename = "D:\\pobrane\\fred.txt";
	private PrintWriter pw = null;
	private File file = null;
	private List<Sprinkler> list;
	
	public Output(List<Sprinkler> list) {
		this.list = list;
	}
	
	private void createFile() {
		try {
			file = new File(filename);
			if(!file.exists())
				file.createNewFile();
			}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Couldn't open file", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
	
	public void printToFile() {
		createFile();
		Iterator<Sprinkler> it = list.iterator();
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			while(it.hasNext())	{
				pw.println(it.next());
			}
			pw.close();
		}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Couldn't write to file", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
	
	

}
