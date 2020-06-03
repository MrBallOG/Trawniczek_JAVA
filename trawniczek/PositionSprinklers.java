package trawniczek;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.lang.model.util.ElementScanner14;
import javax.swing.JOptionPane;

public class PositionSprinklers implements Runnable{

	private boolean running = true;
	private short[][]lawn;
	private int num;
	private int period;
	private boolean set_rebounds;
	private BlockingQueue<short[][]> q;
	private List<Sprinkler> sprlist;				// List of sprinklers
	
	public PositionSprinklers(short[][]lawn, int num, int period, boolean rebounds, BlockingQueue<short[][]> q) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.set_rebounds = rebounds;
		this.q = q;
		sprlist = new ArrayList<Sprinkler>();
		// funkcja ustawiajaca podlewaczki
	}

	/*
	 *  Uses addSprinklers() to put sprinklers on the lawn and then incrementField() to update values of fields for next frame of animation 
	 *  Passes lawn to queue q in order to update frame
	 */
	@Override
	public void run() {	
		short iteration_number = 0;
		
		while(num>0 && running != false) {
			try {
				Thread.sleep(period*100);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null, "Niespodziewane przerwanie podlewania", "Error", JOptionPane.PLAIN_MESSAGE);
				num = 0;
			}
			if(iteration_number == 0)
				position();   //addSprinklers();  
			else
				incrementField(iteration_number);
			
			try {
				q.put(lawn);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null, "Niespodziewane przerwanie podlewania", "Error", JOptionPane.PLAIN_MESSAGE);
				num = 0;
			}
			iteration_number++;
			num--;
		}
		running = false;
	}
	
	/*
	 *  Stops animation thread when all iterations are finished 
	 */
	public boolean getRunning() {
		return running;
	}
	
	/*
	 *  Stops animation thread if it got interrupted
	 */
	public void animationGotInterrupted() {
		running = false;
	}
	
	public List<Sprinkler> getSprlist() {
		for(int j = 0; j<10; j++) {							//tymczasowe zeby cos zwracac
			switch(j%4) {
			case 0:
				sprlist.add(new Sprinkler(90, 52-j, j+10, 1));
				break;
			case 1:
				sprlist.add(new Sprinkler(180, j, j+10, 34));
				break;
			case 2:
				sprlist.add(new Sprinkler(270, j+55, j, 3));
				break;
			case 3:
				sprlist.add(new Sprinkler(360, 18-j, j+35));
				break;
			}
		}
		return sprlist;
	}
	
	/*
	 *  Puts sprinklers on the lawn 
	 */
	private void addSprinklers() {
		Iterator<Sprinkler> it = sprlist.iterator();
		while(it.hasNext())
			it.next().putSprinkler(lawn, set_rebounds);
	}
	
	/*
	 *  Increments values of fields for next animation frame
	 *  Every grass field has basic value of 1, so in order to get value it should be incremented by, subtract 1 and divide by 
	 *  current iteration number
	 */
	private void incrementField(short iteration_number) {
		short temp;
		for(int i = 0; i<lawn.length; i++)
			for(int j = 0; j<lawn[0].length; j++) {
				if(lawn[i][j] != 1 && lawn[i][j] != 0) {
					temp = lawn[i][j]; 
					lawn[i][j] = (short) ((temp - (short)1)/iteration_number + temp);
				}
			}
	}
	
	/*
	 *  282 is 400(diameter) divided by square root of 2, it is used to minimize gaps between circles
	 *  subtract 400 to make sure that first and last circle in row or column can fit
	 */
	public void position() {  					//dodawaj do sprlist kolejne podelwaczki, trza zrobi�
		/*
		int countx = (lawn[0].length-400) / 282;                        
		int county = (lawn.length-400) / 282;							
		int rx = countx > 0 ? ((lawn[0].length-400) % 282)/countx : 0;	
		int ry = county > 0 ? ((lawn.length-400) % 282)/county : 0;	
		
		int x0 = 200;
		int y0 = 200;
		
		while(y0<=lawn.length-200) {
			while(x0<=lawn[0].length-200) {	
				if(lawn[y0][x0] != 0)
					for(int yc = -200; yc<200; yc++)        
						for(int xc = -200; xc<200; xc++) 
							if(xc*xc+yc*yc <= 200*200) 
								if(lawn[y0+yc][x0+xc] != 0)
									lawn[y0+yc][x0+xc]+=10;
				x0+=282+rx;
			}
				x0 = 200;
				y0+=282+ry;
			}
			*/
		scanTheLawnForRectangles();
		Sprinkler s = new Sprinkler(270, 1000, 600, 4);
		s.putSprinkler(lawn, set_rebounds);
						
	}

	/*
	 *	Iterates on lawn to find possible rectangles to put sprinklers in
	 */
	public void scanTheLawnForRectangles()
	{
		int x0 = 0; // x of the cursor
		int y0 = 0; // y of the cursor
		int pixels_distance = 100; // jump distance
		while (y0 < lawn.length)
		{
			while (x0 < lawn[0].length)
			{
				if (lawn[y0][x0] > 0) // if not wall or already used
				{
					int availableSpace = checkSpace(x0, y0, pixels_distance); // check in which direction it will be shorter
					if (availableSpace >= 4) // if it is at least 2x2
					{
						int rect_x = 0; // x size of rectangle in 100x100 squares
						int rect_y = 0; // y size of rectangle in 100x100 squares
						if (availableSpace % 2 == 0)
						{
							rect_x = availableSpace / 2;
							rect_y = scanVerticalSize(rect_x, x0, y0, pixels_distance);
							fillRectangleVerticaly(rect_x, rect_y, x0, y0);
						}
						else
						{
							rect_y = availableSpace / 2;
							rect_x = scanHorizontalSize(rect_y, x0, y0, pixels_distance);
							fillRectangleHorizontaly(rect_x, rect_y, x0, y0);
						}
						//System.out.println("[x|y]: " + rect_x + ", " + rect_y + " range: [" + x0/pixels_distance + "-" + (x0/pixels_distance + rect_x-1) + "]:[" + y0/pixels_distance + "-" + (y0/pixels_distance + rect_y-1) + "]");
						
					}
				}
				x0 += pixels_distance;
			}
			x0 = 0;
			y0 += pixels_distance;
		}
	}

	/*
	 *	Tries to find shorter dimension for the rectangle
	 *
	 * 	Could add better prorities later
	 */
	private int checkSpace(int x0, int y0, int pixels_distance)
	{
		int count = 0;
		int x = x0;
		int y = y0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress && count < 20)
		{
			count++;
			if (x0 + count * pixel >= lawn[0].length)
			{
				in_progress = false;
				count = count * 2;
			}
			else if (y0 + count * pixel >= lawn.length)
			{
				in_progress = false;
				count = count * 2 + 1;
			}
			if (in_progress)
			{
				x = x0 + count * pixel;
				for (y = y0; y < y0 + count * pixel; y += pixel)
				{
					if (lawn[y][x] <= 0)
					{
						in_progress = false;
						count = count * 2;
						break;
					}
				}
			}
			if (in_progress)
			{
				y = y0 + count * pixel;
				for (x = x0; x <= x0 + count * pixel; x += pixel)
				{
					if (lawn[y][x] <= 0)
					{
						in_progress = false;
						count = count * 2 + 1;
						break;
					}
				}
			}
		}
		return count;
	}

	private int scanVerticalSize(int H, int x0, int y0, int pixels_distance)
	{
		int x = x0;
		int y = y0;
		int V = 0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress)
		{
			for (x = x0; x < x0 + H * pixel; x += pixel)
			{
				if (lawn[y][x] <= 0)
				{
					in_progress = false;
					break;
				}
				lawn[y][x] *= -1;
			}
			if (in_progress)
			{
				V++;
				y += pixel;
				if (y >= lawn.length)
				{
					in_progress = false;
				}
			}
		}
		return V;
	}

	private int scanHorizontalSize(int V, int x0, int y0, int pixels_distance)
	{
		int x = x0;
		int y = y0;
		int H = 0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress)
		{
			for (y = y0; y < y0 + V * pixel; y += pixel)
			{
				if (lawn[y][x] <= 0)
				{
					in_progress = false;
					break;
				}
				lawn[y][x] *= -1;
			}
			if (in_progress)
			{
				H++;
				x += pixel;
				if (x >= lawn[0].length)
				{
					in_progress = false;
				}
			}
		}
		return H;
	}

	private void fillRectangleVerticaly(int H, int V, int x0, int y0)
	{
		
	}

	private void fillRectangleHorizontaly(int H, int V, int x0, int y0)
	{
		
	}
}
