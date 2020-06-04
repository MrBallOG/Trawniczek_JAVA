package positioning;

import java.util.List;

import sprinkler.Sprinkler;

public class PositionSprinklers {
	
	protected short[][]lawn;
	protected List<Sprinkler> sprlist;				// List of sprinklers
	
	/*
	 *	Iterates on lawn to find possible rectangles to put sprinklers in
	 */
	public void scanTheLawnForRectangles() {
		int x0 = 0; // x of the cursor
		int y0 = 0; // y of the cursor
		int pixels_distance = 100; // jump distance
		while (y0 < lawn.length)
		{
			while (x0 < lawn[0].length)
			{
				if (lawn[y0][x0] > 0) // if not wall or already used
				{
					int availableSpace = checkSpace(x0, y0, pixels_distance); // check in which direction it will be shorter
					if (availableSpace >= 4) // if it is at least 2x2
					{
						int rect_x = 0; // x size of rectangle in 100x100 squares
						int rect_y = 0; // y size of rectangle in 100x100 squares
						if (availableSpace % 2 == 0)
						{
							rect_x = availableSpace / 2;
							rect_y = scanVerticalSize(rect_x, x0, y0, pixels_distance);
							fillRectangleVerticaly(rect_x, rect_y, x0, y0);
						}
						else
						{
							rect_y = availableSpace / 2;
							rect_x = scanHorizontalSize(rect_y, x0, y0, pixels_distance);
							fillRectangleHorizontaly(rect_x, rect_y, x0, y0);
						}
					}
				}
				x0 += pixels_distance;
			}
			x0 = 0;
			y0 += pixels_distance;
		}
	}

