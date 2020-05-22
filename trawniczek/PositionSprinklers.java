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
		
		while(num>0) {
			try {
				Thread.sleep(period*100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 100; i<500; i++) {
				for(int j = 100; j<500; j++) {
					lawn[i][j]+=20;
				}
			}
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
}
