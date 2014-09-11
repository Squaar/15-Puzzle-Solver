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
			System.out.println(board.toString());
		}		
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Please specify a file with the puzzle.");
			System.exit(1);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		Board solution = breadthFirstSearch(board);
		System.out.println();
		System.out.println(solution);
		System.out.println(solution.getSolutionPath());
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

	public static Board breadthFirstSearch(Board board){
		LinkedList<Board> queue = new LinkedList<Board>();
		queue.add(board);
		while(queue.size()>0){
			System.out.println(queue.size());
			Board currentBoard = queue.remove();
			if(currentBoard.isSolved())
				return currentBoard;

			Board.Direction[] moves = currentBoard.moveableDirections();
			for(Board.Direction d: moves)
				queue.add(currentBoard.move(d));
		}
		return null; //should never get here
	}
}