	/*
	 *	Tries to find shorter dimension for the rectangle
	 *
	 * 	Could add better prorities later
	 */
	private int checkSpace(int x0, int y0, int pixels_distance) {
		int count = 0;
		int x = x0;
		int y = y0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress && count < 20)
		{
			count++;
			if (x0 + count * pixel >= lawn[0].length)
			{
				in_progress = false;
				count = count * 2;
			}
			else if (y0 + count * pixel >= lawn.length)
			{
				in_progress = false;
				count = count * 2 + 1;
			}
			if (in_progress)
			{
				x = x0 + count * pixel;
				for (y = y0; y < y0 + count * pixel; y += pixel)
				{
					if (lawn[y][x] <= 0)
					{
						in_progress = false;
						count = count * 2;
						break;
					}
				}
			}
			if (in_progress)
			{
				y = y0 + count * pixel;
				for (x = x0; x <= x0 + count * pixel; x += pixel)
				{
					if (lawn[y][x] <= 0)
					{
						in_progress = false;
						count = count * 2 + 1;
						break;
					}
				}
			}
		}
		return count;
	}

	private int scanVerticalSize(int H, int x0, int y0, int pixels_distance) {
		int x = x0;
		int y = y0;
		int V = 0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress)
		{
			for (x = x0; x < x0 + H * pixel; x += pixel)
			{
				if (lawn[y][x] <= 0)
				{
					in_progress = false;
					break;
				}
				lawn[y][x] *= -1;
			}
			if (in_progress)
			{
				V++;
				y += pixel;
				if (y >= lawn.length)
				{
					in_progress = false;
				}
			}
		}
		return V;
	}

	private int scanHorizontalSize(int V, int x0, int y0, int pixels_distance) {
		int x = x0;
		int y = y0;
		int H = 0;
		int pixel = pixels_distance;
		boolean in_progress = true;
		while (in_progress)
		{
			for (y = y0; y < y0 + V * pixel; y += pixel)
			{
				if (lawn[y][x] <= 0)
				{
					in_progress = false;
					break;
				}
				lawn[y][x] *= -1;
			}
			if (in_progress)
			{
				H++;
				x += pixel;
				if (x >= lawn[0].length)
				{
					in_progress = false;
				}
			}
		}
		return H;
	}

	private void fillRectangleVerticaly(int H, int V, int x0, int y0) {
		if (H == 2 || H == 3)
		{
			int x = (x0 + x0 + H * 100)/2;
			int sy = y0 ;
			int ey = y0 + V * 100;
			int ry = (ey - sy - 1) / 300 + 1;
			int jump;
			int y;
			if (ry > 1)
			{
				jump = (ey - sy) / (ry);
				y = sy + jump/2;
			}
			else
			{
				jump = (ey - sy) / 2;
				y = y0 + jump;
			}
			for (int i = 0; i < ry; i++)
			{
				sprlist.add(new Sprinkler(360, x, y));
				y += jump;
			}
		}
		if (H == 4)
		{
			int x1 = x0;
			int x2 = x0 + 399;
			int sy = y0 ;
			int ey = y0 + V * 100;
			int ry = (V - 1 ) / 5 + 1;
			int jump;
			int y;
			if (ry > 1)
			{
				int offset = (V - 5 * (ry - 1)) * 50;
				y = y0 + offset;
				jump = 500;
			}
			else
			{
				jump = (ey - sy) / 2;
				y = y0 + jump;
			}
			for (int i = 0; i < ry; i++)
			{
				if (i % 2 == 0)
				{
					sprlist.add(new Sprinkler(180, x1, y, 14));
				}
				else
				{
					sprlist.add(new Sprinkler(180, x2, y, 23));
				}
				y += jump;
			}
		}
		if (H == 5)
		{
			int mod5 = V % 5;
			if (mod5 <= 2)
			{
				sprlist.add(new Sprinkler(90, x0, y0, 4));
				sprlist.add(new Sprinkler(90, x0 + 499, y0 + 499 + 100 * mod5, 2));
				if (V-5-mod5 >= 5)
				{
					fillRectangleVerticaly(H, V - 5 - mod5, x0, y0 + 500 + 100 * mod5);
				}
			}
			else
			{
				sprlist.add(new Sprinkler(90, x0, y0, 4));
				sprlist.add(new Sprinkler(90, x0, y0 + 499 + 100 * mod5, 1));
				sprlist.add(new Sprinkler(180, x0 + 499 , (y0 +y0 + 499 + 100 * mod5 ) / 2, 23));
				if (V-5-mod5 >= 5)
				{
					fillRectangleVerticaly(H, V - 5 - mod5, x0, y0 + 500 + 100 * mod5);
				}
			}
		}
		if (H >= 6)
		{
			fillRectangleVerticaly(3, V, x0, y0);
			fillRectangleVerticaly(H - 3, V, x0 + 300, y0);
		}
	}

	private void fillRectangleHorizontaly(int H, int V, int x0, int y0) {
		if (V == 2 || V == 3)
		{
			int y = (y0 + y0 + V * 100)/2;
			int sx = x0 ;
			int ex = x0 + H * 100;
			int rx = (ex - sx - 1) / 300 + 1;
			int jump;
			int x;
			if (rx > 1)
			{
				jump = (ex - sx) / (rx);
				x = sx + jump/2;
			}
			else
			{
				jump = (ex - sx) / 2;
				x = x0 + jump;
			}
			for (int i = 0; i < rx; i++)
			{
				sprlist.add(new Sprinkler(360, x, y));
				x += jump;
			}
		}
		if (V == 4)
		{
			int y1 = y0;
			int y2 = y0 + 399;
			int sx = x0 ;
			int ex = x0 + H * 100;
			int rx = (H - 1) / 5 + 1;
			int jump;
			int x;
			if (rx > 1)
			{
				int offset = (H - 5 * (rx - 1)) * 50;
				x = x0 + offset;
				jump = 500;
			}
			else
			{
				jump = (ex - sx) / 2;
				x = x0 + jump;
			}
			for (int i = 0; i < rx; i++)
			{
				if (i % 2 == 0)
				{
					sprlist.add(new Sprinkler(180, x, y1, 34));
				}
				else
				{
					sprlist.add(new Sprinkler(180, x, y2, 12));
				}
				x += jump;
			}
		}
		if (V == 5)
		{
			int mod5 = H % 5;
			if (mod5 <= 2)
			{
				sprlist.add(new Sprinkler(90, x0, y0, 4));
				sprlist.add(new Sprinkler(90, x0 + 499 + 100 * mod5, y0 + 499 , 2));
				if (H-5-mod5 >= 5)
				{
					fillRectangleHorizontaly(H - 5 - mod5, V , x0 + 500 + 100 * mod5, y0 );
				}
			}
			else
			{
				sprlist.add(new Sprinkler(90, x0, y0, 4));
				sprlist.add(new Sprinkler(90, x0 + 499 + 100 * mod5, y0 , 3));
				sprlist.add(new Sprinkler(180, (x0 + x0 + 499 + 100 * mod5) / 2, y0 + 499, 12));
				if (H-5-mod5 >= 5)
				{
					fillRectangleHorizontaly(H - 5 - mod5, V , x0 + 500 + 100 * mod5, y0 );
				}
			}
		}
		if (V >= 6)
		{
			fillRectangleHorizontaly(H, 3, x0, y0);
			fillRectangleHorizontaly(H, V - 3, x0 , y0 + 300);
		}
	}
}