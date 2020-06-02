package trawniczek;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Output {

	private String filename = "list_of_sprinklers.txt";             // name  of output file containing list of sprinklers
	private String bitmapname = "bitmap.bmp";						// name of bitmap containing last frame of animation
	private File file = null;										
	private File bitmap = null;
	private List<Sprinkler> sprlist;
	
	public Output(List<Sprinkler> list) {
		sprlist = list;
	}
	
	/*
	 *  Creates file if it doesn't exist, otherwise overwrites it
	 */
	private File createFile(String filename, File file) {
		try {
			file = new File(filename);
			if(!file.exists())
				file.createNewFile();
			}
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Blad w otwarciu/tworzeniu pliku", "Error", JOptionPane.PLAIN_MESSAGE);
			return null;
		}
		return file;
	}
	
	/*
	 *  Writes to file information about used sprinklers
	 *  Uses PrintWriter in order to utilize println() method
	 */
	public void writeToFile() {
		file = createFile(filename, file);
		if(file == null)
			return;
		Iterator<Sprinkler> it = sprlist.iterator();
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
			pw.println("Liczba wszytskich podlewaczek: " + sprlist.size()+ "\n");
			while(it.hasNext())	{
				pw.println(it.next());
			}
		}
		catch(IOException e) {JOptionPane.showMessageDialog(null, "Blad w pisaniu do pliku", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
	
	/*
	 *  Creates bitmap from last frame of animation saved in BufferedImage buffimg
	 */
	public void createBitmap(BufferedImage buffimg) {
		bitmap = createFile(bitmapname, bitmap);
		if(bitmap == null)
			return;
		try {
			ImageIO.write(buffimg, "bmp", bitmap);
		} catch (IOException e) {JOptionPane.showMessageDialog(null, "Blad w tworzeniu bitmapy", "Error", JOptionPane.PLAIN_MESSAGE);}
	}
}
