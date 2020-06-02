package trawniczek;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.*;

public class Animation extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final int WIN_HEIGHT = 600; 										  // height of the window
	private static final int WIN_WIDTH = 1200; 										  // width of the window
	private final int center_x;
	private final int center_y;
	private short [][]lawn;
	private int num;
	private int period;
	private boolean set_rebounds;
	private Thread animate;   										
	private Thread calculate;													
	private final BlockingQueue<short[][]> q = new ArrayBlockingQueue<short[][]>(1);   // used for concurrency
	private PositionSprinklers ps;													   
	private Animate an;
	private Output out;																   // creates output file and bitmap
	private BufferedImage buffimg;												       // stores image to be painted
	
	
	public Animation(short [][]lawn, int num, int period, boolean rebounds) {
		this.lawn = lawn;
		this.num = num;
		this.period = period;
		this.set_rebounds = rebounds;
		center_x = (WIN_WIDTH - lawn[0].length/10)/2;           // determines x value of starting point of drawing frame
		center_y = (WIN_HEIGHT - lawn.length/10)/2-20;			// determines y value of starting point of drawing frame
		setLayout(new BorderLayout());
		calculate();
		animate();
		add(an, BorderLayout.CENTER);							// puts JPanel that shows animation in the center of this JPanel
	}
	
	/*
	 * 	Uses SwingWorker in order to keep GUI operational while waiting for update
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
	 *  Uses SwingWorker in order to keep GUI operational while preparing an update
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

		private static final long serialVersionUID = 1L;

		public Animate() {
			setBackground(new Color(255, 145, 164));            // settles color of background so it matches color of first screen
		}
		
		/*
		 *  Takes calculated values of lawn fields from queue q and updates frame of animation
		 *  After animation ends, orders file with list of sprinklers and bitmap to be created
		 */
		@Override
		public void run() {
			while(ps.getRunning()) {
				try {
					lawn = q.take();
				} catch (InterruptedException e) {
					JOptionPane.showMessageDialog(null, "Niespodziewane przerwanie animacji", "Error", JOptionPane.PLAIN_MESSAGE);
					ps.animationGotInterrupted();
				}
				repaint();
			}
			paintBitmap();
			out = new Output(ps.getSprlist());
			out.writeToFile();
			out.createBitmap(buffimg);
		}
		
		/*
		 *  Saves last frame of animation in BufferedImage so it can be turned into a bitmap
		 *  Works like paintComponent()
		 */
		private void paintBitmap() {
			buffimg = new BufferedImage(lawn[0].length/10, lawn.length/10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = buffimg.createGraphics();
			for(int i = 0; i<lawn.length/10; i++) {
				for(int j = 0; j<lawn[0].length/10; j++) {
					g2d.setColor(setColorOfPixel(i,j));		
					g2d.fillRect(j, i, 1, 1);       					
				}
			}
			g2d.dispose();
		}
		
		/*
		 *  Creates frame of animation by using 1x1 colored squares as pixels
		 */
		@Override
		public void paintComponent(Graphics g) {   
			super.paintComponent(g);
			for(int i = 0; i<lawn.length/10; i++) {
				for(int j = 0; j<lawn[0].length/10; j++) {
					g.setColor(setColorOfPixel(i,j));				// sets color of pixel
					g.fillRect(center_x+j, center_y+i, 1, 1);       // creates pixel
				}
			}
			g.dispose();
		}
		
		/*
		 *  Sets color of pixel depending on value of mode
		 */
		private Color setColorOfPixel(int i, int j) {
			short mode = calculateMode(10*i, 10*j);
			if(mode == 0)
				return Color.BLACK;
			else if (mode == 1)
				return Color.WHITE;                    
			else if (mode > 180)
				return new Color(184, 15, 10);						// overwatered, crimson red
			else if (mode < 70)
				return new Color(245-2*mode, 255-mode, 50);			// not watered enough, yellowish to olive
			else
				return new Color(0, 280-mode, 0);					// shades of green
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