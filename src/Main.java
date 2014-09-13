import puzzle.Board;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main{
	public static void main(String args[]){
		Board board = null;
		try{
			board = getPuzzle(args[0]);
		}		
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Please specify a file with the puzzle.");
			System.exit(1);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		Board breadthSolution = breadthFirstSearch(board);
		printSolution(board, breadthSolution);
		System.out.println("================================");
		Board depthSolution = iterativeDepthFirstSearch(board);
		printSolution(board, depthSolution);
	}

	public static Board getPuzzle(String filePath) throws IOException{
		Scanner sc = new Scanner(new File(filePath));
		sc.useDelimiter("[\n,]");
		int[][] matrix = new int[4][4];
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(sc.hasNext()){
					String s = sc.next();
					if(s.equals("_"))
							matrix[i][j] = 0;
					else
						matrix[i][j] = Integer.parseInt(s);
				}
			}
		}
		sc.close();
		return new Board(matrix);
	}

	public static void printSolution(Board board, Board solution){
		System.out.println(board);

		for(Board.Direction d: solution.getSolutionPath()){
			board = board.move(d);
			System.out.println(d);
			System.out.println(board);
		}
	}

	public static Board breadthFirstSearch(Board board){
		LinkedList<Board> queue = new LinkedList<Board>();
		LinkedList<Board> checked = new LinkedList<Board>();
		queue.add(board);
		while(queue.size()>0){
			Board currentBoard = queue.remove();
			if(currentBoard.isSolved()){
				return currentBoard;
			}
			checked.add(board);

			Board.Direction[] moves = currentBoard.moveableDirections();
			for(Board.Direction d: moves){
				Board newBoard = currentBoard.move(d);
				boolean found = false;
				for(Board b: checked){
					if(b.equals(newBoard)){
						found = true;
						break;
					}
				}
				if(!found)
					queue.add(newBoard);
			}
		}
		return null; //should never get here
	}

	public static Board iterativeDepthFirstSearch(Board board){
		int depth = 0;
		
		while(true){
			LinkedList<Board> stack = new LinkedList<Board>();
			LinkedList<Board> checked = new LinkedList<Board>();
			stack.push(board);

			while(stack.size()>0){
				Board currentBoard = stack.pop();
				if(currentBoard.isSolved())
					return currentBoard;
				checked.push(currentBoard);

				Board.Direction[] moves = currentBoard.moveableDirections();
				for(Board.Direction d: moves){
					Board newBoard = currentBoard.move(d);
					boolean found = false;
					for(Board b: checked){
						if(b.equals(newBoard)){
							found = true;
							break;
						}
					}
					if(!found && newBoard.getSolutionPath().size() < depth){
						stack.push(newBoard);
					}
				}
			}
			depth++;
		}
	}
}
