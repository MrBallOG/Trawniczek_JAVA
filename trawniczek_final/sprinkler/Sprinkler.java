package sprinkler;

public class Sprinkler extends SprinkleLawn{

	private int type;								// 90/180/270/360
	private int x0;									// x value of center of sprinkler
	private int y0;									// y value of center of sprinkler
	private int deg; 								// number of quadrant that is: 90 filled, 270 empty, 180 -> 12 means 1, 2 filled
	private int radius;								// radius of sprinkler: 360-200, 270-300, 180-400, 90-500
	private int waterlvl;							// amount of water for lawn to be watered 360-1, 270-2, 180-3, 90-4
	private short lawn[][];
	private boolean set_rebounds;
	
	
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
	 *  Used for writing to file, turns Sprinkler's field to text
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
				break;
			case 2:
				fillQuadrant2();
				break;
			case 3:
				fillQuadrant3();
				break;
			case 4:
				fillQuadrant4();
				break;
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
				break;
			case 2:
				fillQuadrant1();
				fillQuadrant3();
				fillQuadrant4();
				break;
			case 3:
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant4();
				break;
			case 4:
				fillQuadrant1();
				fillQuadrant2();
				fillQuadrant3();
				break;
			}
		}
		else if(type == 180) {
			radius = 400;
			waterlvl = 3;
			switch(deg) {
			case 12:
				fillQuadrant1();
				fillQuadrant2();
				break;
			case 23:
				fillQuadrant2();
				fillQuadrant3();
				break;
			case 34:
				fillQuadrant3();
				fillQuadrant4();
				break;
			case 14:
				fillQuadrant1();
				fillQuadrant4();
				break;
			}
		}
		else {
			radius = 200;
			waterlvl = 1;
			fillQuadrant1();
			fillQuadrant2();
			fillQuadrant3();
			fillQuadrant4();
		}
	}
	
	/*
	 *  fillQuadrant() methods use appropriate quadrant() methods of superclass SprinkeLawn
	 *  quadrant() methods skip x=0 column and y=0 row, but given how Animation class creates pixel, those fields are negligible
	 */
	private void fillQuadrant1() {
		quadrant1DownUp(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
		quadrant1LeftRight(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
	}
	
	private void fillQuadrant2() {
		quadrant2DownUp(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
		quadrant2RightLeft(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
	}
	
	private void fillQuadrant3() {
		quadrant3UpDown(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
		quadrant3RightLeft(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
	}
	
	private void fillQuadrant4() {
		quadrant4UpDown(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
		quadrant4LeftRight(type, x0, y0, lawn, radius, waterlvl, set_rebounds);
	}

}