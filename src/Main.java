import puzzle.Board;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main{
	public static void main(String args[]){
		System.out.println("Matt Dumford - mdumfo2@uic.edu\n15-Puzzle Solver\n");
		Board board = null;

		// read from file version
		// try{
		// 	board = getPuzzleFromFile(args[0]);
		// }		
		// catch(ArrayIndexOutOfBoundsException e){
		// 	System.err.println("Please specify a file with the puzzle.");
		// 	System.exit(1);
		// }
		// catch(IOException e){
		// 	e.printStackTrace();
		// }
		
		// args version
		try{
			board = getPuzzleFromArgs(args);
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Not enough arguments");
			System.exit(1);
		}

		try{
			System.out.println("Breadth First Search:");
			long start = System.nanoTime();
			Solution solution = breadthFirstSearch(board);
			long duration = System.nanoTime() - start;
			printSolutionUgly(board, solution.solution);
			System.out.println(duration + ", " + solution.memory);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}
		
		try{
			System.out.println("\n\nIterative Depth First Search:");
			long start = System.nanoTime();
			Solution solution = iterativeDepthFirstSearch(board);
			long duration = System.nanoTime() - start;
			printSolutionUgly(board, solution.solution);
			System.out.println(duration + ", " + solution.memory);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}

		try{
			System.out.println("\n\nA* Heuristic 1:");
			long start = System.nanoTime();
			Solution solution = aStar1(board);
			long duration = System.nanoTime() - start;
			printSolutionUgly(board, solution.solution);
			System.out.println(solution.expandedNodes);
			System.out.println(duration + ", " + solution.memory);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}

		try{
			System.out.println("\n\nA* Heuristic 2:");
			long start = System.nanoTime();
			Solution solution = aStar2(board);
			long duration = System.nanoTime() - start;
			printSolutionUgly(board, solution.solution);
			System.out.println(solution.expandedNodes);
			System.out.println(duration + ", " + solution.memory);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}
	}

	public static Board getPuzzleFromArgs(String args[]){
		int[][] matrix = new int[4][4];
		int k = 0;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				String s = args[k];
				if(s.equals("_"))
						matrix[i][j] = 0;
				else
					matrix[i][j] = Integer.parseInt(s);
				k++;
			}
		}
		return new Board(matrix);

	}

	public static Board getPuzzleFromFile(String filePath) throws IOException{
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

	public static void printSolutionPretty(Board board, Board solution){
		System.out.println(board);

		for(Board.Direction d: solution.getSolutionPath()){
			board = board.move(d);
			System.out.println(d);
			System.out.println(board);
		}
	}
	
	public static void printSolutionUgly(Board board, Board solution){
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				System.out.print(board.getBoard()[i][j] + " ");
		System.out.println();
		for(Board.Direction d: solution.getSolutionPath()){
			board = board.move(d);
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
					System.out.print(board.getBoard()[i][j] + " ");
			System.out.println(d);
		}
	}

	public static Solution breadthFirstSearch(Board board){
		LinkedList<Board> queue = new LinkedList<Board>();
		LinkedList<Board> checked = new LinkedList<Board>();
		queue.add(board);
		while(queue.size()>0){
			Board currentBoard = queue.remove();
			if(currentBoard.isSolved()){
				double memory = 100 - Runtime.getRuntime().freeMemory() * 100.0 / Runtime.getRuntime().totalMemory();
				return new Solution(currentBoard, memory);
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

	public static Solution iterativeDepthFirstSearch(Board board){
		int depth = 0;
		
		while(true){
			LinkedList<Board> stack = new LinkedList<Board>();
			LinkedList<Board> checked = new LinkedList<Board>();
			stack.push(board);

			while(stack.size()>0){
				Board currentBoard = stack.pop();
				if(currentBoard.isSolved()){
					double memory = 100 - Runtime.getRuntime().freeMemory() * 100.0 / Runtime.getRuntime().totalMemory();
					return new Solution(currentBoard, memory);
				}
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

	public static Solution aStar1(Board board){
		ArrayList<Board> fringe = new ArrayList<Board>();
		fringe.add(board);
		int expandedNodes = 0;
		while(fringe.size() > 0){
			int min = fringe.get(0).getHeuristic1();
			int minPos = 0;
			for(int i=0; i<fringe.size(); i++){
				if(fringe.get(i).getHeuristic1() < min){
					min = fringe.get(i).getHeuristic1();
					minPos = i;
				}
			}

			Board expand = fringe.remove(minPos);
			if(expand.isSolved()){
				double memory = 100 - Runtime.getRuntime().freeMemory() * 100.0 / Runtime.getRuntime().totalMemory();
				return new Solution(expand, expandedNodes, memory);
			}
			expandedNodes++;
			for(Board.Direction d: expand.moveableDirections()){
				fringe.add(expand.move(d));
			}
		}
		return null; // should never get here
	}

	public static Solution aStar2(Board board){
		ArrayList<Board> fringe = new ArrayList<Board>();
		fringe.add(board);
		int expandedNodes = 0;
		while(fringe.size() > 0){
			int min = fringe.get(0).getHeuristic2();
			int minPos = 0;
			for(int i=0; i<fringe.size(); i++){
				if(fringe.get(i).getHeuristic2() < min){
					min = fringe.get(i).getHeuristic2();
					minPos = i;
				}
			}

			Board expand = fringe.remove(minPos);
			if(expand.isSolved()){
				double memory = 100 - Runtime.getRuntime().freeMemory() * 100.0 / Runtime.getRuntime().totalMemory();
				return new Solution(expand, expandedNodes, memory);
			}
			expandedNodes++;
			for(Board.Direction d: expand.moveableDirections()){
				fringe.add(expand.move(d));
			}
		}
		return null; // should never get here
	}

	private static class Solution{
		public int expandedNodes;
		public Board solution;
		public double memory;
		public Solution(Board solution, int expandedNodes, double memory){
			this.expandedNodes = expandedNodes;
			this.solution = solution;
			this.memory = memory;
		}
		public Solution(Board solution, double memory){
			this.solution = solution;
			this.memory = memory;
		}
	}
}

