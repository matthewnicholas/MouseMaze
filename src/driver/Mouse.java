package driver;

import java.util.LinkedList;

public class Mouse
{
	Coord location;
	LinkedList<Coord> traversedSpaces = new LinkedList<Coord>();
	
	public Mouse(int X, int Y)
	{
		location = new Coord(X, Y);
	}
	
	public int getX() {return location.getX();}
	public int getY() {return location.getY();}
	// TODO: test
	public boolean move(Maze maze, Direction direction)
	{
		if (look(maze, direction) == Choices.AVAILABLE)
		{
			this.location.setDirection(direction);
			return true;
		}
		// else don't move
		return false;
	}
	// TODO: test
	public void rememberSpace(Coord remember)
	{
		traversedSpaces.add(remember);
	}
	// TODO: test // maybe just delete this method in favor of *.contains() ?
	private boolean isTraversed(Coord coord)
	{
		// look through where mouse has been
		for (int i = 0; i < traversedSpaces.size(); i++)
		{
			// if any spots match, mouse has been there
			if ((traversedSpaces.get(i).getX() == coord.getX()) &&
				(traversedSpaces.get(i).getY() == coord.getY()))
				return true;
		}
		// if none match, mouse doesn't remember being there
		return false;
	}
	// TODO: test
	private Choices look(Maze maze, Direction direction)
	{
		// look at a coord without changing it
		Coord lookAtCoord = new Coord (location.getX(), location.getY());
		lookAtCoord.setDirection(direction);
		
		// remember what mouse saw there
		char look = maze.getCharAtPosition(lookAtCoord.getX(), lookAtCoord.getY());
		
		// figure out what it just saw
		if (look == 'X')
			return Choices.BLOCKED;
		else if (look == ' ' || look == 'E')
		{
			if (isTraversed(lookAtCoord))
				return Choices.TRAVERSED;
			else
				return Choices.AVAILABLE;
		}
		else
			return Choices.BLOCKED;
	}
	
	
	// TODO: test
	public void mazeTick(Maze maze)
	{			
		// if all open areas were traversed, backtrack
		if ((look(maze, Direction.UP) != Choices.AVAILABLE) &&
				(look(maze, Direction.DOWN) != Choices.AVAILABLE) &&
				(look(maze, Direction.LEFT) != Choices.AVAILABLE) &&
				(look(maze, Direction.RIGHT) != Choices.AVAILABLE))				
				traversedSpaces.removeLast();
		// TODO: prioritize "left side"
		// with regards to previous movement direction
		if(move(maze, Direction.LEFT))
			return;
		else if (move(maze, Direction.DOWN))
			return;
		else if (move(maze, Direction.RIGHT))
			return;
		else if (move(maze, Direction.UP))
			return;
	}
	private enum Choices {
		TRAVERSED,
		AVAILABLE,
		BLOCKED
	}
}
