package test;

import java.awt.Point;
import java.util.ArrayList;

public class Board{

	private int[][] board;
	public enum Direction {UP, DOWN, LEFT, RIGHT}
	private Point zeroPos;

	private int[][] solvedBoard = {
		{1, 2, 3, 4},
		{5, 6, 7, 8},
		{9, 10, 11, 12},
		{13, 14, 15, 0}
	};

	public Board(int[][] board){
		this.board = board;
		this.zeroPos = findZero();
	}

	private Point findZero() throws IllegalStateException{
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				if(board[i][j] == 0)
					return new Point(i, j);
			}
		}
		throw new IllegalStateException("Zero not found.");
	}

	public boolean isSolved(){
		return this.board == solvedBoard;
	}

	public String toString(){
		String s = "";
		for(int[] r: this.board){
			s += r.toString() + "\n";
		}
		return s;
	}

	public Direction[] moveableDirections(){
		ArrayList<Direction> goodDirs = new ArrayList<Direction>();
		if(zeroPos.x != 0)
			goodDirs.add(Direction.RIGHT);
		if(zeroPos.x != 3)
			goodDirs.add(Direction.LEFT);
		if(zeroPos.y != 0)
			goodDirs.add(Direction.DOWN);
		if(zeroPos.y != 3)
			goodDirs.add(Direction.UP);

		return goodDirs.toArray(new Direction[0]);
	}

	public Board move(Direction d){
		switch(d){
			case UP:
				return swapTile(zeroPos, new Point(zeroPos.x, zeroPos.y-1));
			case DOWN:
				return swapTile(zeroPos, new Point(zeroPos.x, zeroPos.y+1));
			case LEFT:
				return swapTile(zeroPos, new Point(zeroPos.x-1, zeroPos.y));
			case RIGHT:
				return swapTile(zeroPos, new Point(zeroPos.x+1, zeroPos.y));
		}
		throw new UnsupportedOperationException("Cannot move in that direction");
	}
	
	private Board swapTile(Point a, Point b){
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
		return new Board(newBoard);
	}
}
