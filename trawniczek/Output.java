package trawniczek;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Output {

	private String filename = "list_of_sprinklers.txt";
	private String bitmapname = "bitmap.bmp";
	private File file = null;
	private File bitmap = null;
	private List<Sprinkler> sprlist;
	
	public Output(List<Sprinkler> list) {
		sprlist = list;
	}
	
	private File createFile(String filename, File file) {
		try {
			file = new File(filename);
			if(!file.exists())
				file.createNewFile();
			}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Couldn't open file", "Error", JOptionPane.PLAIN_MESSAGE);}
		return file;
	}
	
	public void printToFile() {
		file = createFile(filename, file);
		Iterator<Sprinkler> it = sprlist.iterator();
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
			pw.println("Liczba wszytskich podlewaczek: " + sprlist.size()+ "\n");
			while(it.hasNext())	{
				pw.println(it.next());
			}
		}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Couldn't write to file", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
	
	public void createBitmap(BufferedImage buffimg) {
		bitmap = createFile(bitmapname, bitmap);
		try {
			ImageIO.write(buffimg, "bmp", bitmap);
		} catch (IOException e) {JOptionPane.showMessageDialog(null, "Couldn't create bitmap", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
}
