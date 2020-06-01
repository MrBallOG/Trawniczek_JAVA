package trawniczek;

public class Sprinkler {

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
	 *  fillQuadrant() methods skip x=0 column and y=0 row, but given how Animation class creates pixel, those fields are negligible
	 */
	private void fillQuadrant1() {
		quadrant1DownUp();
		quadrant1LeftRight();
	}
	
	private void fillQuadrant2() {
		quadrant2DownUp();
		quadrant2RightLeft();
	}
	
	private void fillQuadrant3() {
		quadrant3UpDown();
		quadrant3RightLeft();
	}
	
	private void fillQuadrant4() {
		quadrant4UpDown();
		quadrant4LeftRight();
	}
	
	/*
	 *  Updates Lawn's first quadrant from south to north
	 *  int i - used to count which column to update 
	 *  int j - used to count pixels to update in column
	 *  int xi - x coordinate of pixel to be watered
	 *  int direction - indicates direction of water, 1 if water goes south, -1 if water goes north (rebounds)
	 */
	private void quadrant1DownUp() {
		int i = y0-1;
		//sprinkle towards south if you can (not a wall)
		while(i >= y0 - radius && i >= 0 && lawn[i][x0] != 0)
			{
			int j = x0+1;
			int xi;
			int direction;
			//bounce immediately if wall
			if(x0 == lawn[0].length - 1 || lawn[i][x0+1] == 0)
				{
				xi = x0;
				direction = -1;
				}
			else
				{
				xi = x0+1;
				direction = 1;
				}
			//sprinkling towards south inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == 1)
						lawn[i][xi] += waterlvl;
				}
				else 
					lawn[i][xi] += waterlvl;
				//should the direction be changed (rebound)?
				if(xi+direction == -1 || xi+direction == lawn[0].length || lawn[i][xi+direction] == 0)
					{
					direction *= -1;
					}
				else
					{
					xi += direction;
					}
				++j;
				}
			--i;
			}
	}
	
	/*
	 * Analogical to quadrant1DownUp(...) but updates first quadrant from west to east
	 */
	private void quadrant1LeftRight() {
		int j = x0 + 1;
		//sprinkle towards west if you can (not a wall)
		while(j <= x0 + radius && j < lawn[0].length && lawn[y0][j] != 0)
			{
			int i = y0-1;
			int yi;
			int direction;
			//bounce immediately if wall
			if(y0 == 0 || lawn[y0-1][j] == 0)
				{
				yi = y0;
				direction = 1;
				}
			else
				{
				yi = y0-1;
				direction = -1;
				}
			//sprinkling towards west inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == -1)
						lawn[yi][j] += waterlvl;
				}
				else
					lawn[yi][j] += waterlvl;
				//should the direction be changed (rebound)?
				if(yi+direction == -1 || yi+direction == lawn.length || lawn[yi+direction][j] == 0)
					{
					direction *= -1;
					}
				else
					{
					yi += direction;
					}
				--i;
				}
			++j;
			}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates second quadrant
	 */
	private void quadrant2DownUp() {
			int i = y0-1;
			//sprinkle towards north if you can (not a wall)
			while(i >= y0 - radius && i >= 0 && lawn[i][x0] != 0)
				{
				int j = x0-1;
				int xi;
				int direction;
				//bounce immediately if wall
				if(x0 == 0 || lawn[i][x0-1] == 0)
					{
					xi = x0;
					direction = 1;
					}
				else
					{
					xi = x0-1;
					direction = -1;
					}
				//sprinkling towards north inside of a circle
				while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
					{
					if(!set_rebounds) {
						if(direction == -1)
							lawn[i][xi] += waterlvl;
					}
					else 
						lawn[i][xi] += waterlvl;
					//should the direction be changed (rebound)?
					if(xi+direction == -1 || xi+direction == lawn[0].length || lawn[i][xi+direction] == 0)
						{
						direction *= -1;
						}
					else
						{
						xi += direction;
						}
					--j;
					}
				--i;
				}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates second quadrant from east to west
	 */
	private void quadrant2RightLeft() {
		int j = x0 - 1;
		//sprinkle towards west if you can (not a wall)
		while(j >= x0 - radius && j >= 0 && lawn[y0][j] != 0)
			{
			int i = y0-1;
			int yi;
			int direction;
			//bounce immediately if wall
			if(y0 == 0 || lawn[y0-1][j] == 0)
				{
				yi = y0;
				direction = 1;
				}
			else
				{
				yi = y0-1;
				direction = -1;
				}
			//sprinkling towards west inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == -1)
						lawn[yi][j] += waterlvl;
				}
				else 
					lawn[yi][j] += waterlvl;
				//should the direction be changed (rebound)?
				if(yi+direction == -1 || yi+direction == lawn.length || lawn[yi+direction][j] == 0)
					{
					direction *= -1;
					}
				else
					{
					yi += direction;
					}
				--i;
				}
			--j;
			}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates third quadrant from north to south
	 */
	private void quadrant3UpDown() {
		int i = y0+1;
		//sprinkle towards north if you can (not a wall)
		while(i <= y0 + radius && i < lawn.length && lawn[i][x0] != 0)
			{
			int j = x0-1;
			int xi;
			int direction;
			//bounce immediately if wall
			if(x0 == 0 || lawn[i][x0-1] == 0)
				{
				xi = x0;
				direction = 1;
				}
			else
				{
				xi = x0-1;
				direction = -1;
				}
			//sprinkling towards north inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == -1)
						lawn[i][xi] += waterlvl;
				}
				else
					lawn[i][xi] += waterlvl;
				//should the direction be changed (rebound)?
				if(xi+direction == -1 || xi+direction == lawn[0].length || lawn[i][xi+direction] == 0)
					{
					direction *= -1;
					}
				else
					{
					xi += direction;
					}
				--j;
				}
			++i;
			}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates third quadrant from east to west
	 */
	private void quadrant3RightLeft() {
		int j = x0 - 1;
		//sprinkle towards east if you can (not a wall)
		while(j >= x0 - radius && j >= 0 && lawn[y0][j] != 0)
			{
			int i = y0+1;
			int yi;
			int direction;
			//bounce immediately if wall
			if(y0 == lawn.length - 1 || lawn[y0+1][j] == 0)
				{
				yi = y0;
				direction = -1;
				}
			else
				{
				yi = y0+1;
				direction = 1;
				}
			//sprinkling towards east inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == 1)
						lawn[yi][j] += waterlvl;
				}
				else
					lawn[yi][j] += waterlvl;
				//should the direction be changed (rebound)?
				if(yi+direction == -1 || yi+direction == lawn.length || lawn[yi+direction][j] == 0)
					{
					direction *= -1;
					}
				else
					{
					yi += direction;
					}
				++i;
				}
			--j;
			}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates fourth quadrant from north to south
	 */
	private void quadrant4UpDown() {
		int i = y0+1;
		//sprinkle towards south if you can (not a wall)
		while(i <= y0 + radius && i < lawn.length && lawn[i][x0] != 0)
			{
			int j = x0+1;
			int xi;
			int direction;
			//bounce immediately if wall
			if(x0 == lawn[0].length - 1 || lawn[i][x0+1] == 0)
				{
				xi = x0;
				direction = -1;
				}
			else
				{
				xi = x0+1;
				direction = 1;
				}
			//sprinkling towards south inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == 1)
						lawn[i][xi] += waterlvl;
				}
				else
					lawn[i][xi] += waterlvl;
				//should the direction be changed (rebound)?
				if(xi+direction == -1 || xi+direction == lawn[0].length || lawn[i][xi+direction] == 0)
					{
					direction *= -1;
					}
				else
					{
					xi += direction;
					}
				++j;
				}
			++i;
			}
	}
	
	/*
	 * Analogical to Quadrant1DownUp(...) but updates fourth quadrant from west to east
	 */
	private void quadrant4LeftRight() {
		int j = x0 + 1;
		//sprinkle towards east if you can (not a wall)
		while(j <= x0 + radius && j < lawn[0].length && lawn[y0][j] != 0)
			{
			int i = y0+1;
			int yi;
			int direction;
			//bounce immediately if wall
			if(y0 == lawn.length - 1 || lawn[y0+1][j] == 0)
				{
				yi = y0;
				direction = -1;
				}
			else
				{
				yi = y0+1;
				direction = 1;
				}
			//sprinkling towards east inside of a circle
			while((y0-i)*(y0-i)+(x0-j)*(x0-j) <= radius*radius)
				{
				if(!set_rebounds) {
					if(direction == 1)
						lawn[yi][j] += waterlvl;
				}
				else
					lawn[yi][j] += waterlvl;
				//should the direction be changed (rebound)?
				if(yi+direction == -1 || yi+direction == lawn.length || lawn[yi+direction][j] == 0)
					{
					direction *= -1;
					}
				else
					{
					yi += direction;
					}
				++i;
				}
			++j;
			}
	}
	
}
