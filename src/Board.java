import java.awt.Point;

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
		return null;
	}
}
