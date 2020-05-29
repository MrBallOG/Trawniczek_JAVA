package trawniczek;

public class Sprinkler {

	private int type;								// 90/180/270/360
	private int x0;									// x value of center of sprinkler
	private int y0;									// y value of center of sprinkler
	private int deg; 								// number of quadrant that is: 90 filled, 270 empty, 180 -> 12 means 1, 2 filled
	private int radius;								// radius of sprinkler
	private int waterlvl;							// amount of water for lawn to be watered
	private short lawn[][];
	private boolean set_rebounds;
	private boolean quadrant1;
	private boolean quadrant2;
	private boolean quadrant3;
	private boolean quadrant4;
	
	
	public Sprinkler(int type, int x0, int y0) { 	// 360 sprinkler
		this(type, x0, y0, 0);
	}
	
	public Sprinkler(int type, int x0, int y0, int deg) {
		this.type = type; 
		this.x0 = x0;
		this.y0 = y0; 
		this.deg = deg;
	}
	
	/*
	 *  Used for writing to file 
	 */
	@Override
	public String toString() {
		if(type == 90)
			return String.format("typ: %d,   koordynaty: (%d, %d), cwiartka zajeta: %d\n", type, x0, y0, deg);
		else if(type == 270)
			return String.format("typ: %d,  koordynaty: (%d, %d), cwiartka pusta: %d\n", type, x0, y0, deg);
		else if(type == 180) {
			switch(deg) {
			case 12:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 1,2\n", type, x0, y0);
			case 23:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 2,3\n", type, x0, y0);
			case 34:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 3,4\n", type, x0, y0);
			case 14:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 1,4\n", type, x0, y0);
			}
		}
		return String.format("typ: %d,  koordynaty: (%d, %d)\n", type, x0, y0);
	}
	
	/*
	 *  Chooses which quadrants should be watered by given type of sprinkler
	 */
	
	public void putSprinkler(short [][]lawn, boolean set_rebounds) {
		this.lawn = lawn;
		this.set_rebounds = set_rebounds;
		if(type == 90) {
			radius = 500;
			waterlvl = 4;
			switch(deg) {
			case 1:
				fillQuadrant1();
				quadrant4 = false;
			case 2:
				fillQuadrant2();
				quadrant1 = false;
			case 3:
				fillQuadrant3();
				quadrant2 = false;
			case 4:
				fillQuadrant4();
				quadrant3 = false;
			}
		}
		else if(type == 270) {
			radius = 300;
			waterlvl = 2;
			switch(deg) {
			case 1:
				fillQuadrant2();
				fillQuadrant3();
				fillQuadrant4();
				quadrant1 = false;
				//removeStripeHorizontalNegative();
				//removeStripeVerticalNegattive();
			case 2:
				fillQuadrant1();
				fillQuadrant3();
				fillQuadrant4();
				quadrant2 = false;
				//removeStripeHorizontalPositive();
				//removeStripeVerticalNegattive();
			case 3:
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant4();
				quadrant3 = false;
				//removeStripeHorizontalPositive();
				//removeStripeVerticalPositive();
			case 4:
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant3();
				quadrant4 = false;
				//removeStripeHorizontalNegative();
				//removeStripeVerticalPositive();
			}
		}
		else if(type == 180) {
			radius = 400;
			waterlvl = 3;
			switch(deg) {
			case 12:
				fillQuadrant1();
				fillQuadrant2();
				quadrant3 = false;
				quadrant4 = false;
				//removeStripeVerticalPositive();
			case 23:
				fillQuadrant2();
				fillQuadrant3();
				quadrant1 = false;
				quadrant4 = false;
				//removeStripeHorizontalNegative();
			case 34:
				fillQuadrant3();
				fillQuadrant4();
				quadrant1 = false;
				quadrant2 = false;
				//removeStripeVerticalNegattive();
			case 14:
				fillQuadrant1();
				fillQuadrant4();
				quadrant2 = false;
				quadrant3 = false;
				//removeStripeHorizontalPositive();
			}
		}
		else {
			radius = 200;
			waterlvl = 1;
			fillQuadrant1();
			fillQuadrant2();
			fillQuadrant3();
			fillQuadrant4();
			waterField(x0,y0);                      // fillQuadrant() methods in case of 360 skip center point
			//removeStripeHorizontalPositive();
			//removeStripeHorizontalNegative();
			//removeStripeVerticalPositive();
			//removeStripeVerticalNegattive();
		}
		
		
		for(int y = 0; y<=radius; y++)        // to chwilowo tu
			for(int x = 0; x<=radius; x++) 
				if(x*x+y*y <= radius*radius) {
				//	if(y0+y<0)
						
					if(lawn[y0-y][x0+x] != 0)
						lawn[y0-y][x0+x]+=waterlvl;
				}
	}

