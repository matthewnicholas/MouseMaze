package Mouse_in_a_maze;

/***
 * @author Matthew Nicholas
 * @credits original code by Jay Laughlin
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Driver 
{
	public static void main(String[] args) 
	{
		final int numberOfMice = 4;
		Mouse mice[] = new Mouse[numberOfMice];
		
		Maze maze = new Maze("Maze.txt");
		
		for(int i=0; i<numberOfMice; i++) {
			Mouse micky = maze.getMouse((char)(i+'A'));
			mice[i] = micky;
		}
		
		//find the end of the maze
		boolean emptyMaze = false;
		while (!emptyMaze)
		{
			for(Mouse micky : mice) {
				try{
				maze.drawMaze(mice);
				//Move the mouse
				micky.move(micky.senseDirection(maze, mice));
				Thread.sleep(1);
				}catch(Exception e) {}
			}
			//if (the mouse is at the exit)
			emptyMaze = true;
			for (Mouse m : mice)
				if(m.isInMaze())
					emptyMaze = false;
		}
		
		//System.out.println("Micky found the cheese");
		for (int i = 0; i < mice.length; i++) {
			System.out.println("Mouse " + i+1 + "(" + mice[i].sym + " int:" + mice[i].getInt() + ") escaped in " + mice[i] + " moves");
		}

	}

}
//This class should remain as is
class Maze
{
	//test maze
	int size = 0;//width and height (square)
	
	private String maze="";
	public Maze() //additional test case (to rule out file reading problems)
	{
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
	} //use the test maze
	public Maze(String mazeName)
	{
		try {
			Scanner s = new Scanner(new FileReader(new File(mazeName)));
			size = s.nextInt(); //first line in the file
			s.nextLine();
			for (int i=0;i<size;i++)
				maze += s.nextLine();
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
	public Mouse getMouse(char symbol)
	{
		for (int i=0;i<maze.length();i++)
		{
			if (maze.charAt(i) == symbol) //this is where the mouse is
			{
				maze = maze.substring(0, i) + ' ' + maze.substring(i+1);
				float intelligence = (float)(Math.random()*80)/100 + .2f;
				return new Mouse(i/size,i%size, symbol, intelligence);
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
	 * @param m - list of mice in the maze
	 */
	public void drawMaze(Mouse mice[])
	{
		for (Mouse m : mice) {
			for (int row=0;row<size;row++)
			{
				for (int col=0;col<size;col++)
				{
					if(m.getX() == col && m.getY() == row && m.isInMaze())
						System.out.print(m.sym);
					else
					System.out.print(maze.charAt(row*10+col));
	
				}
				System.out.println();
			}
		}
	}
}
class Mouse
{
	private int X, Y;
	//is the mouse still searching the maze
	private boolean inMaze = true;
	//objects in motion tend to keep that motion
	private int preferedDirection;
	//intelligence factor determins whether memories are created
	private float intelligence = .2f;
	//the mouses memories are storied in it's brain
	private point brain[] = new point[100];
	//metrix
	private int moves = 0;
	private int memIndex = 0;
	public char sym = 'M';
	

	public Mouse(int x, int y, char sym, float intelligence)
	{
		X=x;
		Y=y;
		this.sym = sym;
		this.intelligence = intelligence;
	}	
	public int getX() 
	{
		return X;
	}	
	public int getY() 
	{
		return Y;
	}
	public void MoveUp()
	{
		Y--;
	}
	public void MoveDown()
	{
		Y++;
	}	
	public void MoveRight()
	{
		X++;
	}
	public void MoveLeft()
	{
		X--;
	}
	
	Mouse(int x, int y, char sym){
		this(x, y, sym, .2f);
	}
	
