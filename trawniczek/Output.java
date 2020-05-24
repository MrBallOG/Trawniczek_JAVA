package trawniczek;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Output {

	private String filename = "D:\\pobrane\\fred.txt";
	private File file = null;
	private List<Sprinkler> sprlist;
	
	public Output(List<Sprinkler> list) {
		sprlist = list;
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
		Iterator<Sprinkler> it = sprlist.iterator();
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
			pw.println("Liczba wszytskich podlewaczek: " + sprlist.size()+ "\n");
			while(it.hasNext())	{
				pw.println(it.next());
			}
		}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Couldn't write to file", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
}
