package trawniczek;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.*;

public class Animation extends JPanel {
	
	private static final int WIN_HEIGHT = 600; 										  // height of the window
	private static final int WIN_WIDTH = 1200; 										  // width of the window
	private short[][]lawn;
	private int num;
	private int period;
	private boolean set_rebounds;
	private int center_x;
	private int center_y;
	private Thread animate;   										
	private Thread calculate;													
	private final BlockingQueue<short[][]> q = new ArrayBlockingQueue<short[][]>(1);   // used for concurrency
	private PositionSprinklers ps;													   
	private Animate an;																   
	
	
	public Animation(short[][]lawn, int num, int period, boolean rebounds) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.set_rebounds = rebounds;
		setLayout(new BorderLayout());
		calculate();
		animate();
		add(an, BorderLayout.CENTER);
		
	}
	
	/*
	 *  Creates thread that updates frame of animation 
	 */
	private void animate() {
		an = new Animate();
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				animate = new Thread(an);
				animate.start();
				animate.join();
				return null;
			}
			
		};
		worker.execute();
	}
	
	/*
	 *  Creates thread that updates lawn
	 */
	private void calculate() {
		ps = new PositionSprinklers(lawn, num, period, set_rebounds, q);
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				calculate = new Thread(ps);
				calculate.start();
				calculate.join();
				return null;
			}
			
		};
		worker.execute();
	}

	private class Animate extends JPanel implements Runnable {

		public Animate() {
			setBackground(new Color(255, 145, 164));
		}
		
		/*
		 *  Queue q takes calculated values of lawn fields and updates frame of animation
		 */
		@Override
		public void run() {
			while(ps.getRunning()) {
				try {
					lawn = q.take();
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
				repaint();
			}
		}
		
		/*
		 *  Creates frame of animation
		 */
		@Override
		public void paintComponent(Graphics g) {   
			super.paintComponent(g);
			center_x = (WIN_WIDTH - lawn[0].length/10)/2;           //determines starting point of drawing frame
			center_y = (WIN_HEIGHT - lawn.length/10)/2-20;	
			short mode;
			for(int i = 0; i<lawn.length/10; i++) {
				for(int j = 0; j<lawn[0].length/10; j++) {
					mode = calculateMode(10*i, 10*j);
					if(mode == 0)
						g.setColor(Color.BLACK);
					else if (mode == 1)
						g.setColor(Color.WHITE);                    
					else if (mode > 150)
						g.setColor(new Color(184, 15, 10));			// overwatered, crimson red
					else
						g.setColor(new Color(0, 255-mode, 0));		// shades of green
					g.fillRect(center_x+j, center_y+i, 1, 1);       // creates pixel
				}
			}
			
			g.dispose();
		}
		
		/*
		 *  Looks for the most popular value inside 10x10 lawn square to settle color of pixel
		 */
		private short calculateMode(int i, int j) {
			int count = 0;
			short[] sorted = new short[100];
			for(int ii = i; ii<i+10; ii++) {
				for(int jj = j; jj<j+10; jj++) {
					sorted[count++] = lawn[ii][jj];
				}
			}
			Arrays.sort(sorted);
			short previous = sorted[0];
			short mode = sorted[0];
			count = 1;
			int current_max = 1;
			
			for (int ii = 1; ii < sorted.length; ii++) {
			        if (sorted[ii] == previous)
			            count++;
			        else {
			            if (count > current_max) {
			                mode = sorted[ii-1];
			                current_max = count;
			            }
			            previous = sorted[ii];
			            count = 1;
			        }
			    }

			return count > current_max ? sorted[sorted.length-1] : mode;
		}
		
		
	}

}