	/***
	 * mouse looks randomly until he can find a way to go
	 * balance with an ai factor that remembers where it has been
	 * to avoid infinte loops
	 * @param m - maze in which the mouse resides
	 * @return 1-up; 2-right; 3-down; 4-left
	 */
	public int senseDirection(Maze m, Mouse mice[]) {
		char look;
		//to keep from getting cut off by a perpendicular path
		boolean ignoreMemory = false;
		int frustrationGauge = 0;
		
		if(!isInMaze()) return 0;
		
		while(true) {
			switch(preferedDirection) {
			case 1:
				look = Y-1 > -1 ? m.getCharAtPosition(Y-1, X) : 'X';
				if(look != 'X') {
					escape(look=='E'?false:true);
					//check if there is another mouse
					for(Mouse rival : mice)
						if(rival.getX() == X && rival.getY() == Y)
							break;

						if(!remember(X, Y-1) || ignoreMemory)
							return preferedDirection;
				}
				break;
				
			case 2:
				look = Y+1 < m.size ? m.getCharAtPosition(Y, X+1) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					for(Mouse rival : mice)
						if(rival.getX() == X && rival.getY() == Y)
							break;
						
					if(!remember(X+1, Y) || ignoreMemory)
						return preferedDirection;
					
				}
				break;
				
			case 3:
				look = Y+1 < m.size ? m.getCharAtPosition(Y+1, X) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					for(Mouse rival : mice)
						if(rival.getX() == X && rival.getY() == Y)
							break;
					
					if(!remember(X+1, Y) || ignoreMemory)
						return preferedDirection;
				}
				break;
				
			case 4:
				look = Y-1 > -1 ? m.getCharAtPosition(Y, X-1) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					for(Mouse rival : mice)
						if(rival.getX() == X && rival.getY() == Y)
							break;
					
					if(!remember(X+1, Y) || ignoreMemory)
						return preferedDirection;
				}
				break;
			}
			//smell another mouse and if i do i should go that way else just randomly pick a direction
			int didSmell = 0;//smelledAnother(mice); (mystery bug)
			int newDirection = getBiasedDirection(preferedDirection);
			preferedDirection = didSmell != 0 ? didSmell : newDirection;
			if(frustrationGauge > 4) {
				ignoreMemory = true;
			}
			frustrationGauge++;
		}
	}
	
	public int smelledAnother(Mouse mice[]) {
		for(Mouse m : mice) {
			for(int i = 1; i<5; i++) {
				for(point p : m.brain) {
					switch(i) {
					case 1:
						if(X == p.getX() && Y-1 == p.getY())
							return i;
					case 2:
						if(X+1 == p.getX() && Y == p.getY())
							return i;
						
					case 3:
						if(X == p.getX() && Y+1 == p.getY())
							return i;
					
					case 4:
						if(X-1 == p.getX() && Y == p.getY())
							return i;
					}
				}
			}
		}
		return 0;
	}
	
	//turning left and right is more likely they all the way around
	public int getBiasedDirection(int dir) {
		double rando = Math.random();
		//turn left
		if (rando > .6) {
			return (dir-1 > 0 ? dir-1:4);
		}
		//turn right
		else if (rando > .2) {
			return (dir+1 < 4? dir+1:4);
		}
		//uturn
		else{
			return (dir-2 > 0 ? dir-2 : (dir-2 > -1 ? 4 : 3));
		}
		
	}
	
	public void move(int direction) {
		if(direction == 0) return;
		//should we create a memory of this location
		float memory = (float)Math.random();
		if (memory <= intelligence) {
			//add location memory
			brain[memIndex] = new point(X, Y);
			memIndex++;
			//mice can only remember so much
			if (memIndex > 99){
				memIndex = 0;
			}
		}
		
		moves++;
		//move
		switch(direction) {
		case 1:
			MoveUp();
			break;
		case 2:
			MoveRight();
			break;
		case 3:
			MoveDown();
			break;
		case 4:
			MoveLeft();
			break;
		}
	}
	
	public boolean isInMaze() {
		return this.inMaze;
	}
	
	protected void escape(boolean inMaze) {
		this.inMaze = inMaze;
	}
	
	private boolean remember(int x, int y) {
		for (point p : brain) {
			if(p == null)
				return false;
			
			if(p.getX() == x && p.getY() == y);
				return true;
		}
		//not in memory
		return false;
	}
	
	public int getDirection() {
		return preferedDirection;
	}
	
	public void setDirection(int d) {
		this.preferedDirection = d;
	}
	
	public float getInt() {
		return intelligence;
	}
	
	public String toString() {
		return Integer.toString(moves);
	}
}

//memory data holder
class point{
	private int x,y;
	
	point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}

class robotMouse extends Mouse{
	/***
	 * Wall flower method
	 * @param x - start pos passed to parent class
	 * @param y - start pos passed to parent class
	 */
	robotMouse(int x, int y, char sym){
		super(x, y, sym);
	}
	
	public int senseDirection(Maze m){
		//always look to our left
		int startLook = ((getDirection() > 0 ? getDirection():1)+7)%4;
		char look;
		while(true) {
			switch(startLook) {
			case 1:
				look = this.getY()-1 > -1 ? m.getCharAtPosition(this.getY()-1, this.getX()) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					setDirection(startLook);
					return startLook;
					
				}
				break;
				
			case 2:
				look = this.getX()+1 < m.size ? m.getCharAtPosition(this.getY(), this.getX()+1) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					setDirection(startLook);
					return startLook;
					
				}
				break;
				
			case 3:
				look = this.getY()+1 < m.size ? m.getCharAtPosition(this.getY()+1, this.getX()) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					setDirection(startLook);
					return startLook;
				}
				break;
				
			case 4:
				look = this.getX()-1 > -1 ? m.getCharAtPosition(this.getY(), this.getX()-1) : 'X';
				if(look != 'X' && look != 'M') {
					escape(look=='E'?false:true);
					setDirection(startLook);
					return startLook;
				}
				break;
			}
			startLook++;
		}
		
	}
}