	private void fillQuadrant1() {
		int startx = 0;
		int starty = 1;
		if(quadrant4 == false)
			starty = 0;
		
	}
	
	private void fillQuadrant2() {
		int startx = -1;
		int starty = 0;
		if(quadrant1 == false)
			startx = 0;
	}
	
	private void fillQuadrant3() {
		int startx = 0;
		int starty = -1;
		if(quadrant2 == false)
			starty = 0;
	}
	
	private void fillQuadrant4() {
		int startx = 1;
		int starty = 0;
		if(quadrant3 == false)
			startx = 0;
	}
	// if lanw == 0, if mirror = 0 bo nie moze byc czyli nie zainicjowany, ale przy cwiartkach trzeba zerowac gdy np dojdzie do tan high
	private void waterField(int x, int y) {
		lawn[y0-y][x0+x]+=waterlvl;
	}
	
	private void mirrorOX(int mirror, int x, int y) {
		if(set_rebounds == true) {
			if(x > 0) {
				x = 2*mirror - x - 1;
				waterField(x, y);
			}
			else {
				x = 2*mirror - x + 1;
				waterField(x, y);
			}
		}
	}
	
	private void mirrorOY(int mirror, int x, int y) {
		if(set_rebounds == true) {
			if(y > 0) {
				y = 2*mirror - y - 1;
				waterField(x, y);
			}
			else {
				y = 2*mirror - y + 1;
				waterField(x, y);
			}
		}
	}
	
	private void mirrorOXOY(int mirror, int x, int y) {
		if(set_rebounds == true) {
			if(x > 0) {
				x = 2*mirror - x - 1;
			}
			else {
				x = 2*mirror - x + 1;
			}
		
			if(y > 0) {
				y = 2*mirror - y - 1;
			}
			else {
				y = 2*mirror - y + 1;
			}
			waterField(x, y);
		}
	}
	
	// delete from here ------------------------------------------------------------------------------------------------------------
	private void removeStripeHorizontalPositive() {
		short temp;
		for(int i = 0; i<=radius; i++) {
			if(x0+i == lawn[0].length)
				break;
			temp = lawn[y0][x0+i];
			lawn[y0][x0+i] = (short) ((temp - (short)1)/2);
		}
	}
	
	private void removeStripeHorizontalNegative() {
		short temp;
		for(int i = 0; i>=-radius; i--) {
			if(x0+i == -1)
				break;
			temp = lawn[y0][x0+i];
			lawn[y0][x0+i] = (short) ((temp - (short)1)/2);
		}
	}
	
	private void removeStripeVerticalPositive() {
		short temp;
		for(int i = 0; i<=radius; i++) {
			if(y0-i == -1)
				break;
			temp = lawn[y0-i][x0];
			lawn[y0-i][x0] = (short) ((temp - (short)1)/2);
		}
	}
	
	private void removeStripeVerticalNegattive() {
		short temp;
		for(int i = 0; i>=-radius; i--) {
			if(y0-i == lawn.length)
				break;
			temp = lawn[y0-i][x0];
			lawn[y0-i][x0] = (short) ((temp - (short)1)/2);
		}
	}
}
