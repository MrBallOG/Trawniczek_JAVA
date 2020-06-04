package positioning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;

import sprinkler.Sprinkler;

public class UpdateSprinklers extends PositionSprinklers implements Runnable {

	private boolean running = true;
	private int num;
	private int period;
	private boolean set_rebounds;
	private BlockingQueue<short[][]> q;
	
	public UpdateSprinklers(short[][]lawn, int num, int period, boolean rebounds, BlockingQueue<short[][]> q) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.set_rebounds = rebounds;
		this.q = q;
		sprlist = new ArrayList<Sprinkler>();
		scanTheLawnForRectangles();                   	// positions sprinklers on the lawn
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
				addSprinklers();  
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
}
