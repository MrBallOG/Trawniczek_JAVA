package trawniczek;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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
		
		while(num>0) {
			try {
				Thread.sleep(period*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(iteration_number == 0)
				position();   //addSprinklers();  
			else
				incrementField(iteration_number);
			
			try {
				q.put(lawn);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			iteration_number++;
			num--;
		}
		running = false;
	}
	
	/*
	 *  Stops animation thread
	 */
	public boolean getRunning() {
		return running;
	}
	
	public List<Sprinkler> getSprlist() {
		for(int j = 0; j<10; j++) {							//tymczasowe zeby cos zwracac
			switch(j%4) {
			case 0:
				sprlist.add(new Sprinkler(90, 52-j, j+10, 1));
				break;
			case 1:
				sprlist.add(new Sprinkler(180, 15-j, j+10, 34));
				break;
			case 2:
				sprlist.add(new Sprinkler(270, j+5, j, 3));
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
	public void position() {  					//dodawaj do sprlist kolejne podelwaczki, trza zrobiæ
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
		Sprinkler s = new Sprinkler(360, 1000, 600, 4);
		s.putSprinkler(lawn, set_rebounds);
						
	}
}
