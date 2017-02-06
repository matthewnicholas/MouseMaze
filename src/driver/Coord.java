package driver;

/**
 * @params X - positive integer
 * @params Y - positive integer
 * @return contains X and Y; -1 means not yet set
 * @see Direction
 */
public class Coord
{
	private int X;
	private int Y;
	
	public Coord()
	{
		this.X = -1;
		this.Y = -1;
	}
	
	public Coord(int X, int Y)
	{
		this.X = X;
		this.Y = Y;
	}
	
	public void setX(int X)
	{
		if (X >= 0)
			this.X = X;
		else
			this.X = -1;
	}
	public void setY(int Y)
	{
		if (Y >= 0)
			this.Y = Y;
		else
			this.X = -1;	
	}
	public int getX()
	{
		if (this.X == -1)
			System.out.println("Coord:  X has not been set yet!");
		return this.X;
	}
	public int getY()
	{
		if (this.Y == -1)
			System.out.println("Coord:  Y has not been set yet!");
		return this.Y;
	}
	/**
	 * @param direction
	 * @return Coord that is adjacent self, in direction specified. Where "0, 0" is "top left" of a coordinate plane
	 */
	public Coord setDirection (Direction direction)
	{
		Coord resultingCoord = this;
		switch (direction)
		{
			// x, y changes (increment/decrement respectively on case)
		case UP: case NORTH:
			// __, --
			resultingCoord.setY(this.getY() -1);
			break;
		case DOWN: case SOUTH:
			// __, ++
			resultingCoord.setY(this.getY() +1);
			break;
		case RIGHT: case EAST:
			// ++,__
			resultingCoord.setX(this.getX() +1);
			break;
		case LEFT: case WEST:
			// --,__
			resultingCoord.setX(this.getX() -1);
			break;
		default:
			// -1, -1
			resultingCoord = new Coord();
			break;
		}
		
		return resultingCoord;
	}
}
