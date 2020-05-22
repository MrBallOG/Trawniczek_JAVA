package trawniczek;

import java.awt.Color;
import java.util.concurrent.BlockingQueue;

public class PositionSprinklers implements Runnable{

	private boolean running = true;
	private short[][]lawn;
	private int num;
	private int period;
	private boolean rebounds;
	private BlockingQueue<short[][]> q;
	
	public PositionSprinklers(short[][]lawn, int num, int period, boolean rebounds, BlockingQueue<short[][]> q) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.rebounds = rebounds;
		this.q = q;
	}

	@Override
	public void run() {
		int countx = (lawn[0].length-400) / 282;
		int county = (lawn.length-400) / 282;
		int rx = ((lawn[0].length-400) % 282)/countx;
		int ry = ((lawn.length-400) % 282)/county;
		
		while(num>0) {
			try {
				Thread.sleep(period*100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			position(rx, ry);

			try {
				q.put(lawn);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			num--;
		}
		running = false;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public void position(int rx, int ry) {
		int x0 = 200;
		int y0 = 200;
		
		while(y0<=lawn.length-200) {
			while(x0<=lawn[0].length-200) {	
				if(lawn[y0][x0] != 0)
					for(int yc = -200; yc<200; yc++)        //sprawdza czy jest przeszkoda
						for(int xc = -200; xc<200; xc++) 
							if(xc*xc+yc*yc <= 200*200) 
								if(lawn[y0+yc][x0+xc] != 0)
									lawn[y0+yc][x0+xc]+=10;
				x0+=282+rx;
			}
				x0 = 200;
				y0+=282+ry;
			}
		
	}
}
