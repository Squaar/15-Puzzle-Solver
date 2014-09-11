import java.awt.Point;
import java.util.ArrayList;

public class Board{

	private int[][] board;
	public enum Direction {UP, DOWN, LEFT, RIGHT}
	private ArrayList<Direction> solutionPath;
	private Point zeroPos;

	private static final int[][] solvedBoard = {
		{1, 2, 3, 4},
		{5, 6, 7, 8},
		{9, 10, 11, 12},
		{13, 14, 15, 0}
	};

	public Board(int[][] board){
		this.board = board;
		this.zeroPos = findZero();
		this.solutionPath = new ArrayList<Direction>();
	}
	
	private Board(int[][] board, ArrayList<Direction> solutionPath){
		this.board = board;
		this.zeroPos = findZero();
		this.solutionPath = solutionPath;
	}

	private Point findZero() throws IllegalStateException{
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(board[i][j] == 0){
					return new Point(j, i);
				}
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
			for(int i: r){
				s += i + " ";
			}
			s += "\n";
		}
		return s;
	}

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

	public Board move(Direction d){
		ArrayList<Direction> thisSolution = this.solutionPath;
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

	public ArrayList<Direction> getSolutionPath(){
		return solutionPath;
	}
}
