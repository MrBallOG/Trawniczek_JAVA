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
				quadrant4 = false;
				fillQuadrant1();
				break;
			case 2:
				quadrant1 = false;
				fillQuadrant2();
				break;
			case 3:
				quadrant2 = false;
				fillQuadrant3();
				break;
			case 4:
				quadrant3 = false;
				fillQuadrant4();
				break;
			}
		}
		else if(type == 270) {
			radius = 300;
			waterlvl = 2;
			switch(deg) {
			case 1:
				quadrant1 = false;
				fillQuadrant2();
				fillQuadrant3();
				fillQuadrant4();
				break;
				//removeStripeHorizontalNegative();
				//removeStripeVerticalNegattive();
			case 2:
				quadrant2 = false;
				fillQuadrant1();
				fillQuadrant3();
				fillQuadrant4();
				break;
				//removeStripeHorizontalPositive();
				//removeStripeVerticalNegattive();
			case 3:
				quadrant3 = false;
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant4();
				break;
				//removeStripeHorizontalPositive();
				//removeStripeVerticalPositive();
			case 4:
				quadrant4 = false;
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant3();
				break;
				//removeStripeHorizontalNegative();
				//removeStripeVerticalPositive();
			}
		}
		else if(type == 180) {
			radius = 400;
			waterlvl = 3;
			switch(deg) {
			case 12:
				quadrant3 = false;
				quadrant4 = false;
				fillQuadrant1();
				fillQuadrant2();
				break;
				//removeStripeVerticalPositive();
			case 23:
				quadrant1 = false;
				quadrant4 = false;
				fillQuadrant2();
				fillQuadrant3();
				break;
				//removeStripeHorizontalNegative();
			case 34:
				quadrant1 = false;
				quadrant2 = false;
				fillQuadrant3();
				fillQuadrant4();
				break;
				//removeStripeVerticalNegattive();
			case 14:
				quadrant2 = false;
				quadrant3 = false;
				fillQuadrant1();
				fillQuadrant4();
				break;
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
			waterField(0,0);                      // fillQuadrant() methods in case of 360 skip center point
			//removeStripeHorizontalPositive();
			//removeStripeHorizontalNegative();
			//removeStripeVerticalPositive();
			//removeStripeVerticalNegattive();
		}
	}

	private void fillQuadrant1() {
		int startx = 0;
		int starty = 1;
		if(quadrant4 == false)
			starty = 0;
		
		boolean no_obstacle = true;
		int mirror_x = 0;
		int mirror_y = 0;
		int mirror_xy = 0;
		int y_low = starty;
		int y_high = 0;
		int tanh = 0;
		int tanl = 0;
		int tanm = 0;
		
		for(int y = starty; y<=radius; y++)      
			for(int x = startx; x<=radius; x++) 					
				if(lawn[y0-y][x0+x] == 0){
					if(lawn[y0-(y-1)][x0+x] != 0) {    			// przyp 1 pusto pod  
						if(y-1 <= starty ) {
							if(y-1 == starty ) { 
								for(int xx = startx; x<=radius; x++) 
									if(xx*xx+y*y <= radius*radius) 
										waterField(xx,y);
								y++;
							}
						}
						else {
							if(y_high != 0)
								y_low = y_high;
							y_high = y;
							tanl = tanh;
							tanh = y/x;
							int temp_x = x;
							while(inBoundsX(temp_x) && lawn[y0-y][x0+temp_x] == 0) {
								if(temp_x*temp_x+y*y == radius*radius)
									break;
								temp_x++;
							}
							if(temp_x*temp_x+y*y == radius*radius) {
								for(int yy = y_low; yy<=radius; yy++)
									for(int xx = startx; xx<=radius; xx++)
										if(lawn[y0-yy][x0+xx] != 0 && yy < y)
											waterField(xx,yy);
										else
											if(yy>y && yy/xx < tanh )
												mirrorOY(y, xx, yy);
							}
							else {
								
							}
						} 
					}
					if (lawn[y0-(y+100)][x0+x] != 0) {  		// przyp 2 pusto nad
						
					}
					if (lawn[y0-(y+100)][x0+x] == 0) { 			// przyp 3 nie pusto nad
						y_high = y;
						while(lawn[y0-(y_high)][x0+x] == 0)
							y_high += 100;
						
					}
					
				}
		
		if(!no_obstacle) {
			for(int y = y_high; y<=radius; y++)      
				for(int x = startx; x<=radius; x++) 
					if(x*x+y*y <= radius*radius) 					
						waterField(x,y);
		}
		
		if(no_obstacle) {
			for(int y = starty; y<=radius; y++)      
				for(int x = startx; x<=radius; x++) 
					if(x*x+y*y <= radius*radius) 					
						waterField(x,y);
		}
	}
	
	private void fillQuadrant2() {
		int startx = -1;
		int starty = 0;
		if(quadrant1 == false)
			startx = 0;
		
		for(int y = starty; y<=radius; y++)      
			for(int x = startx; x>=-radius; x--) 
				if(x*x+y*y <= radius*radius) {					
					//if(lawn[y0-y][x0+x] != 0)
						waterField(x,y);
				}
	}
	
	private void fillQuadrant3() {
		int startx = 0;
		int starty = -1;
		if(quadrant2 == false)
			starty = 0;
		
		for(int y = starty; y>=-radius; y--)      
			for(int x = startx; x>=-radius; x--) 
				if(x*x+y*y <= radius*radius) {					
					if(lawn[y0-y][x0+x] != 0)
						waterField(x,y);
				}
	}
	
	private void fillQuadrant4() {
		int startx = 1;
		int starty = 0;
		if(quadrant3 == false)
			startx = 0;
		
		for(int y = starty; y>=-radius; y--)      
			for(int x = startx; x<=radius; x++) 
				if(x*x+y*y <= radius*radius) {					
					if(lawn[y0-y][x0+x] != 0)
						waterField(x,y);
				}
	}
	private boolean inBoundsX(int x) {
		if(x0+x<lawn[0].length && x0+x>-1)
			return true;
		return false;
	}
	
	private boolean inBoundsY(int y) {
		if(y0-y<lawn.length && y0-y>-1 )
			return true;
		return false;
	}
	
	private void waterField(int x, int y) {
		if(inBoundsX(x) && inBoundsY(y))
			lawn[y0-y][x0+x]+=waterlvl;
		else if(!inBoundsX(x) && inBoundsY(y)) {
			if(x0+x>=lawn[0].length)
				mirrorOX(lawn[0].length-x0, x, y);
			else
				mirrorOX(-1-x0, x, y);
		}
		else if(inBoundsX(x) && !inBoundsY(y)) {
			if(y0-y>=lawn.length)
				mirrorOY(-(lawn.length-x0), x, y);
			else
				mirrorOY(1+y0, x, y);
		}
		else {
			int mirrorX;
			int mirrorY;
			if(x0+x>=lawn[0].length)
				mirrorX = lawn[0].length-x0;
			else
				mirrorX = -1-x0;
			if(y0-y>=lawn.length)
				mirrorY = -(lawn.length-x0);
			else
				mirrorY = 1+y0;
			mirrorOXOY(mirrorX,mirrorY, x, y);
		}
		
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
	
	private void mirrorOXOY(int mirror_x,int mirror_y, int x, int y) {
		if(set_rebounds == true) {
			if(x > 0) {
				x = 2*mirror_x - x - 1;
			}
			else {
				x = 2*mirror_x - x + 1;
			}
		
			if(y > 0) {
				y = 2*mirror_y - y - 1;
			}
			else {
				y = 2*mirror_y - y + 1;
			}
			waterField(x, y);
		}
	}
}
