package driver;

import java.util.Scanner;

/**
* reads Maze.txt then navigates "M" mouse to "E" exit 
* @author Matthew_Nicholas, Emily_Tewey
*/
// TODO: mouse currently gets stuck at (4,1) and (4,2)
// something to do with backtracking or priorties?
public class Driver
{
	
	public static void main(String[] args)
	{

		Maze maze = new Maze("Maze.txt");
		Mouse micky = maze.getMouse();
		// for printing one part at a time, while testing
		System.out.println("Press \"ENTER\" to continue...");
		Scanner scanner = new Scanner(System.in);
		
		//find the end of the maze
		while (true)
		{
			maze.drawMaze(micky);

			micky.mazeTick(maze);
			
			if (maze.checkWin(micky))
				break;
			
		   scanner.nextLine();
		}
		scanner.close();
		
		System.out.println("Micky found the cheese");
	}

}