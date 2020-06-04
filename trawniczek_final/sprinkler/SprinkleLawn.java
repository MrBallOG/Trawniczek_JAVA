package sprinkler;

public abstract class SprinkleLawn {

	/*
	 *  Updates Lawn's first quadrant from south to north
	 *  int i - used to count which column to update 
	 *  int j - used to count pixels to update in column
	 *  int xi - x coordinate of pixel to be watered
	 *  int direction - indicates direction of water, 1 if water goes south, -1 if water goes north (rebounds)
	 */
	protected void quadrant1DownUp(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant1LeftRight(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant2DownUp(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant2RightLeft(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant3UpDown(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant3RightLeft(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant4UpDown(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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
	protected void quadrant4LeftRight(int type, int x0, int y0, short[][]lawn, int radius, int waterlvl, boolean set_rebounds) {
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