package trawniczek;

public class Sprinkler {

	private int type;								// 90/180/270/360
	private int x;
	private int y;
	private int deg; 								// number of quadrant that is: 90 filled, 270 empty, 180 -> 12 means 1, 2 filled
	
	public Sprinkler(int type, int x, int y) { 		// 360 sprinkler
		this(type, x, y, 0);
	}
	
	public Sprinkler(int type, int x, int y, int deg) {
		this.type = type; 
		this.x = x;
		this.y = y; 
		this.deg = deg;
	}
	
	/*
	 *  Used for writing to file 
	 */
	@Override
	public String toString() {
		if(type == 90)
			return String.format("typ: %d,   koordynaty: (%d, %d), cwiartka zajeta: %d\n", type, x, y, deg);
		else if(type == 270)
			return String.format("typ: %d,  koordynaty: (%d, %d), cwiartka pusta: %d\n", type, x, y, deg);
		else if(type == 180) {
			switch(deg) {
			case 12:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 1,2\n", type, x, y);
			case 23:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 2,3\n", type, x, y);
			case 34:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 3,4\n", type, x, y);
			case 14:
				return String.format("typ: %d,  koordynaty: (%d, %d), cwiartki zajete: 1,4\n", type, x, y);
			}
		}
		return String.format("typ: %d,  koordynaty: (%d, %d)\n", type, x, y);
	}
	
	
	public void putSprinkler(short [][]lawn, boolean set_rebounds) {
		
	}

}
