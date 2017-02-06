/**
 * @author Emily Tewey
 * @author Matthew Nicholas
 */

package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

//This class should remain as is (J_Laughlin)
class Maze
{
	//test maze
	int size = 0;//width and height (square)
	//store the maze.txt input
	private String maze="";
	// easier on the eyes
	private static int columnSpacing= 1;
	// we don't know where exit is yet. we use it to test whether the mouse won
	private int exit = -1;
	
	//use the test maze
	public Maze()
	{
		//additional test case (to rule out file reading problems)
		maze =    "XXXXXXXXXX"+
				  "XM  M X EX"+
				  "X XXXXXM X"+
				  "X X   X XX"+
				  "X X X X  X"+
				  "X X X XX X"+
				  "X   X    X"+
				  "X XXXXXX X"+
				  "X        X"+
				  "XXXXXXXXXX";
		size = 10;
		findExit();
	}
	// use txt maze
	public Maze(String mazeName)
	{
		try {
			Scanner mazeScanner = new Scanner(new FileReader(new File(mazeName)));
			size = mazeScanner.nextInt(); //first line in the file
			mazeScanner.nextLine();
			for (int i=0;i<size;i++)
				maze += mazeScanner.nextLine();
			mazeScanner.close(); // TODO: test // this line was not part of original. Should I be allowed to modify it?
		} catch (FileNotFoundException e) {}
		findExit();
	}

	// read the maze file and find where 'M' mouse is
	public Mouse getMouse()
	{
		for (int i=0;i<maze.length();i++)
		{
			if (maze.charAt(i) == 'M') //this is where the mouse is
			{
				maze = maze.substring(0, i) + ' ' + maze.substring(i+1);
				return new Mouse(i/size,i%size);
			}
		}
		return null;
	}
	/**
	 * Mouse's eyes
	 */
	public char getCharAtPosition(int row, int col)
	{
		return maze.charAt(row*size+col);
	}
	/**
	 * Drawn at each step of the mouse's movement
	 * @param m
	 */
	public void drawMaze(Mouse m)
	{
		for (int row=0;row<size;row++)
		{
			for (int col=0;col<size;col++)
			{
				for (int i = 0; i < columnSpacing; i++)
					System.out.print(" ");
				if (m.location.getX() == col && m.location.getY() == row)
					System.out.print("M");
				else
					System.out.print(maze.charAt(row*size+col));
			}
			System.out.println("");
		}
		System.out.println();
	}
	private void findExit()
	{
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++)
				if (getCharAtPosition(row, column) == 'E')
					exit = row * size + column;
	}
	public int getExit() {return exit;}
	
	// some fun with 2D<-->1D
	public int getExitX() {return exit - getExitY() * size;}
	public int getExitY() {return exit / size;}
	
	public boolean checkWin(Mouse m)
	{
		if (exit != -1)
			// row * size + col  is converting 1D<-->2D
			return exit == m.getY() * size + m.getX();
		else
		{
			// TODO: possible infinite loop here, but I'm just assuming the exit always exists ;-P
			findExit();
			return checkWin(m);
		}
	}
}
	