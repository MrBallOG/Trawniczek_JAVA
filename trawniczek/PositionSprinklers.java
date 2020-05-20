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
	
	public PositionSprinklers(short[][]lawn, int num, int period, boolean rebounds, BlockingQueue<short[][]> queue) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.rebounds = rebounds;
		this.q = queue;
	}

	@Override
	public void run() {
	int it = 0;
		while(it<10) {
		for(int i = 0; i<500; i++) {
			for(int j = 0; j<500; j++) {
				lawn[i][j]+=20;
			}
		}
		try {
			q.put(lawn);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		it++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		running = false;
	}
	
	public boolean getRunning() {
		return running;
	}
}
