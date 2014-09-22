package puzzle;

import java.awt.Point;
import java.util.ArrayList;

public class Board{

	public enum Direction {UP, DOWN, LEFT, RIGHT}

	private int[][] board;
	private ArrayList<Direction> solutionPath;
	private Point zeroPos;
	private int heuristic1;
	private int heuristic2;

	// a solved board
	private static final int[][] solvedBoard = {
		{1, 2, 3, 4},
		{5, 6, 7, 8},
		{9, 10, 11, 12},
		{13, 14, 15, 0}
	};

	public Board(int[][] board){
		this.board = board;
		this.zeroPos = findTile(0);
		this.solutionPath = new ArrayList<Direction>();
		heuristics();
	}
	
	// constructor used to keep track of solution path
	private Board(int[][] board, ArrayList<Direction> solutionPath){
		this.board = board;
		this.zeroPos = findTile(0);
		this.solutionPath = solutionPath;
		heuristics();
	}

	public int getHeuristic1(){
		return heuristic1;
	}

	public int getHeuristic2(){
		return heuristic2;
	}

	// finds the location of a tile in the board
	private Point findTile(int tile) throws IllegalStateException{
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(board[i][j] == tile){
					return new Point(j, i);
				}
			}
		}
		throw new IllegalStateException("Zero not found.");
	}

	// find where a tile should be in a solved board
	private Point findSolvedLocation(int tile){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(solvedBoard[i][j] == tile)
					return new Point(j,i);
			}
		}
		return null; // should never get herre unless something goes super wrong!
	}

	// check to see if the board is solved
	public boolean isSolved(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(board[i][j] != solvedBoard[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	public String toString(){
		String s = "";
		for(int[] r: this.board){
			for(int i: r){
				if(i==0)
					s += "_ ";
				else
					s += i + " ";
			}
			s += "\n";
		}
		return s;
	}

	// the valid directions you can move the blank tile
	public Direction[] moveableDirections(){
		ArrayList<Direction> goodDirs = new ArrayList<Direction>();
		if(zeroPos.x != 3)
			goodDirs.add(Direction.RIGHT);
		if(zeroPos.x != 0)
			goodDirs.add(Direction.LEFT);
		if(zeroPos.y != 3)
			goodDirs.add(Direction.DOWN);
		if(zeroPos.y != 0)
			goodDirs.add(Direction.UP);

		return goodDirs.toArray(new Direction[0]);
	}

	// return a new board as a result of moving the blank tile
	public Board move(Direction d){
		ArrayList<Direction> thisSolution = new ArrayList<Direction>(this.solutionPath);
		switch(d){
			case UP:
				thisSolution.add(Direction.UP);
				return new Board(swapTile(zeroPos, new Point(zeroPos.x, zeroPos.y-1)), thisSolution);
			case DOWN:
				thisSolution.add(Direction.DOWN);
				return new Board(swapTile(zeroPos, new Point(zeroPos.x, zeroPos.y+1)), thisSolution);
			case LEFT:
				thisSolution.add(Direction.LEFT);
				return new Board(swapTile(zeroPos, new Point(zeroPos.x-1, zeroPos.y)), thisSolution);
			case RIGHT:
				thisSolution.add(Direction.RIGHT);
				return new Board(swapTile(zeroPos, new Point(zeroPos.x+1, zeroPos.y)), thisSolution);
		}
		throw new UnsupportedOperationException("Cannot move in that direction");
	}
	
	// construct the new board for .move()
	private int[][] swapTile(Point a, Point b){
		int[][] newBoard = new int[4][4];
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(i == a.y && j == a.x)
					newBoard[i][j] = board[b.y][b.x];
				else if(i == b.y && j == b.x)
					newBoard[i][j] = board[a.y][a.x];
				else
					newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}

	// get the moves taken to get this board from the original
	public ArrayList<Direction> getSolutionPath(){
		return solutionPath;
	}

	public int[][] getBoard(){
		return this.board;
	}

	public boolean equals(Board other){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(this.board[i][j] != other.getBoard()[i][j])
					return false;
			}
		}
		return true;
	}

	// calculate both heuristics
	private void heuristics(){
		int heuristic1 = 0;
		int heuristic2 = 0;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(this.board[i][j] != solvedBoard[i][j]){
					heuristic1++;
					Point rightSpot = findSolvedLocation(this.board[i][j]);
					heuristic2 += Math.abs(rightSpot.y - i) + Math.abs(rightSpot.x - j);
				}
			}
		}
		this.heuristic1 = heuristic1;
		this.heuristic2 = heuristic2;
	}
}


