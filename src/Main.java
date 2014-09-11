import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main{
	public static void main(String args[]){
		try{
			Board board = getPuzzle(args[0]);
			System.out.println(board.toString());

			System.out.println(breadthFirstSearch(board));
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Please specify a file with the puzzle.");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public static Board getPuzzle(String filePath) throws IOException{
		Scanner sc = new Scanner(new File(filePath));
		sc.useDelimiter("[\n,]");
		int[][] matrix = new int[4][4];
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(sc.hasNext()){
					matrix[i][j] = sc.nextInt();
				}
			}
		}
		sc.close();
		return new Board(matrix);
	}

	public static ArrayList<Board.Direction> breadthFirstSearch(Board board){
		ArrayList<Board.Direction> a = new ArrayList<Board.Direction>();
		a.add(Board.Direction.UP);
		return a;
	}
}
