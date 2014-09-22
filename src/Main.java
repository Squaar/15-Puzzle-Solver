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

		// get board from args
		try{
			board = getPuzzleFromArgs(args);
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Not enough arguments");
			System.exit(1);
		}

		// Breadth first search
		try{
			System.out.println("Breadth First Search:");
			long start = System.nanoTime();
			Solution solution = breadthFirstSearch(board);
			long duration = System.nanoTime() - start;
			printTable(board, solution, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}

		System.gc();
		
		// Iterativly deepening depth first search
		try{
			System.out.println("\n\nIterative Depth First Search:");
			long start = System.nanoTime();
			Solution solution = iterativeDepthFirstSearch(board);
			long duration = System.nanoTime() - start;
			printTable(board, solution, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}

		System.gc();

		// A* Heuristic 1
		try{
			System.out.println("\n\nA* Heuristic 1:");
			long start = System.nanoTime();
			Solution solution = aStar1(board);
			long duration = System.nanoTime() - start;
			printTable(board, solution, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}

		System.gc();

		// A* Heuristic 2
		try{
			System.out.println("\n\nA* Heuristic 2:");
			long start = System.nanoTime();
			Solution solution = aStar2(board);
			long duration = System.nanoTime() - start;
			printTable(board, solution, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Out of Memory!");
		}
	}

	// get % JVM memory usage
	public static double memoryUsage(){
		Runtime runtime = Runtime.getRuntime();
		double total = runtime.totalMemory();
		double free = runtime.freeMemory();
		return (total - free) / total * 100;
	}

	// parses table to solve from cmd args
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

	// reads in a puzzle to solve from a file
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

	// Prints solution as a table for HW3
	public static void printTable(Board board, Solution solution, long time){
		System.out.printf("%-40s | %-15s | %s\n", "Board", "Number of Moves", "Solution");
		String moves = "";
		for(Board.Direction d: solution.solution.getSolutionPath())
			moves += d.toString().substring(0,1);
		String boardLine = "";
		for(int[] line: board.getBoard()){
			for(int tile: line){
				boardLine += tile + " ";
			}
		}
		System.out.printf("%-40s | %-15s | %s\n", boardLine, moves.length(), moves);
		System.out.printf("Time (milliseconds): %d\n", time / 1000000);
		System.out.printf("Memory Usage: %.2f%%\n", solution.memory);
		System.out.printf("Expanded Nodes: %d\n", solution.expandedNodes);
	}

	// Depreciated - prints solution path in board formation
	public static void printSolutionPretty(Board board, Board solution){
		System.out.println(board);
		for(Board.Direction d: solution.getSolutionPath()){
			board = board.move(d);
			System.out.println(d);
			System.out.println(board);
		}
	}
	
	// Depreciated - prints solution path, 1 per line
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

	// Breadth first search
	public static Solution breadthFirstSearch(Board board){
		LinkedList<Board> queue = new LinkedList<Board>();
		queue.add(board);
		int expandedNodes = 0;
		while(queue.size()>0){
			Board currentBoard = queue.remove();
			if(currentBoard.isSolved()){
				return new Solution(currentBoard, expandedNodes, memoryUsage());
			}
			expandedNodes++;

			Board.Direction[] moves = currentBoard.moveableDirections();
			for(Board.Direction d: moves){
				Board newBoard = currentBoard.move(d);
				queue.add(newBoard);
			}
		}
		return null; //should never get here
	}

	// Iteratively deepening depth first search
	public static Solution iterativeDepthFirstSearch(Board board){
		int depth = 0;
		int expandedNodes = 0;
		
		while(true){
			LinkedList<Board> stack = new LinkedList<Board>();
			stack.push(board);

			while(stack.size()>0){
				Board currentBoard = stack.pop();
				if(currentBoard.isSolved()){
					return new Solution(currentBoard, expandedNodes, memoryUsage());
				}
				expandedNodes++;

				Board.Direction[] moves = currentBoard.moveableDirections();
				for(Board.Direction d: moves){
					Board newBoard = currentBoard.move(d);
					if(newBoard.getSolutionPath().size() < depth){
						stack.push(newBoard);
					}
				}
			}
			depth++;
		}
	}

	// A* search using heuristic 1
	public static Solution aStar1(Board board){
		ArrayList<Board> fringe = new ArrayList<Board>();
		fringe.add(board);
		int expandedNodes = 0;
		while(fringe.size() > 0){
			int min = fringe.get(0).getHeuristic1() + fringe.get(0).getSolutionPath().size();
			int minPos = 0;
			for(int i=0; i<fringe.size(); i++){
				if(fringe.get(i).getHeuristic1() + fringe.get(i).getSolutionPath().size() < min){
					min = fringe.get(i).getHeuristic1() + fringe.get(i).getSolutionPath().size();
					minPos = i;
				}
			}

			Board expand = fringe.remove(minPos);
			if(expand.isSolved()){
				return new Solution(expand, expandedNodes, memoryUsage());
			}
			expandedNodes++;
			for(Board.Direction d: expand.moveableDirections()){
				fringe.add(expand.move(d));
			}
		}
		return null; // should never get here
	}

	// A* search using heuristic 2
	public static Solution aStar2(Board board){
		ArrayList<Board> fringe = new ArrayList<Board>();
		fringe.add(board);
		int expandedNodes = 0;
		while(fringe.size() > 0){
			int min = fringe.get(0).getHeuristic2() + fringe.get(0).getSolutionPath().size();
			int minPos = 0;
			for(int i=0; i<fringe.size(); i++){
				if(fringe.get(i).getHeuristic2() + fringe.get(i).getSolutionPath().size() < min){
					min = fringe.get(i).getHeuristic2() + fringe.get(i).getSolutionPath().size();
					minPos = i;
				}
			}

			Board expand = fringe.remove(minPos);
			if(expand.isSolved()){
				return new Solution(expand, expandedNodes, memoryUsage());
			}
			expandedNodes++;
			for(Board.Direction d: expand.moveableDirections()){
				fringe.add(expand.move(d));
			}
		}
		return null; // should never get here
	}

	// holds information to be returned by search methods
	private static class Solution{
		public int expandedNodes;
		public Board solution;
		public double memory;
		public Solution(Board solution, int expandedNodes, double memory){
			this.expandedNodes = expandedNodes;
			this.solution = solution;
			this.memory = memory;
		}
	}
}

